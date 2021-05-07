package View;

import Model.BoardCell;
import Model.Preferences;
import Model.SnakeProData;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.lang.Math;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JComponent;

public class SnakeProDisplay extends JComponent {
	public SnakeProData data = new SnakeProData();

	private Clip audioFood;
	private Clip audioCrunch;
	private Clip audioMeow;
	private Image imageFood;

	private int width;
	private int height;

	public SnakeProDisplay(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(false);
		this.width = width;
		this.height = height;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// clear
		g.setColor(Preferences.COLOR_BACKGROUND);
		g.fillRect(0, 0, this.width, this.height);

		// draw title
		// TODO: Part 1a

		// draw board
		for (int row = 0; row < data.getNumRows(); row ++) {
			for (int col = 0; col < data.getNumColumns(); col ++) {
				// TODO: Part 1a
			}
		}
	}

	private Clip getClip(String fileName) {
		try {
			Clip clip = AudioSystem.getClip();
			ClassLoader cl = this.getClass().getClassLoader();
			AudioInputStream ais = AudioSystem.getAudioInputStream(cl.getResource(fileName));
			clip.open(ais);
			return clip;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadResources() {
		try {
			this.audioFood = getClip("resources/Food.wav");
			this.audioCrunch = getClip("resources/crunch.wav");
			this.audioMeow = getClip("resources/cat.wav");
			this.imageFood = ImageIO.read(new File("resources/food.gif"));
			System.out.println("successful loading of audio/images!\n");
			System.out.flush();
		}
		catch (Exception e) {
			System.out.println("problem loading audio/images!\n");
			System.out.flush();
			// e.printStackTrace();
			this.audioFood = null;
			this.audioCrunch = null;
			this.audioMeow = null;
			this.imageFood = null;
		}
	}

	public void startNewGame() {
		data = new SnakeProData();
		data.setStartLocation();
		data.setStartDirection();
	}

	public static void playClip(Clip clip) {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

	/** Plays crunch noise */
	public void playSound_foodEaten() {
		playClip(this.audioCrunch);
	}

	/** Plays food noise */
	public void playSound_food() {
		playClip(this.audioFood);
	}

	/** Plays meow noise */
	public void playSound_meow() {
		playClip(this.audioMeow);
	}

	public void reverseSnake() {
		// TODO: Write helper methods in Model.SnakeProData.java and call them here!
		// Note - we provided suggestions for helper methods.
	}
}