package core;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Game {
	private int height;
	private int width;
	private Tile[][] board;
	private List<Player> playerModel;
	
	public Game(int height, int width) {
		if (isStrictlyPositiveInt(height) && isStrictlyPositiveInt(width)) {
			this.height = height;
			this.width = width;
			
			board = new Tile[height][width];
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					board[y][x] = new Tile(x, y);
				}
			}
		}
		else {
			throw new IllegalArgumentException("Height and width must be strictly positive numbers");
		}
	}
	
	private boolean isStrictlyPositiveInt(int number) {
		return number > 0;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	private boolean checkIfTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
}

