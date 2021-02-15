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
	 * '=' = water
	 * 'o' = player head
	 */
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		this.type = ' ';
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getType() {
		return type;
	}
	
	// Method to set tile type if we dont know what type is. Used when importing board from saved file
	public void setType(char type) {
		if (isValidType(type)) {
			this.type = type;
		}
		else {
			throw new IllegalArgumentException("Type does not exist.");
		}
	}
	
	public void setAir() {
		type = ' ';
	}
	
	public void setGround() {
		type = '#';
	}
	
	public void setFinish() {
		type = '*';
	}
	
	public void setBox() {
		type = 'B';
	}
	
	public void setWater() {
		type = '=';
	}
	
	public void setPlayer() {
		type = 'o';
	}
	
	public boolean isAir() {
		return type == ' ';
	}
	
	public boolean isGround() {
		return type == '#';
	}
	
	public boolean isFinish() {
		return type == '*';
	}
	
	public boolean isBox() {
		return type == 'B';
	}
	
	public boolean isWater() {
		return type == '=';
	}
	
	public boolean isPlayer() {
		return type == 'o';
	}
	
	
	public boolean isCollisionBlock() {
		return isGround() || isBox();
	}
	
	private boolean isValidType(char type) {
		char[] validTypes = {' ', '*', 'B', '=', 'o'};
		boolean contains = false;
		for (char t: validTypes) {
			if (t == type) {
				contains = true;
			}
		}
		return contains;
	}

}
