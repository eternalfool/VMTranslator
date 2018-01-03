package vmtranslator.commands;

import vmtranslator.enums.CommandType;

public class CallCommand implements Command{
    @Override
    public CommandType commandType() {
        return CommandType.C_CALL;
    }

    @Override
    public String arg1() {
        return null;
    }

    @Override
    public int arg2() {
        return 0;
    }
}
