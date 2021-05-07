package Model;

import Controller.TestGame;
import Model.BoardCell;
import Model.CellType;
import Model.Preferences;
import Model.SnakeMode;

import java.awt.Color;
import java.lang.Math;
import java.util.Queue;
import java.util.LinkedList;

public class SnakeProData {
	private BoardCell[][] boardCells2D;
	private int freeSpots = 0;
	private SnakeMode currentMode = SnakeMode.GOING_EAST;
	private LinkedList<BoardCell> foodCells = new LinkedList<BoardCell>();
	private LinkedList<BoardCell> snakeCells = new LinkedList<BoardCell>();
	private boolean gameOver = false;

	// Mapping between direction (names) and keys
	private static final char REVERSE = 'r';
	private static final char UP      = 'i';
	private static final char DOWN    = 'k';
	private static final char LEFT    = 'j';
	private static final char RIGHT   = 'l';
	private static final char AI_MODE = 'a';
	private static final char PLAY_FOOD_NOISE = 's';

	public SnakeProData() {
		int height = Preferences.NUM_CELLS_TALL;
		int width = Preferences.NUM_CELLS_WIDE;
		this.boardCells2D = new BoardCell[height][width];
		this.addWalls();
		this.fillRemainingCells();
	}

	private void addWalls() {
		int height = this.getNumRows();
		int width = this.getNumColumns();

		for (int row = 0; row < height; row++) {
			this.boardCells2D[row][0] = new BoardCell(row, 0, CellType.WALL);
			this.boardCells2D[row][width - 1] = new BoardCell(row, width - 1, CellType.WALL);
		}
		for (int column = 0; column < width; column++) {
			this.boardCells2D[0][column] = new BoardCell(0, column, CellType.WALL);
			this.boardCells2D[height - 1][column] = new BoardCell(height - 1, column, CellType.WALL);
		}
	}

