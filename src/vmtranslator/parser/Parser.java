package vmtranslator.parser;

import vmtranslator.commands.ArithematicAndLogicalCommand;
import vmtranslator.commands.Command;
import vmtranslator.enums.CommandType;
import vmtranslator.commands.MemoryAccessCommand;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    private List<String> commands;
    private Command currentCommand;
    private int numOfCommands;
    private int currentCommandIndex;
    private static final String COMMENT_REGEX = "//.*|(\"(?:\\\\[^\"]|\\\\\"|.)*?\")|(?s)/\\*.*?\\*/";


    public Parser(String fileName) throws IOException {
        File file = new File(fileName);
        commands = Files.lines(file.toPath()).filter(s -> !s.isEmpty()).filter(s -> !s.matches(COMMENT_REGEX)).map
                (String::trim).collect(Collectors.toList());
        currentCommand = parseAsCommand(commands.get(currentCommandIndex));
        numOfCommands = commands.size();
    }

    private Command parseAsCommand(String command) {
        if (command.split("\\s").length == 1) {
            return new ArithematicAndLogicalCommand(command);
        }
        if (command.split("\\s").length == 2) {
            return new ArithematicAndLogicalCommand(command);
        }
        if (command.split("\\s").length == 3) {
            return new MemoryAccessCommand(command);
        }
        throw new RuntimeException("Invalid command");
    }

    public boolean hasMoreCommands() {
        return currentCommandIndex != numOfCommands;
    }

    public void advance() {
        currentCommandIndex++;
        if (hasMoreCommands()) {
            currentCommand = parseAsCommand(commands.get(currentCommandIndex));
        }
    }

    public Command getCurrentCommand() {
        return currentCommand;
    }

    public CommandType commandType() {
        return currentCommand.commandType();
    }

    // Returns first argument of current command.
    public String arg1() {
        return currentCommand.arg1();
    }

    // Returns second argument of current command.
    public int arg2() {
        return currentCommand.arg2();
    }

}
