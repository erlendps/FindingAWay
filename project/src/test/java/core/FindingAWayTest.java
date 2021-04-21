package core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FindingAWayTest {
	
	FindingAWay game;
	
	@BeforeEach
	public void setup() {
		initGame();
	}
	
	private void initGame() {
		game = new FindingAWay(new Level(8, 10));
		game.getTile(1, 1).setFinish();
		game.getTile(1, 3).setGround();
		game.getTile(2, 3).setGround();
		game.getTile(3, 3).setGround();
		game.getTile(7, 4).setBox();
		game.getTile(1, 6).setBox();
		game.getTile(1, 7).setGround();
		game.getTile(2, 7).setGround();
		game.getTile(3, 7).setGround();
		game.getTile(4, 7).setGround();
		game.getTile(4, 6).setGround();
		for (int y = 5; y < game.getHeight(); y++) {
			for (int x = 5; x < game.getWidth()-1; x++) {
				game.getTile(x, y).setGround();}}
		game.addPlayer(2, 6);
	}
	

}
