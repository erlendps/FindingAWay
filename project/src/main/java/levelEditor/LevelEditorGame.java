package levelEditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import core.AbstractGame;
import core.FindingAWay;
import core.Tile;
import core.Level;

public class LevelEditorGame extends AbstractGame {
	private static final long serialVersionUID = 1L;
	private Level level;
	private char type;
	
	public LevelEditorGame(Level level) {
		if (level != null) {
			super.level = level;
			super.height = level.getHeight();
			super.width = level.getWidth();
			super.board = level.getBoard();
			super.playerModel = level.getPlayerModel();
			type = ' ';
		}
		else
			throw new NullPointerException("Level cant be null");
	}
	
	public char getType() {
		return type;
	}
	
	public void setType(char type) {
		List<Character> validTypes = new ArrayList<>(Arrays.asList(' ', '#', 'B', '*', 'o'));
		if (!validTypes.contains(type))
			throw new IllegalArgumentException("Not a valid type");
		this.type = type;
	}
	
	public void setTile(int x, int y) {
		if (getTile(x, y).isPlayer())
			throw new IllegalStateException("Cant overwrite playerModel, use removePlayer insted.");
		if (this.getType() == 'o')
			super.addPlayer(x, y);
		else
			getTile(x, y).setType(this.getType());
	}
	
	


}
