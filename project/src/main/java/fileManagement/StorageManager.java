package fileManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import core.AbstractGame;
import core.FindingAWay;
import core.Level;
import core.Tile;
import levelEditor.LevelEditorGame;
import levelEditor.ValidLevelHelper;

public class StorageManager implements IFileManagement {
	public static final String SAVES_FOLDER = Path.of(System.getProperty("user.home"),
			"tdt4100", "FindingAWay", "saves").toString();

	/*
	 * Loads an AbstractGame. The boolean decides if it should cast to FindingAWay
	 * (false) or LevelEditorGame (true).
	 */
	@Override
	public AbstractGame loadGame(String fileName, boolean loadEditor) throws FileNotFoundException {
		Path path = Path.of(StorageManager.SAVES_FOLDER, fileName);
		
		
		try (Scanner scanner = new Scanner(path.toFile())) {
			int height = scanner.nextInt();
			int width = scanner.nextInt();
			Level level = new Level(height, width);
			Tile[][] board = new Tile[height][width];
			scanner.nextLine();
			String boardString = scanner.nextLine();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					board[y][x] = new Tile(x, y, boardString.charAt(y * width + x));
				}
			}
			int playerModelSize = scanner.nextInt();
			List<Tile> playerModel = new ArrayList<>();
			for (int i = 0; i < playerModelSize; i++) {
				int X = scanner.nextInt();
				int Y = scanner.nextInt();
				playerModel.add(board[Y][X]);
			}
			int finishX = scanner.nextInt();
			int finishY = scanner.nextInt();
			Tile finish = board[finishY][finishX];
			level.update(board, playerModel, finish);
			if (loadEditor) {
				LevelEditorGame editor = new LevelEditorGame(level);
				if (ValidLevelHelper.checkIfValidLevel(editor))
					return editor;
				else
					throw new IllegalStateException("This is not a valid level");
			}
			else {
				FindingAWay game = new FindingAWay(level);
				if (scanner.nextBoolean())
					game.setIsWon();
				if (scanner.nextBoolean())
					game.setGameOver();
				if (scanner.nextBoolean())
					game.setBoxPickedUp();
				
				if (ValidLevelHelper.checkIfValidLevel(game))
					return game;
				else
					throw new IllegalStateException("This is not a valid level");
			}
		}
	}
	
	/*
	 * Saves a AbstractGame with the given file name
	 */
	@Override
	public void saveGame(String fileName, AbstractGame game) throws FileNotFoundException {
		if (!checkFileName(fileName)) 
			throw new IllegalArgumentException("Not a valid file name");
		
		if (!ValidLevelHelper.checkIfValidLevel(game)) {
			throw new IllegalArgumentException("Not a valid level");
		}
		
		Path path = Path.of(StorageManager.SAVES_FOLDER, fileName);
		
		if (createNewFile(path)) {
			try (PrintWriter pw = new PrintWriter(path.toFile())) {
				pw.println(game.getHeight());
				pw.println(game.getWidth());
				for (int y = 0; y < game.getHeight(); y++) {
					for (int x = 0; x < game.getWidth(); x++) {
						pw.print(game.getTile(x, y).getType());
					}
				}
				pw.println();
				pw.println(game.getPlayerModel().size());
				for (Tile tile: game.getPlayerModel()) {
					pw.print(tile.getX());
					pw.print(" ");
					pw.println(tile.getY());
				}
				pw.print(game.getFinish().getX());
				pw.print(" ");
				pw.println(game.getFinish().getY());
				if (game instanceof FindingAWay) {
					pw.println(((FindingAWay) game).isWon());
					pw.println(((FindingAWay) game).isGameOver());
					pw.println(((FindingAWay) game).checkIfBoxPickedUp());
				}
				else {
					for (int i = 0; i < 3; i++) {
						pw.println(false);
					}
				}
				pw.flush();
			}
		}
		else
			throw new FileNotFoundException();
	}

	
	/*
	 * Helper method that creates directories if not created already.
	 * I got it from lecturer Stein L. Tomassen in 
	 * foreksempel/src/main/java/w10/household_complete/storage/StorageManager.java
	 */
	private boolean createNewFile(Path file) {
		try {
			Files.createDirectories(file.getParent());
			File fil = new File(file.toString());
			fil.createNewFile();
			return true;
		} catch (FileAlreadyExistsException e) {
			try {
				File fil = new File(file.toString());
				fil.createNewFile();
				return true;
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	/*
	 * Helper method that validates the name of the file
	 */
	private boolean checkFileName(String fileName) {
		int indexOfSplitter = fileName.lastIndexOf('.');
		if (indexOfSplitter == -1) {
			return false;
		}
		String format = fileName.substring(indexOfSplitter);
		String name = fileName.substring(0, indexOfSplitter);
		if (!format.equals(".txt"))
			return false;
		if (name.length() > 18 || name.length() <= 0)
			return false;
		return true;
	}

}
