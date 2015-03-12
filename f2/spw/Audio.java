package f2.spw;

import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;

import java.io.InputStream;
public class Audio {

	private Sequencer sequencer;

	public Audio(String src) {
		try{
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			InputStream stream = getClass().getResourceAsStream("/f2/spw/Audio/" + src);
			sequencer.setSequence(stream);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

	}

	public void start(){
		sequencer.setMicrosecondPosition(0);
		sequencer.start();		
	}
	public void pause(){
		sequencer.stop();
	}
	public void play(){
		sequencer.start();
	}
	public boolean isPlaying(){
		return sequencer.isRunning();
	}
}