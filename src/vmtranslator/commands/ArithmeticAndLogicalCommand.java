package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.ArithmeticAndLogicalCommandType;
import vmtranslator.enums.CommandType;

@Data
public class ArithmeticAndLogicalCommand implements Command {
    ArithmeticAndLogicalCommandType arithmeticAndLogicalCommandType;

    public ArithmeticAndLogicalCommand(ArithmeticAndLogicalCommandType arithmeticAndLogicalCommandType) {
        this.arithmeticAndLogicalCommandType = arithmeticAndLogicalCommandType;
    }

    public ArithmeticAndLogicalCommand(String command) {
//        System.out.println(command);
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
