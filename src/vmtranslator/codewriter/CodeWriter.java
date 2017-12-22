package vmtranslator.codewriter;

import vmtranslator.commands.ArithematicAndLogicalCommand;
import vmtranslator.commands.Command;
import vmtranslator.commands.MemoryAccessCommand;
import vmtranslator.enums.CommandType;
import vmtranslator.enums.MemoryAccessOperation;
import vmtranslator.enums.VirtualMemorySegment;
import vmtranslator.utils.Utils;

import java.io.*;

import static vmtranslator.enums.VirtualMemorySegment.THAT;
import static vmtranslator.enums.VirtualMemorySegment.THIS;

public class CodeWriter {

    PrintStream printStream = null;
    String fileName;
    int count;

    public CodeWriter(String fileName) throws FileNotFoundException {
        File outputFile = new File(fileName);
        this.fileName = Utils.generateOutputFileNameWithoutExtension(fileName);
        OutputStream os = new FileOutputStream(outputFile);
        printStream = new PrintStream(os);
    }

    public void writeArithmetic(ArithematicAndLogicalCommand arithematicAndLogicalCommand) {
        switch (arithematicAndLogicalCommand.getArithmeticAndLogicalCommandType()) {
            case EQ:
                popTwoValuesFromStack();
                p("D=D-M");
                p("@EQUAL" + count);
                p("D;JEQ");
                p("@SP");
                p("A = M");
                p("M = 0");
                p("@END" + count);
                p("0;JMP");
                p("(EQUAL" + count + ")");
                p("@SP");
                p("A = M");
                p("M = -1");
                p("(END" + count + ")");
                incrementSP();
                break;
            case ADD:
                popTwoValuesFromStack();
                p("M = M + D");
                incrementSP();
                break;
            case SUB:
                popTwoValuesFromStack();
                p("M = M - D");
                incrementSP();
                break;
            case GT:
                popTwoValuesFromStack();
                p("D=M-D");
                p("@GT" + count);
                p("D;JGT");
                p("@SP");
                p("A = M");
                p("M = 0");
                p("@END" + count);
                p("0;JMP");
                p("(GT" + count + ")");
                p("@SP");
                p("A = M");
                p("M = -1");
                p("(END" + count + ")");
                incrementSP();
                break;
            case LT:
                popTwoValuesFromStack();
                p("D=M-D");
                p("@LT" + count);
                p("D;JLT");
                p("@SP");
                p("A = M");
                p("M = 0");
                p("@END" + count);
                p("0;JMP");
                p("(LT" + count + ")");
                p("@SP");
                p("A = M");
                p("M = -1");
                p("(END" + count + ")");
                incrementSP();
                break;
            case AND:
                popTwoValuesFromStack();
                p("M = M & D");
                incrementSP();
                break;
            case OR:
                popTwoValuesFromStack();
                p("M = M | D");
                incrementSP();
                break;
            case NOT:
                p("@SP");
                p("M = M-1");
                p("A = M");
                p("M = !M");
                incrementSP();
                break;
            case NEG:
                p("@SP");
                p("M = M-1");
                p("A = M");
                p("M = -M");
                incrementSP();
                break;
            default:
//                throw new RuntimeException("No such arithmetic command: " + arithematicAndLogicalCommand.toString());
        }

    }

    private void popTwoValuesFromStack() {
        p("@SP");
        p("M = M-1");
        p("A = M");
        p("D = M");
        p("@SP");
        p("M = M-1");
        p("A = M");
    }

    private void decrementSP() {
        printStream.println("@SP");
        printStream.println("M = M-1");
    }

    private void incrementSP() {
        printStream.println("@SP");
        printStream.println("M = M+1");
    }

    void p(String string) {
        printStream.println(string);
    }

    public void writePushPop(MemoryAccessCommand memoryAccessCommand) {
        if (memoryAccessCommand.getMemoryAccessOperation().equals(MemoryAccessOperation.PUSH)) {
            pushMemorySegmentOperation(memoryAccessCommand);
            incrementAndStoreInStackPointer();
        } else if (memoryAccessCommand.getMemoryAccessOperation().equals(MemoryAccessOperation.POP)) {
            popMemorySegmentOperation(memoryAccessCommand);
        }
    }

