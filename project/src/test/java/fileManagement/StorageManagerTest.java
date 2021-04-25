package fileManagement;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.FindingAWay;
import core.Level;
import levelEditor.LevelEditorGame;

public class StorageManagerTest {
	private StorageManager sm;
	private FindingAWay game;
	private LevelEditorGame editor;
	
	@BeforeEach
	public void setup() throws FileNotFoundException {
		sm = new StorageManager();
		setupGame();
		setupEditor();
	}
	
	private void setupGame() throws FileNotFoundException {
		game = new FindingAWay(new Level(5, 5));
		game.getTile(0, 4).setGround();
		game.getTile(1, 4).setGround();
		game.getTile(1, 3).setBox();
		game.getTile(2, 4).setGround();
		game.getTile(3, 4).setGround();
		game.getTile(4, 4).setGround();
		game.addPlayer(0, 3);
		game.addFinish(4, 2);
		sm.saveGame("game.txt", game);
	}
	
	private void setupEditor() throws FileNotFoundException {
		editor = new LevelEditorGame(new Level(5, 5));
		editor.setType('#');
		for (int i = 0; i < 4; i++) {
			editor.setTile(i, 4);
		}
		editor.setType('B');
		editor.setTile(1, 3);
		editor.setType('o');
		editor.setTile(0, 3);
		editor.setType('*');
		editor.setTile(4, 2);
		sm.saveGame("editor.txt", editor);
	}
	
	@Test
	@DisplayName("Test load to FindingAWay")
	public void testLoadGame() {
		FindingAWay temp;
		try {
			temp = (FindingAWay) sm.loadGame("game.txt", false);
		} catch (FileNotFoundException e) {
			fail("Could not load");
			return;
		}
		assertEquals(game.toString(), temp.toString());
		assertFalse(temp.isWon());
		assertEquals(game.getPlayerBody().getX(), temp.getPlayerBody().getX());
		assertEquals(game.getPlayerBody().getY(), temp.getPlayerBody().getY());
		assertEquals(game.getWidth(), temp.getWidth());
		assertEquals(game.getHeight(), temp.getHeight());
	}
	
	@Test
	@DisplayName("Test that box is picked up")
	public void testLoadBoxPickedUp() {
		// test that box is picked up
		game.interactWithBox();
		try {
			sm.saveGame("game.txt", game);
		} catch (FileNotFoundException e) {
			fail("something went wrong");
			return;
		}
		FindingAWay temp;
		try {
			temp = (FindingAWay) sm.loadGame("game.txt", false);
		} catch (FileNotFoundException e) {
			fail("Could not load");
			return;
		}
		assertTrue(temp.checkIfBoxPickedUp());
		assertNotNull(temp.getPlayerBox());
		assertEquals(temp.getTile(1, 2), temp.getPlayerBox());
	}
	
	@Test
	@DisplayName("Test that game is won after loading")
	public void testLoadGameWon() {
		// finishing the game
		while (!game.isWon()) {
			game.moveRight();
		}
		try {
			sm.saveGame("game.txt", game);
		}
		catch (FileNotFoundException e) {
			fail("Error loading");
			return;
		}
		FindingAWay temp;
		try {
			temp = (FindingAWay) sm.loadGame("game.txt", false);
		} catch (FileNotFoundException e) {
			fail("Could not load");
			return;
		}
		assertTrue(temp.isWon());
		assertThrows(IllegalStateException.class, () -> temp.moveLeft());
	}
	
	@Test
	@DisplayName("Test that game is over after loading")
	public void testLoadGameOver() {
		// losing the game
		game.getTile(2, 4).setAir();
		game.moveRight();
		game.moveRight();
		try {
			sm.saveGame("game.txt", game);
		}
		catch (FileNotFoundException e) {
			fail("Error loading");
			return;
		}
		FindingAWay temp;
		try {
			temp = (FindingAWay) sm.loadGame("game.txt", false);
		} catch (FileNotFoundException e) {
			fail("Could not load");
			return;
		}
		assertTrue(temp.isGameOver());
		assertThrows(IllegalStateException.class, () -> temp.moveLeft());
	}

