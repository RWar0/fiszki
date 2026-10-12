package filesWork;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class FileParser {
    // ADD TO FILE(category, content, answer) -> content;;answer

    public static void createCategoryFile(String fileLocation) {
        new Thread(() -> {
            File file = new File(FileConnector.getDATABASE_FOLDER(), fileLocation + ".txt");
            boolean isExist = file.exists();
            if (!isExist) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.err.println("Something gone wrong during creating file: " + fileLocation);
                    LogParser.parseLogToFile(LogImportance.CRITICAL,"Cannot create category file - FileParser (line 26).");
                }
            } else {
                System.err.println("File with this name exist!");
                LogParser.parseLogToFile(LogImportance.WARNING,"Attempting to create a file with an existing name - FileParser (line 30).");
            }
        }).start();
    }

    public static void addQuestionToFile(String category, String content, String answer) {
        new Thread(() -> {
            String categoryFile = FileConnector.getDATABASE_FOLDER() + "/" + category+".txt";
            try {
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(categoryFile, true)));
                writer.println(content.trim()+";;"+answer.trim());
                writer.close();
            } catch (IOException e) {
                System.out.println("Error during writting to the " + categoryFile+".txt" + " file!");
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Error during writting to the " + categoryFile+".txt" + " file! - FileParser (line 44).");
            }
        }).start();
    }
    
    public static void removeCategoryFile(String categoryName) {
        String fileName = categoryName+".txt";
        File file = new File(FileConnector.getDATABASE_FOLDER(), fileName);
        if (file.exists()) {
            if (file.delete()) {
            } 
            else {
                System.err.println("Cannot remove file!");
                LogParser.parseLogToFile(LogImportance.ERROR,"Cannot remove: "+ fileName+ " - FileParser (line 57).");
            }
        } else {
            System.err.println("File doens't exist!");
            LogParser.parseLogToFile(LogImportance.WARNING,"File doesn't exist: "+ fileName+ " - FileParser (line 61).");
        }
    }
}
