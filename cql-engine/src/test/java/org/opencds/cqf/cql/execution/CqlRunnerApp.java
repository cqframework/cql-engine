package org.opencds.cqf.cql.execution;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

import org.cqframework.cql.elm.execution.ExpressionDef;
import org.cqframework.cql.elm.execution.Library;

import org.opencds.cqf.cql.elm.execution.ExpressionDefEvaluator;

/**
 * Created by Darren on 2017-11-15.
 */
public class CqlRunnerApp
{
    public static void main(String[] args)
    {
        if (args.length != 1)
        {
            // TODO: Review whether hostExecutablePath reflects actual
            // main program invocation syntax or not, and accounts for
            // the varied host operating systems or compiled vs debugged.
            String hostExecutablePath = CqlRunnerApp.class
                .getProtectionDomain().getCodeSource().getLocation().getPath();
            System.out.println(
                "Usage: " + hostExecutablePath + " <sourceCodeFilePath>");
            return;
        }

        // File system path for the file containing CQL or ELM source
        // code that the user wishes to execute as their main program.
        // If the user-specified file path is absolute, toAbsolutePath()
        // will just use that as the final path; otherwise it is taken
        // as relative to the host executable's current working directory.
        Path sourceCodeFilePath = FileSystems.getDefault()
            .getPath(args[0]).toAbsolutePath().normalize();

        if (!Files.exists(sourceCodeFilePath)
            || !Files.isRegularFile(sourceCodeFilePath))
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] doesn't exist.");
            return;
        }

        if (!Files.isReadable(sourceCodeFilePath))
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] couldn't be read"
                + " due to lack of permissions.");
            return;
        }

        byte[] sourceCodeFileContent;
        try
        {
            sourceCodeFileContent = Files.readAllBytes(sourceCodeFilePath);
        }
        catch (Exception e)
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] couldn't be read:"
                + "\n" + e.toString());
            return;
        }

        String sourceCodeText;
        try
        {
            // TODO: Try other charsets if decoding as UTF-8 doesn't work.
            // CharsetDecoder.detectedCharset() may also be useful here.
            CharsetDecoder cd = StandardCharsets.UTF_8.newDecoder();
            cd.onMalformedInput(CodingErrorAction.REPORT);
            cd.onUnmappableCharacter(CodingErrorAction.REPORT);
            sourceCodeText = cd.decode(ByteBuffer.wrap(sourceCodeFileContent)).toString();
        }
        catch (Exception e)
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] was not (UTF-8) character data:"
                + "\n" + e.toString());
            return;
        }

        // Try to interpret the sourceCodeText as CQL or ELM and execute it.
        ArrayList<String> errors = new ArrayList<>();
        perform(sourceCodeText, errors);

        if (errors.size() > 0)
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] was character data"
                + " but couldn't be executed as CQL or ELM source code:"
                + "\n" + String.join("\n", errors));
            return;
        }
    }

    private static void perform(String maybeCqlOrElm, ArrayList<String> errors)
    {
        Library library = null;
        try
        {
            // First try and parse the input as ELM.
            library = CqlLibraryReader.read(new ByteArrayInputStream(
                maybeCqlOrElm.getBytes(StandardCharsets.UTF_8)));
        }
        catch (Exception e1)
        {
            // Parsing the input as ELM failed; try and parse it as CQL.
            String elm = CqlToElmLib.maybeCqlToElm(maybeCqlOrElm, errors);
            if (elm == null)
            {
                errors.add("The source code failed to parse either as ELM or as CQL.");
                errors.add("The prior errors were during the attempt to parse as CQL.");
                errors.add("The next errors were during the attempt to parse as ELM:");
                errors.add(e1.toString());
                return;
            }
            try
            {
                library = CqlLibraryReader.read(new ByteArrayInputStream(
                    elm.getBytes(StandardCharsets.UTF_8)));
            }
            catch (Exception e2)
            {
                errors.add("Translation of CQL to ELM succeeded; however"
                    + " parsing that ELM failed due to errors:");
                errors.add(e2.toString());
            }
        }

        // Whether ELM or CQL, we succeeded in parsing it.

        // Next execute all of the regular statements in the library that
        // are not marked "private".
        // Do this such that the only output the user sees from this is the
        // output from any Message() calls.

        Context context = new Context(library);

        for (ExpressionDef statement : library.getStatements().getDef())
        {
            if (!(statement instanceof ExpressionDefEvaluator))
            {
                // This skips over any FunctionDef statements for starters.
                continue;
            }
            if (!statement.getAccessLevel().value().equals("Public"))
            {
                // Note: It appears that Java interns the string "Public"
                // since using != here also seems to work.
                continue;
            }
            try
            {
                statement.evaluate(context);
            }
            catch (Exception e3)
            {
                errors.add("Execution of CQL or ELM statement named ["
                    + statement.getName() + "] failed due to error:");
                errors.add(e3.toString());
            }
        }
    }
}
