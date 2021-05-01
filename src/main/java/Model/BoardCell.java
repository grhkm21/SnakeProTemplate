package Model;

import java.awt.Color;

public class BoardCell {
	private int row;
	private int col;

	private CellType myCellType;

	private boolean addedToSearchList = false; 
	private BoardCell parent = null; 

	public BoardCell(int row, int col, CellType myCellType) {
		this.row = row;
		this.col = col;
		this.myCellType = myCellType;
	}

	public int getRow() {
		return this.row;
	}

	public int getColumn() {
		return this.col;
	}

	public boolean isWall() {
		return this.myCellType == CellType.WALL;
	}

	public boolean isOpen() {
		return this.myCellType == CellType.OPEN;
	}

	public boolean isFood() {
		return this.myCellType == CellType.FOOD;
	}

	public boolean isBody() {
		return this.myCellType == CellType.BODY;
	}

	public boolean isHead() {
		return this.myCellType == CellType.HEAD;
	}

	public Color getCellColor() {
		if (this.isWall()) return Preferences.COLOR_WALL;
		if (this.isOpen()) return Preferences.COLOR_OPEN;
		if (this.isFood()) return Preferences.COLOR_FOOD;
		if (this.isBody()) return Preferences.COLOR_BODY;
		if (this.isHead()) return Preferences.COLOR_HEAD;
		return Preferences.COLOR_OPEN;
	}

	public void becomeFood() {
		this.myCellType = CellType.FOOD;
	}

	public void becomeOpen() {
		this.myCellType = CellType.OPEN;
	}

	public void becomeHead() {
		this.myCellType = CellType.HEAD;
	}

	public void becomeBody() {
		this.myCellType = CellType.BODY;
	}

	public void setAddedToSearchList() {
		this.addedToSearchList = true;
	}

	public boolean inSearchListAlready() {
		return this.addedToSearchList;
	}

	public void clear_RestartSearch() {
		this.addedToSearchList = false;
		this.parent = null;
	}

	public void setParent(BoardCell p) {
		this.parent = p;
	}
	
	public BoardCell getParent() {
		return this.parent;
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

	public String toStringParent(){
		if (this.parent == null) {
			return "[null]";
		} else {
			return "[" + this.parent.row + ", " + this.parent.col + "]";
		}
	}
}