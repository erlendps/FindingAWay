package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGame implements Serializable {
	private static final long serialVersionUID = 1L;
	protected int height;
	protected int width;
	protected Tile[][] board;
	protected List<Tile> playerModel;
	protected Level level;
	
	
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
	
	public void addPlayer(int x, int y) {
		if (playerModel == null) {
			if (getTile(x, y).isAir() && getTile(x, y-1).isAir()) {
				getTile(x,y).setPlayer();
				getTile(x,y-1).setPlayer();
				playerModel = new ArrayList<>();
				playerModel.add(0, getTile(x, y));   // player body
				playerModel.add(1, getTile(x, y-1)); // player head
			}
			else
				throw new IllegalArgumentException("Invalid placement of player model");
		}
		else
			throw new IllegalStateException("Already player on board.");
	}
	
	public void removePlayer() {
		if (playerModel == null)
			throw new IllegalStateException("Can't remove player when it is non-existing");
		for (Tile tile: playerModel) {
			tile.setAir();
		}
		playerModel = null;
	}
	
	protected boolean isTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	public Tile getPlayerHead() {
		if (playerModel == null)
			throw new NullPointerException("Playermodel does not exist");
		return getPlayerModel().get(1);//playerModel.get(1);
		
	}
	
	public Tile getPlayerBody() {
		if (playerModel == null)
			throw new NullPointerException("Playermodel does not exist");
		return getPlayerModel().get(0);
	}
	
	public Tile getPlayerBox() {
		if (playerModel == null)
			throw new NullPointerException("Playermodel does not exist");
		if (playerModel.size() > 2)
			return getPlayerModel().get(2);
		return null;
	}
	
	public List<Tile> getPlayerModel() {
		if (playerModel != null)
			return new ArrayList<>(playerModel);
		return null;
	}
}
