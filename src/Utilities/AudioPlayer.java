package Utilities;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	private Clip clip;
	
	public AudioPlayer(String s) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResource("/res/" + s)); // Loads the file
			AudioFormat baseFormat = ais.getFormat(); // and creates a data stream with it
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false); // decode it in the way i want the audio to be
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais); // and makes sure the audio clip is ready to be used
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public void play() {
		if (clip == null) return; // return if the clip couldn't have been load, to prevent a crash of the game
		stop(); // if the clip is arleady playing, stop it
		clip.setFramePosition(0); // and start it from the begginning
		clip.start();
	}
	
	public void restart() {
		if (clip == null) return; // return if the clip couldn't have been load, to prevent a crash of the game
		stop(); // if the clip is arleady playing, stop it
		clip.start(); // and start it from the point it was left
	}
	
	public void stop() {
		if (clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
	
	public boolean isActive() {
		return clip.isRunning(); // returns a boolean value, whether the clip is playing (and so the user hears it) or not
	}
	
	public void SetVolume(float volume) {
		  if (volume < 0f || volume > 1f) // makes sure the volume isn't more than 1 or less than 0 
		        throw new IllegalArgumentException("Volume not valid: " + volume); // to prevent a crash in case of a mistake
		    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // uses a FloatControl to gain the settings management of the audio
		    gainControl.setValue(20f * (float) Math.log10(volume)); // and changes the volume to the function's input
	}
	
}
