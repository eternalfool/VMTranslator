package vmtranslator.utils;

public class Utils {
    public static String generateOutputFileName(String fileName) {
        String fName = fileName.split("\\.")[0];
        return fName + ".asm";
    }

    public static String generateOutputFileNameWithoutExtension(String fileName) {
        String fName = fileName.split("\\.")[0];
        String[] justFileNames = fName.split("/");
        return justFileNames[justFileNames.length - 1];
    }

    // Test
    public static void main(String[] args) {
        System.out.println(generateOutputFileNameWithoutExtension("/Users/shashwat/Softwares/nand2tetris/nand2tetris/07/MemoryAccess/StaticTest/StaticTest.vm"));
    }

}
