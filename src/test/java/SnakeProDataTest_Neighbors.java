import static org.junit.Assert.*;

import Model.SnakeProData;
import Controller.TestGame;
import Model.BoardCell;
import org.junit.Test;


public class SnakeProDataTest_Neighbors {
	// Want pictures of the test boards?
	// https://tinyurl.com/wl4zelg
	@Test
	public void test_NorthNeighborWithArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell focalCell = myData.getCell(2, 3);
		BoardCell neighborCell = myData.getNorthNeighbor(focalCell);
		assertEquals("[1, 3, X]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(1, 3));
	}
	@Test
	public void test_NorthNeighborWithoutArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell neighborCell = myData.getNorthNeighbor();
		assertEquals("[0, 2, *]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(0, 2));

	}
	@Test
	public void test_SouthNeighborWithArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell focalCell = myData.getCell(2, 3);
		BoardCell neighborCell = myData.getSouthNeighbor(focalCell);
		assertEquals("[3, 3,  ]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(3, 3));
	}
	@Test
	public void test_SouthNeighborWithoutArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell neighborCell = myData.getSouthNeighbor();
		assertEquals("[2, 2,  ]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(2, 2));
	}
	@Test
	public void test_EastNeighborWithArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell focalCell = myData.getCell(2, 3);
		BoardCell neighborCell = myData.getEastNeighbor(focalCell);
		assertEquals("[2, 4,  ]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(2, 4));
	}
	@Test
	public void test_EastNeighborWithoutArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell neighborCell = myData.getEastNeighbor();
		assertEquals("[1, 3, X]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(1, 3));
	}

	@Test
	public void test_WestNeighborWithArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell focalCell = myData.getCell(2, 3);
		BoardCell neighborCell = myData.getWestNeighbor(focalCell);
		assertEquals("[2, 2,  ]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(2, 2));
	}
	@Test
	public void test_WestNeighborWithoutArgument() {
		SnakeProData myData = new SnakeProData(TestGame.G1);
		BoardCell neighborCell = myData.getWestNeighbor();
		assertEquals("[1, 1, B]", neighborCell.toString());
		// check they're the same object (Not a new Model.BoardCell!)
		assertTrue(neighborCell == myData.getCell(1, 1));
	}
}
