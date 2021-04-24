package core;

import java.util.List;

public class Level {
	private int height;
	private int width;
	private Tile finish;
	private Tile[][] board;
	private List<Tile> playerModel;
	
	public Level(int height, int width) {
		if (height > 0 && width > 0) {
			this.height = height;
			this.width = width;
			
			board = new Tile[height][width];

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					board[y][x] = new Tile(x, y);
				}
			}
		}
		else {
			throw new IllegalArgumentException("Height and width must be" + 
					" strictly positive.");
		}
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Tile[][] getBoard() {
		return board;
	}
	
	public List<Tile> getPlayerModel() {
		return playerModel;
	}
	
	public Tile getFinish() {
		return finish;
	}
	
	private boolean isTile(Tile tile) {
		if (tile == null)
			return false;
		
		return tile.getX() >= 0 && tile.getX() < getWidth() 
				&& tile.getY() >= 0 && tile.getY() < getHeight();
	}
	
	public void update(Tile[][] board, List<Tile> playerModel, Tile finish) {
		setBoard(board);
		setPlayerModel(playerModel);
		setFinish(finish);
	}
	
	private void setBoard(Tile[][] board) {
		if (board != null && (board.length == getHeight() && board[0].length == getWidth()))
			this.board = board;
		else 
			throw new IllegalArgumentException("Not a valid board");
	}
	
	private void setPlayerModel(List<Tile> playerModel) {
		if (playerModel != null) {
			int countPlayerTile = 0;
			int countBox = 0;
			for (Tile tile: playerModel) {
				switch (tile.getType()) {
				case 'o':
					countPlayerTile++;
					break;
				case 'B':
					countBox++;
					break;
				}
			}
			if (playerModel.size() == 2) {
				if (countPlayerTile != 2)
					throw new IllegalArgumentException("this is not a playermodel.");
			}
			else if(playerModel.size() == 3) {
				if (countPlayerTile != 2 || countBox != 1)
					throw new IllegalArgumentException("this is not a playermodel.");
				}
			else
				throw new IllegalArgumentException("this is not a playermodel.");
		}
		this.playerModel = playerModel;
	}
	
	private void setFinish(Tile finish) {
		if (!isTile(finish) && finish != null)
			throw new IllegalArgumentException("This is not a valid tile");
		this.finish = finish;
	}
	
	public static void main(String[] args) {
		Level l = new Level(8, 10);
		System.out.println(l.getPlayerModel());
	}
}
