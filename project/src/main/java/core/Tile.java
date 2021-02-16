package core;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

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
	
	public void setType(char type) {
		List<Character> validTypes = new ArrayList<>(Arrays.asList(' ', '#', '*', 'B', '=', 'o'));
		if (validTypes.contains(type)) {
			this.type = type;
		}
		else {
			throw new IllegalArgumentException("Invalid type");
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

}
