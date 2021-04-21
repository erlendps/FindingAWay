package levelEditor;

import core.AbstractGame;
import core.FindingAWay;

public class ValidLevelHelper {
	
	public static boolean checkIfValidLevel(AbstractGame level) {
		if (level == null)
			return false;
		return checkIfPlayerAdded(level) && checkIfCollisionUnderPlayer(level)
				&& checkExactlyOneGoal(level);
	}
	
	private static boolean checkIfPlayerAdded(AbstractGame level) {
		if (level.getPlayerModel() == null)
			return false;
		return true;
	}
	
	private static boolean checkIfCollisionUnderPlayer(AbstractGame level) {
		return level.getTile(level.getPlayerBody().getX(), level.getPlayerBody().getY() + 1).isCollisionBlock();
	}
	
	private static boolean checkExactlyOneGoal(AbstractGame level) {
		int count = 0;
		for (int y = 0; y < level.getHeight(); y++) {
			for (int x = 0; x < level.getWidth(); x++) {
				if (level.getTile(x, y).isFinish())
					count++;
			}
		}
		return count == 1;
	}
}
