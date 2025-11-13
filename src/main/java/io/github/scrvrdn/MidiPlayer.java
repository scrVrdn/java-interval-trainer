package io.github.scrvrdn;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class MidiPlayer {
    private static final int PITCH_DURATION = 500;
    private static final int DELAY_BETWEEN_NOTES = 50;

    private Synthesizer synth;
    private Receiver receiver;
    private volatile boolean isRunning = false;

    public MidiPlayer() throws MidiUnavailableException {
        this.synth = MidiSystem.getSynthesizer();
        this.receiver = this.synth.getReceiver();
    }

    public void open() throws MidiUnavailableException {
        this.synth.open();
    }

    public boolean isOpen() {
        return this.synth.isOpen();
    }

    public void close() {
        this.isRunning = false;
        
        this.receiver.close();
        this.synth.close();
    }

    public void play(QueriedInterval interval) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        if (interval == null) throw new IllegalArgumentException("Given input was null");
        this.isRunning = true;
        if (interval.getDirection() == AppConstants.ASCENDING) ascending(interval);
        else if (interval.getDirection() == AppConstants.DESCENDING) descending(interval);
        else if (interval.getDirection() == AppConstants.SIMULTANEOUS) simultaneous(interval);
        this.isRunning = false;
    }

    private void ascending(QueriedInterval interval) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        // play lower note
        playNote(interval.getLo());
        Thread.sleep(DELAY_BETWEEN_NOTES);

        // play higher note
        playNote(interval.getHi());
    }

    private void descending(QueriedInterval interval) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        // play higher note
        playNote(interval.getHi());
        Thread.sleep(DELAY_BETWEEN_NOTES);

        // play lower note
        playNote(interval.getLo());
    }

    private void simultaneous(QueriedInterval interval) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        playMultipleNotes(new int[]{interval.getLo(), interval.getHi()});
    }

    private void playNote(int pitch) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        ShortMessage noteOn = new ShortMessage();
        noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch, 127);
        sendMidiMessage(noteOn);
        
        Thread.sleep(PITCH_DURATION);

        ShortMessage noteOff = new ShortMessage();
        noteOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, 0);
        sendMidiMessage(noteOff);
    }

    private void playMultipleNotes(int[] pitches) throws InvalidMidiDataException, IllegalStateException, InterruptedException {
        for (int pitch : pitches) {
            ShortMessage noteOn = new ShortMessage();
            noteOn.setMessage(ShortMessage.NOTE_ON, 0, pitch, 127);
            sendMidiMessage(noteOn);
        }

        Thread.sleep(PITCH_DURATION);

        for (int pitch : pitches) {
            ShortMessage noteOff = new ShortMessage();
            noteOff.setMessage(ShortMessage.NOTE_OFF, 0, pitch, 0);
            sendMidiMessage(noteOff);
        }
    }

    private void sendMidiMessage(MidiMessage message) {
        if (this.isRunning && this.receiver != null) {
            this.receiver.send(message, - 1);
        }
    }
}
