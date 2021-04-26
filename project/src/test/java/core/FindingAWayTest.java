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
		game.addFinish(1, 1);
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
				game.getTile(x, y).setGround();
				}
			}
		game.addPlayer(2, 6);
	}
	
	/*
	 * The first part of testing will test the methods in the abstract superclass.
	 * The second part of testing will test methods specific for the FindingAWay
	 * class. The LevelEditorGame class will therefore not test its supermethods.
	 * The only exception is that we first test the constructor
	 */
	
	@Test
	@DisplayName("Test constructor")
	public void testConstructor() {
		Level level = new Level(10, 12);
		FindingAWay g2 = new FindingAWay(level);
		
		assertEquals(10, g2.getHeight());
		assertEquals(12, g2.getWidth());
		
		// test that all tiles are of type "air"
		for (int y = 0; y < g2.getHeight(); y++) {
			for (int x = 0; x < g2.getWidth(); x++) {
				assertTrue(g2.getTile(x, y).isAir());
			}
		}
		assertNull(g2.getFinish());
		assertNull(g2.getPlayerModel());
		
		assertEquals(level, g2.getLevel());
	}
	
	@Test
	@DisplayName("Test simple getters and setters")
	public void testSimpleGettersAndSetters() {
		// get height and width
		assertEquals(8, game.getHeight());
		assertEquals(10, game.getWidth());
		
		assertEquals(game.board[6][2], game.getTile(2, 6));
		
		// test on a tile that does not exist
		assertThrows(IllegalArgumentException.class, () -> game.getTile(-1, 1));
	}
	
	@Test
	@DisplayName("Test playerModel methods")
	public void testPlayerModel() {
		assertEquals(game.playerModel, game.getPlayerModel());
		game.removePlayer();
		assertNull(game.getPlayerModel());
		assertThrows(IllegalStateException.class, () -> game.removePlayer());
		assertThrows(NullPointerException.class, () -> game.getPlayerBody());
		// test invalid placement getTile(1, 7) is of type ground
		// getTile(2, 1) is of type air, but the tile under is also air
		assertThrows(IllegalArgumentException.class, () -> game.addPlayer(1, 7));
		assertThrows(IllegalArgumentException.class, () -> game.addPlayer(2, 1));
		// test addPlayer on valid tile
		game.addPlayer(2, 6);
		assertEquals(game.board[6][2], game.getPlayerBody());
		assertEquals(game.board[5][2], game.getPlayerHead());
		assertTrue(game.getTile(2, 6).isPlayer());
		// test that you can only add one player
		assertThrows(IllegalStateException.class, () -> game.addPlayer(3, 6));
	}
	
	@Test
	@DisplayName("Test finish tile methods")
	public void testFinish() {
		assertThrows(IllegalStateException.class, () -> game.addFinish(2, 1));
		game.removeFinish();
		// test that you cant remove something that does not exist.
		assertThrows(IllegalStateException.class, () -> game.removeFinish());
		// test the addFinish method with invalid placements
		assertThrows(IllegalArgumentException.class, () -> game.addFinish(-1, 5));
		assertThrows(IllegalArgumentException.class, () -> game.addFinish(3, 7));
		// test that the addFinish method works
		game.addFinish(1, 1);
		assertEquals(game.board[1][1], game.getFinish());
		assertTrue(game.getTile(1, 1).isFinish());
	}
	
	@Test
	@DisplayName("Test the update level method")
	public void testUpdateLevel() {
		Level level = new Level(10, 12);
		FindingAWay g2 = new FindingAWay(level);
		g2.getTile(5, 5).setGround();
		g2.addPlayer(5, 4);
		g2.updateLevel();
		assertEquals(level, g2.getLevel());
		assertEquals(g2.getPlayerModel(), level.getPlayerModel());
		assertEquals(g2.board, level.getBoard());
	}
	
	/*
	 * Second part of testing, where we test the game logic
	 */
	
	@Test
	@DisplayName("Test simple chekcers of logic")
	public void testSimpleCheckers() {
		assertFalse(game.isGameOver());
		assertFalse(game.isWon());
		assertFalse(game.checkIfBoxPickedUp());
	}
	
	@Test
	@DisplayName("Test interact with box")
	public void testInteractWithBox() {
		// test getPlayerBox method when box is not picked up
		assertNull(game.getPlayerBox());
		// pick up box
		game.interactWithBox();
		assertTrue(game.checkIfBoxPickedUp());
		assertEquals(3, game.getPlayerModel().size());
		assertEquals(game.getTile(1, 5), game.getPlayerBox());
		assertTrue(game.getPlayerBox().isBox());
		// drop box
		game.interactWithBox();
		assertFalse(game.checkIfBoxPickedUp());
		assertEquals(2, game.getPlayerModel().size());
		assertNull(game.getPlayerBox());
		
		// test edge cases where there are two boxes on top of each other
		// the highest box should be picked up
		game.getTile(1, 5).setBox();
		game.interactWithBox();
		assertNotEquals(game.getTile(1, 6), game.getPlayerBox());
		assertTrue(game.getTile(1, 6).isBox());
		assertEquals(game.getTile(1, 5), game.getPlayerBox());
		assertTrue(game.getTile(1, 6).isBox());
		// test when no box is near the player
		game.interactWithBox();
		game.getTile(1, 6).setAir();
		game.getTile(1, 5).setAir();
		assertThrows(IllegalStateException.class, () -> game.interactWithBox());
		// test when there is no playermodel
		game.removePlayer();
		game.getTile(1, 6).setBox();
		assertThrows(NullPointerException.class, () -> game.interactWithBox());
	}
	
	@Test
	@DisplayName("Test swap box side")
	public void testSwapBoxSide() {
		//test when player has not picked up box
		assertThrows(IllegalStateException.class, () -> game.swapBoxSide());
		// test when player has picked up box
		game.interactWithBox();
		game.swapBoxSide();
		assertEquals(game.getTile(3, 5), game.getPlayerBox());
		assertTrue(game.getTile(3, 5).isBox());
		assertFalse(game.getTile(1, 5).isBox());
		// test that you cant swap box side if you have ground beside player head
		game.getTile(1, 5).setGround();
		assertThrows(IllegalStateException.class, () -> game.swapBoxSide());
		
		// test that swapping box side so that the box covers finish tile, the game finishes
		game.getTile(1, 5).setAir();
		game.removeFinish();
		game.addFinish(1, 5);
		game.swapBoxSide();
		assertTrue(game.isWon());
	}
	
	@Test
	@DisplayName("Test basic movement without a box")
	public void testBasicMovement() {
		// moving on flat terrain with no box
		game.moveRight();
		assertEquals(game.getTile(3, 6), game.getPlayerBody());
		assertTrue(game.getTile(3, 6).isPlayer());
		assertFalse(game.getTile(2, 6).isPlayer());
		assertEquals(game.getTile(3, 5), game.getPlayerHead());
		assertTrue(game.getTile(3, 5).isPlayer());
		assertEquals(2, game.getPlayerModel().size());
		
		game.moveLeft();
		assertEquals(game.getTile(2, 6), game.getPlayerBody());
		assertTrue(game.getTile(2, 6).isPlayer());
		assertEquals(game.getTile(2, 5), game.getPlayerHead());
		assertTrue(game.getTile(2, 5).isPlayer());
		assertEquals(2, game.getPlayerModel().size());
	}
	
	@Test
	@DisplayName("Test climb staris and fall")
	public void testClimbAndFall() {
		// test that you can move up on a box
		game.moveLeft();
		assertEquals(game.getTile(1, 5), game.getPlayerBody());
		assertEquals(game.getTile(1, 4), game.getPlayerHead());
		assertTrue(game.getTile(1, 5).isPlayer());
		assertTrue(game.getTile(1, 4).isPlayer());
		
		// test that the player will "fall" when air is under them
		game.moveRight();
		assertEquals(game.getTile(2, 6), game.getPlayerBody());
		assertTrue(game.getTile(2, 6).isPlayer());
		assertEquals(game.getTile(2, 5), game.getPlayerHead());
		assertTrue(game.getTile(2, 5).isPlayer());
		
		// test that the player can climb regular ground tiles
		game.moveRight();
		game.moveRight();
		assertEquals(game.getTile(4, 5), game.getPlayerBody());
		assertEquals(game.getTile(4, 4), game.getPlayerHead());
		assertTrue(game.getTile(4, 5).isPlayer());
		assertTrue(game.getTile(4, 4).isPlayer());
		assertTrue(game.getTile(4, 6).isGround()); // tile under playerbody
	}
	
	@Test
	@DisplayName("Test a move that makes you lose")
	public void testLoserMove() {
		game.moveLeft();
		game.moveLeft();
		assertTrue(game.isGameOver());
		// tests that you cant move after game is over
		assertThrows(IllegalStateException.class, () -> game.moveRight());
	}
	
	@Test
	@DisplayName("Test a move that makes you win")
	public void testWinnerMove() {
		game.removeFinish();
		game.addFinish(3, 5);
		game.moveRight();
		assertTrue(game.isWon());
		assertThrows(IllegalStateException.class, () -> game.moveLeft());
	}
	
	@Test
	@DisplayName("Test invalid moves")
	public void testInvalidMoves() {
		game.getTile(1, 5).setGround();
		assertThrows(IllegalStateException.class, () -> game.moveLeft());
		// test trying to move outside of board
		game.getTile(1, 5).setAir();
		game.getTile(0, 6).setGround();
		game.moveLeft();
		game.moveLeft();
		assertThrows(IllegalStateException.class, () -> game.moveLeft());
		// test when player has picked up box
		initGame();
		game.getTile(0, 6).setGround();
		game.interactWithBox();
		game.moveLeft();
		assertThrows(IllegalStateException.class, () -> game.moveLeft());
	}
	
	@Test
	@DisplayName("test movement when the level is not valid")
	public void testMovementInvalidLevel() {
		game.removeFinish();
		assertThrows(IllegalStateException.class, () -> game.moveRight());
		game.addFinish(1, 1);
		game.removePlayer();
		assertThrows(IllegalStateException.class, () -> game.moveRight());
		game.addPlayer(2, 6);
		game.getTile(2, 7).setAir();
		assertThrows(IllegalStateException.class, () -> game.moveRight());
	}
	
	@Test
	@DisplayName("Test movement with box picked up")
	public void testMovementWithBox() {
		game.interactWithBox();
		game.moveRight();
		assertEquals(game.getTile(2, 5), game.getPlayerBox());
		assertEquals(3, game.getPlayerModel().size());
		// move up
		game.moveRight();
		assertEquals(game.getTile(3, 4), game.getPlayerBox());
		assertTrue(game.getPlayerBox().isBox());
		// falling with box
		game.moveLeft();
		assertEquals(game.getTile(2, 5), game.getPlayerBox());
		
		/*
		 * Edge case, if the player falls while holding box, and the box hits
		 * a collision block before the playerbody
		 */
		FindingAWay g2 = new FindingAWay(new Level(10, 5));
		g2.getTile(4, 3).setGround();
		g2.getTile(3, 3).setGround();
		g2.getTile(3, 2).setBox();
		g2.addPlayer(4, 2);
		g2.addFinish(0, 0);
		g2.getTile(1, 6).setGround();
		g2.getTile(2, 9).setGround();
		g2.interactWithBox();
		g2.moveLeft();
		g2.moveLeft();
		// here comes the test, the player should "hang" from the Tile at 2, 6
		assertEquals(g2.getTile(1, 5), g2.getPlayerBox());
		assertEquals(g2.getTile(2, 5), g2.getPlayerHead());
		assertEquals(g2.getTile(2, 6), g2.getPlayerBody());
		// then the player drops the box, the player should then fall
		g2.interactWithBox();
		assertEquals(g2.getTile(2, 8), g2.getPlayerBody());
		
	}
	

}
