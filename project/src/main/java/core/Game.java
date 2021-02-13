package core;

public class Game {
	private int height;
	private int width;
	private int[][] board;
	
	public Game(int height, int width) {
		if (isStrictlyPositiveInt(height) && isStrictlyPositiveInt(width)) {
			this.height = height;
			this.width = width;
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

