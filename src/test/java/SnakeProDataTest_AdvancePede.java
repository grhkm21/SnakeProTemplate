import static org.junit.Assert.*;

import Controller.SnakeProBrain;
import Controller.TestGame;
import org.junit.Test;

public class SnakeProDataTest_AdvancePede {

	@Test
	public void test_eatFood() {
		SnakeProBrain brain = new SnakeProBrain(TestGame.G1);
		brain.updateSnake();
		String boardString = brain.draw.data.toString();
		String correctBoardString = "******\n" + "*BBH *\n" + "*    *\n"
				+ "*    *\n" + "*    *\n" + "******\n";
		// Sample debugging output:
		// System.out.println("G1");
		// System.out.println("Expected:");
		// System.out.println(correctBoardString);
		// System.out.println("Actual:");
		// System.out.println(boardString);
		assertEquals(correctBoardString, boardString);
	}
	
	@Test
	public void test_noFoodEaten() {
		SnakeProBrain brain = new SnakeProBrain(TestGame.G1);
		brain.updateSnake();
		String boardString = brain.draw.data.toString();
		String correctBoardString = "******\n" + "* BH *\n" + "* X  *\n"
				+ "*    *\n" + "*    *\n" + "******\n";
		// Sample debugging output:
		// System.out.println("G2");
		// System.out.println("Expected:");
		// System.out.println(correctBoardString);
		// System.out.println("Actual:");
		// System.out.println(boardString);
		assertEquals(correctBoardString, boardString);
	}
}
