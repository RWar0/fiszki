package data;

import filesWork.FileParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class DataAdministration {

    private static Map<String, List<PairQnA>> mapOfCategoriesAndQuestions;

    static {
        mapOfCategoriesAndQuestions = new HashMap<>();
    }

    // Functions:
    public static void addCategory(String categoryName) {
        boolean isTheSame;
        
        try {
            isTheSame = getCategoriesAsList().stream().filter(item -> item.equals(categoryName)).findAny().orElse(null) != null;
        } catch (NullPointerException ex) {
            isTheSame = false;
            LogParser.parseLogToFile(LogImportance.WARNING,"Null pointer exception during checking if category exist (Not important) - DataAdministration (line 29).");
        }
        
        if (!isTheSame) {
            mapOfCategoriesAndQuestions.put(categoryName, null);
        } else {
            System.err.println("There is category with the same name!");
        }
    }
    
    // adding with triming(removing) whitespaces at beggining and end.
    public static void addQuestion(String category, String content, String answer) {
        //mapOfCategoriesAndQuestions.put(category, Arrays.asList(new PairQnA(content, answer)));
        mapOfCategoriesAndQuestions.computeIfAbsent(category, k -> new ArrayList<>()).add(new PairQnA(content.trim(), answer.trim()));
    }
    
    public static void removeCategoryWithFile(String categoryName) {
        mapOfCategoriesAndQuestions.remove(categoryName);
        FileParser.removeCategoryFile(categoryName);
    }
    
    // Getters and setters
    public static List<String> getCategoriesAsList() {
        return new ArrayList<>(mapOfCategoriesAndQuestions.keySet());
    }
    
    public static List<PairQnA> getQnAFromCategory(String category) { 
        List<PairQnA> copiedList = new ArrayList<>(mapOfCategoriesAndQuestions.get(category));
        return copiedList;
    }
}
