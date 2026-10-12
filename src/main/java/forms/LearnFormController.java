package forms;

import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import logs.LogImportance;
import logs.LogParser;
import play.Play;

/**
 * @author Rafal Waremczuk
 */
public class LearnFormController implements Initializable {

    @FXML
    private ComboBox<String> categorySelection;
    @FXML
    private Label contentLabel;
    @FXML
    private Label correctAnswerLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label questionNoLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Button nextQuestionBtn;
    @FXML
    private Button startWithCategoryBtn;
    @FXML
    private TextField userAnswerField;
    @FXML
    private CheckBox doesCharactersCaseMatterCheckBox;
    
    private static boolean finish;
    
    
    private static boolean check;
    
    static {
        check = true;
        finish = false;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            try {
                com.fiszki.App.getFilesWorkThread().join();
            } catch (InterruptedException e) {
                LogParser.parseLogToFile(LogImportance.CRITICAL,"Thread Exception(InterruptedException): wait for FilesWorkThread().join() - LearnFormController (line 57).");
            }
            try {
                data.DataAdministration.getCategoriesAsList().stream().forEach(item -> categorySelection.getItems().add(item));
            } catch (NullPointerException ex) {
                System.err.println("Categories list empty!");
                LogParser.parseLogToFile(LogImportance.ERROR,"Null pointer exception in category list (empty list) - LearnFormController (line 63).");
            }
        }).start();
        errorLabel.setText("");
        nextQuestionBtn.setDisable(true);
        correctAnswerLabel.setText("");
        nextQuestionBtn.setDisable(true);
        nextQuestionBtn.setText("Sprawdź");
        questionNoLabel.setText("");
        totalLabel.setText("");
        contentLabel.setText("Czekam na rozpoczęcie");
        userAnswerField.setDisable(true);
        categorySelection.setDisable(false);
        correctAnswerLabel.setTextFill(Color.web("#009703"));
        correctAnswerLabel.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        correctAnswerLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        nextQuestionBtn.setVisible(false);
        doesCharactersCaseMatterCheckBox.setSelected(false);
    }

    // FXML Functions
    @FXML
    public void handleStartWithCategoryBtn() {
        if(!finish) {
            if (categorySelection.getValue() == null || categorySelection.getValue().isBlank()) {
                errorLabel.setText("Wybierz kategorię!");
            } else {
                errorLabel.setText("");
                Play.initRound(doesCharactersCaseMatterCheckBox.isSelected(), categorySelection.getValue());
                if (Play.isStarted() && (Play.getCurrentContent() != null || Play.getCurrentContent().isBlank())) {
                    userAnswerField.setDisable(false);
                    categorySelection.setDisable(true);
                    contentLabel.setText(Play.getCurrentContent());
                    correctAnswerLabel.setText("");
                    nextQuestionBtn.setDisable(false);
                    nextQuestionBtn.setText("Sprawdź");
                    questionNoLabel.setText(Play.getCurrentQuestionNumber()+"");
                    totalLabel.setText("/ "+Play.getQuestionQuantity());
                    finish = true;
                    startWithCategoryBtn.setText("Zakończ");
                    nextQuestionBtn.setVisible(true);
                } else {
                    errorLabel.setText("Nie można rozpocząć!");
                }
            }
        }
        else {
            forceEnd();
        }
    }
    
    @FXML
    public void handleEnterOnUserAnswerField(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            handleNextQuestionBtn();
            userAnswerField.requestFocus();
        }
    }
    
    @FXML
    public void handleNextQuestionBtn() {
        if(check) {
            if(Play.isCorrect(userAnswerField.getText())) {
                correctAnswerLabel.setTextFill(Color.web("#009703"));
                correctAnswerLabel.setText("Brawo!");
            }
            else {
                correctAnswerLabel.setTextFill(Color.web("#ff0000"));
                correctAnswerLabel.setText("Błędne!\nPoprawnie: " + Play.getCurrentAnswer());
            }
            check = false;
            nextQuestionBtn.setText("Dalej");
        }
        else {
            correctAnswerLabel.setTextFill(Color.web("#009703"));
            if(Play.isStarted()) {
                Play.setNextQuestion();
                contentLabel.setText(Play.getCurrentContent());
                correctAnswerLabel.setText("");
                nextQuestionBtn.setText("Sprawdź");
                questionNoLabel.setText(Play.getCurrentQuestionNumber()+"");
                userAnswerField.requestFocus();
            }
            else {
                setCorrectAnswerLabelAtTheEnd();
                contentLabel.setText("Runda zakończona");
                nextQuestionBtn.setVisible(false);
                nextQuestionBtn.setDisable(true);
                userAnswerField.setDisable(true);
                categorySelection.setDisable(false);
                finish = false;
                startWithCategoryBtn.setText("Rozpocznij");
            }
            check = true;
            userAnswerField.setText("");
        }
    }
    
    // Addidtional funtions
    private void forceEnd() {
        correctAnswerLabel.setTextFill(Color.web("ffa000"));
        correctAnswerLabel.setText("Koniec\nWynik: " + Play.getPoints() + " / " + (Play.getCurrentQuestionNumber()-1));
        contentLabel.setText("Runda zatrzymana przed końcem wszystkich pytań. Poddałe(a)ś się ?");
        nextQuestionBtn.setDisable(true);
        nextQuestionBtn.setVisible(false);
        userAnswerField.setDisable(true);
        categorySelection.setDisable(false);
        finish = false;
        startWithCategoryBtn.setText("Rozpocznij");
    }
    
    private void setCorrectAnswerLabelAtTheEnd() {
        double calculatePercent = ((double)Play.getPoints()/(double)Play.getQuestionQuantity())*100;
        DecimalFormat formater = new DecimalFormat("#.##");
        formater.setRoundingMode(RoundingMode.HALF_UP);
        String textForLabel = "Koniec\nWynik: " + Play.getPoints() + " / " + Play.getQuestionQuantity() + " ("+formater.format(calculatePercent)+"%)\n";
        correctAnswerLabel.setTextFill(Color.web("#009703"));
        if(calculatePercent < 50){
            correctAnswerLabel.setTextFill(Color.web("#ff0000"));
        }
        if(calculatePercent == 0) {
            textForLabel += "Najgorszy wynik!";
        }
        else if(calculatePercent < 30) {
            textForLabel += "Wynik niezadowalający!";
        }
        else if(calculatePercent < 60) {
            textForLabel += "Wynik nie najlepszy, nie najgorszy!";
        }
        else if(calculatePercent < 80) {
            textForLabel += "Wynik już trochę zadowalający!";
        }
        else if(calculatePercent < 100) {
            textForLabel += "Świetny wynik!";
        }
        else if(calculatePercent == 100) {
            textForLabel += "Rewelacyjne odpowiedzi!";
        }
        else {
            textForLabel += "Coś nie tak z obliczeniami";
        }
        correctAnswerLabel.setText(textForLabel);
    }
}
