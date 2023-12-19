package email.java.process;

import java.io.File;
import java.util.List;

import email.java.config.LoadConfig;
import email.java.model.Email;

public class EmailProcess {

    public static void process(String directoryPath) {

        ReadFile reader = new ReadFile(directoryPath);

        System.out.println(directoryPath);

        List<File> msgFiles = reader.getFiles();

        for (File file : msgFiles) {
            Email email = new Email(file);
            try {
                email.readFile();
                System.out.println(email.fileName());
                renameFile(file, email);
            } catch (Exception e) {
                System.out.println(e);
            }
        }

    }

    public static void renameFile(File sourceFile, Email email) {

        File targetFile = new File(LoadConfig.getInstance().getPathEmail() + "/" + email.fileName());

        if (targetFile.exists()) {
            String baseName = targetFile.getName().replaceFirst("[.][^.]+$", ""); // Remove the file extension
            String extension = targetFile.getName().substring(baseName.length());
            int i = 1;

            while (targetFile.exists()) {
                String newName = baseName + " (" + i + ")" + extension;
                targetFile = new File(targetFile.getParent(), newName);
                i++;
            }
        }

        if (sourceFile.exists()) {
            if (sourceFile.renameTo(targetFile)) {
                System.out.println("File renamed successfully: " + targetFile.getName());
            } else {
                System.out.println("Failed to rename the file: " + sourceFile.getName());
            }
        } else {
            System.out.println("Source file does not exist.");
        }
    }

}
