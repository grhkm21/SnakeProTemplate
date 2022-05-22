package Model;

import static Model.CellType.*;
import static Model.Preferences.*;
import static Model.SnakeMode.*;

import Controller.TestGame;
import Model.BoardCell;
import Model.CellType;
import Model.SnakeMode;

import java.awt.Color;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class SnakeProData {
	private BoardCell[][] boardCells2D;
	private int freeSpots = 0;
	private boolean isWrap = true;
	private ArrayList<BoardCell> target = new ArrayList<BoardCell>();
	private ArrayList<BoardCell> nextMove = new ArrayList<BoardCell>();
	private ArrayList<Boolean> nextMoveBFS = new ArrayList<Boolean>();
	public ArrayList<SnakeMode> currentMode = new ArrayList<SnakeMode>();
	private LinkedList<BoardCell> foodCells = new LinkedList<BoardCell>();
	private ArrayList<LinkedList<BoardCell>> snakeCells = new ArrayList<LinkedList<BoardCell>>();
	private ArrayList<Integer> scores = new ArrayList<Integer>();
	private ArrayList<Boolean> gameOver = new ArrayList<Boolean>();
	private String typedCode = "";
	private boolean rainbowCheat = false;

	/*
		TODO:
		- Score label
		- Draw own label (Instead of JButton cuz they look bad)
		- Other game modes (random)
		- Make everything customisable (different window or popup window?)
			- Create CustomButton class
			- Create map<String, CustomButton> to access/create buttons and listen
			- Make method to add CustomButton
			- CustomButton should allow different types of prompt
			- Type sanitisation (integers, colors, font)
			- Align place
			 
		- Paused label
		- circle cells? remove border if two cells adjacent? -> settings styles?
		- Portals
		- Types of food (more score? decrease length?)
		- Speed pads (speed up slow down)
		- Leaderboard
		- Number of lives
		- Voronoi Diagram

		DONE:
		- Layout (GridLayout, BoxLayout)
		- Better cells
		- Scale window to board size
		> Added color interpolation
		- BFS/DFS AI mode
		- Add enemy
			- play until die, compare final score
			- no time limit
			- DONT REMOVE THE SNAKE AFTER DEATH
		> executable file
		- show BFS path
		- cheat codes (1973208)
	*/

	public SnakeProData() {
		this.boardCells2D = new BoardCell[NUM_CELLS_TALL][NUM_CELLS_WIDE];
		this.currentMode = new ArrayList<SnakeMode>();
		this.snakeCells = new ArrayList<LinkedList<BoardCell>>();
		this.scores = new ArrayList<Integer>();
		this.target = new ArrayList<BoardCell>();
		this.gameOver = new ArrayList<Boolean>();
		this.nextMove = new ArrayList<BoardCell>();
		this.nextMoveBFS = new ArrayList<Boolean>();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			if (snakeID == 0) {
				this.currentMode.add(GOING_EAST);
			} else if (snakeID == 1) {
				this.currentMode.add(RANDOM_MODE);
			} else {
				this.currentMode.add(BFS_MODE);
			}
			this.snakeCells.add(new LinkedList<BoardCell>());
			this.scores.add(0);
			this.target.add(new BoardCell(-1, -1, OPEN));
			this.gameOver.add(false);
			this.nextMove.add(null);
			this.nextMoveBFS.add(false);
		}
		this.addWalls();
		this.fillRemainingCells();
	}

	private void addWalls() {
		int cnt = 0;
		for (int col = 0; col < getNumCols() - 1; col ++) {
			this.boardCells2D[0][col] = new BoardCell(0, col, WALL, cnt);
			cnt ++;
		}
		for (int row = 0; row < getNumRows() - 1; row ++) {
			this.boardCells2D[row][getNumCols() - 1] = new BoardCell(row, getNumCols() - 1, WALL, cnt);
			cnt ++;
		}
		for (int col = getNumCols() - 1; col > 0; col --) {
			this.boardCells2D[getNumRows() - 1][col] = new BoardCell(getNumRows() - 1, col, WALL, cnt);
			cnt ++;
		}
		for (int row = getNumRows() - 1; row > 0; row --) {
			this.boardCells2D[row][0] = new BoardCell(row, 0, WALL, cnt);
			cnt ++;
		}
	}

	private void fillRemainingCells() {
		int height = this.getNumRows();
		int width = this.getNumCols();

		this.freeSpots = 0;
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				if (this.boardCells2D[row][col] == null) {
					this.boardCells2D[row][col] = new BoardCell(row, col, OPEN);
					this.freeSpots++;
				}
			}
		}
	}

