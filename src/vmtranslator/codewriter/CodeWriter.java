package vmtranslator.codewriter;

import vmtranslator.commands.*;
import vmtranslator.enums.MemoryAccessOperation;
import vmtranslator.enums.VirtualMemorySegment;
import vmtranslator.utils.Utils;

import java.io.*;

import static vmtranslator.enums.VirtualMemorySegment.THAT;
import static vmtranslator.enums.VirtualMemorySegment.THIS;

public class CodeWriter {

    private PrintStream printStream = null;
    private String fileName;
    private int count;

    public CodeWriter(String fileName) throws FileNotFoundException {
        setFileName(fileName);
    }

    public void setFileName(String fileName) throws FileNotFoundException {
        File outputFile = new File(fileName);
        this.fileName = Utils.generateOutputFileNameWithoutExtension(fileName);
        OutputStream os = new FileOutputStream(outputFile);
        printStream = new PrintStream(os);
    }

    private void writeArithmetic(ArithmeticAndLogicalCommand arithematicAndLogicalCommand) {
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
                throw new RuntimeException("No such arithmetic command: " + arithematicAndLogicalCommand.toString());
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

    public void writeInit() {

    }

    private void incrementSP() {
        p("@SP");
        p("M = M+1");
    }

    void p(String string) {
        printStream.println(string);
    }

    private void writePushPop(MemoryAccessCommand memoryAccessCommand) {
        if (memoryAccessCommand.getMemoryAccessOperation().equals(MemoryAccessOperation.PUSH)) {
            pushMemorySegmentOperation(memoryAccessCommand);
            incrementAndStoreInStackPointer();
        } else if (memoryAccessCommand.getMemoryAccessOperation().equals(MemoryAccessOperation.POP)) {
            popMemorySegmentOperation(memoryAccessCommand);
        }
    }

    private void writeGoto(GotoCommand command) {
        p("@" + command.getLabelName());
        p("0;JMP");
    }

    private void writeLabel(LabelCommand command) {
        p("(" + command.getLabelName() + ")");
    }

    private void writeIf(IfGotoCommand command) {
        p("@SP");
        p("M = M - 1");
        p("A = M");
        p("D = M");
        p("@" + command.getLabelName());
        p("D;JNE");
    }

    private void writeFunction(FunctionCommand command) {
        p("(" + command.getClassMethodName() + ")");
        // initialize local variables
        // nvar times 0
        p("@" + command.getNvars());
        p("D = A");
        p("@NVAR" + count);
        p("M = D");
        p("(PUSHING-NVAR" + count + ")");
        p("@NVAR" + count);
        p("D = M");
        p("@END-PUSH-NVAR" + count);
        p("D;JEQ");
        writePushPop(new MemoryAccessCommand("push constant 0"));
        p("@NVAR" + count);
        p("M = M-1");
        p("@PUSHING-NVAR" + count);
        p("0;JMP");
        p("(END-PUSH-NVAR" + count + ")");

    }

    private void writeCall(CallCommand command) {

    }

    private void writeReturn(ReturnCommand command) {
        // moves return value to the caller
        // reinstates caller state
        // goto Foo.ret.1

        // push return value <--- already done?

        // endframe = LCL
        p("@LCL");
        p("A = M");
        p("D = A");
        p("@ENDFRAME" + count);
        p("M = D");

        // retAddress = *(endFrame - 5)
        p("@ENDFRAME" + count);
        p("D = M");
        storeDAtTopOfStackAndIncrementSP();
        writePushPop(new MemoryAccessCommand("push constant 5"));
        writeArithmetic(new ArithmeticAndLogicalCommand("sub"));
        popTopElementIntoD();
        p("A = D");
        p("D = M");
        p("@RET_ADDR" + count);
        p("M = D");

        // SP = ARG + 1 (just save arg)
        p("@ARG");
        p("D = M");
        p("@OLD_ARG");
        p("M = D");
//        storeDAtTopOfStackAndIncrementSP();

        // ARG[0] = pop()
//        storeDAtTopOfStackAndIncrementSP();
        p("@ARG");
        p("M = D");

        // THAT = *(endframe - 1)
        p("@ENDFRAME" + count);
        p("D = M");
        storeDAtTopOfStackAndIncrementSP();
        writePushPop(new MemoryAccessCommand("push constant 1"));
        writeArithmetic(new ArithmeticAndLogicalCommand("sub"));
//        storeTopElementInDAndIncrementSP();
        popTopElementIntoD();

        p("A = D");
        p("D = M");
        p("@THAT");
        p("M = D");

        // THIS = *(endframe - 2)
        p("@ENDFRAME" + count);
        p("D = M");
        storeDAtTopOfStackAndIncrementSP();
        writePushPop(new MemoryAccessCommand("push constant 2"));
        writeArithmetic(new ArithmeticAndLogicalCommand("sub"));
//        storeTopElementInDAndIncrementSP();
        popTopElementIntoD();

        p("A = D");
        p("D = M");
        p("@THIS");
        p("M = D");


        // ARG = *(endframe - 3)
        p("@ENDFRAME" + count);
        p("D = M");
        storeDAtTopOfStackAndIncrementSP();
        writePushPop(new MemoryAccessCommand("push constant 3"));
        writeArithmetic(new ArithmeticAndLogicalCommand("sub"));
//        storeTopElementInDAndIncrementSP();
        popTopElementIntoD();

        p("A = D");
        p("D = M");
        p("@ARG");
        p("M = D");


        // LCL = *(endframe - 4)
        p("@ENDFRAME" + count);
        p("D = M");
        storeDAtTopOfStackAndIncrementSP();
        writePushPop(new MemoryAccessCommand("push constant 4"));
        writeArithmetic(new ArithmeticAndLogicalCommand("sub"));
//        storeTopElementInDAndIncrementSP();
        popTopElementIntoD();

        p("A = D");
        p("D = M");
        p("@LCL");
        p("M = D");

        // SP = ARG + 1
//        storeTopElementInDAndIncrementSP();
        popTopElementIntoD();
        p("@OLD_ARG");
        p("A = M");
        p("M = D");
        p("D = A + 1");
        p("@SP");
        p("M = D");

        p("@RET_ADDR" + count);
        p("A = M");
        p("0;JMP");
//        writeGoto(new GotoCommand("goto RET_ADDR" + count));
    }

    private void popTopElementIntoD() {
        p("@SP");
        p("M = M-1");
        p("A = M");
        p("D = M");
    }

    private void storeTopElementInDAndIncrementSP() {
        p("@SP");
        p("M = M-1");
        p("A = M");
        p("D = M");
        incrementSP();
    }

    private void storeDAtTopOfStackAndIncrementSP() {
        p("@SP");
        p("A = M");
        p("M = D");
        incrementSP();
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
        switch (command.commandType()) {
            case C_ARITHMETIC:
                writeArithmetic((ArithmeticAndLogicalCommand) command);
                break;
            case C_PUSH:
            case C_POP:
                writePushPop((MemoryAccessCommand) command);
                break;
            case C_LABEL:
                writeLabel((LabelCommand) command);
                break;
            case C_GOTO:
                writeGoto((GotoCommand) command);
                break;
            case C_IF:
                writeIf((IfGotoCommand) command);
                break;
            case C_FUNCTION:
                writeFunction((FunctionCommand) command);
                break;
            case C_RETURN:
                writeReturn((ReturnCommand) command);
                break;
            case C_CALL:
                writeCall((CallCommand) command);
                break;
        }

    }

    public void close() throws IOException {
        printStream.close();
    }

    public static void main(String[] args) {
        String command = "pop local 0         // initializes sum = 0";
        if (command.contains("//")) {
            command = command.substring(0, command.indexOf("//"));
        }
        System.out.println(command);

    }
}
