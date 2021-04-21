package fileManagement;

import core.AbstractGame;

public interface IFileManagement {
	
	AbstractGame loadGame(String fileName, String path);
	
	boolean saveGame(String fileName, AbstractGame game);

}