<<<<<<< HEAD
	public void setStartLocation(int snakeID) {
		this.snakeCells.set(snakeID, new LinkedList<BoardCell>());
		if (snakeID == 0) {
			for (int index = 1; index < START_LENGTH; index ++) {
				BoardCell body = this.getCell(1, index);
				body.becomeBody(snakeID);
				this.snakeCells.get(snakeID).addLast(body);
			}
			BoardCell head = this.getCell(1, START_LENGTH);
			head.becomeHead(snakeID);
			this.snakeCells.get(snakeID).addLast(head);
		} else if (snakeID == 1) {
			for (int index = 1; index < START_LENGTH; index ++) {
				BoardCell body = this.getCell(this.getNumRows() - 2, this.getNumCols() - index - 1);
				body.becomeBody(snakeID);
				this.snakeCells.get(snakeID).addLast(body);
			}
			BoardCell head = this.getCell(this.getNumRows() - 2, this.getNumCols() - START_LENGTH - 1);
			head.becomeHead(snakeID);
			this.snakeCells.get(snakeID).addLast(head);
		} else if (snakeID == 2) {
			for (int index = 1; index < START_LENGTH; index ++) {
				BoardCell body = this.getCell(index, this.getNumCols() - 2);
				body.becomeBody(snakeID);
				this.snakeCells.get(snakeID).addLast(body);
			}
			BoardCell head = this.getCell(START_LENGTH, this.getNumCols() - 2);
			head.becomeHead(snakeID);
			this.snakeCells.get(snakeID).addLast(head);
		} else if (snakeID == 3) {
			for (int index = 1; index < START_LENGTH; index ++) {
				BoardCell body = this.getCell(this.getNumRows() - index - 1, 1);
				body.becomeBody(snakeID);
				this.snakeCells.get(snakeID).addLast(body);
			}
			BoardCell head = this.getCell(this.getNumRows() - START_LENGTH - 1, 1);
			head.becomeHead(snakeID);
			this.snakeCells.get(snakeID).addLast(head);
		}
	}

	public int getScore(int snakeID) {
		return this.scores.get(snakeID);
=======
	public void setStartLocation() {
		BoardCell body = this.getCell(1, 1);
		BoardCell head = this.getCell(1, 2);
		this.snakeCells.addLast(body);
		this.snakeCells.addLast(head);
		head.becomeHead();
		body.becomeBody();
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
	}

	public boolean inBFSMode(int snakeID) {
		return this.currentMode.get(snakeID) == SnakeMode.BFS_MODE;
	}

	public boolean inRandomMode(int snakeID) {
		return this.currentMode.get(snakeID) == SnakeMode.RANDOM_MODE;
	}

	public int getNumRows() {
		return this.boardCells2D.length;
	}

	public int getNumCols() {
		return this.boardCells2D[0].length;
	}

	public int getSnakeLength(int snakeID) {
		return this.snakeCells.get(snakeID).size();
	}

	public BoardCell getCell(int row, int col) {
		// https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorMod-int-int-
		// map row to [1, getNumRows() - 1], col to [1, getNumCols() - 1]
		if (isWrap) {
			row = Math.floorMod(row - 1, getNumRows() - 2) + 1;
			col = Math.floorMod(col - 1, getNumCols() - 2) + 1;
		}
		if (row >= this.getNumRows() || col >= this.getNumCols() || row < 0 || col < 0) {
			// System.out.println(String.format("Trying to access cell outside of the Board: (row, col) = (%d, %d)", row, col));
			return null;
		}
		return this.boardCells2D[row][col];
	}

	public BoardCell getNoWrapCell(int row, int col) {
		if (row >= this.getNumRows() || col >= this.getNumCols() || row < 0 || col < 0) {
			// System.out.println(String.format("Trying to access cell outside of the Board: (row, col) = (%d, %d)", row, col));
			return null;
		}
		return this.boardCells2D[row][col];
	}

	public boolean noFood() {
		return this.foodCells.isEmpty();
	}

	public void addFood() {
		int row = (int) (this.getNumRows() * Math.random());
<<<<<<< HEAD
		int col = (int) (this.getNumCols() * Math.random());
=======
		int col = (int) (this.getNumColumns() * Math.random());
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
		this.addFood(row, col);
	}

	public void addFood(int row, int col) {
		BoardCell cell = this.getCell(row, col);
		
		if (cell.isOpen()) {
			cell.becomeFood();
			foodCells.addLast(cell);
			// resetTargets();
		} else {
<<<<<<< HEAD
			double totalSize = this.getNumCols() * this.getNumRows();
=======
			double totalSize = this.getNumColumns() * this.getNumRows();
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
			double currentFreeSpots = this.freeSpots - this.snakeCells.size() - this.foodCells.size();
			double ratioFree = currentFreeSpots / totalSize;
			if (ratioFree < 0.2) {
				System.err.println("Not adding more food");
			} else {
				addFood();
			}
		}
	}

<<<<<<< HEAD
	private void resetTargets() {
		target = new ArrayList<BoardCell>();
		nextMoveBFS = new ArrayList<Boolean>();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			this.target.add(new BoardCell(-1, -1, OPEN));
			this.nextMoveBFS.add(false);
		}
	}

	// Helper method for Controller.SnakeProBrain.advanceSnake
	public void growSnake(int snakeID, BoardCell nextCell) {
		for (int i = 0; i < foodCells.size(); i ++) {
			if (foodCells.get(i).equals(nextCell)) {
				foodCells.remove(i);
				break;
			}
		}
		this.getSnakeHead(snakeID).becomeBody(snakeID);
		nextCell.becomeHead(snakeID);
		snakeCells.get(snakeID).addLast(nextCell);
		scores.set(snakeID, scores.get(snakeID) + 1);
	}

	public void shiftSnake(int snakeID, BoardCell nextCell) {
		this.getSnakeHead(snakeID).becomeBody(snakeID);
		nextCell.becomeHead(snakeID);
		snakeCells.get(snakeID).addLast(nextCell);
		snakeCells.get(snakeID).getFirst().becomeOpen();
		snakeCells.get(snakeID).removeFirst();
	}
