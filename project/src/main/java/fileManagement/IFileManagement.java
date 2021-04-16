package fileManagement;

import core.AbstractGame;

public interface IFileManagement {
	
	AbstractGame loadGame(String fileName);
	
	boolean saveGame(String fileName, AbstractGame game);

}
