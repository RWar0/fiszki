package play;

import data.DataAdministration;
import data.PairQnA;
import java.util.List;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class Play {
    private static int points;
    private static int questionQuantity;
    private static int currentQuestionNumber;
    
    private static List<PairQnA> listQnA;
    private static PairQnA currentQnA;
    
    private static boolean started;
    private static boolean doesCharactersCaseMatter;
    
    static {
        points = 0;
        questionQuantity = 0;
        currentQuestionNumber = 1;
        started = false;
        doesCharactersCaseMatter = false;
    }
    
    public static void initRound(boolean lookAtCharsCase, String categoryName) {
        try {
            listQnA = DataAdministration.getQnAFromCategory(categoryName);
            if(!listQnA.isEmpty()) {
                int random = (int) (Math.random() * listQnA.size());
                currentQnA = listQnA.get(random);
                questionQuantity = listQnA.size();
                points = 0;
                currentQuestionNumber = 1;
                started = true;
                doesCharactersCaseMatter = lookAtCharsCase;
            }
        } catch (Exception e) {
            started = false;
            System.err.println("CANNOT INIT ROUND!");
            LogParser.parseLogToFile(LogImportance.ERROR,"Cannot init round (probably empty category) - Play (line 42).");
        }
    }
    
    public static boolean isCorrect(String answer) {
        if (doesCharactersCaseMatter && answer.equals(getCurrentAnswer())) {
            points++;
            return true;
        }
        else if (!doesCharactersCaseMatter && answer.equalsIgnoreCase(getCurrentAnswer())){
            points++;
            return true;
        }
        else {
            return false;
        }
    }
    
    public static void setNextQuestion() {
        currentQuestionNumber++;
        listQnA.remove(currentQnA);
        started = listQnA.size() > 1;
        if(!listQnA.isEmpty()) {
            int random = (int) (Math.random() * listQnA.size());
            currentQnA = listQnA.get(random);
        }
        else {
            started = false;
        }
    }
    
    
    public static String getCurrentContent() {
        return currentQnA.getContent();
    }
    
    public static String getCurrentAnswer() {
        return currentQnA.getAnswer();
    }
    
    public static boolean isStarted() {
        return started;
    }
    
    public static int getPoints() {
        return points;
    }
    public static int getQuestionQuantity() {
        return questionQuantity;
    }
    public static int getCurrentQuestionNumber() {
        return currentQuestionNumber;
    }
}
