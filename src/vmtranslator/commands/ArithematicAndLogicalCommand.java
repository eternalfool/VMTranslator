package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.ArithmeticAndLogicalCommandType;
import vmtranslator.enums.CommandType;

@Data
public class ArithematicAndLogicalCommand implements Command {
    ArithmeticAndLogicalCommandType arithmeticAndLogicalCommandType;

    public ArithematicAndLogicalCommand(ArithmeticAndLogicalCommandType arithmeticAndLogicalCommandType) {
        this.arithmeticAndLogicalCommandType = arithmeticAndLogicalCommandType;
    }

    public ArithematicAndLogicalCommand(String command) {
        this.arithmeticAndLogicalCommandType = ArithmeticAndLogicalCommandType.get(command);

    }

    @Override
    public CommandType commandType() {
        return CommandType.C_ARITHMETIC;
    }

    @Override
    public String arg1() {
        return arithmeticAndLogicalCommandType.name();
    }

    @Override
    public int arg2() {
        throw new RuntimeException("ArithmeticAndLogical command doesn't have arg2");
    }

    @Override
    public String toString() {
        return arithmeticAndLogicalCommandType.getCommand();
    }
}
