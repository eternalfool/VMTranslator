package vmtranslator.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum MemoryAccessOperation {
    PUSH("push"),
    POP("pop");

    public String command;

    MemoryAccessOperation(String command) {
        this.command = command;
    }

    private static final Map<String, MemoryAccessOperation> lookup = new HashMap<>();

    static {
        for (MemoryAccessOperation m : EnumSet.allOf(MemoryAccessOperation.class))
            lookup.put(m.command, m);
    }

    public static MemoryAccessOperation get(String command) {
        return lookup.get(command);
    }

}
