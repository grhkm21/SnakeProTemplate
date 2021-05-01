package Model;

import java.awt.Color;
import java.awt.Font;

public final class Preferences {
	public static final int REFRESH_RATE = 2;
	public static final int FOOD_ADD_RATE = 25;
	public static final int SLEEP_TIME = 30; // milliseconds between updates

	public static final int NUM_CELLS_WIDE = 50;
	public static final int NUM_CELLS_TALL = 30;
	public static final int CELL_SIZE = 10;
	public static final int SPACE_FOR_BUTTONS = 190;
	public static final int GAMEBOARDHEIGHT = NUM_CELLS_TALL * CELL_SIZE + SPACE_FOR_BUTTONS;
	public static final int GAMEBOARDWIDTH = NUM_CELLS_WIDE * CELL_SIZE;

	public static final Color COLOR_BACKGROUND = new Color(245, 214, 76);
	public static final Color COLOR_WALL = Color.BLUE;
	public static final Color COLOR_FOOD = Color.ORANGE;
	public static final Color COLOR_OPEN = Color.WHITE;
	public static final Color COLOR_HEAD = Color.BLACK;
	public static final Color COLOR_BODY = Color.GREEN;

	public static final int TITLE_X = 100;
	public static final int TITLE_Y = 40;
	public static final Font TITLE_FONT = new Font("Helvetica", Font.PLAIN, 30);	
	public static final Color TITLE_COLOR = new Color(236, 240, 241);
	public static final String TITLE = "Gareth's Snake Pro"; // TODO: Update the title!

	public static final int GAME_OVER_X = 150;
	public static final int GAME_OVER_Y = 200;
	public static final Font GAME_OVER_FONT = new Font("Helvetica", Font.PLAIN, 60);	
	public static final Color GAME_OVER_COLOR = Color.BLUE;
	public static final String GAME_OVER_TEXT = "Game Over";
}
