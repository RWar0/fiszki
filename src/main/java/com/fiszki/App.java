package com.fiszki;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.layout.StackPane;
import logs.LogParser;


public class App extends Application {
    private static Thread filesWorkThread;
    
    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = FXMLLoader.load(getClass().getResource("/forms/MainForm.fxml"));
        Scene scene = new Scene(root);
        
        stage.setTitle("Fiszki");
        //stage.setResizable(false);
        
        stage.setScene(scene);
        
        stage.show();
    }
    
    public static void main(String[] args) {
        
        new Thread(() -> {
            LogParser.initLogs();
        }).start();
        
        filesWorkThread = new Thread(() -> {
            filesWork.FileConnector.initFiles();
        });
        filesWorkThread.start();
        
        launch();
    }
    
    // Getters and setters
    public static Thread getFilesWorkThread() {
        return filesWorkThread;
    }
}