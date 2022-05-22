package Model;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;

public final class Preferences {
<<<<<<< HEAD

	public static final char REVERSE         = 'r';
	public static final char UP              = 'i';
	public static final char DOWN            = 'k';
	public static final char LEFT            = 'j';
	public static final char RIGHT           = 'l';
	public static final char BFS             = 'b';
	public static final char PLAY_FOOD_NOISE = 's';
	public static final char TOGGLE_PAUSE    = ' ';
	public static final char CHEAT           = 'x';
	public static final char NEW_GAME        = 'n';
	public static final char WRAP            = 'w';
	public static final char VORONOI         = 'v';
	public static final char CONFIRM         = 10;
	public static final char DELETE          = 8;
	
	// default parameters
	public static final int WALL_CYCLE_SPEED = 1;
	public static final int FOOD_ADD_RATE = 5;
	public static final int BFS_SLEEP_TIME = 60; // milliseconds between updates
	public static final int USER_SLEEP_TIME = 45; // milliseconds between updates
	public static final int SNAKES = 4;
	public static final int START_LENGTH = 2;
=======
	// TODO: Step 1g
	public static final int REFRESH_RATE = 2;
	public static final int FOOD_ADD_RATE = 25;
	public static final int SLEEP_TIME = 30; // milliseconds between updates

	public static final int NUM_CELLS_WIDE = 50;
	public static final int NUM_CELLS_TALL = 30;
	public static final int CELL_SIZE = 10;
	public static final int SPACE_FOR_BUTTONS = 190;
	public static final int CELLS_BUFFER = 60;
	public static final int GAMEBOARDHEIGHT = NUM_CELLS_TALL * CELL_SIZE + SPACE_FOR_BUTTONS;
	public static final int GAMEBOARDWIDTH = NUM_CELLS_WIDE * CELL_SIZE + CELLS_BUFFER;
>>>>>>> 4a7f241022243d75c22b72dcf552ca46d2f00e48

	public static final Color COLOR_BACKGROUND = new Color(0xF5E8DA);
	public static final Color COLOR_BUTTON = Color.ORANGE;
	public static final Color COLOR_WALL = Color.BLUE;
	public static final Color COLOR_FOOD = Color.ORANGE;
	// each snake has different color
	public static final Color[] COLOR_HEAD = {new Color(0x167D7F), new Color(0xECF87F), new Color(0x7832C8), new Color(0xB5E5CF)};
	public static final Color[] COLOR_TAIL = {new Color(0xDDFFE7), new Color(0x3D550C), new Color(0xC8D2FF), new Color(0xFCB5AC)};
	// dead has a special color palette
	public static final Color COLOR_DEAD_HEAD = new Color(200, 50, 120);
	public static final Color COLOR_DEAD_TAIL = new Color(255, 130, 200);
	public static final Color COLOR_OPEN = Color.WHITE;
	public static final float SATURATION = 0.9F;
	public static final float BRIGHTNESS = 0.8F;

	// used by score label, title and others
	public static final Font INFO_FONT = new Font("Helvetica", Font.PLAIN, 30);	
	public static final Font BUTTON_FONT = new Font("Helvetica", Font.BOLD, 24);	
	public static final Font GAMEOVER_FONT = new Font("Helvetica", Font.BOLD, 60);

	// Reference: https://developer.apple.com/library/archive/documentation/Cocoa/Conceptual/FontHandling/Tasks/GettingFontMetrics.html
	public static final int NUM_CELLS_WIDE = 60;
	public static final int NUM_CELLS_TALL = 35;
	public static final int CELL_SIZE = 16;
	public static final int CELL_BORDER = 3;
	public static final int CELLS_BUFFER = 30;
	public static final int SPACE_FOR_BUTTONS = 80;
	public static final int SPACE_FOR_LABELS = new Canvas().getFontMetrics(INFO_FONT).getHeight() * 3 / 2;

	// GAMEBOARD and DISPLAY
	public static final int GAMEBOARD_OFFSET_X = CELLS_BUFFER;
	public static final int GAMEBOARD_OFFSET_Y = SPACE_FOR_LABELS + CELLS_BUFFER;
	public static final int GAMEBOARD_WIDTH = NUM_CELLS_WIDE * CELL_SIZE;
	public static final int GAMEBOARD_HEIGHT = NUM_CELLS_TALL * CELL_SIZE;
	public static final int DISPLAY_WIDTH = GAMEBOARD_WIDTH + 2 * CELLS_BUFFER;
	public static final int DISPLAY_HEIGHT = SPACE_FOR_LABELS + GAMEBOARD_HEIGHT + 2 * CELLS_BUFFER;
	
	// Gameover and Title
	public static final Color TITLE_COLOR = new Color(236, 240, 241);
	public static final String TITLE = "Gareth's Cringe Project";
	public static final Color GAMEOVER_COLOR = Color.BLUE;
	public static final String GAMEOVER = "Game Over";
	public static final Color PAUSE_COLOR = Color.BLUE;
	public static final String PAUSE = "PAUSED";
	
	// Cheat codes
	public static final String CHEAT_CODE = "1973208";
}
