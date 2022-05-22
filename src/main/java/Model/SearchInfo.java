package Model;

public class SearchInfo {
	private BoardCell cell;
	private int dist;

	public SearchInfo(BoardCell cell, int dist) {
		this.cell = cell;
		this.dist = dist;
	}

	public BoardCell getCell() {
		return this.cell;
	}

	public int getDist() {
		return this.dist;
	}
}
