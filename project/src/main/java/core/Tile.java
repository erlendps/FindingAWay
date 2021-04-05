package core;

import java.io.Serializable;

public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;
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
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getType() {
		return type;
	}
	

	// Method to set tile type if we dont know what type is
	public void setType(char type) {
		if (isValidType(type)) { //&& !isGround()) {
			this.type = type;
		}
		else {
			throw new IllegalArgumentException("Type does not exist.");
		}
	}
	
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
	
	public void setWater() {
		setType('=');
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
	
	public boolean isWater() {
		return getType() == '=';
	}
	
	public boolean isPlayer() {
		return getType() == 'o';
	}
	
	
	public boolean isCollisionBlock() {
		return isGround() || isBox();
	}
	
	private boolean isValidType(char type) {
		char[] validTypes = {' ', '#', '*', 'B', '=', 'o'};
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
		return String.format("%s" ,getType());
	}

}