=======
	// TODO: Step 1c
	// Add helper method for Controller.SnakeProBrain.advanceSnake
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48

	public BoardCell getNorthNeighbor(BoardCell cell) {
		int newRow = cell.getRow() - 1;
		int newCol = cell.getCol();
		return this.getCell(newRow, newCol);
	}

	public BoardCell getSouthNeighbor(BoardCell cell) {
		int newRow = cell.getRow() + 1;
		int newCol = cell.getCol();
		return this.getCell(newRow, newCol);
	}

	public BoardCell getEastNeighbor(BoardCell cell) {
		int newRow = cell.getRow();
		int newCol = cell.getCol() + 1;
		return this.getCell(newRow, newCol);
	}

	public BoardCell getWestNeighbor(BoardCell cell) {
		int newRow = cell.getRow();
		int newCol = cell.getCol() - 1;
		return this.getCell(newRow, newCol);
	}

<<<<<<< HEAD
	public BoardCell getNorthNeighbor(int snakeID) {
		return this.getNorthNeighbor(this.getSnakeHead(snakeID));
	}

	public BoardCell getSouthNeighbor(int snakeID) {
		return this.getSouthNeighbor(this.getSnakeHead(snakeID));
	}

	public BoardCell getEastNeighbor(int snakeID) {
		return this.getEastNeighbor(this.getSnakeHead(snakeID));
	}

	public BoardCell getWestNeighbor(int snakeID) {
		return this.getWestNeighbor(this.getSnakeHead(snakeID));
	}

	public BoardCell getNextCellInDir(int snakeID) {
		switch (this.currentMode.get(snakeID)) {
			case GOING_NORTH:
				return this.getNorthNeighbor(snakeID);
			case GOING_SOUTH:
				return this.getSouthNeighbor(snakeID);
			case GOING_EAST:
				return this.getEastNeighbor(snakeID);
			case GOING_WEST:
				return this.getWestNeighbor(snakeID);
=======
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
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
			default:
				return null;
		}
	}

	public ArrayList<BoardCell> getNeighbors(BoardCell center) {
		ArrayList<BoardCell> neighbors = new ArrayList<BoardCell>();
		neighbors.add(getNorthNeighbor(center));
		neighbors.add(getSouthNeighbor(center));
		neighbors.add(getEastNeighbor(center));
		neighbors.add(getWestNeighbor(center));
		return neighbors;
	}
	
	// used by random-controlled snake
	public BoardCell getNeighboringCell(BoardCell start) {
		ArrayList<BoardCell> neighbors = getNeighbors(start);
		ArrayList<BoardCell> options = new ArrayList<BoardCell>();
		for (BoardCell mc : neighbors) {
			if (mc != null && mc.isOpen()) {
				options.add(mc);
			}
		}
		if (options.size() == 0) {
			return neighbors.get(0);
		}
		Collections.shuffle(options);
		return options.get(0);
	}

	public void setMode(int snakeID, SnakeMode mode) {
		if (this.currentMode.get(snakeID).equals(GOING_NORTH) && mode.equals(GOING_SOUTH)) return;
		if (this.currentMode.get(snakeID).equals(GOING_EAST) && mode.equals(GOING_WEST)) return;
		if (this.currentMode.get(snakeID).equals(GOING_SOUTH) && mode.equals(GOING_NORTH)) return;
		if (this.currentMode.get(snakeID).equals(GOING_WEST) && mode.equals(GOING_EAST)) return;
		this.currentMode.set(snakeID, mode);
	}

	public void setStartDirection(int snakeID) {
		if (snakeID == 0) {
			this.setMode(snakeID, GOING_EAST);
		} else if (snakeID == 1) {
			this.setMode(snakeID, RANDOM_MODE);
		} else {
			this.setMode(snakeID, BFS_MODE);
		}
	}

	public void setStartScore(int snakeID) {
		this.scores.set(snakeID, 0);
	}

	public BoardCell getSnakeHead(int snakeID) {
		return this.getSnakeKth(snakeID, 0);
	}

	public BoardCell getSnakeTail(int snakeID) {
		return this.getSnakeKth(snakeID, -1);
	}

	public BoardCell getSnakeNeck(int snakeID) {
		return this.getSnakeKth(snakeID, 1);
	}

	public BoardCell getSnakeKth(int snakeID, int k) {
		// count from head
		int snakeLength = getSnakeLength(snakeID);
		int rev = snakeLength - k - 1;
		rev = (rev % snakeLength + snakeLength) % snakeLength;
		return this.snakeCells.get(snakeID).get(rev);
	}

	public Color getCellColor(int row, int col) {
		BoardCell cell = getCell(row, col);
		return cell.getCellColor();
	}

	public void computeBFS(int snakeID) {
		this.nextMove.set(snakeID, null);
		for (BoardCell[] row : boardCells2D) {
			for (BoardCell cell : row) {
				cell.setParent(snakeID, null);
			}
		}
		// Initialize the search.
		Queue<BoardCell> cellsToSearch = new LinkedList<BoardCell>();
		ArrayList<BoardCell[]> candidate = new ArrayList<BoardCell[]>();
		this.resetCellsForNextSearch();

		int curBest;
		if (target.get(snakeID).isFood()) {
			curBest = distanceTo(snakeID, target.get(snakeID));
		} else {
			curBest = 999999999;
		}

		getSnakeHead(snakeID).setAddedToSearchList();
		cellsToSearch.add(getSnakeHead(snakeID));
		
		while (!cellsToSearch.isEmpty()) {
			BoardCell searchCell = cellsToSearch.remove();
			if (searchCell.isFood() && (!target.get(snakeID).isFood() || searchCell.equals(target.get(snakeID)))) {
				BoardCell curCell = searchCell;
				int distance = distanceTo(snakeID, curCell);
				if (distance <= curBest && countSpace(curCell) >= snakeCells.size()) {
					if (distance < curBest) {
						candidate.clear();
						curBest = distance;
					}
					while (!curCell.getParent(snakeID).equals(getSnakeHead(snakeID))) {
						curCell = curCell.getParent(snakeID);
					}
					candidate.add(new BoardCell[]{curCell, searchCell});
				}
			}
			for (BoardCell neighbor : getNeighbors(searchCell)) {
				if ((neighbor.isOpen() || neighbor.isFood()) && !neighbor.inSearchListAlready()) {
					neighbor.setAddedToSearchList();
					neighbor.setParent(snakeID, searchCell);
					cellsToSearch.add(neighbor);
				}
			}
		}
		BoardCell finalResult;
		if (!candidate.isEmpty()) {
			Collections.shuffle(candidate);
			BoardCell[] result = candidate.get(0);
			finalResult = result[0];
			target.set(snakeID, result[1]);
			nextMoveBFS.set(snakeID, true);
		} else {
			finalResult = getNeighboringCell(getSnakeHead(snakeID));
			target.set(snakeID, finalResult);
		}
		nextMove.set(snakeID, finalResult);
		debugCycle ++;
		if (debugCycle % 100 == 0) {
			if (countSpace(finalResult) >= snakeCells.size()) {
				System.out.print("[CHILLING] ");
			} else {
				System.out.print("[SHITTING] ");
			}
			System.out.println("Moving to " + finalResult + " with " + countSpace(finalResult) + " empty spaces.");
		}
	}

	public BoardCell getNextCellFromBFS(int snakeID) {
		computeBFS(snakeID);
		return nextMove.get(snakeID);
	}

	public BoardCell getTargetCell(int snakeID) {
		if (nextMoveBFS.get(snakeID)) {
			return target.get(snakeID);
		}
		return null;
	}

	private int distanceTo(int snakeID, BoardCell cell) {
		int verticalDistance = Math.abs(getSnakeHead(snakeID).getRow() - cell.getRow());
		int horizontalDistance = Math.abs(getSnakeHead(snakeID).getCol() - cell.getCol());
		if (isWrap) {
			horizontalDistance = Math.min(horizontalDistance, getNumCols() - 2 - horizontalDistance);
			verticalDistance = Math.min(verticalDistance, getNumCols() - 2 - verticalDistance); 
		}
		return horizontalDistance + verticalDistance;
	}

	public ArrayList<BoardCell> floodFill(BoardCell cell) {
		// count number of neighboring cells
		int cnt = 0;
		HashMap<BoardCell, Boolean> mp = new HashMap<BoardCell, Boolean>();
		ArrayList<BoardCell> searchQueue = new ArrayList<BoardCell>();
		searchQueue.add(cell);
		mp.put(cell, true);
		while (cnt < searchQueue.size()) {
			BoardCell searchCell = searchQueue.get(cnt);
			for (BoardCell neighbor : getNeighbors(searchCell)) {
				if (neighbor == null) {
					continue;
				}
				if (!neighbor.isOpen() && !neighbor.isFood()) {
					continue;
				}
				if (mp.containsKey(neighbor)) {
					continue;
				}
				mp.put(neighbor, true);
				searchQueue.add(neighbor);
			}
			cnt ++;
		}
		return searchQueue;
	}
	
	public int countSpace(BoardCell cell) {
		return floodFill(cell).size();
	}

	public int[][][] voronoiFill() {
		int[][][] voronoiDistance = new int[NUM_CELLS_TALL][NUM_CELLS_WIDE][SNAKES];
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			int cnt = 0;
			HashMap<BoardCell, Boolean> mp = new HashMap<BoardCell, Boolean>();
			ArrayList<SearchInfo> searchQueue = new ArrayList<SearchInfo>();
			BoardCell headCell = this.getSnakeHead(snakeID);
			searchQueue.add(new SearchInfo(headCell, 0));
			mp.put(headCell, true);
			while (cnt < searchQueue.size()) {
				SearchInfo info = searchQueue.get(cnt);
				BoardCell cell = info.getCell();
				int dist = info.getDist();
				voronoiDistance[cell.getRow()][cell.getCol()][snakeID] = dist;
				for (BoardCell neighbor : getNeighbors(cell)) {
					if (neighbor == null) {
						continue;
					}
					if (!neighbor.isOpen() && !neighbor.isFood()) {
						continue;
					}
					if (mp.containsKey(neighbor)) {
						continue;
					}
					mp.put(neighbor, true);
					searchQueue.add(new SearchInfo(neighbor, dist + 1));
				}
				cnt ++;
			}
		}
		return voronoiDistance;
	}

	public void resetCellsForNextSearch() {
		for (int row = 0; row < this.getNumRows(); row ++) {
			for (int col = 0; col < this.getNumCols(); col ++) {
				boardCells2D[row][col].restartSearch();
			}
		}
	}

	public void updateSnakeID() {
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			LinkedList<BoardCell> snake = snakeCells.get(snakeID);
			for (int index = 0; index < snake.size(); index ++) {
				snake.get(index).setID(index);
				snake.get(index).setSnakeID(snakeID);
			}
		}
	}
	
	private void updateSnakeDirection(int snakeID) {
		BoardCell neck = this.getSnakeNeck(snakeID);
		if (this.getNorthNeighbor(snakeID).equals(neck)) currentMode.set(snakeID, GOING_SOUTH);
		else if (this.getSouthNeighbor(snakeID).equals(neck)) currentMode.set(snakeID, GOING_NORTH);
		else if (this.getEastNeighbor(snakeID).equals(neck)) currentMode.set(snakeID, GOING_WEST);
		else if (this.getWestNeighbor(snakeID).equals(neck)) currentMode.set(snakeID, GOING_EAST);
		else System.out.println("Weird direction");
	}

	public void reverseSnake(int snakeID) {
		this.getSnakeHead(snakeID).becomeBody();
		this.getSnakeTail(snakeID).becomeHead();
		LinkedList<BoardCell> revSnake = new LinkedList<BoardCell>();
		Iterator<BoardCell> it = snakeCells.get(snakeID).descendingIterator();
		while (it.hasNext()) {
			BoardCell snakeCell = it.next();
			revSnake.add(snakeCell);
		}
		snakeCells.set(snakeID, revSnake);
		updateSnakeDirection(snakeID);
	}

	public void setGameOver(int snakeID) {
		this.gameOver.set(snakeID, true);
	}

	public boolean getGameOver(int snakeID) {
		return this.gameOver.get(snakeID);
	}

	public boolean getGameOver() {
		int deadSnakes = 0;
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			if (this.getGameOver(snakeID)) {
				deadSnakes ++;
			}
		}
		return deadSnakes == Math.max(1, SNAKES - 1);
	}

	public String getCode() {
		return this.typedCode;
	}

	public void setCode(String typedCode) {
		this.typedCode = typedCode;
	}

	public void checkCode() {
		if (this.typedCode.equals(CHEAT_CODE)) {
			this.rainbowCheat = true;
		}
	}

	public boolean getRainbow() {
		return this.rainbowCheat;
	}

	public void toggleWrap() {
		this.isWrap = !this.isWrap;
	}

	public boolean getWrap() {
		return this.isWrap;
	}

	public String getScores() {
		ArrayList<String> res = new ArrayList<String>();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			res.add(String.valueOf(this.getScore(snakeID)));
		}
		return String.join(" - ", res);
	}

	/* ---------------------- */
	/* Testing Infrastructure */
	/* ---------------------- */

	public static SnakeProData getTestGame(TestGame gameNum) {
		return new SnakeProData(gameNum);
	}

	// Constructor used exclusively for testing!
	public SnakeProData(TestGame gameNum) {
		this.isWrap = false;
		this.boardCells2D = new BoardCell[6][6];
		this.fillRemainingCells();

		this.currentMode.add(GOING_EAST);
		this.snakeCells.add(new LinkedList<BoardCell>());
		this.scores.add(0);
		this.target.add(new BoardCell(-1, -1, OPEN));
		this.gameOver.add(false);
		this.nextMove.add(null);
		this.nextMoveBFS.add(false);

		if (gameNum.snakeAtStart()) {
			this.testing_snakeAtStartLocation(gameNum);
			this.setMode(0, GOING_EAST);
		} else {
			this.testing_snakeNotAtStartLocation(gameNum);
		}

		this.addWalls();
		this.fillRemainingCells();
	}

	private void testing_snakeAtStartLocation(TestGame gameNum) {
<<<<<<< HEAD
		this.setStartLocation(0);
=======
		this.setStartLocation();
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48
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
		int width = this.getNumCols();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				BoardCell cell = this.getCell(row, col);
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
			snakeCells.get(0).add(body2);
			snakeCells.get(0).add(body1);
			snakeCells.get(0).add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G13) {
			BoardCell body2 = this.getCell(3, 2);
			BoardCell body1 = this.getCell(2, 2);
			BoardCell head = this.getCell(2, 1);
			snakeCells.get(0).add(body2);
			snakeCells.get(0).add(body1);
			snakeCells.get(0).add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G14) {
			BoardCell body2 = this.getCell(2, 2);
			BoardCell body1 = this.getCell(3, 2);
			BoardCell head = this.getCell(3, 1);
			snakeCells.get(0).add(body2);
			snakeCells.get(0).add(body1);
			snakeCells.get(0).add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		} else if (gameNum == TestGame.G15) {
			BoardCell body2 = this.getCell(3, 2);
			BoardCell body1 = this.getCell(3, 3);
			BoardCell head = this.getCell(3, 4);
			snakeCells.get(0).add(body2);
			snakeCells.get(0).add(body1);
			snakeCells.get(0).add(head);
			head.becomeHead();
			body2.becomeBody();
			body1.becomeBody();
		}
	}

	public String toString() {
		String result = "";
		for (int r = 0; r < this.getNumRows(); r++) {
			for (int c = 0; c < this.getNumCols(); c++) {
				BoardCell cell = this.getCell(r, c);
				result += cell.toStringType();
			}
			result += "\n";
		}
		return result;
	}
	
	public String toStringParents(int snakeId) {
		String result = "";
		for (int r = 0; r < this.getNumRows(); r++) {
			for (int c = 0; c < this.getNumCols(); c++) {
				BoardCell cell = this.getCell(r, c);
				result += cell.toStringParent(snakeId) + "\t";
			}
			result += "\n";
		}
		return result;
	}

	private int debugCycle = 0;
}
