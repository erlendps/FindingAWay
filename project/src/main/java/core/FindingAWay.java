package core;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import levelEditor.ValidLevelHelper;


public class FindingAWay extends AbstractGame {
	private boolean isWon = false;
	private boolean isGameOver = false;
	private boolean boxPickedUp = false;

	public FindingAWay(Level level) {
		if (level != null) {
			super.level = level;
			super.height = level.getHeight();
			super.width = level.getWidth();
			super.board = level.getBoard();
			super.playerModel = level.getPlayerModel();
			super.finish = level.getFinish();
		}
		else
			throw new IllegalArgumentException("Level cant be null");
	}
	
	// either picks up or drops a box depending on if box is picked up.
	public void interactWithBox() {
		Tile boxTile = getBoxTileNearPlayer();

		if (boxTile != null) {
			if (!checkIfBoxPickedUp()) {
				int dy = boxTile.getY() - getPlayerHead().getY();
				int targetY = boxTile.getY() - dy;
				boxTile.setAir();
				getTile(boxTile.getX(), targetY).setBox();
				playerModel.add(2, getTile(boxTile.getX(), targetY));
				updateLevel();
			}
			else {
				playerModel.remove(2);
				while (boxInAir(boxTile)) {
					if (!isTile(boxTile.getX(), boxTile.getY() + 1))
						break;
					boxTile.setAir();
					boxTile = getTile(boxTile.getX(), boxTile.getY() + 1);
					boxTile.setBox();
					updateLevel();
				}

			}
			setBoxPickedUp();
			playerFalling();
		}
		else
			throw new IllegalStateException("No box near the player.");
	}
	
	// method that swaps the side the box is on, if its a valid "move"
	public void swapBoxSide() {
		if (!checkIfBoxPickedUp())
			throw new IllegalStateException("No box to swap side with.");
		
		if (getPlayerBox() == null)
			throw new NullPointerException("Box tile is null.");
		
		int dx = getPlayerBox().getX() - getPlayerHead().getX();
		int targetX = getPlayerHead().getX() - dx;
		int targetY = getPlayerHead().getY();
		if (!isTile(targetX, targetY) || getTile(targetX, targetY).isCollisionBlock())
			throw new IllegalStateException("Cant swap to this tile.");
		
		getPlayerBox().setAir();
		playerModel.set(2, getTile(targetX, targetY));
		getPlayerBox().setBox();
		if (checkIfFinished())
			setIsWon();
		
		updateLevel();
		playerFalling();
		}
	
	// helper method that returns true if the box is in the air, used only when
	// player drops a box.
	private boolean boxInAir(Tile box) {
		if (isTile(box.getX(), box.getY() + 1) &&
				getTile(box.getX(), box.getY() + 1).isAir())
			return true;
		else return false;
	}
	
	// helper method that returns a box Tile if it is close enough to the player.
	// returns null otherwise
	private Tile getBoxTileNearPlayer() {
		if (getPlayerModel() == null)
			throw new NullPointerException("Playermodel does not exist, add it first");
		List<Tile> iteratorList = new ArrayList<>();
		iteratorList.addAll(getPlayerModel());
		Collections.reverse(iteratorList); 	
		// reversing because if playerModel contains box-tile, box tile will be the index 0
		// if not, we get the head as index 0, so the code then checks if a box is closest
		// to the head of the player.
		
		// if this is true, we know its a box that the player is holding, which 
		// must be prioritized.
		if (iteratorList.get(0).isBox())
			return getTile(iteratorList.get(0).getX(), iteratorList.get(0).getY());
		
		// if over is false, we know iteratorList (reversed playerModel) does not
		// contain a box tile. Using try/catch so that it doesnt fail if it comes
		// to a tile that does not exists.
		for (Tile tile: iteratorList) {
			try {
				if (getTile(tile.getX()-1, tile.getY()).isBox()) 
					return getTile(tile.getX()-1, tile.getY());
				else if (getTile(tile.getX()+1, tile.getY()).isBox())
					return getTile(tile.getX()+1, tile.getY());	
			}
			catch (Exception e) {
				try {
					if (getTile(tile.getX()-1, tile.getY()).isBox()) 
						return getTile(tile.getX()-1, tile.getY());
				}
				catch (Exception f) {
					if (getTile(tile.getX()+1, tile.getY()).isBox())
						return getTile(tile.getX()+1, tile.getY());	
				}
			}
		}
		return null;
	}
	
	// method that returns true if the player is in the air after a move
	private boolean playerInAir() {
		Tile playerBody = getPlayerBody();
		Tile playerBox = getPlayerBox();
		if (isTile(playerBody.getX(), playerBody.getY() + 1)) {
			// needs two implementations depending on wheter the player is holding 
			// a box or not.
			if (playerBox == null) {
				if (!getTile(playerBody.getX(), playerBody.getY() + 1).isCollisionBlock())
					return true;
				else
					return false;
			}
			else {
				// if player holds a box, we need to consider the possibility that
				// the box tile could be the first that collides.
				if (getTile(playerBody.getX(), playerBody.getY() + 1).isCollisionBlock()
						|| getTile(playerBox.getX(), playerBox.getY() + 1).isCollisionBlock())
					return false;
				else
					return true;
			}
		}
		else {
			setGameOver();
			return false;
		}
	}
	
