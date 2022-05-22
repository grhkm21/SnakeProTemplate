package Model;

import static Model.CellType.*;
import static Model.Preferences.*;
import java.awt.Color;
import java.util.ArrayList;

public class BoardCell {
	private int row;
	private int col;
	// when it is snake, tail = 0, head = len - 1
	// when it is wall, clockwise from top left is 0 to len - 1
	private int snakeID = -1;
	private int id = -1;

	private CellType myCellType;

	private boolean addedToSearchList = false; 
	private ArrayList<BoardCell> parent = new ArrayList<BoardCell>();

	public BoardCell(int row, int col, CellType myCellType) {
		this.row = row;
		this.col = col;
		this.myCellType = myCellType;
		this.resetParent();
	}

	public BoardCell(int row, int col, CellType myCellType, int id) {
		if (myCellType != WALL) {
			throw new IllegalArgumentException("Wrong constructor used!");
		}
		this.row = row;
		this.col = col;
		this.myCellType = myCellType;
		this.id = id;
		this.resetParent();
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	public int getID() {
		return this.id;
	}

	public int getSnakeID() {
		return this.snakeID;
	}

	public boolean isWall() {
		return this.myCellType == WALL;
	}

	public boolean isOpen() {
		return this.myCellType == OPEN;
	}

	public boolean isFood() {
		return this.myCellType == FOOD;
	}

	public boolean isBody() {
		return this.myCellType == BODY;
	}

	public boolean isHead() {
		return this.myCellType == HEAD;
	}

	public boolean isSnake() {
		return this.isBody() || this.isHead();
	}

	public Color getCellColor() {
		if (this.isWall()) return null;
		if (this.isOpen()) return COLOR_OPEN;
		if (this.isFood()) return COLOR_FOOD;
		if (this.isBody()) return null;
		if (this.isHead()) return null;
		return null;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setSnakeID(int snakeID) {
		this.snakeID = snakeID;
	}

	public void becomeFood() {
		this.myCellType = FOOD;
	}

	public void becomeOpen() {
		this.myCellType = OPEN;
	}

	public void becomeHead() {
		this.myCellType = HEAD;
	}

	public void becomeBody() {
		this.myCellType = BODY;
	}

	public void becomeHead(int snakeID) {
		this.myCellType = HEAD;
		this.snakeID = snakeID;
	}

	public void becomeBody(int snakeID) {
		this.myCellType = BODY;
		this.snakeID = snakeID;
	}

	public void setAddedToSearchList() {
		this.addedToSearchList = true;
	}

	public boolean inSearchListAlready() {
		return this.addedToSearchList;
	}

	public void restartSearch() {
		this.addedToSearchList = false;
	}

	public void resetParent() {
		this.parent = new ArrayList<BoardCell>();
		for (int snakeID = 0; snakeID < SNAKES; snakeID ++) {
			this.parent.add(null);
		}
	}

	public void setParent(int snakeID, BoardCell p) {
		if (this.parent == null) {
			resetParent();
		}
		this.parent.set(snakeID, p);
	}
	
	public BoardCell getParent(int snakeID) {
		if (this.parent == null) {
			resetParent();
		}
		return this.parent.get(snakeID);
	}

	/* ---------------------------- */
	/* Helper functions for testing */
	/* ---------------------------- */

	public String toString() {
		return "[" + this.row + ", " + this.col + ", " + this.toStringType() + "]";
	}

	public String toStringType() {
		return this.myCellType.getDisplayChar();
	}

	public String toStringParent(int snakeID) {
		if (this.parent == null || this.parent.get(snakeID) == null) {
			return "[null]";
		} else {
			return "[" + this.parent.get(snakeID).row + ", " + this.parent.get(snakeID).col + "]";
		}
	}
}