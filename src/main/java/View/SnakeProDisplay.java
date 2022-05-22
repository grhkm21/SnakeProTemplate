package View;

import static Model.Preferences.*;

import Model.BoardCell;
import Model.SnakeMode;
import Model.SnakeProData;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.lang.Math;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class SnakeProDisplay extends JComponent {
	public SnakeProData data;

	private Clip audioFood;
	private Clip audioCrunch;
	private Clip audioMeow;
	private Image imageFood;

	private int width;
	private int height;
	private int cycle;
	private int gameOffsetX = 0;
	private int gameOffsetY = SPACE_FOR_LABELS;
	
	private boolean gameRunning = false;
	private boolean isVoronoi = false;

	public SnakeProDisplay(int width, int height) {
		this.data = new SnakeProData();
		this.width = width;
		this.height = height;
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setLocation(0, SPACE_FOR_BUTTONS + CELLS_BUFFER);
		// this.loadResources();
		this.repaint();
	}

	// Reference: https://www.oracle.com/java/technologies/painting.html#swing_summary
	// this.repaint() -> this.paintComponent(), this.paintBorder(), this.paintChildren()
	public void paintComponent(Graphics g) {
		debugCycle ++;
		super.paintComponent(g);
		data.updateSnakeID();
		this.clearBoard(g);
		this.drawBoard(g);
		this.drawDivision(g);
		if (data.inBFSMode(0)) {
			this.drawBFSVisualisation(g);
		}
		this.drawLabels(g);
		this.drawCheat(g);
	}

	private void clearBoard(Graphics g) {
		// clear board
		g.setColor(COLOR_BACKGROUND);
		g.fillRect(0, 0, this.width, this.height);
	}

	private void drawBoard(Graphics g) {
		// https://docs.oracle.com/javase/7/docs/api/java/awt/Color.html#getRGBColorComponents(float[])
		// referenced https://stackoverflow.com/questions/50819434/convert-rgb-values-to-color-wheel-coordinates for HSV/HSB
		int[][][] voronoiDistance = new int[NUM_CELLS_TALL][NUM_CELLS_WIDE][SNAKES];
		if (isVoronoi) {
			voronoiDistance = data.voronoiFill();
		}
		ArrayList<Integer> snakeLength = new ArrayList<Integer>();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			snakeLength.add(data.getSnakeLength(snakeID));
		}
		int wallLength = (NUM_CELLS_WIDE + NUM_CELLS_TALL - 2) * 2;
		data.updateSnakeID();
		// draw board
		for (int row = 0; row < data.getNumRows(); row ++) {
			for (int col = 0; col < data.getNumCols(); col ++) {
				Color curColor = data.getCellColor(row, col);
				BoardCell curCell;
				if (row == 0 || row == NUM_CELLS_TALL - 1 || col == 0 || col == NUM_CELLS_WIDE - 1) {
					curCell = data.getNoWrapCell(row, col);
				} else {
					curCell = data.getCell(row, col);
				}
				int cellID = curCell.getID();
				int thisID = curCell.getSnakeID();
				// snake head / body
				// linear interpolation
				if (curCell.isHead() || curCell.isBody()) {
					boolean snakeDead = data.getGameOver(thisID);
					float[] tailColorRGB;
					float[] headColorRGB;
					if (snakeDead) {
						tailColorRGB = COLOR_DEAD_TAIL.getRGBColorComponents(null);
						headColorRGB = COLOR_DEAD_HEAD.getRGBColorComponents(null);
					} else {
						tailColorRGB = COLOR_TAIL[thisID].getRGBColorComponents(null);
						headColorRGB = COLOR_HEAD[thisID].getRGBColorComponents(null);
					}
					BoardCell prevCell = data.getSnakeKth(thisID, cellID + 1);
					float interpolateFactor;
					if (snakeLength.get(thisID) > 1) {
						interpolateFactor = cellID * 1.0F / (snakeLength.get(thisID) - 1);
					} else {
						interpolateFactor = 1.0F;
					}
					float[] newRGB = interpolate(tailColorRGB, headColorRGB, interpolateFactor);
					curColor = new Color(newRGB[0], newRGB[1], newRGB[2]);
				} else if (curCell.isWall()) {
					int shiftedID;
					if (data.getRainbow()) {
						shiftedID = (cellID + this.cycle / WALL_CYCLE_SPEED) % wallLength;
					} else {
						shiftedID = cellID;
					}
					float hueFactor = (shiftedID * 2.0F / (wallLength - 1)) % 1.0F;
					curColor = Color.getHSBColor(hueFactor, SATURATION, data.getRainbow() ? BRIGHTNESS : BRIGHTNESS / 5);
				}
				// draw only if it is not open
				boolean borderNorth = true;
				boolean borderEast = true;
				boolean borderSouth = true;
				boolean borderWest = true;
				if (curCell.isOpen()) {
					borderNorth = false;
					borderEast = false;
					borderSouth = false;
					borderWest = false;
					if (isVoronoi) {
						int[] dist = voronoiDistance[curCell.getRow()][curCell.getCol()];
						int minID = -1;
						int minDist = 9999999;
						for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
							if (dist[snakeID] < minDist) {
								minID = snakeID;
								minDist = dist[snakeID];
							}
						}
						curColor = COLOR_HEAD[minID];
					}
				} else if (curCell.isHead() || curCell.isBody()) {
					BoardCell northCell = data.getNorthNeighbor(curCell);
					BoardCell eastCell = data.getEastNeighbor(curCell);
					BoardCell southCell = data.getSouthNeighbor(curCell);
					BoardCell westCell = data.getWestNeighbor(curCell);
					if (northCell != null &&
							(northCell.isHead() || northCell.isBody()) &&
							northCell.getSnakeID() == thisID &&
							Math.abs(northCell.getID() - cellID) == 1) {
						borderNorth = false;
					}
					if (eastCell != null &&
							(eastCell.isHead() || eastCell.isBody()) &&
							eastCell.getSnakeID() == thisID &&
							Math.abs(eastCell.getID() - cellID) == 1) {
						borderEast = false;
					}
					if (southCell != null &&
							(southCell.isHead() || southCell.isBody()) &&
							southCell.getSnakeID() == thisID &&
							Math.abs(southCell.getID() - cellID) == 1) {
						borderSouth = false;
					}
					if (westCell != null &&
							(westCell.isHead() || westCell.isBody()) &&
							westCell.getSnakeID() == thisID &&
							Math.abs(westCell.getID() - cellID) == 1) {
						borderWest = false;
					}
				}
				drawCell(g, curCell, curColor, borderNorth, borderEast, borderSouth, borderWest);
			}
		}
		cycle ++;
	}

	private void drawDivision(Graphics g) {
		Color[] randColors = new Color[6];
		for (int i = 0; i < 6; i ++) {
			float hueFactor = (i / 3.5F) % 1.0F;
			randColors[i] = new Color(Color.HSBtoRGB(hueFactor, SATURATION, BRIGHTNESS));
		}
		HashMap<BoardCell, Boolean> mp = new HashMap<BoardCell, Boolean>();
		int shift = 0;
		for (int sum = 2; sum < data.getNumRows() + data.getNumCols() - 3; sum ++) {
			for (int row = 1; row < data.getNumRows() - 1 && 1 <= sum - row && sum - row < data.getNumCols() - 1; row ++) {
				int col = sum - row;
				BoardCell key = data.getCell(row, col);
				if (key == null || mp.containsKey(key) || !key.isOpen()) {
					continue;
				}
				Color curColorRGB = randColors[shift % 6];
				Color curColorARGB = new Color(curColorRGB.getRed()   / 255.0F,
							       curColorRGB.getGreen() / 255.0F,
							       curColorRGB.getBlue()  / 255.0F,
							       0.15F);
				shift ++;

				for (BoardCell cell : data.floodFill(key)) {
					if (cell.isOpen()) {
						mp.put(cell, true);
						drawCell(g, cell, curColorARGB, false, false, false, false);
					}
				}
			}
		}
	}

	private void drawBFSVisualisation(Graphics g) {
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			if (data.getGameOver(snakeID)) {
				continue;
			}
			if (data.getTargetCell(snakeID) == null) {
				continue;
			}
			Color drawColor = new Color(140 - 30 * snakeID, 100 + 30 * snakeID, 160 - 30 * snakeID);
			BoardCell curCell = data.getTargetCell(snakeID);
			BoardCell nextCell = curCell.getParent(snakeID);
			while (nextCell != null && nextCell != data.getSnakeHead(snakeID)) {
				drawLine(g, curCell, nextCell, drawColor);
				curCell = nextCell;
				nextCell = curCell.getParent(snakeID);
			}
		}
	}

	private void drawCell(Graphics g, BoardCell cell, Color color, boolean borderNorth, boolean borderEast, boolean borderSouth, boolean borderWest) {
		int cellX = cell.getCol() * CELL_SIZE + gameOffsetX;
		int cellY = cell.getRow() * CELL_SIZE + gameOffsetY;
		// draw border if cell is not open
		if (!cell.isOpen()) {
			g.setColor(Color.BLACK);
			g.fillRect(cellX, cellY, CELL_SIZE, CELL_SIZE);
		}
		// draw cell
		int cellInnerSize = CELL_SIZE - 2 * CELL_BORDER;
		g.setColor(color);
		g.fillRect(cellX + CELL_BORDER, cellY + CELL_BORDER, cellInnerSize, cellInnerSize);
		// "overwrite" border if needed
		if (!borderNorth) {
			g.fillRect(cellX + CELL_BORDER, cellY, cellInnerSize, CELL_BORDER);
		}
		if (!borderEast) {
			g.fillRect(cellX + CELL_BORDER + cellInnerSize, cellY + CELL_BORDER, CELL_BORDER, cellInnerSize);
		}
		if (!borderSouth) {
			g.fillRect(cellX + CELL_BORDER, cellY + CELL_BORDER + cellInnerSize, cellInnerSize, CELL_BORDER);
		}
		if (!borderWest) {
			g.fillRect(cellX, cellY + CELL_BORDER, CELL_BORDER, cellInnerSize);
		}
		if (!borderNorth && !borderEast) {
			g.fillRect(cellX + CELL_BORDER + cellInnerSize, cellY, CELL_BORDER, CELL_BORDER);
		}
		if (!borderEast && !borderSouth) {
			g.fillRect(cellX + CELL_BORDER + cellInnerSize, cellY + CELL_BORDER + cellInnerSize, CELL_BORDER, CELL_BORDER);
		}
		if (!borderSouth && !borderWest) {
			g.fillRect(cellX, cellY + CELL_BORDER + cellInnerSize, CELL_BORDER, CELL_BORDER);
		}
		if (!borderWest && !borderNorth) {
			g.fillRect(cellX, cellY, CELL_BORDER, CELL_BORDER);
		}
	}

	private void drawLine(Graphics g, BoardCell cell1, BoardCell cell2, Color color) {
		// not adjacent -> wall wrap -> no need to draw line
		if (Math.abs(cell1.getRow() - cell2.getRow()) + Math.abs(cell1.getCol() - cell2.getCol()) != 1) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10));
		int midX1 = cell1.getCol() * CELL_SIZE + CELL_SIZE / 2 + gameOffsetX;
		int midY1 = cell1.getRow() * CELL_SIZE + CELL_SIZE / 2 + gameOffsetY;
		int midX2 = cell2.getCol() * CELL_SIZE + CELL_SIZE / 2 + gameOffsetX;
		int midY2 = cell2.getRow() * CELL_SIZE + CELL_SIZE / 2 + gameOffsetY;
		g2.draw(new Line2D.Float(midX1, midY1, midX2, midY2));
	}

	private void drawLabels(Graphics g) {
		// draw title
		g.setColor(Color.BLACK);
		this.drawCenteredText(g, TITLE, 0, 0, this.width, SPACE_FOR_LABELS, INFO_FONT);
		// draw game over if applicable
		if (data.getGameOver()) {
			g.setColor(GAMEOVER_COLOR);
			this.drawCenteredText(g, GAMEOVER, gameOffsetX, gameOffsetY, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT, GAMEOVER_FONT);
		} else if (!this.gameRunning) {
			g.setColor(PAUSE_COLOR);
			this.drawCenteredText(g, PAUSE, gameOffsetX, gameOffsetY, GAMEBOARD_WIDTH, GAMEBOARD_HEIGHT, GAMEOVER_FONT);
		}
	}

	// adapted from https://stackoverflow.com/questions/27706197/how-can-i-center-graphics-drawstring-in-java
	// rectX, rectY are top left
	private void drawCenteredText(Graphics g, String text, int rectX, int rectY, int rectWidth, int rectHeight, Font font) {
		FontMetrics metrics = g.getFontMetrics(font);
		int x = rectX + (rectWidth - metrics.stringWidth(text)) / 2;
		int y = rectY + (rectHeight - metrics.getHeight()) / 2 + metrics.getAscent();
		g.setFont(font);
		g.drawString(text, x, y);
	}

	private void drawCheat(Graphics g) {
		String typedCode = data.getCode();
		int cheatSize = 3;
		for (int i = 0; i < typedCode.length(); i ++) {
			int squareX = GAMEBOARD_WIDTH - (typedCode.length() - i) * cheatSize;
			g.setColor(i < CHEAT_CODE.length() && typedCode.charAt(i) == CHEAT_CODE.charAt(i) ? Color.GREEN : Color.RED);
			g.fillRect(squareX, 0, cheatSize, cheatSize);
		}
	}

	public void pause() {
		this.gameRunning = false;
	}

	public void go() {
		this.gameRunning = true;
	}

	public void toggleVoronoi() {
		this.isVoronoi = !this.isVoronoi;
	}

	public boolean getVoronoi() {
		return this.isVoronoi;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
			e.printStackTrace();
			this.audioFood = null;
			this.audioCrunch = null;
			this.audioMeow = null;
			this.imageFood = null;
		}
	}

	public void startNewGame() {
		data = new SnakeProData();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			data.setStartLocation(snakeID);
			data.setStartDirection(snakeID);
			data.setStartScore(snakeID);
		}
	}

	public static void playClip(Clip clip) {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public void playSound_foodEaten() {
		playClip(this.audioCrunch);
	}

	public void playSound_food() {
		playClip(this.audioFood);
	}

	public void playSound_meow() {
		playClip(this.audioMeow);
	}

	private float interpolate(float x, float y, float k) {
		// find point t=k between x(t=0) and y(t=1)
		return x + (y - x) * k;
	}

	private float[] interpolate(float[] x, float[] y, float k) {
		float[] res = new float[x.length];
		for (int i = 0; i < x.length; i ++) {
			res[i] = interpolate(x[i], y[i], k);
		}
		return res;
	}

	private int debugCycle = 0;
}
