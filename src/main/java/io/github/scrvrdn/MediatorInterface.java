package io.github.scrvrdn;
public interface MediatorInterface {
    void setPrimary(PrimaryController primary);
    void setSettings(SettingsController settings);
    void setSceneManager(SceneManager sceneManager);
    void switchToScene(String scene);
    void updateSettings(String setting, Object value);
    void refreshPrimary();
    void closeApplication();
}
