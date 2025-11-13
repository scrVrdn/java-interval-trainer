package io.github.scrvrdn;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryController {
    
    private Button[] intervalButtons;
    @FXML private Button unison;
    @FXML private Button minSecond;
    @FXML private Button majSecond;
    @FXML private Button minThird;
    @FXML private Button majThird;
    @FXML private Button fourth;
    @FXML private Button tritone;
    @FXML private Button fifth;
    @FXML private Button minSixth;
    @FXML private Button majSixth;
    @FXML private Button minSeventh;
    @FXML private Button majSeventh;
    @FXML private Button octave;
    @FXML private Button minNinth;
    @FXML private Button majNinth;
    @FXML private Button minTenth;
    @FXML private Button majTenth;
    @FXML private Button eleventh;
    @FXML private Button augEleventh;
    @FXML private Button twelfth;

    @FXML private Button playButton;
    @FXML private Button nextButton;
    @FXML private Button startQuitButton;

    @FXML private Label statsLabel;

    private MidiPlayer mp;
    private IntervalFactory intervalFactory;
    private QueriedInterval interval;
    
    private boolean settingsChanged = false;    
    
    public void initialize() {
        Mediator mediator = Mediator.getInstance();
        mediator.setPrimary(this);
        this.intervalFactory = new IntervalFactory();
       
        try {
            this.mp = new MidiPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.intervalButtons = new Button[]{unison, minSecond, majSecond, minThird, majThird, fourth, tritone, fifth, minSixth, majSixth, minSeventh, majSeventh, octave, minNinth, majNinth, minTenth, majTenth, eleventh, augEleventh, twelfth};

        bindIntervalsToButtons();
        mediator.refreshPrimary();

        disableAllButtons();     
    }

    private void bindIntervalsToButtons() {
        int i = 0;
        for (Button button : this.intervalButtons) {
            button.setUserData(Interval.getInterval(i));
            i++;
        }
    }
    
    private void disableUnusedButtons() {
        for (Button button : this.intervalButtons) {
            if (!this.intervalFactory.contains((Interval) button.getUserData())) button.setDisable(true);
            else button.setDisable(false);
        }
    }

    private void disableAllButtons() {
        for (Button button : this.intervalButtons) {
            button.setDisable(true);
        }
    }

    private void enableUsedButtons() {
        for (Button button : this.intervalButtons) {
            if (this.intervalFactory.contains((Interval) button.getUserData())) button.setDisable(false);
        }
    }

    @FXML
    private void switchToSettings() throws IOException {
        Mediator.getInstance().switchToScene("settings");
    }
    
    @FXML
    private void handleStartQuitButton() {
        enableUsedButtons();
        try {
            if (!this.mp.isOpen()) this.mp.open();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        playButton.setDisable(false);
        nextButton.setDisable(false);

        startQuitButton.setText("Quit");
        startQuitButton.setOnAction(e -> handleCloseButton());
        handleNextButton();
    }

    @FXML
    private void handleNextButton() {
        try {
            Platform.runLater(() -> {
            this.interval = this.intervalFactory.buildInterval();
            resetButtonColors();

            if (this.settingsChanged) {
                disableUnusedButtons();
                this.settingsChanged = false;
            }

            new Thread(() -> handlePlayButton()).start();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlayButton() {
        // code for debugging:
        // System.out.println("Looking for: " + this.interval.getInterval() + ", Midi values: " + this.interval.getLo() + " - " + this.interval.getHi() + ", Dir: " + this.interval.getDirection());
        
        try {
            if (!this.mp.isOpen()) this.mp.open();
            this.mp.play(this.interval);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
   @FXML
    private void handleAnswerButton(ActionEvent event) {        
        Button source = (Button) event.getSource();
        // System.out.println(source.getUserData());
        if ((Interval) source.getUserData() != this.interval.getInterval()) {
            source.getStyleClass().add("wrong");
        } else {
            handleNextButton();
        }        
    }

    private void resetButtonColors() {
        for (Button button : intervalButtons) {
            button.getStyleClass().remove("wrong");
        }
    }

    @FXML
    private void handleCloseButton() {
        closeApplication();               
    }

    void closeApplication() {
        if (mp != null && mp.isOpen()) {
            mp.close();
            System.out.println("Synth is closed: " + !(this.mp.isOpen()));
        }
        Platform.exit(); 
    }

    /*************************** */
    /* Applying new settings    */
    /****************************/
    public void setDirections(byte newDirections) {
        this.intervalFactory.setIntervalDirections(newDirections);
    }

    public void setIntervals(int newIntervals) {
        this.intervalFactory.setIntervals(newIntervals);
        this.settingsChanged = true;
    }
}