	private void fillRemainingCells() {
		int height = this.getNumRows();
		int width = this.getNumColumns();

		this.freeSpots = 0;
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				if (this.boardCells2D[row][column] == null) {
					this.boardCells2D[row][column] = new BoardCell(row, column, CellType.OPEN);
					this.freeSpots++;
				}
			}
		}
	}

	public void setStartLocation() {
		BoardCell body = this.getCell(1, 1);
		BoardCell head = this.getCell(1, 2);
		this.snakeCells.addLast(body);
		this.snakeCells.addLast(head);
		head.becomeHead();
		body.becomeBody();
	}

	public boolean inAImode() {
		return this.currentMode == SnakeMode.AI_MODE;
	}

	public int getNumRows() {
		return this.boardCells2D.length;
	}

	public int getNumColumns() {
		return this.boardCells2D[0].length;
	}

	public BoardCell getCell(int row, int col) {
		if (row >= this.getNumRows() || col >= this.getNumColumns() || row < 0 || col < 0) {
			System.err.println("Trying to access cell outside of the Board:");
			System.err.println("row: " + row + " col: " + col);
			System.exit(0);
		}
		return this.boardCells2D[row][col];
	}

	public boolean noFood() {
		return this.foodCells.isEmpty();
	}

	public void addFood() {
		int row = (int) (this.getNumRows() * Math.random());
		int col = (int) (this.getNumColumns() * Math.random());
		this.addFood(row, col);
	}

	public void addFood(int row, int col) {
		BoardCell cell = this.getCell(row, col);
		
		if (cell.isOpen()) {
			cell.becomeFood();
			foodCells.addLast(cell);
		} else {
			double totalSize = this.getNumColumns() * this.getNumRows();
			double currentFreeSpots = this.freeSpots - this.snakeCells.size() - this.foodCells.size();
			double ratioFree = currentFreeSpots / totalSize;
			if (ratioFree < 0.2) {
				System.err.println("Not adding more food");
			} else {
				addFood();
			}
		}
	}

	// TODO: Step 1c
	// Add helper method for Controller.SnakeProBrain.advanceSnake

	public BoardCell getNorthNeighbor(BoardCell cell) {
		return this.getCell(cell.getRow() - 1, cell.getColumn());
	}

	public BoardCell getSouthNeighbor(BoardCell cell) {
		return this.getCell(cell.getRow() + 1, cell.getColumn());
	}

	public BoardCell getEastNeighbor(BoardCell cell) {
		return this.getCell(cell.getRow(), cell.getColumn() + 1);
	}

	public BoardCell getWestNeighbor(BoardCell cell) {
		return this.getCell(cell.getRow(), cell.getColumn() - 1);
	}

	public BoardCell getNorthNeighbor() {
		// TODO: Part 1b
		return null;
	}

	public BoardCell getSouthNeighbor() {
		// TODO: Part 1b
		return null;
	}

	public BoardCell getEastNeighbor() {
		// TODO: Part 1b
		return null;
	}

	public BoardCell getWestNeighbor() {
		// TODO: Part 1b
		return null;
	}

	public BoardCell getNextCellInDir() {
		switch (this.currentMode) {
			case GOING_NORTH:
				return this.getNorthNeighbor();
			case GOING_SOUTH:
				return this.getSouthNeighbor();
			case GOING_EAST:
				return this.getEastNeighbor();
			case GOING_WEST:
				return this.getWestNeighbor();
			default:
				return null;
		}
	}

	public BoardCell[] getNeighbors(BoardCell center) {
		BoardCell[] neighborsArray = {getNorthNeighbor(center), getSouthNeighbor(center),  getEastNeighbor(center), getWestNeighbor(center)};
		return neighborsArray;
	}

	public BoardCell getRandomNeighboringCell(BoardCell start) {
		BoardCell[] neighborsArray = getNeighbors(start);
		for (BoardCell mc : neighborsArray) {
			if (mc.isOpen()) {
				return mc;
			}
		}
		return neighborsArray[0];
	}

	public void setDirectionNorth() {
		this.currentMode = SnakeMode.GOING_NORTH;
	}

	public void setDirectionSouth() {
		this.currentMode = SnakeMode.GOING_SOUTH;
	}

	public void setDirectionEast() {
		this.currentMode = SnakeMode.GOING_EAST;
	}

	public void setDirectionWest() {
		this.currentMode = SnakeMode.GOING_WEST;
	}

	public void setMode_AI() {
		this.currentMode = SnakeMode.AI_MODE;
	}

	public void setStartDirection() {
		this.setDirectionEast();
	}

	public BoardCell getSnakeHead() {
		return this.snakeCells.peekLast();
	}

	public BoardCell getSnakeTail() {
		return this.snakeCells.peekFirst();
	}

	public BoardCell getSnakeNeck() {
		int lastSnakeCellIndex = this.snakeCells.size() - 1;
		return this.snakeCells.get(lastSnakeCellIndex - 1);
	}

	public Color getCellColor(int row, int col) {
		BoardCell cell = getCell(row, col);
		return cell.getCellColor();
	}

	public BoardCell getNextCellFromBFS() {
		// Initialize the search.
		this.resetCellsForNextSearch();
		Queue<BoardCell> cellsToSearch = new LinkedList<BoardCell>();
		BoardCell snakeHead = this.getSnakeHead();
		snakeHead.setAddedToSearchList();
		cellsToSearch.add(snakeHead);
		
		BoardCell closestFoodCell = null;
		
		// Search!
		// TODO: Make sure you understand the code above and implement yours!
		
		// Note: we encourage you to write the helper method 
		// getFirstCellInPath below to do the backtracking to calculate the next cell!

		// If the search fails, just move somewhere.
		return this.getRandomNeighboringCell(snakeHead);
	}

	// Find cell whose parent is the snake head
	private BoardCell getFirstCellInPath(BoardCell start) {
		// TODO: Implement
		return null;
	}

	public void resetCellsForNextSearch() {
		for (BoardCell[] row : this.boardCells2D) {
			for (BoardCell cell : row) {
				cell.clear_RestartSearch();
			}
		}
	}

	// TODO: helper method for reverse
	// Step 1: unlabel the head
	// Step 2: reverse the body parts
	// Step 3: relabel the head
	// Step 4: calculate the new direction after reversing!

	public void revSnake() {

	}

	public void setGameOver() {
		this.gameOver = true;
	}

	public boolean getGameOver() {
		return this.gameOver;
	}

	/* ---------------------- */
	/* Testing Infrastructure */
	/* ---------------------- */

	public static SnakeProData getTestGame(TestGame gameNum) {
		return new SnakeProData(gameNum);
	}

	// Constructor used exclusively for testing!
	public SnakeProData(TestGame gameNum) {
		this.boardCells2D = new BoardCell[6][6];
		this.addWalls();
		this.fillRemainingCells();
		if (gameNum.snakeAtStart()) {
			this.testing_snakeAtStartLocation(gameNum);
			this.setDirectionEast();
		} else {
			this.testing_snakeNotAtStartLocation(gameNum);
		}

	}

	private void testing_snakeAtStartLocation(TestGame gameNum) {
		this.setStartLocation();
		if (gameNum == TestGame.G1) {
			this.getCell(1, 3).becomeFood();
		} else if (gameNum == TestGame.G2) {
			this.getCell(2, 2).becomeFood();
		} else if (gameNum == TestGame.G3) {
			this.getCell(1, 4).becomeFood();
		} else if (gameNum == TestGame.G4) {
			this.getCell(2, 1).becomeFood();
		} else if (gameNum == TestGame.G5) {
			this.getCell(4, 1).becomeFood();
		} else if (gameNum == TestGame.G6) {
			this.getCell(1, 3).becomeFood();
			this.getCell(3, 1).becomeFood();
		} else if (gameNum == TestGame.G7) {
			this.getCell(2, 2).becomeFood();
			this.getCell(1, 4).becomeFood();
		} else if (gameNum == TestGame.G8) {
			this.getCell(1, 4).becomeFood();
			this.getCell(4, 2).becomeFood();
		} else if (gameNum == TestGame.G9) {
			this.getCell(2, 1).becomeFood();
			this.getCell(2, 4).becomeFood();
		} else if (gameNum == TestGame.G10) {
			this.getCell(4, 1).becomeFood();
			this.getCell(4, 4).becomeFood();
		} else if (gameNum == TestGame.G11) {
			// No food :)
		}
		// Add all food to the food cells
		int height = this.getNumRows();
		int width = this.getNumColumns();
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				BoardCell cell = this.getCell(row, column);
				if (cell.isFood()) {
					this.foodCells.add(cell);
				}
			}
		}
	}

	private void testing_snakeNotAtStartLocation(TestGame gameNum) {
		if (gameNum == TestGame.G12) {
			BoardCell body2 = this.getCell(2, 3);
			BoardCell body1 = this.getCell(2, 2);
			BoardCell head = this.getCell(2, 1);
			this.snakeCells.add(body2);
			this.snakeCells.add(body1);
			this.snakeCells.add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G13) {
			BoardCell body2 = this.getCell(3, 2);
			BoardCell body1 = this.getCell(2, 2);
			BoardCell head = this.getCell(2, 1);
			this.snakeCells.add(body2);
			this.snakeCells.add(body1);
			this.snakeCells.add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G14) {
			BoardCell body2 = this.getCell(2, 2);
			BoardCell body1 = this.getCell(3, 2);
			BoardCell head = this.getCell(3, 1);
			this.snakeCells.add(body2);
			this.snakeCells.add(body1);
			this.snakeCells.add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G15) {
			BoardCell body2 = this.getCell(3, 2);
			BoardCell body1 = this.getCell(3, 3);
			BoardCell head = this.getCell(3, 4);
			this.snakeCells.add(body2);
			this.snakeCells.add(body1);
			this.snakeCells.add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		}
	}

	public String toString() {
		String result = "";
		for (int r = 0; r < this.getNumRows(); r++) {
			for (int c = 0; c < this.getNumColumns(); c++) {
				BoardCell cell = this.getCell(r, c);
				result += cell.toStringType();
			}
			result += "\n";
		}
		return result;
	}
	
	public String toStringParents() {
		String result = "";
		for (int r = 0; r < this.getNumRows(); r++) {
			for (int c = 0; c < this.getNumColumns(); c++) {
				BoardCell cell = this.getCell(r, c);
				result += cell.toStringParent() + "\t";
			}
			result += "\n";
		}
		return result;
	}
}