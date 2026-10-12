package forms;

import data.DataAdministration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */
public class AddFormController implements Initializable {
    
    @FXML
    private ComboBox<String> categorySelectionBox;
    @FXML
    private TextField contentsField;
    @FXML
    private TextField answerField;
    @FXML
    private TextField addCategoryField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button removeCategoryBtn;
    
    // init
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorLabel.setText("");
        errorLabel.setTextFill(Color.web("#ff0000"));
        removeCategoryBtn.setDisable(true);
        removeCategoryBtn.setVisible(false);
        new Thread(() -> {
            try {
                com.fiszki.App.getFilesWorkThread().join();
            } catch (InterruptedException e) {
                LogParser.parseLogToFile(LogImportance.CRITICAL, "Thread Exception(InterruptedException): wait for FilesWorkThread().join() - AddFormController (line 44).");
            }
            try {
                data.DataAdministration.getCategoriesAsList().stream().forEach(item -> categorySelectionBox.getItems().add(item));
            } catch(NullPointerException ex) {
                System.err.println("Categories list empty!");
                LogParser.parseLogToFile(LogImportance.ERROR, "Null pointer exception in category list (empty list) - AddFormController (line 50).");
            }
        }).start();
        
    }
    
    
    // FXML methods
    @FXML
    public void handleAddQuestionyBtn() {
        errorLabel.setTextFill(Color.web("#ff0000"));
        if(categorySelectionBox.getValue() == null || categorySelectionBox.getValue().isBlank()) {
            errorLabel.setText("Wybierz kategorie!");
        }
        else if(contentsField.getText().isBlank() || answerField.getText().isBlank()) {
            errorLabel.setText("Uzupełnij pola!");
        }
        else {
            filesWork.FileParser.addQuestionToFile(categorySelectionBox.getValue(), contentsField.getText(), answerField.getText());
            DataAdministration.addQuestion(categorySelectionBox.getValue(), contentsField.getText(), answerField.getText());
            errorLabel.setTextFill(Color.web("#009703"));
            errorLabel.setText("Dodano");
            contentsField.setText("");
            answerField.setText("");
            contentsField.requestFocus();
        }
    }
    
    @FXML
    public void handleEnterOnAddQuestionFields(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            handleAddQuestionyBtn();
            contentsField.requestFocus();
        }
    }
    
    @FXML
    public void handleAddCategoryBtn() {
        errorLabel.setTextFill(Color.web("#ff0000"));
        if(!addCategoryField.getText().isBlank() && addCategoryField.getText() != null) {
            
            filesWork.FileParser.createCategoryFile(addCategoryField.getText());
            data.DataAdministration.addCategory(addCategoryField.getText());
            
            categorySelectionBox.getItems().add(addCategoryField.getText());
            errorLabel.setTextFill(Color.web("#009703"));
            errorLabel.setText("Dodano kategorię");
            addCategoryField.setText("");
        }
        else {
            errorLabel.setText("Uzupełnij nazwę kategorii!");
        }
    }
    
    @FXML
    public void handleEnterOnAddCategoryField(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            handleAddCategoryBtn();
        }
    }
    
    @FXML
    public void checkIfSelectedCategory() {
        if(categorySelectionBox.getValue() != null && !categorySelectionBox.getValue().isBlank()) {
            removeCategoryBtn.setDisable(false);
            removeCategoryBtn.setVisible(true);
            contentsField.requestFocus();
        }
    }
    
    @FXML
    public void handleRemoveCategoryBtn() {
        errorLabel.setTextFill(Color.web("#ff0000"));
        if(categorySelectionBox.getValue() != null && !categorySelectionBox.getValue().isBlank()) {
            DataAdministration.removeCategoryWithFile(categorySelectionBox.getValue());
            categorySelectionBox.getItems().remove(categorySelectionBox.getValue());
            categorySelectionBox.getSelectionModel().clearSelection();
            removeCategoryBtn.setDisable(true);
            removeCategoryBtn.setVisible(false);
            errorLabel.setText("Usunięto kategorię");
        }
        else {
            System.err.println("Selected category is empty!");
        }
    }
}
