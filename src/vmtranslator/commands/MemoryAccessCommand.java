package vmtranslator.commands;

import lombok.Data;
import vmtranslator.enums.CommandType;
import vmtranslator.enums.MemoryAccessOperation;
import vmtranslator.enums.VirtualMemorySegment;

@Data
public class MemoryAccessCommand implements Command {
    MemoryAccessOperation memoryAccessOperation;
    VirtualMemorySegment virtualMemorySegment;
    int index;

    public MemoryAccessCommand(MemoryAccessOperation memoryAccessOperation, VirtualMemorySegment
            virtualMemorySegments, int index) {
        this.memoryAccessOperation = memoryAccessOperation;
        this.virtualMemorySegment = virtualMemorySegments;
        this.index = index;
    }

    public MemoryAccessCommand(String command) {
        String memoryAccess = command.split("\\s")[0];
        String virtualSegment = command.split("\\s")[1];
        Integer indexi = Integer.valueOf(command.split("\\s")[2]);
        this.memoryAccessOperation = MemoryAccessOperation.get(memoryAccess);
        this.virtualMemorySegment = VirtualMemorySegment.get(virtualSegment);
        this.index = indexi;
    }

    @Override
    public CommandType commandType() {
        if (memoryAccessOperation.equals(MemoryAccessOperation.PUSH)) {
            return CommandType.C_PUSH;
        } else if (memoryAccessOperation.equals(MemoryAccessOperation.POP)) {
            return CommandType.C_POP;
        }
        throw new RuntimeException("Invalid commandType: " + memoryAccessOperation.name());
    }

    @Override
    public String arg1() {
        return virtualMemorySegment.name();
    }

    @Override
    public int arg2() {
        return index;
    }

    @Override
    public String toString() {
        return memoryAccessOperation.command + " " +
                virtualMemorySegment.getVirtualMemorySegment() + " " + index;
    }
}
