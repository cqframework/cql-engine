package org.opencds.cqf.cql.retrieve;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.Helpers;
import org.opencds.cqf.cql.runtime.Code;
import org.opencds.cqf.cql.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.terminology.TerminologyProvider;
import org.opencds.cqf.cql.terminology.ValueSetInfo;
import org.opencds.cqf.cql.util.ValueSetUtil;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class FileBasedFhirTerminologyProvider implements TerminologyProvider {

    private String uri;
    private FhirContext fhirContext;
    private boolean initialized;

    private Map<String, Iterable<Code>> valueSetIndex = new HashMap<>();

    public FileBasedFhirTerminologyProvider(FhirContext fhirContext, String uri) {
        if (uri == null || uri.isEmpty() || !Helpers.isFileUri(uri)) {
            throw new IllegalArgumentException("File Terminology provider requires a valid path to Terminology resources");
        }

        this.fhirContext = fhirContext;
        this.uri = uri;
        this.initialized = false;
    }

    @Override
    public boolean in(Code code, ValueSetInfo valueSet) {
        if (code == null || valueSet == null) {
            throw new IllegalArgumentException("code and valueset must not be null when testing 'in'.");
        }

        Iterable<Code> codes = this.expand(valueSet);
        if (codes == null) {
            return false;
        }

        // TODO: Handle Versions
        for (Code c : codes) {
            if (c.getCode().equals(code.getCode()) && c.getSystem().equals(code.getSystem())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterable<Code> expand(ValueSetInfo valueSet) {
        if (valueSet == null) {
            throw new IllegalArgumentException("valueset must not be null when attempting to expand");
        }

        if (!this.initialized) {
            this.initialize();
        }

        if (!this.valueSetIndex.containsKey(valueSet.getId())) {
            throw new IllegalArgumentException(String.format("Unable to locate valueset %s", valueSet.getId()));
        }

        return this.valueSetIndex.get(valueSet.getId());
    }

    @Override
    // TODO: We dont know about codes systems...
    public Code lookup(Code code, CodeSystemInfo codeSystem) {
        return null;
    }

    private void initialize() {
        FilenameFilter filter = new FilenameFilter() {

            public boolean accept(File f, String name)
            {
                return name.endsWith(".json") || name.endsWith(".xml");
            }
        };

        // TODO: We probably want more intelligent handling in the event
        // We're not given a path. It's possible that this will result in a run-time
        // error if terminology can not be resolved.
        if (this.uri != null) {
            Path path = Paths.get(this.uri);
            File parent = new File(path.toAbsolutePath().toString());

            File[] files = parent.listFiles(filter);


            if (files != null && files.length > 0) {
                for (File f : files) {
                    this.loadAsValueSet(f.getAbsolutePath());
                }
            }
        }

        this.initialized = true;
    }

    private void loadAsValueSet(String path) {
        Path filePath = Path.of(path);
        try {
            String content = new String (Files.readAllBytes(filePath));

            IParser parser = path.endsWith(".json") ? this.fhirContext.newJsonParser() : this.fhirContext.newXmlParser();
            IBaseResource resource = parser.parseResource(new StringReader(content));

            String resourceType = ValueSetUtil.getResourceType(this.fhirContext, resource);

            // Skip resources that are not ValueSets;
            if (!resourceType.equals("ValueSet")) {
                return;
            }

            String url = ValueSetUtil.getUrl(fhirContext, resource);
            Iterable<Code> codes = ValueSetUtil.getCodesInExpansion(this.fhirContext, resource);

            if (codes == null) {
                codes = ValueSetUtil.getCodesInCompose(this.fhirContext, resource);
            }

            this.valueSetIndex.put(url, codes);

        }
        catch (IOException e) {
            throw new IllegalArgumentException(String.format("Unable to load resource located at %s.", path));
        }
    }
}