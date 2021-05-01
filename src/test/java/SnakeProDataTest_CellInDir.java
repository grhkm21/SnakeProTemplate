import static org.junit.Assert.*;

import Model.SnakeProData;
import Controller.TestGame;
import Model.BoardCell;
import org.junit.Test;


public class SnakeProDataTest_CellInDir {

	@Test
	public void test_getNextCellNorth() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		myData.setDirectionNorth();
		BoardCell neighborCell = myData.getNextCellInDir();
		assertEquals("[0, 2, *]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(0, 2));
	}

	@Test
	public void test_getNextCellSouth() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		myData.setDirectionSouth();
		BoardCell neighborCell = myData.getNextCellInDir();
		assertEquals("[2, 2,  ]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(2, 2));
	}
	@Test
	public void test_getNextCellEast() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		myData.setDirectionEast();
		BoardCell neighborCell = myData.getNextCellInDir();
		assertEquals("[1, 3, X]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(1, 3));
	}

	@Test
	public void test_getNextCellWest() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		myData.setDirectionWest();
		BoardCell neighborCell = myData.getNextCellInDir();
		assertEquals("[1, 1, B]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(1, 1));
	}


}
