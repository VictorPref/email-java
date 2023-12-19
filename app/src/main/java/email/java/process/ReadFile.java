package email.java.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {

    String directoryPath;

    public ReadFile(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<File> getFiles() {

        List<File> msgFiles = new ArrayList<>();

        File folder = new File(directoryPath);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".msg")) {
                        msgFiles.add(file);
                    }
                }
            }
        } else {
            System.out.println("The specified folder does not exist or is not a directory.");
        }

        return msgFiles;

    }
}
