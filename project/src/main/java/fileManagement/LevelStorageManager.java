package fileManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.AbstractGame;
import core.FindingAWay;
import levelEditor.LevelEditorGame;


/*
 * This is basically a copy of the StorageManager class, but the path is 
 * pointing to the levels directory.
 */

public class LevelStorageManager implements IFileManagement {
	
	private static final String PATH = System.getProperty("user.home") + "/tdt4100/FindingAWay/levels/";

	@Override
	public LevelEditorGame loadGame(String fileName) {
		Path path = Path.of(LevelStorageManager.PATH, fileName);
		try (FileInputStream fis = new FileInputStream(path.toFile());
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			FindingAWay tempGame = (FindingAWay) ois.readObject();
			return new LevelEditorGame(tempGame.getLevel());
			}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public boolean saveGame(String fileName, AbstractGame game) {
		if (!checkFileName(fileName)) {
			return false;
		}
		Path path = Path.of(LevelStorageManager.PATH, fileName);
		if (createNewFile(path)) {
			try (FileOutputStream fos = new FileOutputStream(path.toFile());
					ObjectOutputStream oos = new ObjectOutputStream(fos)) {
				oos.writeObject(game);
				oos.flush();
				return true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	
	/*
	 * Hjelpemetode som sjekker at filen allerede er laget
	 * 
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
	
	private boolean checkFileName(String fileName) {
		int indexOfSplitter = fileName.lastIndexOf('.');
		if (indexOfSplitter == -1) {
			return false;
		}
		String format = fileName.substring(indexOfSplitter);
		String name = fileName.substring(0, indexOfSplitter);
		if (!format.equals(".level") )
			return false;
		if (name.length() > 18 || name.length() <= 0)
			return false;
		return true;
	}

}
