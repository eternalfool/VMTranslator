package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.CommandType;

@Data
public class FunctionCommand implements Command{
    private final Integer nvars;
    private final String classMethodName;

    public FunctionCommand(String command) {
//        String function = command.split("\\s")[0];
        this.classMethodName = command.split("\\s")[1];
        this.nvars = Integer.valueOf(command.split("\\s")[2]);
    }

    @Override
    public CommandType commandType() {
        return CommandType.C_FUNCTION;
    }

    @Override
    public String arg1() {
        return null;
    }

    @Override
    public int arg2() {
        return 0;
    }

    @Override
    public String toString() {
        return "function " + classMethodName + " " + nvars;
    }
}
