package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.CommandType;

@Data
public class IfGotoCommand implements Command{
    private String labelName;

    public IfGotoCommand(String command) {
        labelName = command.split("\\s")[1];
    }

    @Override
    public CommandType commandType() {
        return CommandType.C_IF;
    }

    @Override
    public String arg1() {
        return labelName;
    }

    @Override
    public int arg2() {
        return 0;
    }

    @Override
    public String toString() {
        return "if-goto " + labelName;
    }
}
