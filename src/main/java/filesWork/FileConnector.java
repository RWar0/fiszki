package filesWork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class FileConnector {

    private static final String DATABASE_FOLDER;

    static {
        DATABASE_FOLDER = System.getenv("APPDATA") + File.separator + "RWApps"+ File.separator + "Fiszki" + File.separator + "database";
    }
    // Categories to list

    public static void initFiles() {
        createMainDatabaseFolder();
        categoryFilesToList();
        categoriesDataToMap();
    }

    private static void categoryFilesToList() {
        File folder = new File(DATABASE_FOLDER);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            for (File file : files) {
                String categoryName[] = file.getName().split("\\.");
                data.DataAdministration.addCategory(categoryName[0]);
            }
        } else {
            System.err.println("There isn't database folder!");
            LogParser.parseLogToFile(LogImportance.CRITICAL,"Database folder doesn't exist - FileConnector (line 40).");
        }
    }

    private static void categoriesDataToMap() {
        File folder = new File(DATABASE_FOLDER);

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            for (File file : files) {
                parseDataToCategoryList(file);
            }
            
        } else {
            System.err.println("There isn't database folder!");
            LogParser.parseLogToFile(LogImportance.CRITICAL,"Database folder doesn't exist - FileConnector (line 56).");
        }
    }
    private static void parseDataToCategoryList(File currentFile) {
        new Thread(() -> {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(currentFile));
                String category = currentFile.getName().split("\\.")[0];
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        String[] element = line.split(";;");
                        data.DataAdministration.addQuestion(category, element[0], element[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Error during reading file: " + currentFile.getName() + "\nCannot add one of elements to List!");
                        LogParser.parseLogToFile(LogImportance.ERROR,"Error during reading file: " + currentFile.getName() + ". Cannot add one of elements to List! - FileConnector (line 40).");
                    }
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error during reading file: " + currentFile.getName());
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during reading file: " + currentFile.getName() + " - FileConnector (line 77).");
            }
        }).start();
    }
    
    private static void createMainDatabaseFolder() {
        File databaseFolder = new File(DATABASE_FOLDER);
        if (!databaseFolder.exists()) {
            try {
                databaseFolder.mkdirs();
            } catch (Exception e) {
                System.err.println("Something goes wrong during creating database!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during creating database folder - FileConnector (line 77).");
            }
        }
    }

    // Getters and setters
    public static String getDATABASE_FOLDER() {
        return DATABASE_FOLDER;
    }

}