	@Test
	@DisplayName("Test load to LevelEditorGame")
	public void testLoadEditor() {
		LevelEditorGame temp;
		try {
			temp = (LevelEditorGame) sm.loadGame("game.txt", true);
		} catch (FileNotFoundException e) {
			fail("Could not load");
			return;
		}
		assertEquals(game.toString(), temp.toString());
		assertEquals(game.getPlayerBody().getX(), temp.getPlayerBody().getX());
		assertEquals(game.getPlayerBody().getY(), temp.getPlayerBody().getY());
		assertEquals(game.getWidth(), temp.getWidth());
		assertEquals(game.getHeight(), temp.getHeight());
	}
	
	@Test
	@DisplayName("Test load nonexisting level")
	public void testLoadNonExistingFile() {
		assertThrows(
				FileNotFoundException.class,
				() -> sm.loadGame("not_existing.txt", false)
				);
	}
	
	@Test
	@DisplayName("Test load invalid file")
	public void testLoadInvalidFile() {
		// test when the playerModel is not valid
		assertThrows(Exception.class, () -> sm.loadGame("invalid_playermodel.txt", false));
		// when a tile is not correct
		assertThrows(Exception.class, () -> sm.loadGame("invalid_tile.txt", true));
		// when widht is negative
		assertThrows(Exception.class, () -> sm.loadGame("invalid_width.txt", false));
	}
	
	@Test
	@DisplayName("Test save FindingAWay")
	public void testSaveGame() {
		try {
			sm.saveGame("created_save.txt", game);
		}
		catch (Exception e) {
			fail("error loading");
			return;
		}
		List<String> gameFile;
		List<String> testFile;
		try {
			gameFile = Files.readAllLines(Path.of(StorageManager.SAVES_FOLDER, "game.txt"));
		} catch (IOException e) {
			fail("error reading control file");
			return;
		}
		try {
			testFile = Files.readAllLines(Path.of(StorageManager.SAVES_FOLDER, "created_save.txt"));
		} catch (IOException e) {
			fail("error reading test file");
			return;
		}
		
		assertNotNull(gameFile);
		assertNotNull(testFile);
		assertEquals(gameFile, testFile);
	}
	
	@Test
	@DisplayName("Test save LevelEditorGame")
	public void testSaveEditor() {
		try {
			sm.saveGame("created_save.txt", editor);
		}
		catch (Exception e) {
			fail("error loading");
			return;
		}
		List<String> editorFile;
		List<String> testFile;
		try {
			editorFile = Files.readAllLines(Path.of(StorageManager.SAVES_FOLDER, "editor.txt"));
		} catch (IOException e) {
			fail("error reading control file");
			return;
		}
		try {
			testFile = Files.readAllLines(Path.of(StorageManager.SAVES_FOLDER, "created_save.txt"));
		} catch (IOException e) {
			fail("error reading test file");
			return;
		}
		
		assertNotNull(editorFile);
		assertNotNull(testFile);
		assertEquals(editorFile, testFile);
	}
	
	@Test
	@DisplayName("Test to save an invalid level")
	public void testSaveInvalidLevel() {
		//removing playerModel
		game.removePlayer();
		assertThrows(IllegalArgumentException.class, () -> sm.saveGame("created_save.txt", game));
		game.addPlayer(0, 3);
		// invalid file name
		assertThrows(IllegalArgumentException.class, () -> sm.saveGame("haha.ser", game));
		// invalid finish
		game.removeFinish();
		assertThrows(IllegalArgumentException.class, () -> sm.saveGame("created_save.txt", game));
	}
	
	@AfterAll
	public static void teardown() {
		File tempFile = new File(StorageManager.SAVES_FOLDER, "created_save.txt");
		File gameFile = new File(StorageManager.SAVES_FOLDER, "game.txt");
		File editorFile = new File(StorageManager.SAVES_FOLDER, "editor.txt");
		tempFile.delete();
		gameFile.delete();
		editorFile.delete();
	}
}
