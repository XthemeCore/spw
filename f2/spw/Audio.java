package f2.spw;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequencer;

import java.io.InputStream;

public class Audio{

	private Sequencer sequencer;

	public Audio(String src) {
		set(src);
	}

	public void set(String src){
		try{
			sequencer = MidiSystem.getSequencer();
			if(!sequencer.isOpen())
				sequencer.open();
			InputStream stream = getClass().getResourceAsStream("/f2/spw/Audio/" + src);
			sequencer.setSequence(stream);
		} catch (Exception e) {
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
		if(sequencer.isRunning())
			return true;
		else
			return false;
	}

}