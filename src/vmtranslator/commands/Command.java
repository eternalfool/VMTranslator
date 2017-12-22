package vmtranslator.commands;

import vmtranslator.enums.CommandType;

public interface Command {
    CommandType commandType();
    String arg1();
    int arg2();
}
