package fileManagement;

import core.AbstractGame;

public interface IFileManagement {
	
	AbstractGame loadGame(String fileName, boolean loadEditor);
	
	boolean saveGame(String fileName, AbstractGame game);

}
