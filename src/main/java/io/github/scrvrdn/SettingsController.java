package io.github.scrvrdn;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class SettingsController {
    private byte currentDirections = 0;
    private byte newDirections = AppConstants.ASCENDING | AppConstants.DESCENDING | AppConstants.SIMULTANEOUS;
    private int currentIntervals = 0;
    private int newIntervals = 0b00011111111111111110;
    
    // directions
    @FXML private CheckBox ascending;
    @FXML private CheckBox descending;
    @FXML private CheckBox simultaneous;

    // intervals
    @FXML private CheckBox selectAll;
    @FXML private CheckBox deselectAll;

    private CheckBox[] intervalCheckBoxes;
    
    @FXML private CheckBox unison;
    @FXML private CheckBox minSecond;
    @FXML private CheckBox majSecond;
    @FXML private CheckBox minThird;
    @FXML private CheckBox majThird;
    @FXML private CheckBox fourth;
    @FXML private CheckBox tritone;
    @FXML private CheckBox fifth;
    @FXML private CheckBox minSixth;
    @FXML private CheckBox majSixth;
    @FXML private CheckBox minSeventh;
    @FXML private CheckBox majSeventh;
    @FXML private CheckBox octave;
    @FXML private CheckBox minNinth;
    @FXML private CheckBox majNinth;
    @FXML private CheckBox minTenth;
    @FXML private CheckBox majTenth;
    @FXML private CheckBox eleventh;
    @FXML private CheckBox augEleventh;
    @FXML private CheckBox twelfth;

    // pitch range
    private int currentMaxPitch = AppConstants.MAX_MIDI_VALUE;
    private int maxPitch;
    private int currentMinPitch = AppConstants.MIN_MIDI_VALUE;
    private int minPitch;
    private int minSpan = Interval.length() - 1;
    @FXML private Spinner<Integer> maxPitchSpinner;
    @FXML private Spinner<Integer> minPitchSpinner;

    // control buttons
    @FXML private Button apply;
    @FXML private Button discard;


    public void initialize() {
        Mediator.getInstance().setSettings(this);
        checkSelectedDirections();

        this.intervalCheckBoxes = new CheckBox[]{unison, minSecond, majSecond, minThird, majThird, fourth, tritone, fifth, minSixth, majSixth, minSeventh, majSeventh, octave, minNinth, majNinth, minTenth, majTenth, eleventh, augEleventh, twelfth};

        addBitValueToIntervals();
        checkSelectedIntervals();

        // initialize pitch range spinners
        SpinnerValueFactory<Integer> maxPitchValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(AppConstants.MIN_MIDI_VALUE, AppConstants.MAX_MIDI_VALUE, this.currentMaxPitch);
        this.maxPitchSpinner.setValueFactory(maxPitchValueFactory);
        this.maxPitch = this.maxPitchSpinner.getValue();
        
        this.maxPitchSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.maxPitch = newValue;
            int minValue = this.maxPitch - this.minSpan;
            if (this.minPitch > minValue) {
                this.minPitchSpinner.getValueFactory().setValue(minValue);
                this.minPitch = minValue;
            }
        });

        SpinnerValueFactory<Integer> minPitchValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(AppConstants.MIN_MIDI_VALUE, AppConstants.MAX_MIDI_VALUE, this.currentMinPitch);
        this.minPitchSpinner.setValueFactory(minPitchValueFactory);
        this.minPitch = this.minPitchSpinner.getValue();

        minPitchSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.minPitch = newValue;
            int maxValue = this.minPitch + this.minSpan;
            if (this.maxPitch < maxValue) {
                this.maxPitchSpinner.getValueFactory().setValue(maxValue);
                this.maxPitch = maxValue;
            }
        });
    }

    // checkmark all selected directions
    private void checkSelectedDirections() {
        ascending.setSelected((this.newDirections & AppConstants.ASCENDING) == AppConstants.ASCENDING);
        descending.setSelected((this.newDirections & AppConstants.DESCENDING) == AppConstants.DESCENDING);
        simultaneous.setSelected((this.newDirections & AppConstants.SIMULTANEOUS) == AppConstants.SIMULTANEOUS);
    }

    // associate interval checkboxes with their corresponding bit representation
    private void addBitValueToIntervals() {
        int bit = 1;
        for (CheckBox cb : this.intervalCheckBoxes){
            cb.setUserData(bit);
            bit <<= 1;
        }
    }

    // checkmark all selected intervals
    private void checkSelectedIntervals() {
        for (CheckBox cb : this.intervalCheckBoxes) {
            if ((this.newIntervals & (int) cb.getUserData()) == (int) cb.getUserData()) cb.setSelected(true);
        }
    }

    // select directions
    @FXML
    private void setDirections() {
        if (ascending.isSelected()) {
            this.newDirections |= AppConstants.ASCENDING;
        } else {
            this.newDirections &= ~AppConstants.ASCENDING;
        }

        if (descending.isSelected()) {
            this.newDirections |= AppConstants.DESCENDING;
        } else {
            this.newDirections &= ~AppConstants.DESCENDING;
        }

        if (simultaneous.isSelected()) {
            this.newDirections |= AppConstants.SIMULTANEOUS;
        } else {
            this.newDirections &= ~AppConstants.SIMULTANEOUS;
        }

        // System.out.println(this.newDirections);
    }
    
    // select all intervals
    @FXML
    private void selectAllIntervals() {
        if (deselectAll.isSelected()) deselectAll.setSelected(false);
        for (CheckBox cb : this.intervalCheckBoxes) {
            cb.setSelected(true);
            this.newIntervals = AppConstants.MAX_INTERVALS;
        }
    }

    @FXML
    private void deselectAllIntervals() {
        if (selectAll.isSelected()) selectAll.setSelected(false);
        for (CheckBox cb : this.intervalCheckBoxes) {
            cb.setSelected(false);
            this.newIntervals = 0;
        }
    }

    // add an interval to or remove it from the set
    @FXML
    private void selectInterval(ActionEvent event) {
        if (selectAll.isSelected()) selectAll.setSelected(false);
        if (deselectAll.isSelected()) deselectAll.setSelected(false);

        CheckBox source = (CheckBox) event.getSource();
        if (source.isSelected()) {
            this.newIntervals |= (int) source.getUserData();
        } else {
            this.newIntervals &= ~((int) source.getUserData());
        }
        
        // System.out.println(this.newIntervals);
    }

    // TODO: set tempo

    // TODO: load profile

    // TODO: save profile

    // TODO: save new profile

    // TODO: set as default profile


    @FXML
    protected void applyAll() throws IOException {
        validateChanges();

        Mediator mediator = Mediator.getInstance();
        if (this.newDirections != this.currentDirections) {
            mediator.updateSettings("directions", Byte.valueOf(this.newDirections));
            this.currentDirections = this.newDirections;
            // System.out.println(this.newDirections);
        }

        if (this.newIntervals != this.currentIntervals) {
            mediator.updateSettings("intervals", Integer.valueOf(this.newIntervals));
            this.currentIntervals = this.newIntervals;
            // System.out.println(this.newIntervals);
        }

        if (this.maxPitch != this.currentMaxPitch) {
            mediator.updateSettings("maxPitch", Integer.valueOf(this.maxPitch));
            this.currentMaxPitch = this.maxPitch;
        }

        if (this.minPitch != this.currentMinPitch) {
            mediator.updateSettings("minPitch", Integer.valueOf(this.minPitch));
            this.currentMinPitch = this.minPitch;
        }

        switchToPrimary();
    }

    private void validateChanges() {
        if (this.newDirections == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("At least one direction must be selected!");
            alert.showAndWait();
            return;
        }

        if (this.newIntervals == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("At least one interval must be selected!");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void discardChanges() throws IOException {
        if (this.newDirections != this.currentDirections) {
            this.newDirections = this.currentDirections;
            checkSelectedDirections();
        }

        if (this.newIntervals != this.currentIntervals) {
            this.newIntervals = this.currentIntervals;
            checkSelectedIntervals();
        }

        // TODO: reset pitch range
    }

    @FXML
    private void switchToPrimary() throws IOException {
        discardChanges();
        Mediator.getInstance().switchToScene("primary");        
    }
    
}