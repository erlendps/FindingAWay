package core;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Game {
	private int height;
	private int width;
	private Tile[][] board;
	private List<Tile> playerModel;

	
	public Game(int height, int width) {
		if (width > 0 && width > 0) {
			this.height = height;
			this.width = width;

			this.board = new Tile[height][width];
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
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Tile getTile(int x, int y) {
		if (isTile(x, y)) {
			return board[y][x];
		}
		else {
			throw new IllegalArgumentException("Tile does not exist");
		}
	}
	
	private boolean isTile(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}
	
	private boolean checkIfTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	@Override
	public String toString() {
		String out = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				out += getTile(x, y);
			}
			out += "\n";
		}
		return out;
	}
}

