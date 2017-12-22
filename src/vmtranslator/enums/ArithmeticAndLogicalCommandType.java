package vmtranslator.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ArithmeticAndLogicalCommandType {
    ADD("add"),
    SUB("sub"),
    NEG("neg"),
    EQ("eq"),
    GT("gt"),
    LT("lt"),
    AND("and"),
    OR("or"),
    NOT("not");

    private String command;
    private static final Map<String, ArithmeticAndLogicalCommandType> lookup = new HashMap<>();

    ArithmeticAndLogicalCommandType(String command) {
        this.command = command;

    }
    
    static {
        for (ArithmeticAndLogicalCommandType m : EnumSet.allOf(ArithmeticAndLogicalCommandType.class))
            lookup.put(m.command, m);
    }

    public static ArithmeticAndLogicalCommandType get(String command) {
        return lookup.get(command);
    }
}