	// checks if a move is valid, i.e, if the target tiles are all air (or in 
	// an edge case, part of the playerModel)
	private boolean isValidMove(int dx) {
		List<Tile> targets;
		if (isMovingUp(dx))
			targets = getTargets(dx, -1);
		else
			targets = getTargets(dx, 0);
		
		if (targets == null)
			return false;
		
		for (Tile tile: targets) {
			if (!tile.isAir() && !getPlayerModel().contains(tile) && !tile.isFinish())
				return false;
		}
		return true;
	}
	
	// helper method that returns true if the player is intending to move "up" by
	// checking the tile beside the playerBody in the inteded direction.
	private boolean isMovingUp(int dx) {
		if (isTile(getPlayerBody().getX() + dx, getPlayerBody().getY()) &&
				getTile(getPlayerBody().getX() + dx, getPlayerBody().getY()).isCollisionBlock())
			return true;
		return false;	
	}
	
	// helper method that returns a list of the target Tiles (mathing the order
	// of the playerModel).
	private List<Tile> getTargets(int dx, int dy) {
		List<Tile> targets = new ArrayList<>();
		
		for (Tile tile: playerModel) {
			int targetX = tile.getX() + dx;
			int targetY = tile.getY() + dy;
			if (!isTile(targetX, targetY))
				return null;
			Tile targetTile = getTile(targetX, targetY);
			targets.add(targetTile);
		}
		return targets;
	}
	
	// helper method that returns a list of Characters (tile types) that matches
	// the order of the playerModel.
	private List<Character> getPlayerModelTypes() {
		List<Character> types = new ArrayList<>();
		for (Tile tile: getPlayerModel()) {
			types.add(tile.getType());
		}
		return types;
	}
	
	// moves the player if a series of validations are true
	private void move(int dx) {
		if (isWon() || isGameOver())
			throw new IllegalStateException("Game finished");
		
		if (!ValidLevelHelper.checkIfValidLevel(this))
			throw new IllegalStateException("Something is not right, the level is not valid.");
		
		List<Tile> targetPlayerModel;
		if (!isValidMove(dx))
			throw new IllegalStateException("Not a valid move");
		
		// move to the side and up
		else if (isValidMove(dx) && isMovingUp(dx)) 
			targetPlayerModel = getTargets(dx, -1);
		
		// move only to the side
		else 
			targetPlayerModel = getTargets(dx, 0);
		
		List<Character> playerModelTypes = getPlayerModelTypes();
		for (Tile tile: getPlayerModel()) {
			tile.setAir();
		}
		for (int i = 0; i < getPlayerModel().size(); i++) {
			targetPlayerModel.get(i).setType(playerModelTypes.get(i));
		}
		playerModel = targetPlayerModel;
		if (checkIfFinished())
			setIsWon();
		
		updateLevel();
		playerFalling();
	}
	
	// method that makes the player "fall" if the player is in the air after 
	// interacting (dropping) a box or after a move.
	private void playerFalling() {
		List<Character> playerModelTypes;
		List<Tile> targetPlayerModel;
		while(playerInAir()) {
			playerModelTypes = getPlayerModelTypes();
			for (Tile tile: getPlayerModel()) {
				tile.setAir();
			}
			targetPlayerModel = getTargets(0, 1);
			for (int i = 0; i < playerModel.size(); i++) {
				targetPlayerModel.get(i).setType(playerModelTypes.get(i));
			}
			playerModel = targetPlayerModel;
			if (checkIfFinished())
				setIsWon();
			updateLevel();
		}
	}
	
	// method that returns true if the game is finished
	private boolean checkIfFinished() {
		for (Tile tile: getPlayerModel()) {
			if (tile == getFinish())
				return true;
		}
		return false;
	}
	
	// moves left (dx = -1)
	public void moveLeft() {
		move(-1);
	}
	
	// moves right (dx = 1)
	public void moveRight() {
		move(1);
	}
	
	// setters and getters for isGameOver, isWon and boxPickedUp
	public boolean isGameOver() {
		return isGameOver;
	}
	
	public void setGameOver() {
		isGameOver = true;
	}
	
	public boolean isWon() {
		return isWon;
	}
	
	public void setIsWon() {
		isWon = true;
	}
	
	public boolean checkIfBoxPickedUp() {
		return boxPickedUp;
	}
	
	public void setBoxPickedUp() {
		if (checkIfBoxPickedUp())
			boxPickedUp = false;
		else
			boxPickedUp = true;
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
		if (isGameOver())
			out += "\n\nGame Over";
		else if (isWon())
			out += "\n\nGame won";
		
		return out;
	}

	

	public static void main(String[] args) throws FileNotFoundException {
		FindingAWay game = new FindingAWay(new Level(8, 10));
		game.getPlayerModel();
		game.addFinish(1, 1);;
		game.getTile(1, 3).setGround();
		game.getTile(2, 3).setGround();
		game.getTile(3, 3).setGround();
		game.getTile(7, 4).setBox();
		game.getTile(1, 6).setBox();
		game.getTile(1, 7).setGround();
		game.getTile(2, 7).setGround();
		game.getTile(3, 7).setGround();
		game.getTile(4, 7).setGround();
		game.getTile(4, 6).setGround();
		for (int y = 5; y < game.getHeight(); y++) {
			for (int x = 5; x < game.getWidth()-1; x++) {
				game.getTile(x, y).setGround();}}
		game.updateLevel();
		game.addPlayer(2, 6);		
	}
}

