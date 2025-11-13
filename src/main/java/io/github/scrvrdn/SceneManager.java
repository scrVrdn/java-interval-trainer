package io.github.scrvrdn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private Map<String, Scene> cachedScenes;
    private Stage stage;

    public SceneManager(Stage stage) {
        this.cachedScenes = new HashMap<>();
        this.stage = stage;
        Mediator.getInstance().setSceneManager(this);

        preloadScenes();
    }

    public void showScene(String sceneID) {
        Scene scene = this.cachedScenes.get(sceneID);
        if (scene != null) {
            this.stage.setScene(scene);
            this.stage.show();
        }
    }

    public Scene getScene(String scene) {
        return this.cachedScenes.get(scene);
    }

    private void preloadScenes() {
        this.cachedScenes.put("settings", loadScene("settings.fxml"));
        this.cachedScenes.put("primary", loadScene("primary.fxml"));        
    }

    private Scene loadScene(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            return scene;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
