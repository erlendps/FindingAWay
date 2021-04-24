package fileManagement;

import java.io.FileNotFoundException;

import core.AbstractGame;

public interface IFileManagement {
	
	AbstractGame loadGame(String fileName, boolean loadEditor) throws FileNotFoundException;
	
	boolean saveGame(String fileName, AbstractGame game) throws FileNotFoundException;

}
