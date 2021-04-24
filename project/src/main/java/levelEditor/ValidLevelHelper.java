package levelEditor;

import core.AbstractGame;


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
		boolean out;
		try {
			out = level.getTile(level.getPlayerBody().getX(), level.getPlayerBody().getY() + 1).isCollisionBlock();
		}
		catch (IllegalArgumentException e) {
			out = true; // returns true if tile under player is not a tile.
		}
		return out;
	}
	
	private static boolean checkExactlyOneFinish(AbstractGame level) {
		if (level.getFinish() == null)
			return false;
		return true;
	}
	
}
