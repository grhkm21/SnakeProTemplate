package Controller;

import Model.BoardCell;
import Model.Preferences;
import Model.SnakeProData;
import View.SnakeProDisplay;

import java.awt.event.*;
import java.net.URL;
import java.awt.*;
import java.io.*;

import javax.swing.*;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;

public class SnakeProBrain extends JFrame implements ActionListener, KeyListener {
	public SnakeProDisplay draw;

	public JButton newGameButton;
	public JButton pauseButton;
	public JButton startButton;

	private int cycleNum = 0;

	private static final char REVERSE = 'r';
	private static final char UP      = 'i';
	private static final char DOWN    = 'k';
	private static final char LEFT    = 'j';
	private static final char RIGHT   = 'l';
	private static final char AI_MODE = 'a';
	private static final char PLAY_FOOD_NOISE = 's';

	public SnakeProBrain() {
		this.draw = new SnakeProDisplay(600 + 10 * Preferences.CELL_SIZE, Preferences.GAMEBOARDHEIGHT);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

		this.draw.loadResources();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SnakeProBrain frame = new SnakeProBrain();
				frame.setTitle("Testing!");
				frame.setResizable(false);
				frame.setSize(600 + 10 * Preferences.CELL_SIZE, Preferences.GAMEBOARDHEIGHT);
				frame.setMinimumSize(new Dimension(600 + 10 * Preferences.CELL_SIZE, Preferences.GAMEBOARDHEIGHT));
				frame.add(frame.draw);
				frame.initializeButtons();
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	// // Here is how buttons and menu items work...
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == this.newGameButton) {
			System.out.println("New game");
			draw.startNewGame();
			// this.go();
		}
		if (source == this.pauseButton) {
			System.out.println("Paused");
			// this.pause();
		}
		if (source == this.startButton) {
			System.out.println("Started");
			// this.go();
		}
		this.requestFocus(); // makes sure the Applet keeps keyboard focus
	}

	// Add all buttons
	public void initializeButtons() {
		// add a panel for buttons
		JPanel buttonPane = new JPanel(new FlowLayout());
		buttonPane.setBackground(Preferences.COLOR_BACKGROUND);
		this.add(buttonPane, BorderLayout.PAGE_START);
		
		this.newGameButton = new JButton("New Game"); // the text in the button
		this.newGameButton.addActionListener(this); // watch for button presses
		this.newGameButton.addKeyListener(this); // listen for key presses here
		buttonPane.add(this.newGameButton); // add button to the panel

		this.pauseButton = new JButton("Pause"); // a second button
		this.pauseButton.addActionListener(this);
		this.pauseButton.addKeyListener(this);
		buttonPane.add(this.pauseButton);

		this.startButton = new JButton("Start"); // a third button
		this.startButton.addActionListener(this);
		this.startButton.addKeyListener(this);
		buttonPane.add(this.startButton);
	}

	public void keyPressed(KeyEvent e) {
		// TODO: Implement this (other keys)
		switch (e.getKeyChar()) {
			case REVERSE:
				draw.reverseSnake();
				break;
			case AI_MODE:
				draw.data.setMode_AI();
				break;
			case PLAY_FOOD_NOISE:
				draw.playSound_food();
				break;
			default:
				draw.data.setDirectionEast();
		}
	}

	// Thread thread; // the thread controlling the updates
	// boolean threadSuspended; // whether or not the thread is suspended
	// boolean running; // whether or not the thread is stopped

	public void run() {
		// while (this.running) {
		// 	try {
		// 		if (this.thread != null) {
		// 			Thread.sleep(Preferences.SLEEP_TIME);
		// 			synchronized (this) {
		// 				while (this.threadSuspended) {
		// 					this.wait(); // sleeps until notify() wakes it up
		// 				}
		// 			}
		// 		}
		// 	} catch (InterruptedException e) {
		// 		;
		// 	}

		// 	this.cycle(); // this represents 1 update cycle for the environment
		// }
		// this.thread = null;
	}

	public synchronized void go() {
		// if (this.thread == null) {
		// 	this.thread = new Thread(this);
		// 	this.running = true;
		// 	this.thread.start();
		// 	this.threadSuspended = false;
		// } else {
		// 	this.threadSuspended = false;
		// }
		// this.notify(); // wakes up the call to wait(), above
	}

	public void pause() {
		// if (this.thread != null) {
		// 	this.threadSuspended = true;
		// }
	}

	public synchronized void stop() {
		// this.running = false;
		// this.notify();
	}

	public void cycle() {
		// move the snake
		if (this.cycleNum % Preferences.REFRESH_RATE == 0) {
			this.updateSnake();
		}

		// update the list of Food
		this.updateFood();

		// draw the board
		draw.repaint();

		// update the cycle counter
		this.cycleNum ++;
	}

	public void gameOver() {
		this.pause();
		draw.data.setGameOver();
		draw.playSound_meow();
	}

	public void updateSnake() {
		BoardCell nextCell;
		if (draw.data.inAImode()) {
			nextCell = draw.data.getNextCellFromBFS();
		} else {
			nextCell = draw.data.getNextCellInDir();
		}
		this.advanceTheSnake(nextCell);
	}

	public void advanceTheSnake(BoardCell nextCell) {
		if (nextCell.isWall() || nextCell.isBody()) {
			// Oops...we hit something.
			this.gameOver();
			return;
		} else if (nextCell.isFood()) {
			draw.playSound_foodEaten();
			// TODO: Possibly add code here to tell draw 
			//       the snake ate food!
		} else {
			// just regular movement into an open space
			// TODO: Possibly add code to tell draw the snake moved
		}
		// TODO: Possibly add code here too!
		// You'll need to add helper methods to Model.SnakeProData.java
	}

	public void updateFood() {
		if (draw.data.noFood()) {
			draw.data.addFood();
		} else if (this.cycleNum % Preferences.FOOD_ADD_RATE == 0) {
			draw.data.addFood();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	// For testing only
	public SnakeProBrain(TestGame gameNum) {
		this.draw.data = SnakeProData.getTestGame(gameNum);
	}
}
