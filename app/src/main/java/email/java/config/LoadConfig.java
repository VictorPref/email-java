package email.java.config;

import email.java.App;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoadConfig {

    private static LoadConfig INSTANCE;

    private Map<String, String> filters;
    private String path;
    private String pathEmail;

    public Map<String, String> getFilters() {
        return filters;
    }

    public String getPathEmail() {
        return pathEmail;
    }

    private LoadConfig() {
    }

    public static LoadConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LoadConfig();
        }

        return INSTANCE;
    }

    public void loadProperties() {
        String jarFilePath = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            jarFilePath = URLDecoder.decode(jarFilePath, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }

        path = replaceLastSegment(jarFilePath, "filtre.txt");
        pathEmail = replaceLastSegment(jarFilePath, "email");

        try {
            // Read the content of the file with UTF-8 encoding
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            filters = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                // Split each line by a delimiter (e.g., ";") and put it into the dictionary
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    filters.put(parts[0], parts[1]);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String replaceLastSegment(String path, String replacement) {
        int lastSlashIndex = path.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < path.length() - 1) {
            String newPath = path.substring(0, lastSlashIndex + 1) + replacement;
            return newPath;
        } else {
            return path + replacement;
        }
    }

    public void readFiltre() {
        // Display the contents of the dictionary
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }

}
