package levelEditor;

import core.FindingAWay;

public class LevelEditorHelper {
	
	public static boolean checkIfValidLevel(FindingAWay level) {
		if (level == null)
			return false;
		return checkIfPlayerAdded(level) && checkIfCollisionUnderPlayer(level)
				&& checkExactlyOneGoal(level);
	}
	
	private static boolean checkIfPlayerAdded(FindingAWay level) {
		if (level.getPlayerModel() == null)
			return false;
		return true;
	}
	
	private static boolean checkIfCollisionUnderPlayer(FindingAWay level) {
		return level.getTile(level.getPlayerBody().getX(), level.getPlayerBody().getY() + 1).isCollisionBlock();
	}
	
	private static boolean checkExactlyOneGoal(FindingAWay level) {
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
