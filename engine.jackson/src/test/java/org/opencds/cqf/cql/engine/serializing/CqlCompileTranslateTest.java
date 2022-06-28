package org.opencds.cqf.cql.engine.serializing;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.cqframework.cql.cql2elm.*;
import org.cqframework.cql.elm.execution.Library;
import org.cqframework.cql.elm.tracking.TrackBack;
import org.fhir.ucum.UcumEssenceService;
import org.fhir.ucum.UcumException;
import org.fhir.ucum.UcumService;
import org.hl7.cql_annotations.r1.CqlToElmBase;
import org.hl7.elm.r1.Element;
import org.hl7.elm.r1.Expression;
import org.hl7.elm.r1.TypeSpecifier;
import org.hl7.elm.r1.VersionedIdentifier;
import org.opencds.cqf.cql.engine.serializing.jackson.JsonCqlLibraryReader;
import org.opencds.cqf.cql.engine.serializing.jackson.XmlCqlLibraryReader;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.CqlToElmBaseMixIn;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.ElementMixin;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.ExpressionMixin;
import org.opencds.cqf.cql.engine.serializing.jackson.mixins.TypeSpecifierMixin;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlCompileTranslateTest implements ITest {
    private final String path;
    private final String fileName;

    @Factory(dataProvider = "dataMethod")
    public CqlCompileTranslateTest(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @DataProvider
    public static Object[][] dataMethod() throws URISyntaxException {
        List<String[]> filesToTest = new ArrayList<>();
        filesToTest.addAll(collectTestFiles());
        return filesToTest.toArray(new String[filesToTest.size()][]);
    }

    public static List<String[]> collectTestFiles() throws URISyntaxException {
        List<String[]> filesToTest = new ArrayList<>();
        URL dirURL = org.opencds.cqf.cql.engine.execution.CqlMainSuiteTest.class.getResource(".");
        File file = new File(dirURL.toURI());
        for (String fileName : file.list()) {
            if (fileName.endsWith(".cql")) {
                filesToTest.add(new String[]{ file.getAbsolutePath(), fileName });
            }
        }
        return filesToTest;
    }

    public String getTestName() {
        return "test" + fileName.replaceAll(".cql","");
    }

    @Test
    private void testCompileTranscode() throws IOException, JAXBException, UcumException {
        ModelManager modelManager = new ModelManager();
        LibraryManager libraryManager = new LibraryManager(modelManager);
        libraryManager.getLibrarySourceLoader().registerProvider(new LibrarySourceProvider() {
            @Override
            public InputStream getLibrarySource(VersionedIdentifier versionedIdentifier) {
                String libraryFileName = String.format("%s%s.cql",
                    versionedIdentifier.getId(), versionedIdentifier.getVersion() != null ? ("-" + versionedIdentifier.getVersion()) : "");
                return org.opencds.cqf.cql.engine.execution.CqlMainSuiteTest.class.getResourceAsStream(libraryFileName);
            }
        });

        UcumService ucumService = new UcumEssenceService(UcumEssenceService.class.getResourceAsStream("/ucum-essence.xml"));

        File cqlFile = new File(path + "/" + fileName);

        CqlTranslator translator = CqlTranslator.fromFile(cqlFile, modelManager, libraryManager, ucumService);

        if (translator.getErrors().size() > 0) {
            System.err.println("Translation failed due to errors:");
            ArrayList<String> errors = new ArrayList<>();
            for (CqlCompilerException error : translator.getErrors()) {
                TrackBack tb = error.getLocator();
                String lines = tb == null ? "[n/a]" : String.format("[%d:%d, %d:%d]",
                    tb.getStartLine(), tb.getStartChar(), tb.getEndLine(), tb.getEndChar());
                System.err.printf("%s %s%n", lines, error.getMessage());
                errors.add(lines + error.getMessage());
            }
            throw new IllegalArgumentException(errors.toString());
        }

        assertThat(translator.getErrors().size(), is(0));

        Library jsonLibrary = null;
        try {
            jsonLibrary = new JsonCqlLibraryReader().read(translator.toJson());
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("Errors occurred reading ELM from json %s: %s", fileName, e.getMessage()));
        }

        Library xmlLibrary = null;

        // TODO: Replace by new mapper from Translator.
        XmlMapper mapper = XmlMapper.builder()
            .defaultUseWrapper(true)
            .defaultMergeable(true)
            .enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION)
            .enable(ToXmlGenerator.Feature.WRITE_XML_1_1)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .enable(MapperFeature.USE_BASE_TYPE_AS_DEFAULT_IMPL)
            .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            .addModule(new JaxbAnnotationModule())
            .addMixIn(Element.class, ElementMixin.class)
            .addMixIn(Expression.class, ExpressionMixin.class)
            .addMixIn(TypeSpecifier.class, TypeSpecifierMixin.class)
            .addMixIn(CqlToElmBase.class, CqlToElmBaseMixIn.class)
            .build();

        try {
            xmlLibrary = new XmlCqlLibraryReader().read(mapper.writeValueAsString(translator.toELM()));
        }
        catch (Exception e) {
            throw new IllegalArgumentException(String.format("Errors occurred reading ELM from xml %s: %s", fileName, e.getMessage()));
        }

        if (xmlLibrary != null && jsonLibrary != null) {
            Assert.assertTrue(equivalent(xmlLibrary, jsonLibrary));
        }
    }

    private static boolean equivalent(Library left, Library right) {
        if (left == null && right == null) {
            return true;
        }

        if (left != null) {
            return left.getIdentifier().equals(right.getIdentifier());
        }

        // TODO: validate ELM equivalence... big job...
        // Simplest would be to introduce on Executable...

        return false;
    }
}

