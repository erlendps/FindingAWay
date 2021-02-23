package core;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Game {
	private int height;
	private int width;
	private Tile[][] board;
	private List<Tile> playerModel;
	private boolean isWon = false;
	private boolean isGameOver = false;
	private boolean boxPickedUp = false;

	
	public Game(int height, int width) {
		if (width > 0 && width > 0) {
			this.height = height;
			this.width = width;

			this.board = new Tile[height][width];
			board = new Tile[height][width];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					board[y][x] = new Tile(x, y);
				}
			}
		}
		else {
			throw new IllegalArgumentException("Height and width must be strictly positive numbers");
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
				throw new IllegalStateException("Invalid placement of player model");
		}
		else
			throw new IllegalStateException("Already player on board.");
	}
	
//	public void addBox(int x, int y) {
//		if (!getTile(x,y).isAir()) {
//			throw new IllegalStateException("Illegal placement of box");
//		}
//		getTile(x,y).setBox();
//	}
	
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
	
	public void interactWithBox() {
		Tile boxTile = getBoxTileNearPlayer();
		if (!boxTile.isAir())
			if (!boxPickedUp) {
				int dy = boxTile.getY() - playerModel.get(1).getY();
				int targetY = boxTile.getY() - dy;
				boxTile.setAir();
				getTile(boxTile.getX(), targetY).setBox();
				playerModel.add(2, getTile(boxTile.getX(), targetY));
				boxPickedUp = true;
			}
			else {
				playerModel.remove(2);
				while (boxInAir(boxTile)) {
					if (!isTile(boxTile.getX(), boxTile.getY() + 1))
						isGameOver = true;
					if (isGameOver)
						break;
					boxTile.setAir();
					boxTile = getTile(boxTile.getX(), boxTile.getY() + 1);
					boxTile.setBox();
					boxPickedUp = false;
				}
			}
		else
			throw new IllegalStateException("No box near the player.");
	}
	
	private boolean boxInAir(Tile box) {
		if (getTile(box.getX(), box.getY() + 1).isAir()
			&& isTile(box.getX(), box.getY() + 1))
			return true;
		else return false;
	}
	
	private boolean playerInAir() {
		Tile playerBody = playerModel.get(0);
		if (getTile(playerBody.getX(), playerBody.getY() + 1).isAir() 
			&& isTile(playerBody.getX(), playerBody.getY() + 1))
			return true;
		else return false;
	}
	
	private Tile getBoxTileNearPlayer() {
		for (Tile tile: playerModel.subList(0, playerModel.size())) {
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
		return new Tile(0,0);
	}
	
	private boolean isTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	private boolean isTile(Tile tile) {
		return isTile(tile.getX(), tile.getY());
	}
	
	public void swapBoxSide() {
		if (!boxPickedUp)
			throw new IllegalStateException("No box to swap side with.");
		
		int dx = playerModel.get(2).getX() - playerModel.get(1).getX();
		int targetX = playerModel.get(1).getX() - dx;
		int targetY = playerModel.get(1).getY();
		if (!isTile(targetX, targetY) || !getTile(targetX, targetY).isAir())
			throw new IllegalStateException("Cant swap to this tile.");
		
		playerModel.get(2).setAir();
		playerModel.set(2, getTile(targetX, targetY));
		playerModel.get(2).setBox();
		}
	
	private boolean isValidMove(int dx) {
		List<Tile> targets = new ArrayList<>();
		
		for (Tile tile: playerModel) {
			int targetX = tile.getX() + dx;
			int targetY = tile.getY();
			if (!isTile(targetX, targetY))
				return false;
			Tile targetTile = getTile(targetX, targetY);
			targets.add(targetTile);
		}
		if (targets.get(0).isCollisionBlock()) {
			if (!getTile(targets.get(0).getX(), targets.get(0).getY() - 1).isCollisionBlock()) {
				// checks if the tile above is a collision block: if it isnt, it indicates a stair
				for (int i = 0; i < targets.size(); i++) {
					targets.set(i, getTile(targets.get(i).getX(), targets.get(i).getY() - 1));
					// it then shifts all target-tiles 1 tile up.
				}
			} else return false;
		}
		
		for (Tile tile: targets) {
			if (tile.isCollisionBlock() && tile != playerModel.get(playerModel.size()-1))
				return false;}
		
		return true;
	}
	
	private void move(int dx) {
		if (!isValidMove(dx))
			throw new IllegalStateException("Making this move would put the game"
					+ " in an illegal state.");
		
	}
	
	@Override
	public String toString() {
		String out = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (getTile(x, y) == playerModel.get(1))
					out += 'p';
				else
					out += getTile(x, y);
			}
			out += "\n";
		}
		return out;
	}
	
	public void moveLeft() {
		move(-1);
	}
	
	public void moveRight() {
		move(1);
	}
	
	public static void main(String[] args) {
		Game game = new Game(8, 10);
		game.getTile(1, 1).setFinish();
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
		game.addPlayer(2, 6);
		System.out.println(game);
		game.interactWithBox();
		System.out.println(game);
		game.swapBoxSide();
		System.out.println(game);
		game.interactWithBox();
		System.out.println(game);
	}
}

