package core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LevelTest {
	
	private Level level;
	
	private List<Tile> pm;
	private Tile[][] newBoard = new Tile[10][8];
	Tile finish = new Tile(1, 0);
	
	@BeforeEach
	public void setup() {
		level = new Level(10, 8);
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 8; x++) {
				newBoard[y][x] = new Tile(x, y);
			}
		}
		newBoard[0][1].setFinish();
		pm = new ArrayList<>(Arrays.asList(newBoard[0][0], newBoard[1][0]));
		pm.get(0).setPlayer();
		pm.get(1).setPlayer();
		finish.setFinish();
		level.update(newBoard, pm, finish);
	}
	
	@Test
	@DisplayName("Test constructor")
	public void testConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Level(-1, 8));
		assertThrows(IllegalArgumentException.class, () -> new Level(4, 0));
		Level l2 = new Level(5, 3);
		assertEquals(5, l2.getBoard().length);
		assertEquals(3, l2.getBoard()[0].length);
		for (int y = 0; y < 5; y++) {
			for (int x = 0; x < 3; x++) {
				assertEquals(' ', l2.getBoard()[y][x].getType());
			}
		}
		assertNull(l2.getPlayerModel());
		assertNull(l2.getFinish());
	}
	
	@Test
	@DisplayName("Test getters")
	public void testGetters() {
		assertEquals(10, level.getHeight());
		assertEquals(8, level.getWidth());
		assertEquals(pm, level.getPlayerModel());
		assertEquals(newBoard, level.getBoard());
		assertEquals(finish, level.getFinish());
	}
	
	@Test
	@DisplayName("Test update level")
	public void testUpdate() {
		Tile[][] board = new Tile[10][8];
		Tile newFinish = new Tile(7, 9);
		newFinish.setFinish();
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 8; x++) {
				board[y][x] = new Tile(x, y);}}
		board[9][7] = newFinish;
		board[9][0].setPlayer();
		board[8][0].setPlayer();
		List<Tile> newPM = new ArrayList<>(Arrays.asList(board[9][0], newFinish));
		// test invalid playermodel
		assertThrows(IllegalArgumentException.class, () -> level.update(board, newPM, newFinish));
		Tile invalidTile = new Tile(100, 20);
		invalidTile.setPlayer();
		newPM.set(1, invalidTile);
		assertThrows(IllegalArgumentException.class, () -> level.update(board, newPM, newFinish));
		newPM.set(1, board[8][0]);
		
		// test invalid finish
		assertThrows(IllegalArgumentException.class, () -> level.update(board, newPM, invalidTile));
		
		// test invalid board
		assertThrows(IllegalArgumentException.class, () -> level.update(null, newPM, newFinish));
		Tile[][] invalidBoard = new Tile[10][14];
		assertThrows(IllegalArgumentException.class, () -> level.update(invalidBoard, newPM, newFinish));
		
		level.update(board, newPM, newFinish);
		assertEquals(board, level.getBoard());
		assertEquals(newPM, level.getPlayerModel());
		assertEquals(newFinish, level.getFinish());
	}
	
}
