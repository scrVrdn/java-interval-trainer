package io.github.scrvrdn;

public class Mediator implements MediatorInterface {
    private PrimaryController primary;
    private SettingsController settings;
    private SceneManager sceneManager;

    private Mediator() {}

    public static Mediator getInstance() {
        return MediatorHolder.INSTANCE;
    }

    private static class MediatorHolder {
        private static final Mediator INSTANCE = new Mediator();
    }

    public void setPrimary(PrimaryController primary) {
        this.primary = primary;
    }

    public void setSettings(SettingsController settings) {
        this.settings = settings;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void switchToScene(String sceneID) {
        this.sceneManager.showScene(sceneID);
    }

    public void closeApplication() {
        this.primary.closeApplication();
    }

    public void updateSettings(String setting, Object value) {
        // update interval directions
        if (setting.equals("directions")) this.primary.setDirections(((Byte) value).byteValue());

        // update interval set
        if (setting.equals("intervals")) this.primary.setIntervals((int) value);
        
        // update max pitch
        if (setting.equals("maxPitch")) this.primary.setMaxPitch((int) value);

        // update min pitch
        if (setting.equals("minPitch")) this.primary.setMinPitch((int) value);

        // update tempo
        if (setting.equals("tempo")) this.primary.setTempo((int) value);
    }

    public void refreshPrimary() {
        try {
            this.settings.applyAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
