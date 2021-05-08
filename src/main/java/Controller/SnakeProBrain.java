package Controller;

import Model.BoardCell;
import Model.Preferences;
import Model.SnakeProData;
import View.SnakeProDisplay;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeProBrain extends JFrame implements ActionListener, KeyListener {
	public SnakeProDisplay draw;

	public JButton newGameButton;
	public JButton pauseButton;
	public JButton startButton;

	private Timer timer;
	private int cycleNum;

	private static final char REVERSE = 'r';
	private static final char UP      = 'i';
	private static final char DOWN    = 'k';
	private static final char LEFT    = 'j';
	private static final char RIGHT   = 'l';
	private static final char AI_MODE = 'a';
	private static final char PLAY_FOOD_NOISE = 's';

	public SnakeProBrain() {
		draw = new SnakeProDisplay(Preferences.GAMEBOARDWIDTH, Preferences.GAMEBOARDHEIGHT);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		draw.loadResources();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				timer.stop();
			}
		});
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SnakeProBrain frame = new SnakeProBrain();
				frame.setTitle("Testing!");
				frame.setResizable(false);
				frame.setSize(Preferences.GAMEBOARDWIDTH, Preferences.GAMEBOARDHEIGHT);
				frame.setMinimumSize(new Dimension(Preferences.GAMEBOARDWIDTH, Preferences.GAMEBOARDHEIGHT));
				frame.add(frame.draw);
				frame.initializeButtons();
				frame.pack();
				frame.setVisible(true);
			}
		});
	}

	// Here is how buttons and menu items work...
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();

		if (source == this.timer) {
			this.cycle();
		}
		if (source == this.newGameButton) {
			System.out.println("New game");
			draw.startNewGame();
			this.go();
		}
		if (source == this.pauseButton) {
			System.out.println("Paused");
			this.pause();
		}
		if (source == this.startButton) {
			System.out.println("Started");
			this.go();
		}
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
	}

	public void initializeButtons() {
		JPanel buttonPane = new JPanel(new FlowLayout());
		buttonPane.setBackground(Preferences.COLOR_BACKGROUND);
		this.add(buttonPane, BorderLayout.PAGE_START);
		
		this.newGameButton = new JButton("New Game");
		this.newGameButton.addActionListener(this);
		this.newGameButton.addKeyListener(this);
		buttonPane.add(this.newGameButton);

		this.pauseButton = new JButton("Pause");
		this.pauseButton.addActionListener(this);
		this.pauseButton.addKeyListener(this);
		buttonPane.add(this.pauseButton);

		this.startButton = new JButton("Start");
		this.startButton.addActionListener(this);
		this.startButton.addKeyListener(this);
		buttonPane.add(this.startButton);
	}

	public void keyPressed(KeyEvent e) {
		// TODO: Step 1d
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

	public void go() {
        cycleNum = 0;
        timer = new Timer(Preferences.SLEEP_TIME, this);
        timer.setRepeats(true);
        timer.start();

        draw.repaint();
	}

	public void pause() {
		if (timer.isRunning()) {
			timer.stop();
		}
	}

	public void cycle() {
		if (this.cycleNum % Preferences.REFRESH_RATE == 0) {
			this.updateSnake();
		}
		this.updateFood();
		draw.repaint();
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

	private void advanceTheSnake(BoardCell nextCell) {
		if (nextCell.isWall() || nextCell.isBody()) {
			this.gameOver();
			return;
		} else if (nextCell.isFood()) {
			draw.playSound_foodEaten();
			// TODO: Part 1c
		} else if (nextCell.isOpen()) {
			// TODO: Part 1c
		}
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
		this();
		draw.data = SnakeProData.getTestGame(gameNum);
	}
}
