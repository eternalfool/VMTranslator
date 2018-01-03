package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.CommandType;

@Data
public class LabelCommand implements Command{
    private String labelName;

    public LabelCommand(String command) {
        labelName = command.split("\\s")[1];
    }

    @Override
    public CommandType commandType() {
        return CommandType.C_LABEL;
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
        return "label " + labelName;
    }

}
