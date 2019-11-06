import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    String path;
    String inputExt = "vm";
    String outputExt = "asm";

    public Boolean validateFileType(String path) {
        String[] fileNameArr = path.split("\\.");
        String ext = fileNameArr[fileNameArr.length - 1];
        return ext.equals(this.inputExt);
    }

    public String getHackFilePath(String path) {
        String[] fileNameArr = path.split("\\.");
        fileNameArr[fileNameArr.length - 1] = this.outputExt;
        return String.join(".", fileNameArr);
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

    public void translateFile(BufferedReader br, BufferedWriter bw) throws Exception {
        Formatter formatter = new Formatter();
        // Translator translator = new Translator();
        String st;
        ArrayList<String> contents = new ArrayList<String>();
        
        while((st = br.readLine()) != null) {
            contents.add(st);
        }
        
        List<String> formattedContents = formatter.format(contents);
        // List<String> binaryContents = translator.translate(formattedContents);
        formattedContents.forEach(line -> {
            try {
                bw.write(line + "\n");
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        
        br.close();
        bw.close();
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
                    System.out.println(path);
                    String outputFilePath = this.getHackFilePath(path);
                    BufferedReader br = this.getBufferedReader(path);
                    BufferedWriter bw = this.getBufferedWriter(outputFilePath);
                    this.translateFile(br, bw);
                }  
            }
        }
    }

    public FileParser(String args[]) throws Exception {
        this.recurseThroughFiles(args);
    }
}