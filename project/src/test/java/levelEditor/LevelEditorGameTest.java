package levelEditor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Level;

public class LevelEditorGameTest {
	LevelEditorGame editor;
	
	@BeforeEach
	public void setup() {
		editor = new LevelEditorGame(new Level(10, 8));
	}
	
	@Test
	@DisplayName("Test constructor")
	public void testConstructor() {
		LevelEditorGame editor2 = new LevelEditorGame(new Level(5, 6));
		assertEquals(5, editor2.getHeight());
		assertEquals(6, editor2.getWidth());
		assertNull(editor2.getFinish());
		assertNull(editor2.getPlayerModel());
		
		for (int y = 0; y < editor2.getHeight(); y++) {
			for (int x = 0; x < editor2.getWidth(); x++) {
				assertEquals(' ', editor2.getTile(x, y).getType());
			}
		}
		assertEquals(' ', editor2.getEditorType());
		assertThrows(IllegalArgumentException.class, () -> new LevelEditorGame(null));
	}
	
	@Test
	@DisplayName("Test setEditorType")
	public void testSetEditorType() {
		editor.setType('B');
		assertEquals('B', editor.getEditorType());
		editor.setType('o');
		assertEquals('o', editor.getEditorType());
		assertThrows(IllegalArgumentException.class, () -> editor.setType('.'));
	}
	
	@Test
	@DisplayName("Test setTile")
	public void testSetTile() {
		editor.setType('#');
		editor.setTile(0, 0);
		editor.setTile(2, 9);
		assertEquals('#', editor.getTile(0, 0).getType());
		editor.setType('B');
		editor.setTile(1, 0);
		assertEquals('B', editor.getTile(1, 0).getType());
		// testing finish
		editor.setType('*');
		editor.setTile(7, 9);
		assertEquals(editor.getTile(7, 9), editor.getFinish());
		// testing player
		editor.setType('o');
		editor.setTile(2, 8);
		assertEquals(editor.getTile(2, 8), editor.getPlayerBody());
		assertEquals(editor.getTile(2, 7), editor.getPlayerHead());
		// test that you cant edit a tile that does not exist
		editor.setType('#');
		assertThrows(IllegalArgumentException.class, () -> editor.setTile(-1, 0));
	}
	
	@Test
	@DisplayName("Test invalid placements of finish and trying to overwrite finish")
	public void testInvalidFinishAndOverwrite() {
		editor.setType('#');
		editor.setTile(0, 0);
		editor.setType('*');
		assertThrows(IllegalArgumentException.class, () -> editor.setTile(0, 0));
		
		editor.setTile(7, 9);
		// trying to add more than one finish
		assertThrows(IllegalStateException.class, () -> editor.setTile(5, 5));
		// test that you can add finish after you have first removed it
		editor.removeFinish();
		editor.setTile(5, 5);
		assertEquals(editor.getTile(5, 5), editor.getFinish());
		// trying to overwrite existing finish
		editor.setType(' ');
		assertThrows(IllegalStateException.class, () -> editor.setTile(5, 5));
	}
	
	@Test
	@DisplayName("Test invalid placements of player and trying to overwrite player")
	public void testInvalidPlayerAndOverwrite() {
		// The FindingAWayTest has already tested the addPlayer()-method (which
		// LevelEditorGame.setTile(x, y) uses, this test will therefore not 
		// test as thoroughly as the FindingAWayTest
		editor.setType('#');
		editor.setTile(4, 9);
		editor.setTile(5, 9);
		// test an invalid placement
		editor.setType('o');
		assertThrows(IllegalArgumentException.class, () -> editor.setTile(4, 9));
		assertThrows(IllegalArgumentException.class, () -> editor.setTile(4, 7));
		// test that you cannot add more than one player
		editor.setTile(4, 8);
		assertThrows(IllegalStateException.class, () -> editor.setTile(5, 8));
		// test that you can add player after it has been removed
		editor.removePlayer();
		editor.setTile(5, 8);
		assertEquals(editor.getTile(5, 8), editor.getPlayerBody());
		// test that you cannot overwrite
		editor.setType('*');
		assertThrows(IllegalStateException.class, () -> editor.setTile(5, 7));
	}
}
