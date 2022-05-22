import static org.junit.Assert.*;

import Model.SnakeProData;
import Controller.TestGame;
import Model.BoardCell;
import org.junit.Test;


public class SnakeProDataTest_Reverse {
	// Want pictures of the test boards?
	// https://tinyurl.com/wl4zelg
	@Test
	public void test_ReverseNorth() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G14);
		display.reverseSnake(0);
		BoardCell nextCell = display.getNextCellInDir(0);
		assertEquals("[1, 2,  ]", nextCell.toString());
	}
	@Test
	public void test_ReverseSouth() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G13);
		display.reverseSnake(0);
		BoardCell nextCell = display.getNextCellInDir(0);
		assertEquals("[4, 2,  ]", nextCell.toString());
	}
	@Test
	public void test_ReverseEast() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G12);
		display.reverseSnake(0);
		BoardCell nextCell = display.getNextCellInDir(0);
		assertEquals("[2, 4,  ]", nextCell.toString());
	}
	@Test
	public void test_ReverseWest() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G15);
		display.reverseSnake(0);
		BoardCell nextCell = display.getNextCellInDir(0);
		assertEquals("[3, 1,  ]", nextCell.toString());
	}


}
