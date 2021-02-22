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
				playerModel.add(0, getTile(x, y-1));
				playerModel.add(1, getTile(x, y));
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
		if (checkIfTile(x, y)) {
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
				int dy = boxTile.getY() - playerModel.get(0).getY();
				int targetY = boxTile.getY() - dy;
				boxTile.setAir();
				getTile(boxTile.getX(), targetY).setBox();
				playerModel.add(2, getTile(boxTile.getX(), targetY));
				boxPickedUp = true;
			}
			else {
				playerModel.remove(2);
				while (boxInAir(boxTile)) {
					if (!checkIfTile(boxTile.getX(), boxTile.getY() + 1))
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
			&& checkIfTile(box.getX(), box.getY() + 1))
			return true;
		else return false;
	}
	
	private boolean playerInAir() {
		Tile playerBody = playerModel.get(1);
		if (getTile(playerBody.getX(), playerBody.getY() + 1).isAir() 
			&& checkIfTile(playerBody.getX(), playerBody.getY() + 1))
			return true;
		else return false;
	}
	
	private Tile getBoxTileNearPlayer() {

		for (Tile tile: playerModel.subList(0, 2)) {
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
	
	private boolean checkIfTile(int x, int y) {
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight();
	}
	
	@Override
	public String toString() {
		String out = "";
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				out += getTile(x, y);
			}
			out += "\n";
		}
		return out;
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
		game.interactWithBox();
		System.out.println(game);
	}
}

