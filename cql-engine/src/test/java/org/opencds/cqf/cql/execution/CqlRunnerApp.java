package org.opencds.cqf.cql.execution;

import java.nio.ByteBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

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
        CqlRunnerLib.perform(sourceCodeText, errors);

        if (errors.size() > 0)
        {
            System.out.println("The requested source code providing file"
                + " [" + sourceCodeFilePath + "] was character data"
                + " but couldn't be executed as CQL or ELM source code:"
                + "\n" + String.join("\n", errors));
            return;
        }
    }
}
