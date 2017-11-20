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
            // TODO: Review whether host_executable_path reflects actual
            // main program invocation syntax or not, and accounts for
            // the varied host operating systems or compiled vs debugged.
            String host_executable_path = CqlRunnerApp.class
                .getProtectionDomain().getCodeSource().getLoc‌ation().getPath();
            System.out.println(
                "Usage: " + host_executable_path + " <source_code_file_path>");
            return;
        }

        // File system path for the file containing CQL source
        // code that the user wishes to execute as their main program.
        // If the user-specified file path is absolute, toAbsolutePath()
        // will just use that as the final path; otherwise it is taken
        // as relative to the host executable's current working directory.
        Path source_code_file_path = FileSystems.getDefault()
            .getPath(args[0]).toAbsolutePath().normalize();

        if (!Files.exists(source_code_file_path)
            || !Files.isRegularFile(source_code_file_path))
        {
            System.out.println("The requested source code providing file"
                + " [" + source_code_file_path + "] doesn't exist.");
            return;
        }

        if (!Files.isReadable(source_code_file_path))
        {
            System.out.println("The requested source code providing file"
                + " [" + source_code_file_path + "] couldn't be read"
                + " due to lack of permissions.");
            return;
        }

        byte[] source_code_file_content;
        try
        {
            source_code_file_content = Files.readAllBytes(source_code_file_path);
        }
        catch (Exception e)
        {
            System.out.println("The requested source code providing file"
                + " [" + source_code_file_path + "] couldn't be read:"
                + "\n" + e.toString());
            return;
        }

        String source_code_text;
        try
        {
            // TODO: Try other charsets if decoding as UTF-8 doesn't work.
            // CharsetDecoder.detectedCharset() may also be useful here.
            CharsetDecoder cd = StandardCharsets.UTF_8.newDecoder();
            cd.onMalformedInput(CodingErrorAction.REPORT);
            cd.onUnmappableCharacter(CodingErrorAction.REPORT);
            source_code_text = cd.decode(ByteBuffer.wrap(source_code_file_content)).toString();
        }
        catch (Exception e)
        {
            System.out.println("The requested source code providing file"
                + " [" + source_code_file_path + "] was not (UTF-8) character data:"
                + "\n" + e.toString());
            return;
        }

        // Try to interpret the source_code_text as CQL and execute it.
        ArrayList<String> errors = new ArrayList<>();
        CqlRunnerLib.perform(System.out, errors, source_code_text);

        if (errors.size() > 0)
        {
            System.out.println("The requested source code providing file"
                + " [" + source_code_file_path + "] was character data"
                + " but couldn't be executed as CQL source code:"
                + "\n" + String.join("\n", errors));
            return;
        }
    }
}