    private void popMemorySegmentOperation(MemoryAccessCommand memoryAccessCommand) {
        VirtualMemorySegment vmSegment = memoryAccessCommand.getVirtualMemorySegment();
        switch (vmSegment) {
            case STATIC:
                p("@SP");
                p("M = M - 1");
                p("A = M");
                p("D = M");
                p("@" + fileName + "." + memoryAccessCommand.getIndex());
                p("M = D");
                break;
            case CONSTANT:
                throw new RuntimeException("pop constant doesn't make sense");
            case POINTER:
                String ptr;
                if (memoryAccessCommand.getIndex() == 0) {
                    ptr = THIS.getAssemblyCode();
                } else {
                    ptr = THAT.getAssemblyCode();
                }
                p("@SP");
                p("M = M - 1");
                p("A = M");
                p("D = M");
                p("@" + ptr);
                p("M = D");
                break;
            case TEMP:
                storeIndexInD(memoryAccessCommand.getIndex());
                p("@" + vmSegment.getAssemblyCode());
                p("D = A + D");
                p("@TEMP");
                p("M = D");
                p("@SP");
                p("M = M - 1");
                p("A = M");
                p("D = M");
                p("@TEMP");
                p("A = M");
                p("M = D");
                break;
            case LOCAL:
            case ARGUMENT:
            case THIS:
            case THAT:
                storeIndexInD(memoryAccessCommand.getIndex());
                p("@" + vmSegment.getAssemblyCode());
                p("D = M + D");
                p("@TEMP");
                p("M = D");
                p("@SP");
                p("M = M - 1");
                p("A = M");
                p("D = M");
                p("@TEMP");
                p("A = M");
                p("M = D");
                break;
            default:
                throw new RuntimeException("No such memory segment " + vmSegment);
        }
    }

    private void pushMemorySegmentOperation(MemoryAccessCommand memoryAccessCommand) {
        VirtualMemorySegment vmSegment = memoryAccessCommand.getVirtualMemorySegment();
        storeIndexInD(memoryAccessCommand.getIndex());
        switch (vmSegment) {
            case STATIC:
                p("@" + fileName + "." + memoryAccessCommand.getIndex());
                p("D = M");
                break;
            case CONSTANT:
                break;
            case POINTER:
                String ptr;
                if (memoryAccessCommand.getIndex() == 0) {
                    ptr = THIS.getAssemblyCode();
                } else {
                    ptr = THAT.getAssemblyCode();
                }
                p("@" + ptr);
                p("D = M");
                p("@SP");
                p("A = M");
                p("M = D");
                break;
            case TEMP:
                p("@" + vmSegment.getAssemblyCode());
                p("A = A + D");
                p("D = M");
                break;
            case LOCAL:
            case ARGUMENT:
            case THIS:
            case THAT:
                p("@" + vmSegment.getAssemblyCode());
                p("A = M+D");
                p("D = M");
                break;
            default:
                throw new RuntimeException("No such memory segment " + vmSegment);
        }
    }

    private void storeIndexInD(int index) {
        p("@" + index);
        p("D = A");
    }

    private void incrementAndStoreInStackPointer() {
        p("@SP");
        p("A = M");
        p("M = D");
        p("@SP");
        p("M = M+1");
    }

    public void writeCommand(Command command) {
        count++;
        printStream.println("// " + command.toString());
        if (command.commandType().equals(CommandType.C_ARITHMETIC)) {
            writeArithmetic((ArithematicAndLogicalCommand) command);
            return;
        } else if (command.commandType().equals(CommandType.C_POP) || command.commandType().equals(CommandType
                .C_PUSH)) {
            writePushPop((MemoryAccessCommand) command);
            return;
        }
        throw new RuntimeException("Invalid commandType " + command.commandType().name());

    }

    public void close() throws IOException {
        printStream.close();
    }

    public void endProgram() {
        p("@SP");
        p("M = M+1");
    }
}
