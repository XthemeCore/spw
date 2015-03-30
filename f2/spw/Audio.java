package f2.spw;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequencer;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.InvalidMidiDataException;

import java.io.IOException;
import java.io.InputStream;

public class Audio {

	private Sequencer sequencer;
	private boolean loop;
	//private int loopCount;

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
		play(false);

	}
	public void play(boolean loop){
		this.loop = loop;
		pause();
		start();
	}
	public void updatePlay(){
		if(loop)
			sequencer.addMetaEventListener(new MetaEventListener() {
				public void meta(MetaMessage m) {
					if (m.getType() == 47)
						start();
				}
			});
	}
	public boolean isPlaying(){
		return sequencer.isRunning();
	}
}