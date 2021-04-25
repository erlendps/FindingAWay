package levelEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.AbstractGame;
import core.Level;

public class LevelEditorGame extends AbstractGame {
	private char editorType;
	
	public LevelEditorGame(Level level) {
		if (level != null) {
			super.level = level;
			super.height = level.getHeight();
			super.width = level.getWidth();
			super.board = level.getBoard();
			super.playerModel = level.getPlayerModel();
			super.finish = level.getFinish();
			editorType = ' ';
		}
		else
			throw new IllegalArgumentException("Level cant be null");
	}
	
	public char getEditorType() {
		return editorType;
	}
	
	public void setType(char type) {
		List<Character> validTypes = new ArrayList<>(Arrays.asList(' ', '#', 'B', '*', 'o'));
		if (!validTypes.contains(type))
			throw new IllegalArgumentException("Not a valid type");
		this.editorType = type;
	}
	
	public void setTile(int x, int y) {
		if (!isTile(x, y))
			throw new IllegalArgumentException(String.format("X=%d, Y=%d. This is not a tile.", x, y));
		if (getTile(x, y).isPlayer())
			throw new IllegalStateException("Cant overwrite playerModel, use removePlayer insted.");
		if (getTile(x, y).isFinish())
			throw new IllegalStateException("Cant overwrite finish tile, use removeFinish instead.");
		if (getEditorType() == 'o')
			super.addPlayer(x, y);
		else if (getEditorType() == '*')
			super.addFinish(x, y);
		else
			getTile(x, y).setType(getEditorType());
	}
	
	@Override
	public String toString() {
		String out = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getTile(x, y) == getPlayerHead())
					out += 'p';
				else
					out += getTile(x, y);
			}
			out += "\n";
		}
		return out;
	}
	
	


}
