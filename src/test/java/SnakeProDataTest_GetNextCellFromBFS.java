import static org.junit.Assert.*;

import Model.SnakeProData;
import Controller.TestGame;
import Model.BoardCell;
import org.junit.Test;

public class SnakeProDataTest_GetNextCellFromBFS {
	// Want pictures of the test boards?
	// https://tinyurl.com/wl4zelg

	@Test
	public void testG1_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G1);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[1, 3, X]", nextCell.toString());
	}

	@Test
	public void testG2_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G2);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2, X]", nextCell.toString());
	}

	@Test
	public void testG3_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G3);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[1, 3,  ]", nextCell.toString());
	}

	@Test
	public void testG4_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G4);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2,  ]", nextCell.toString());
	}

	@Test
	public void testG5_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G5);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2,  ]", nextCell.toString());
	}

	@Test
	public void testG6_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G6);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[1, 3, X]", nextCell.toString());
	}

	@Test
	public void testG7_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G7);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2, X]", nextCell.toString());
	}

	@Test
	public void testG8_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G8);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[1, 3,  ]", nextCell.toString());
	}

	@Test
	public void testG9_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G9);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2,  ]", nextCell.toString());
	}

	@Test
	public void testG10_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G10);
		BoardCell nextCell = display.getNextCellFromBFS();
		assertEquals("[2, 2,  ]", nextCell.toString());
		System.out.println(display.toStringParents());
	}

	@Test
	public void testG11_BFS() {
		SnakeProData display = SnakeProData.getTestGame(TestGame.G11);
		BoardCell nextCell = display.getNextCellFromBFS();
		// NEED AN OR!
		String possibleResult1 = "[1, 3,  ]";
		String possibleResult2 = "[2, 2,  ]";
		String nextCellString = nextCell.toString();
		assertTrue(possibleResult1.equals(nextCellString)
				|| possibleResult2.equals(nextCellString));
	}

}
