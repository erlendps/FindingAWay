package levelEditor;

import core.AbstractGame;
import core.FindingAWay;

public class ValidLevelHelper {
	
	public static boolean checkIfValidLevel(AbstractGame level) {
		if (level == null)
			return false;
		return checkIfPlayerAdded(level) && checkIfCollisionUnderPlayer(level)
				&& checkExactlyOneFinish(level);
	}
	
	private static boolean checkIfPlayerAdded(AbstractGame level) {
		if (level.getPlayerModel() == null)
			return false;
		return true;
	}
	
	private static boolean checkIfCollisionUnderPlayer(AbstractGame level) {
		return level.getTile(level.getPlayerBody().getX(), level.getPlayerBody().getY() + 1).isCollisionBlock();
	}
	
	private static boolean checkExactlyOneFinish(AbstractGame level) {
		if (level.getFinish() == null)
			return false;
		return true;
	}
}
