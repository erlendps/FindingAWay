package core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TileTest {
	
	private Tile tile;
	
	@BeforeEach
	public void setup() {
		tile = new Tile(0,0);
	}
	
	
	@Test
	@DisplayName("Test the two constructors")
	public void testConstructor() {
		Tile tile2 = new Tile(1, 2);
		assertEquals(1, tile2.getX());
		assertEquals(2, tile2.getY());
		assertEquals(' ', tile2.getType());
		Tile tile3 = new Tile(2, 3, 'B');
		assertEquals('B', tile3.getType());
	}
	
	@Test
	@DisplayName("Test invalid constructor")
	public void testInvalidConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Tile(-1, 0));
		assertThrows(IllegalArgumentException.class, () -> new Tile(0, -1));
		assertThrows(IllegalArgumentException.class, () -> new Tile(3, 3, 'j'));
	}
	
	@Test
	@DisplayName("Test setters")
	public void testSetters() {
		tile.setBox();
		assertEquals('B', tile.getType());
		tile.setFinish();
		assertEquals('*', tile.getType());
		tile.setGround();
		assertEquals('#', tile.getType());
		tile.setPlayer();
		assertEquals('o', tile.getType());
		tile.setAir();
		assertEquals(' ', tile.getType());
		
		// test setType(char type) with invalid type
		assertThrows(IllegalArgumentException.class, () -> tile.setType('L'));
	}
	
	@Test
	@DisplayName("Test collision block")
	public void testIsCollisionBlock() {
		assertFalse(tile.isCollisionBlock());
		tile.setGround();
		assertTrue(tile.isCollisionBlock());
		tile.setBox();
		assertTrue(tile.isCollisionBlock());
		tile.setPlayer();
		assertFalse(tile.isCollisionBlock());
	}
	
	@Test
	@DisplayName("Test 'is'-methods") // will only test a few as they are all the same
	public void testIsMethods() {
		assertTrue(tile.isAir());
		assertFalse(tile.isFinish());
		tile.setGround();
		assertTrue(tile.isGround());
		assertFalse(tile.isPlayer());
	}

}
