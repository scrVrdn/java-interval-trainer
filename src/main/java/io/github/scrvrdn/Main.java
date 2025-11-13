package io.github.scrvrdn;

import javafx.application.Application;
import javafx.stage.Stage;



public class Main extends Application {
    
    private SceneManager sceneManager;

    @Override
    public void start(Stage stage) {
        this.sceneManager = new SceneManager(stage);
        this.sceneManager.showScene("primary");

        stage.setTitle("Simple Interval Trainer");
        
        stage.setOnCloseRequest(event -> {
            event.consume();
            Mediator.getInstance().closeApplication();
        });
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}