package levelEditor;

import core.AbstractGame;


public class ValidLevelHelper {
	
	// checks if a Level AbstractGame object is valid
	public static boolean checkIfValidLevel(AbstractGame level) {
		if (level == null)
			return false;
		return checkIfPlayerAdded(level) && checkIfCollisionUnderPlayer(level)
				&& checkExactlyOneFinish(level);
	}
	
	// method that checks wheter player is added
	private static boolean checkIfPlayerAdded(AbstractGame level) {
		if (level.getPlayerModel() == null)
			return false;
		return true;
	}
	
	// checks that its a collision tile under the player. If the tile under the
	// player is not a tile, it will return true, so that the game can be saved if
	// for example the game is over.
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
	
	// checks that its excayly one finish on the board. It does not mean that there
	// exists a tile that has type finish, because one could save the game after
	// a game is won.
	private static boolean checkExactlyOneFinish(AbstractGame level) {
		if (level.getFinish() == null)
			return false;
		return true;
	}
	
}
