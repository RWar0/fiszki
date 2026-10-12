package forms;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import logs.LogImportance;
import logs.LogParser;

/**
 * @author Rafal Waremczuk
 */

public class MainFormController implements Initializable {
    
    @FXML
    private AnchorPane mainAnchor;
    
    @FXML
    private Button addBtn;

    @FXML
    private Button learnBtn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeCenterPanel("/forms/AddForm.fxml");
        
        addBtn.setDisable(true);
        learnBtn.setDisable(false);
    }
    
    // FXML Functions
    @FXML
    public void handleAddBtn() {
        addBtn.setDisable(true);
        learnBtn.setDisable(false);
        changeCenterPanel("/forms/AddForm.fxml");
    }
    
    @FXML
    public void handleLearnBtn() {
        addBtn.setDisable(false);
        learnBtn.setDisable(true);
        changeCenterPanel("/forms/LearnForm.fxml");
    }
    
    // Additional Functions
    private void changeCenterPanel(String whichFile) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(whichFile));
        try {
            Node content = loader.load();
            AnchorPane.setTopAnchor(content, 0.0);
            AnchorPane.setBottomAnchor(content, 0.0);
            AnchorPane.setLeftAnchor(content, 0.0);
            AnchorPane.setRightAnchor(content, 0.0);
            
            mainAnchor.getChildren().setAll(content);
        } catch (IOException e) {
            System.err.println("CANNOT CHANGE ANCHOR!");
            LogParser.parseLogToFile(LogImportance.CRITICAL, "Cannot change anchor pane - MainFormController (line 65).");
        }
    }
}
