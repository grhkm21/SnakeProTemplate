package Model;

public enum CellType {
	WALL("*"), OPEN(" "), FOOD("X"), HEAD("H"), BODY("B");

	private final String displayChar;

	private CellType(String inputChar) {
		this.displayChar = inputChar;
	}

	public String getDisplayChar() {
		return this.displayChar;
	}
}