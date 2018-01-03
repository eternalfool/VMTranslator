package vmtranslator.commands;

import vmtranslator.enums.CommandType;

public class ReturnCommand implements Command{
    public ReturnCommand(String command) {

    }

    @Override
    public CommandType commandType() {
        return CommandType.C_RETURN;
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
        return "return";
    }
}
