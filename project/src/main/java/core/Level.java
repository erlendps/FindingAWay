package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Level implements Serializable {
	private static final long serialVersionUID = 1L;
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
	
	public void update(Tile[][] board, List<Tile> playerModel, Tile finish) {
		setBoard(board);
		setPlayerModel(playerModel);
		setFinish(finish);
	}
	
	private void setBoard(Tile[][] board) {
		if (board != null && (board.length == getHeight() && board[0].length == getWidth()))
		this.board = board;
	}
	
	private void setPlayerModel(List<Tile> playerModel) {
		if (playerModel != null) {
			for (Tile tile: playerModel) {
				if (!tile.isPlayer())
					throw new IllegalArgumentException("This is not a playermodel");
			}
		}
		this.playerModel = playerModel;
	}
	
	private void setFinish(Tile finish) {
		this.finish = finish;
	}
	
	public static void main(String[] args) {
		Level l = new Level(8, 10);
		System.out.println(l.getPlayerModel());
	}
}
