package core;

public class Tile {
	private int x;
	private int y;
	private char type;
	/*
	 * For the type, these are the different (allowed) types:
	 * ' ' = air
	 * '#' = ground/grass
	 * '*' = goal/finish
	 * 'B' = boxes (moveable)
	 * 'o' = player 
	 */
	
	public Tile(int x, int y) {
		if (x >= 0 && y >= 0) {
			this.x = x;
			this.y = y;
			this.type = ' ';
		}
		else {
			throw new IllegalArgumentException("X and Y coordinates must be positive.");
		}
	}
	
	public Tile(int x, int y, char type) {
		this(x, y);
		setType(type);
	}
	
	// getters
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getType() {
		return type;
	}
	
	// generalized setter, when its now known which type the user would set.
	public void setType(char type) {
		if (isValidType(type)) {
			this.type = type;
		}
		else {
			throw new IllegalArgumentException("Type does not exist.");
		}
	}
	
	// setters
	public void setAir() {
		setType(' ');
	}
	
	public void setGround() {
		setType('#');
	}
	
	public void setFinish() {
		setType('*');
	}
	
	public void setBox() {
		setType('B');
	}

	
	public void setPlayer() {
		setType('o');
	}
	
	
	public boolean isAir() {
		return getType() == ' ';
	}
	
	public boolean isGround() {
		return getType() == '#';
	}
	
	public boolean isFinish() {
		return getType() == '*';
	}
	
	public boolean isBox() {
		return getType() == 'B';
	}
	
	public boolean isPlayer() {
		return getType() == 'o';
	}
	
	public boolean isCollisionBlock() {
		return isGround() || isBox();
	}
	
	private boolean isValidType(char type) {
		char[] validTypes = {' ', '#', '*', 'B', 'o'};
		boolean contains = false;
		for (char t: validTypes) {
			if (t == type) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	@Override
	public String toString() {
		return String.format("%s" , getType());
	}

}
