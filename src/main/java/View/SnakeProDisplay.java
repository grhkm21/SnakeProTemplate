package View;

import Model.BoardCell;
import Model.Preferences;
import Model.SnakeProData;

import java.util.LinkedList;
import java.awt.event.*;
import java.util.Queue;
import java.lang.Math;
import java.net.URL;
import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;


public class SnakeProDisplay extends JComponent {
	public SnakeProData data = new SnakeProData();

	private Clip audioFood;
	private Clip audioCrunch;
	private Clip audioMeow;

	private int width;
	private int height;

	private Image imageFood;

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
		g.setFont(Preferences.TITLE_FONT);
		g.setColor(Preferences.TITLE_COLOR);
		g.drawString(Preferences.TITLE, Preferences.TITLE_X, Preferences.TITLE_Y);

		// draw board
		for (int row = 0; row < data.getNumRows(); row ++) {
			for (int col = 0; col < data.getNumColumns(); col ++) {
				Color curColor = data.getCellColor(row, col);
				g.setColor(curColor);
				BoardCell curCell = data.getCell(row, col);
				int cellX = col * Preferences.CELL_SIZE;
				int cellY = row * Preferences.CELL_SIZE;
				g.fillRect(cellX + 20, cellY + Preferences.TITLE_Y + 20, Preferences.CELL_SIZE, Preferences.CELL_SIZE);
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadResources() {
		try {
			this.audioFood = getClip("resources/Food.au");
			this.audioCrunch = getClip("resources/crunch.au");
			this.audioMeow = getClip("resources/cat.au");
			this.imageFood = ImageIO.read(new File("resources/food.gif"));
			System.out.println("successful loading of audio/images!\n");
			System.out.flush();
		} catch (Exception e) {
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
		data.placeSnakeAtStartLocation();
		data.setStartDirection();
		this.repaint();
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