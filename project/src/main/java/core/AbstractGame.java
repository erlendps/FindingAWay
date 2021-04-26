package core;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGame {
	protected int height;
	protected int width;
	protected Tile finish;
	protected Tile[][] board;
	protected List<Tile> playerModel;
	protected Level level;
	
	
	// getters
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Tile getTile(int x, int y) {
		if (isTile(x, y)) {
			return board[y][x];
		}
		else {
			throw new IllegalArgumentException("Tile does not exist");
		}
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Tile getFinish() {
		return finish;
	}
	
	// adds a finish tile to the board, and gives it the correct reference.
	public void addFinish(int x, int y) {
		if (finish != null)
			throw new IllegalStateException("Cant have more than one goal, use remove finish");
		if (!isTile(x, y))
			throw new IllegalArgumentException("Not a valid tile to place finish on");
		if (getTile(x, y).isAir()) {
			getTile(x, y).setFinish();
			finish = getTile(x, y);
		}
		else
			throw new IllegalArgumentException("Invalid placement of finish tile");	
	}
	
	// removes the finish tile if it exists.
	public void removeFinish() {
		if (finish == null)
			throw new IllegalStateException("Cant remove finish tile when it does not exist");
		finish.setAir();
		finish = null;
	}
	
	// adds a playermodel to the board, and references the tiles on the board
	// correctly to the list playerModel.
	public void addPlayer(int x, int y) {
		if (playerModel == null) {
			if (isTile(x, y) && isTile(x, y-1)) {
				if (getTile(x, y).isAir() && getTile(x, y-1).isAir()
						&& getTile(x, y+1).isCollisionBlock()) {
					getTile(x,y).setPlayer();
					getTile(x,y-1).setPlayer();
					playerModel = new ArrayList<>();
					playerModel.add(0, getTile(x, y));   // player body
					playerModel.add(1, getTile(x, y-1)); // player head
				}
				else
					throw new IllegalArgumentException("Invalid placement of player model.\n"
							+ "The tile under the player must be an collision block");
			}
			else
				throw new IllegalArgumentException("One of the tiles is not a tile on the board");
		}
		else
			throw new IllegalStateException("Already player on board, use remove player");
	}
	
	// removes the playermodel if it exists.
	public void removePlayer() {
		if (playerModel == null)
			throw new IllegalStateException("Can't remove player when it is non-existing");
		for (Tile tile: getPlayerModel()) {
			tile.setAir();
		}
		playerModel = null;
	}
	
	// helper-method to validate if a tile exists on the board, it is protected
	// such that subclasses can use it.
	protected boolean isTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	// Returns a copy of the playerModel if it exists. Returns null otherwise
	public List<Tile> getPlayerModel() {
		if (playerModel != null)
			return new ArrayList<>(playerModel);
		return null;
	}
	
	// returns the playerbody (index 0). It is equivilant as to doing:
	// getPlayerModel().get(0)
	public Tile getPlayerBody() {
		if (getPlayerModel() == null)
			throw new NullPointerException("Playermodel does not exist");
		return getPlayerModel().get(0);
	}
	
	// returns the playerhead (index 1 of playerModel). Throws a NullPointerException
	// if playerModel is null, because you are trying to access an index of null.
	public Tile getPlayerHead() {
		if (getPlayerModel() == null)
			throw new NullPointerException("Playermodel does not exist");
		return getPlayerModel().get(1);
		
	}
	
	// returns the box the player is holding (if a box is picked up) otherwise 
	// it returns null.
	public Tile getPlayerBox() {
		if (getPlayerModel() == null)
			throw new NullPointerException("Playermodel does not exist");
		if (getPlayerModel().size() > 2)
			return getPlayerModel().get(2);
		return null;
	}
	
	// updates the level
	public void updateLevel() {
		level.update(board, playerModel, finish);
	}
}
