import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    String path;
    String inputExt = "vm";
    String outputExt = "asm";
    BufferedWriter bw;

    public Boolean validateFileType(String path) {
        String[] fileNameArr = path.split("\\.");
        String ext = fileNameArr[fileNameArr.length - 1];
        return ext.equals(this.inputExt);
    }

    public String getAssemblyFilePath(String path) {
        String[] fileNameArr = path.split("\\/");
        String AssemblyFileName = fileNameArr[fileNameArr.length - 1] + "." + this.outputExt;
        return String.join("/", fileNameArr) + "/" + AssemblyFileName;
    }

    public String getShortFileName(String path) {
        String[] fileNameArr = path.split("\\/");
        String[] fileNameArr2 = fileNameArr[fileNameArr.length - 1].split("\\.");
        return fileNameArr2[0];
    }

    public BufferedReader getBufferedReader(String path) throws Exception {
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        return br;
    }

    public BufferedWriter getBufferedWriter(String path) throws Exception {
        File file = new File(path);
        FileWriter fr = new FileWriter(file);
        BufferedWriter br = new BufferedWriter(fr);
        return br;
    }

    public void translateFile(BufferedReader br, String fileName) throws Exception {
        Formatter formatter = new Formatter();
        Translator translator = new Translator(fileName);
        String st;
        ArrayList<String> contents = new ArrayList<String>();
        
        while((st = br.readLine()) != null) {
            contents.add(st);
        }
        
        List<String> formattedContents = formatter.format(contents);
        List<String> assemblyCode = translator.translate(formattedContents);
        assemblyCode.forEach(line -> {
            try {
                bw.write(line + "\n");
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        br.close();
    }

    public void writeBootstrapCode() {
        List<String> bootstrapCode = Translator.getBootstrapCode();
        bootstrapCode.forEach(line -> {
            try {
                bw.write(line + "\n");
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void recurseThroughFiles(String args[]) throws Exception {
        for (String path: args) {
            File filePath = new File(path);
            if(filePath.isDirectory()) {
                File[] listOfFiles = filePath.listFiles();
                String[] fileNames = new String[listOfFiles.length];
                for(int i = 0; i < listOfFiles.length; i++) {
                    fileNames[i] = path + "/" + listOfFiles[i].getName();
                }
                recurseThroughFiles(fileNames);
            } else if(filePath.isFile()) {
                Boolean isValidFileType = this.validateFileType(path);
                if(isValidFileType) {
                    String fileName = this.getShortFileName(path);
                    BufferedReader br = this.getBufferedReader(path);
                    this.translateFile(br, fileName);
                }
            }
        }
    }

    public FileParser(String args[]) throws Exception {
        String AssemblyFilePath = this.getAssemblyFilePath(args[0]);
        bw = this.getBufferedWriter(AssemblyFilePath);
        this.writeBootstrapCode();
        this.recurseThroughFiles(args);
        bw.close();
    }
}