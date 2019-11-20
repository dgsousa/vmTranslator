import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Translator {
    int increment = 0;
    Map<String, String> commandMap = new HashMap<String, String>() {{
        put("add", "C_ARITHMETIC");
        put("sub", "C_ARITHMETIC");
        put("neg", "C_ARITHMETIC");
        put("eq", "C_ARITHMETIC");
        put("gt", "C_ARITHMETIC");
        put("lt", "C_ARITHMETIC");
        put("and", "C_ARITHMETIC");
        put("or", "C_ARITHMETIC");
        put("not", "C_ARITHMETIC");
        put("push", "C_PUSH");
    }};

    public List<String> writeArithmetic(String[] commandArr) {
        if(commandArr[0].equals("add")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D+M", "@SP", "M=M-1"
            });
            return translatedCommand;
        } else if(commandArr[0].equals("sub")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=M-D", "@SP", "M=M-1"
            });
            return translatedCommand;
        } else if(commandArr[0].equals("neg")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "M=-M"
            });
            return translatedCommand;
        } else if(commandArr[0].equals("eq")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + increment, "D;JEQ",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + increment, "D;JMP", "(Comp" + increment + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + increment + ")",
                "@SP", "M=M+1"
            });
            increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("gt")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + increment, "D;JGT",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + increment, "D;JMP", "(Comp" + increment + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + increment + ")",
                "@SP", "M=M+1"
            });
            increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("lt")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + increment, "D;JLT",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + increment, "D;JMP", "(Comp" + increment + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + increment + ")",
                "@SP", "M=M+1"
            });
            increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("and")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D&M", "@SP", "M=M-1"
            });
            return translatedCommand;
        } else if(commandArr[0].equals("or")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D|M", "@SP", "M=M-1"
            });
            return translatedCommand;
        } else {
            List<String> translatedCommand = Arrays.asList(new String[]{
                "@SP", "A=M-1", "M=!M"
            });
            return translatedCommand;
        }
    }

    public List<String> writePushPop(String[] commandArr) {
        if(commandArr[0].equals("push") && commandArr[1].equals("constant")) {
            List<String> translatedCommand = Arrays.asList(new String[]{ "@" + commandArr[2], "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1" });      
            return translatedCommand;
        }
        return Arrays.asList();
    }

    public List<String> translateLineToAssembly(String line) {
        String[] commandArr = line.split(" ");
        String command = commandArr[0];
        String commandType = commandMap.get(command);
        if(commandType.equals("C_ARITHMETIC")) {
            return writeArithmetic(commandArr);
        } else if(commandType.equals("C_PUSH")) {
            return writePushPop(commandArr);
        }
        return Arrays.asList();
    }
    
    public List<String> translate(List<String> contents) {
        List<String> assemblyCode = contents
            .stream()
            .flatMap(line -> translateLineToAssembly(line).stream())
            .collect(Collectors.toList());
        return assemblyCode;
    }
}