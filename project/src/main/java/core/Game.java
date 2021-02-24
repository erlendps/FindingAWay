package core;
import java.util.ArrayList;
import java.util.List;

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
		Tile playerBody = getPlayerBody();
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
	
	
	public void swapBoxSide() {
		if (!boxPickedUp)
			throw new IllegalStateException("No box to swap side with.");
		
		int dx = getPlayerBox().getX() - getPlayerHead().getX();
		int targetX = getPlayerHead().getX() - dx;
		int targetY = getPlayerHead().getY();
		if (!isTile(targetX, targetY) || !getTile(targetX, targetY).isAir())
			throw new IllegalStateException("Cant swap to this tile.");
		
		getPlayerBox().setAir();
		playerModel.set(2, getTile(targetX, targetY));
		getPlayerBox().setBox();
		}
	
	
	private int checkIfValidMove(int dx) {
		List<Tile> targets = new ArrayList<>();
		
		for (Tile tile: playerModel) {
			int targetX = tile.getX() + dx;
			int targetY = tile.getY();
			if (!isTile(targetX, targetY))
				return 0;
			Tile targetTile = getTile(targetX, targetY);
			targets.add(targetTile);
		}
		if (targets.get(0).isCollisionBlock()) {
			if (!getTile(targets.get(0).getX(), targets.get(0).getY() - 1).isCollisionBlock()
				|| 
				getTile(targets.get(0).getX(), targets.get(0).getY() - 1) == playerModel.get(playerModel.size() - 1)) {
				// checks if the tile above is a collision block: if it isnt, it indicates a stair
				for (int i = 0; i < targets.size(); i++) {
					targets.set(i, getTile(targets.get(i).getX(), targets.get(i).getY() - 1));
				} // it then shifts all target-tiles 1 tile up.
					
				for (Tile tile: targets) {
					if (tile.isCollisionBlock() && tile != playerModel.get(playerModel.size()-1))
						return 0;}
				return 2;
				
			} else return 0;
		}
		
		for (Tile tile: targets) {
			if (tile.isCollisionBlock() && tile != playerModel.get(playerModel.size()-1))
				return 0;}
		
		return 1;
	}
	
	
	private void move(int dx) {
		if (isWon || isGameOver)
			throw new IllegalStateException("Game finished");
		int controller = checkIfValidMove(dx);
		if (controller == 0)
			throw new IllegalStateException("Making this move would put the game"
					+ " in an illegal state.");
		else if (controller == 1) {
			List<Tile> newPlayerModel = new ArrayList<>();
			for (Tile tile: playerModel) {
				newPlayerModel.add(getTile(tile.getX() + dx, tile.getY()));
				tile.setAir();
			}
			
			for (Tile tile: newPlayerModel.subList(0, 2)) {
				if (tile.isFinish())
					isWon = true;}
			
			for (int i = 0; i < newPlayerModel.size(); i++) {
				if (i == 2)
					newPlayerModel.get(i).setBox();
				else
					newPlayerModel.get(i).setPlayer();
			}
			playerModel = newPlayerModel;
			
			while (playerInAir()) {
				Tile playerBody = getPlayerBody();
				
				if (!isTile(playerBody.getX(), playerBody.getY() + 1)) {
					isGameOver = true;
				}
				for (Tile tile: playerModel) {
					tile.setAir();
				}
				List<Tile> newPlayer = new ArrayList<>();
				for (Tile tile: playerModel) {
					Tile targetTile = getTile(tile.getX(), tile.getY() + 1);
					try { 
						if (tile == getPlayerBox())
							targetTile.setBox();
						else
							targetTile.setPlayer();
					} catch (Exception e) {
						targetTile.setPlayer();
					}
					newPlayer.add(targetTile);
				}
				playerModel = newPlayer;
			}
		}
		else {
			for (Tile tile: playerModel) {
				tile.setAir();
			}
			List<Tile> newPlayerModel = new ArrayList<>();
			for (Tile tile: playerModel) {
				Tile targetTile = getTile(tile.getX() + dx, tile.getY() - 1);
				if (targetTile.isFinish())
					isWon = true;
				try { 
					if (tile == getPlayerBox())
						targetTile.setBox();
					else
						targetTile.setPlayer();
				} catch (Exception e) {
					targetTile.setPlayer();
				}
				newPlayerModel.add(targetTile);
			}
			playerModel = newPlayerModel;
		}
	}
	
	
	private boolean isTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	public void moveLeft() {
		move(-1);
	}
	
	public void moveRight() {
		move(1);
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
	
	private boolean isGameOver() {
		return isGameOver;
	}
	
	private boolean isWon() {
		return isWon;
	}
	
	private Tile getPlayerHead() {
		return playerModel.get(1);
	}
	
	private Tile getPlayerBody() {
		return playerModel.get(0);
	}
	
	private Tile getPlayerBox() {
		return playerModel.get(2);
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
		game.moveRight();
		System.out.println(game);
		game.moveRight();
		System.out.println(game);
		game.moveRight();
		game.swapBoxSide();
		System.out.println(game);
		game.interactWithBox();
		System.out.println(game);
		game.moveRight();
		game.interactWithBox();
		System.out.println(game);
		game.moveLeft();
		game.swapBoxSide();
		System.out.println(game);
		game.interactWithBox();
		System.out.println(game);
		game.moveLeft();
		game.moveLeft();
		System.out.println(game);
		game.moveLeft();
		game.moveLeft();
		System.out.println(game);
	}
}

