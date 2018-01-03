package vmtranslator;

import vmtranslator.codewriter.CodeWriter;
import vmtranslator.commands.Command;
import vmtranslator.parser.Parser;

import java.io.File;
import java.io.IOException;

import static vmtranslator.utils.Utils.generateOutputFileName;

public class Main {

    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        System.out.println("Working on file: " + fileName);
        if (isFolder(fileName)){

        }
        String outputFileName = generateOutputFileName(fileName);
        Parser parser = new Parser(fileName);
        CodeWriter codeWriter = new CodeWriter(outputFileName);
        while (parser.hasMoreCommands()) {
            Command currentCommand = parser.getCurrentCommand();
            codeWriter.writeCommand(currentCommand);
            parser.advance();
        }
        codeWriter.close();
    }

    private static boolean isFolder(String fileName) {
        File file = new File(fileName);
        return file.isDirectory();
    }

}
