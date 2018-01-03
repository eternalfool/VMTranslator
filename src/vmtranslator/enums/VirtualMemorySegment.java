package vmtranslator.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum VirtualMemorySegment {
    ARGUMENT("argument", "ARG"),
    LOCAL("local", "LCL"),
    STATIC("static", "STATIC"),
    CONSTANT("constant", "CONST"),
    THIS("this", "THIS"),
    THAT("that", "THAT"),
    POINTER("pointer", "PTR"),
    TEMP("temp", "5");

    private String virtualMemorySegment;
    private String assemblyCode;

    private static final Map<String, VirtualMemorySegment> lookup = new HashMap<>();

    static {
        for (VirtualMemorySegment m : EnumSet.allOf(VirtualMemorySegment.class))
            lookup.put(m.virtualMemorySegment, m);
    }

    public static VirtualMemorySegment get(String vmSegment) {
        return lookup.get(vmSegment);
    }
}
