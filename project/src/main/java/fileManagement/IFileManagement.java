package fileManagement;

import core.FindingAWay;

public interface IFileManagement {
	
	FindingAWay loadGame(String fileName);
	
	boolean saveGame(String fileName, FindingAWay game);

}
