import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Translator {
    int increment = 0;
    String fileName;
    String currentFunctionName = "";
    String commandComment = "";
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
        put("pop", "C_POP");
        put("label", "C_LABEL");
        put("goto", "C_GOTO");
        put("if-goto", "C_IF");
        put("function", "C_FUNCTION");
        put("call", "C_CALL");
        put("return", "C_RETURN");
    }};

    public String getCommandComment(String[] commandArr) {
        return "// " + String.join(" ", commandArr);
    }

    public List<String> writeArithmetic(String[] commandArr) {
        String fileIncrement = this.fileName + this.increment;
        if(commandArr[0].equals("add")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D+M", "@SP", "M=M-1"
            });
        } else if(commandArr[0].equals("sub")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=M-D", "@SP", "M=M-1"
            });
        } else if(commandArr[0].equals("neg")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "M=-M"
            });
        } else if(commandArr[0].equals("eq")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                this.commandComment, "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + fileIncrement, "D;JEQ",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + fileIncrement, "D;JMP", "(Comp" + fileIncrement + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + fileIncrement + ")",
                "@SP", "M=M+1"
            });
            this.increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("gt")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                this.commandComment, "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + fileIncrement, "D;JGT",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + fileIncrement, "D;JMP", "(Comp" + fileIncrement + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + fileIncrement + ")",
                "@SP", "M=M+1"
            });
            this.increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("lt")) {
            List<String> translatedCommand = Arrays.asList(new String[]{
                this.commandComment, "@SP", "M=M-1", "A=M", "D=M", "M=0", "A=A-1", "D=M-D", "@Comp" + fileIncrement, "D;JLT",
                "@SP", "M=M-1", "A=M", "M=0", "@Done" + fileIncrement, "D;JMP", "(Comp" + fileIncrement + ")",
                "@SP", "M=M-1", "A=M", "M=-1", "(Done" + fileIncrement + ")",
                "@SP", "M=M+1"
            });
            this.increment += 1;
            return translatedCommand;
        } else if(commandArr[0].equals("and")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D&M", "@SP", "M=M-1"
            });
        } else if(commandArr[0].equals("or")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "D=M", "M=0", "A=A-1", "M=D|M", "@SP", "M=M-1"
            });
        } else if(commandArr[0].equals("not")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "A=M-1", "M=!M"
            });
        } else { // should never be reached
            return Arrays.asList();
        }
    }

    public List<String> writePush(String[] commandArr) {
        if(commandArr[1].equals("constant")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        } else if(commandArr[1].equals("local")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@LCL", "D=D+M", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1" 
            });      
        } else if(commandArr[1].equals("argument")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@ARG", "D=D+M", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        } else if(commandArr[1].equals("this")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@THIS", "D=D+M", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });
        } else if(commandArr[1].equals("that")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@THAT", "D=D+M", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        } else if(commandArr[1].equals("temp")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@R5", "D=D+A", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        } else if(commandArr[1].equals("pointer")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@R3", "D=D+A", "A=D", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        } else if(commandArr[1].equals("static")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + this.fileName + commandArr[2], "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1"
            });      
        }
        return Arrays.asList();
    }

    public List<String> writePop(String[] commandArr) {
        if(commandArr[1].equals("constant")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "M=M-1", "A=M", "M=0"
            });      
        } else if(commandArr[1].equals("local")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@LCL", "D=D+M", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });      
        } else if(commandArr[1].equals("argument")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@ARG", "D=D+M", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });      
        } else if(commandArr[1].equals("this")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@THIS", "D=D+M", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });      
        } else if(commandArr[1].equals("that")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@THAT", "D=D+M", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });      
        } else if(commandArr[1].equals("temp")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@R5", "D=D+A", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });
        } else if(commandArr[1].equals("pointer")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@" + commandArr[2], "D=A", "@R3", "D=D+A", "@R13", "M=D", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@R13", "A=M", "M=D"
            });
        } else if(commandArr[1].equals("static")) {
            return Arrays.asList(new String[]{
                this.commandComment, "@SP", "M=M-1", "A=M", "D=M", "M=0", "@" + this.fileName + commandArr[2], "M=D"
            });
        }
        return Arrays.asList();
    }

    public List<String> writeLabel(String[] commandArr) {
        return Arrays.asList(new String[]{
            this.commandComment, "(" + this.currentFunctionName + "$" + commandArr[1] + ")"
        });
    }

    public List<String> writeGoto(String[] commandArr) {
        return Arrays.asList(new String[]{
            this.commandComment, "@" + this.currentFunctionName + "$" + commandArr[1], "D;JMP"
        });
    }

    public List<String> writeIf(String[] commandArr) {
        return Arrays.asList(new String[]{
            this.commandComment, "@SP", "M=M-1", "A=M", "D=M", "@" + this.currentFunctionName + "$" + commandArr[1], "D;JNE"
        });
    }

    public List<String> writeCall(String[] commandArr) {
        String returnString = commandArr[1] + "$return." + increment; 
        List<String> callCommand = Arrays.asList(new String[]{
            this.commandComment,
            "// - push return-address", "@" + returnString, "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push LCL", "@LCL", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push ARG", "@ARG", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push THIS", "@THIS", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push THAT", "@THAT", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - ARG = SP-n-5", "@SP", "D=M", "@" + commandArr[2], "D=D-A", "@5", "D=D-A", "@ARG", "M=D",
            "// - LCL = SP", "@SP", "D=M", "@LCL", "M=D",
            "// - goto f", "@" + commandArr[1], "D;JMP",
            "// - (return-address)", "(" + returnString + ")"
        });
        increment += 1;
        return callCommand;
    }

    public List<String> writeFunction(String[] commandArr) {
        this.currentFunctionName = commandArr[1];
        List<String> translatedCommand = new ArrayList<String>(Arrays.asList(new String[] {
            this.commandComment, "// - (f)", "(" + this.currentFunctionName + ")",
        }));
        List<String> initializeLocal = Arrays.asList(new String[] {
            "// - initialize local variable", "@SP", "A=M", "M=0", "@SP", "M=M+1"
        });

        for(int i = 0; i < Integer.parseInt(commandArr[2]); i++) {
            translatedCommand.addAll(initializeLocal);
        } 
        return translatedCommand;
    }

    public List<String> writeReturn(String[] commandArr) {
         return Arrays.asList(new String[]{
            this.commandComment,
            "// - FRAME = LCL", "@LCL", "D=M", "@R13", "M=D",
            "// - RET = *(FRAME-5)", "@R13", "D=M", "@5", "D=D-A", "A=D", "D=M", "@R14", "M=D",
            "// - *ARG = pop()", "@SP", "M=M-1", "A=M", "D=M", "M=0", "@ARG", "A=M", "M=D",
            "// - SP = ARG+1", "@ARG", "D=M", "@SP", "M=D+1",
            "// - THAT = *(FRAME-1)", "@R13", "D=M", "@1", "D=D-A", "A=D", "D=M", "@THAT", "M=D",
            "// - THIS = *(FRAME-2)", "@R13", "D=M", "@2", "D=D-A", "A=D", "D=M", "@THIS", "M=D",
            "// - ARG = *(FRAME-3)", "@R13", "D=M", "@3", "D=D-A", "A=D", "D=M", "@ARG", "M=D",
            "// - LCL = *(FRAME-4)", "@R13", "D=M", "@4", "D=D-A", "A=D", "D=M", "@LCL", "M=D",
            "// - goto RET", "@R14", "A=M", "D;JMP"
        });
    }

    public List<String> translateLineToAssembly(String line) {
        String[] commandArr = line.split(" ");
        String command = this.commandMap.get(commandArr[0]);
        this.commandComment = this.getCommandComment(commandArr);
        if(command.equals("C_ARITHMETIC")) {
            return this.writeArithmetic(commandArr);
        } else if(command.equals("C_PUSH")) {
            return this.writePush(commandArr);
        } else if(command.equals("C_POP")) {
            return this.writePop(commandArr);
        } else if(command.equals("C_LABEL")) {
            return this.writeLabel(commandArr);
        } else if(command.equals("C_GOTO")) {
            return this.writeGoto(commandArr);
        } else if(command.equals("C_IF")) {
            return this.writeIf(commandArr);
        } else if(command.equals("C_FUNCTION")) {
            return this.writeFunction(commandArr);
        } else if(command.equals("C_CALL")) {
            return this.writeCall(commandArr);
        } else if(command.equals("C_RETURN")) {
            return this.writeReturn(commandArr);
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

    public static List<String> getBootstrapCode() {
        String returnString = "Bootstrap.$return";
        List<String> bootstrapCode = Arrays.asList(new String[] {
            "// Boostrap code", "@256", "D=A", "@SP", "M=D",
            "// - Call Sys.init",
            "// - push return-address", "@" + returnString, "D=A", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push LCL", "@LCL", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push ARG", "@ARG", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push THIS", "@THIS", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - push THAT", "@THAT", "D=M", "@SP", "A=M", "M=D", "@SP", "M=M+1",
            "// - ARG = SP-n-5", "@SP", "D=M", "@0", "D=D-A", "@5", "D=D-A", "@ARG", "M=D",
            "// - LCL = SP", "@SP", "D=M", "@LCL", "M=D",
            "// - goto f", "@Sys.init", "D;JMP",
            "// - (return-address)", "(" + returnString + ")"
        });
        return bootstrapCode;
    }

    public Translator(String fileName) {
        this.fileName = fileName;
    }
}