package Controller;

import static Model.Preferences.*;
import static Model.SnakeMode.*;

import Model.BoardCell;
import Model.CustomButton;
import Model.SnakeProData;
import View.SnakeProDisplay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

public class SnakeProBrain extends JFrame implements ActionListener, KeyListener {
	public SnakeProDisplay draw;

	private CustomButton newGameButton;
	private CustomButton pauseButton;
	private CustomButton startButton;
	private CustomButton wrapButton;
	private CustomButton voronoiButton;
	private CustomButton scoresLabel;
	private JPanel buttonPanel;

	private boolean receivedInstructions = false;

	private Timer userTimer;
	private Timer bfsTimer;
	private Timer wallTimer;
	private int cycleNum;

	private Timer typeTimer;

	private ArrayList<Character> allowed = new ArrayList<Character>();

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SnakeProBrain frame = new SnakeProBrain();
				frame.setTitle("Testing!");
				frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
				frame.setResizable(false);
				frame.initializeButtons();
				frame.add(frame.draw);
				frame.getRootPane().setBorder(
						BorderFactory.createMatteBorder(CELLS_BUFFER, CELLS_BUFFER, CELLS_BUFFER, CELLS_BUFFER, COLOR_BACKGROUND));
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	public SnakeProBrain() {
		// in the code below, all this.draw will be referenced as draw
		this.draw = new SnakeProDisplay(GAMEBOARD_WIDTH, SPACE_FOR_LABELS + GAMEBOARD_HEIGHT);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		// special characters allowed before game starts
		allowed.add(TOGGLE_PAUSE);
		allowed.add(CHEAT);
		allowed.add(CONFIRM);
		allowed.add(DELETE);
		allowed.add(NEW_GAME);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (userTimer.isRunning()) {
					userTimer.stop();
				}
				if (bfsTimer.isRunning()) {
					bfsTimer.stop();
				}
				if (wallTimer.isRunning()) {
					wallTimer.stop();
				}
			}
		});
		wallTimer = new Timer(USER_SLEEP_TIME, this);
		wallTimer.start();
	}

	// Here is how buttons and menu items work...
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == this.userTimer) {
			this.userCycle();
		} else if (source == this.bfsTimer) {
			this.snakeCycle();
		} else if (source == this.wallTimer) {
			draw.repaint();
		} else if (source == this.startButton) {
			this.go();
		} else if (source == this.pauseButton) {
			this.pause();
		} else if (source == this.newGameButton) {
			draw.startNewGame();
			this.go();
		} else if (source == this.wrapButton) {
			draw.data.toggleWrap();
			wrapButton.setText("Wrap: " + (draw.data.getWrap() ? "ON" : "OFF"));
		} else if (source == this.voronoiButton) {
			draw.toggleVoronoi();
			voronoiButton.setText("Voronoi: " + (draw.getVoronoi() ? "ON" : "OFF"));
		} else {
			System.out.println("Source " + source.toString() + " not recognised. Maybe it is not suppported?");
		}
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
	}

	public void initializeButtons() {
		buttonPanel = new JPanel(new GridLayout(2, 3));
		buttonPanel.setPreferredSize(new Dimension(GAMEBOARD_WIDTH, SPACE_FOR_BUTTONS));
		buttonPanel.setLocation(CELLS_BUFFER, 0);
		buttonPanel.setBackground(COLOR_BACKGROUND);

		newGameButton = new CustomButton("New Game");
		newGameButton.addActionListener(this);
		// newGameButton.addKeyListener(this);
		buttonPanel.add(newGameButton);

		pauseButton = new CustomButton("Pause");
		pauseButton.addActionListener(this);
		// pauseButton.addKeyListener(this);
		buttonPanel.add(pauseButton);

		startButton = new CustomButton("Start");
		startButton.addActionListener(this);
		// startButton.addKeyListener(this);
		buttonPanel.add(startButton);

		wrapButton = new CustomButton("Wrap: ON");
		wrapButton.addActionListener(this);
		// wrapButton.addKeyListener(this);
		buttonPanel.add(wrapButton);

		voronoiButton = new CustomButton("Voronoi: OFF");
		voronoiButton.addActionListener(this);
		// voronoiButton.addKeyListener(this);
		buttonPanel.add(voronoiButton);

		scoresLabel = new CustomButton("[Score will be here]");
		// scoresLabel.addActionListener(this);
		// scoresLabel.addKeyListener(this);
		buttonPanel.add(scoresLabel);

		this.add(buttonPanel);	
	}

	public void keyPressed(KeyEvent e) {
		if (userTimer == null && !allowed.contains(e.getKeyChar())) {
			return;
		}
		if (receivedInstructions && userTimer.isRunning()) {
			return;
		}
		switch (e.getKeyChar()) {
			case NEW_GAME:
				draw.startNewGame();
				this.go();
				break;

			case PLAY_FOOD_NOISE:
				draw.playSound_food();
				break;

			case REVERSE:
				draw.data.reverseSnake(0);
				break;

			case BFS:
				draw.data.setMode(0, BFS_MODE);
				break;

			case UP:
				receivedInstructions = true;
				draw.data.setMode(0, GOING_NORTH);
				break;

			case DOWN:
				receivedInstructions = true;
				draw.data.setMode(0, GOING_SOUTH);
				break;

			case RIGHT:
				receivedInstructions = true;
				draw.data.setMode(0, GOING_EAST);
				break;

			case LEFT:
				receivedInstructions = true;
				draw.data.setMode(0, GOING_WEST);
				break;

			case TOGGLE_PAUSE:
				if (userTimer == null || draw.data.getGameOver()) {
					draw.startNewGame();
					this.go();
				} else if (userTimer.isRunning()) {
					this.pause();
				} else {
					this.go();
				}
				break;

			case CHEAT:
				draw.startNewGame();
				int cx = 12;
				for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
					for (int i = 1; i <= cx; i ++) {
						for (int j = 1; j <= cx; j ++) {
							if (i == 1 && j <= 2) continue;
							int k = (i % 2 == 1 ? j : cx + 1 - j);
							draw.data.growSnake(snakeID, draw.data.getCell(i, j));
						}
					}
					draw.data.setMode(snakeID, BFS_MODE);
				}
				this.go();
				break;

			case CONFIRM:
			 	draw.data.checkCode();
				draw.data.setCode("");
				break;

			case DELETE:
				draw.data.setCode(draw.data.getCode().substring(0, Math.max(0, draw.data.getCode().length() - 1)));
				break;

			case WRAP:
				draw.data.toggleWrap();
				wrapButton.setText("Wrap: " + (draw.data.getWrap() ? "ON" : "OFF"));
				break;
			
			case VORONOI:
				draw.toggleVoronoi();
				voronoiButton.setText("Voronoi: " + (draw.getVoronoi() ? "ON" : "OFF"));

			default:
				break;
		}
	}

	public void keyTyped(KeyEvent e) {
		if (!isValid(e.getKeyChar())) {
			return;
		}
		this.typed(e.getKeyChar());
	}

	private boolean isValid(char c) {
		return '0' <= c && c <= '9';
	}

	private void typed(char c) {
		ActionListener chr = new ActionListener() {
			public void actionPerformed(ActionEvent evnt) {
				 // display of this message is basically an action which is associated with swing timer until timer stops
				System.out.println(String.format("Cheat code expired. Clearing buffer (%s).", draw.data.getCode()));
				draw.data.setCode("");
			}
		};
		if (typeTimer != null && typeTimer.isRunning()) {
			typeTimer.stop();
		}
		c = Character.toUpperCase(c);
		System.out.println("Typed " + c + " " + (int) c);
		draw.data.setCode(draw.data.getCode() + c);
		typeTimer = new Timer(3000, chr);
		typeTimer.setRepeats(false);
		typeTimer.start();
		// this method is used to deal with cheat codes
	}

	public void go() {
		this.cycleNum = 0;
		// clear old timer
		System.out.println(userTimer + " " + bfsTimer);
		if (userTimer != null && bfsTimer != null && userTimer.isRunning() && bfsTimer.isRunning()) {
			userTimer.stop();
			bfsTimer.stop();
		}
		System.out.println(userTimer + " " + bfsTimer);
		userTimer = new Timer(USER_SLEEP_TIME, this);
		userTimer.setRepeats(true);
		userTimer.start();
		bfsTimer = new Timer(BFS_SLEEP_TIME, this);
		bfsTimer.setRepeats(true);
		bfsTimer.start();
		System.out.println(userTimer + " " + bfsTimer);
		draw.go();
		
		draw.repaint();
	}

	public void pause() {
		if (userTimer.isRunning()) {
			userTimer.stop();
		}
		if (bfsTimer.isRunning()) {
			bfsTimer.stop();
		}
		draw.pause();
	}

	public void userCycle() {
		receivedInstructions = false;
		this.updateSnake(0);
		this.updateFood();
		draw.repaint();
		this.cycleNum ++;
		scoresLabel.setText(draw.data.getScores());
	}

	public void snakeCycle() {
		for (int snakeID = 1; snakeID < SNAKES; snakeID ++) {
			this.updateSnake(snakeID);
		}
		this.updateFood();
		draw.repaint();
		this.cycleNum ++;
		scoresLabel.setText(draw.data.getScores());
	}

	public void gameOver(int snakeID) {
		draw.data.setGameOver(snakeID); 
		if (draw.data.getGameOver()) {
			userTimer.stop();
			bfsTimer.stop();
			draw.playSound_meow();
		}
	}

	public void updateSnake(int snakeID) {
		if (draw.data.getGameOver(snakeID)) {
			return;
		}
		BoardCell nextCell;
		if (draw.data.inBFSMode(snakeID)) {
			nextCell = draw.data.getNextCellFromBFS(snakeID);
		} else if (draw.data.inRandomMode(snakeID)) {
			nextCell = draw.data.getNeighboringCell(draw.data.getSnakeHead(snakeID));
		} else {
			nextCell = draw.data.getNextCellInDir(snakeID);
		}
		this.advanceSnake(snakeID, nextCell);
	}

	public void advanceSnake(int snakeID, BoardCell nextCell) {
		if (nextCell.isWall() || nextCell.isBody()) {
			this.gameOver(snakeID);
			return;
		} else if (nextCell.isFood()) {
			draw.playSound_foodEaten();
			draw.data.growSnake(snakeID, nextCell);
		} else {
			draw.data.shiftSnake(snakeID, nextCell);
		}
	}

	public void updateFood() {
		if (draw.data.noFood()) {
			for (int snakeCnt = 0; snakeCnt < SNAKES; snakeCnt ++) {
				draw.data.addFood();
			}
		} else if (this.cycleNum % FOOD_ADD_RATE == 0 && userTimer.isRunning()) {
			draw.data.addFood();
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	// For testing only
	public SnakeProBrain(TestGame gameNum) {
		this();
		draw.data = SnakeProData.getTestGame(gameNum);
	}
}
