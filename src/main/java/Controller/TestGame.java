package Controller;

public enum TestGame {
	// Want pictures of the test boards?
	// http://tinyurl.com/snakeProTestBoards

	G1(true), G2(true), G3(true), G4(true), G5(true), G6(true), G7(true), G8(
			true), G9(true), G10(true), G11(true), G12(false), G13(false), G14(
			false), G15(false);
	private boolean snakeAtStart;

	private TestGame(boolean snakeAtStartInput) {
		this.snakeAtStart = snakeAtStartInput;
	}

	public boolean snakeAtStart() {
		return this.snakeAtStart;
	}
}
