package core;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GameController {
	
	Game game;
	
	@FXML
	Pane board;
	
	@FXML
	Button boxInteractor;
	
	@FXML
	Text levelText;
	
	@FXML
	Text winText = new Text();
	
	@FXML 
	Text loseText = new Text();
	
	@FXML
	ChoiceBox<String> choiceBox;
	
	@FXML
	private void initialize() {
		initLevelOne();
		createBoard();
		initChoiceBox();
		drawBoard();
	}
	
	
	private void initLevelOne() {
		game = new Game(12, 14);
		for (int y = 7; y < game.getHeight(); y++) {
			for (int x = 2; x < 13; x++) {
				game.getTile(x, y).setGround();}}
		for (int y = 5; y < 7; y++) {
			for (int x = 6; x < 10; x++) {
				game.getTile(x, y).setGround();}}
		game.getTile(5, 6).setGround();
		game.getTile(2, 3).setGround();
		game.getTile(3, 3).setGround();
		game.getTile(4, 3).setGround();
		game.getTile(10, 6).setGround();
		game.getTile(2, 6).setBox();
		game.getTile(8, 4).setBox();
		game.getTile(2, 1).setFinish();
		
		game.addPlayer(3, 6);
	}
	
	private void initLevelTwo() {
		game = new Game(12, 14);
	}
	
	private void initLevelThree() {
		game = new Game(12, 14);
	}
	
	private void initChoiceBox() { 
		choiceBox.setItems(FXCollections.observableArrayList("Level 1", "Level 2", "Level 3"));
		choiceBox.setTooltip(new Tooltip("Select the level you want to play"));	
	}
	
	private void createBoard() {
		board.getChildren().clear();
		
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				Pane tile = new Pane();
				tile.setTranslateX(x*30);
				tile.setTranslateY(y*30);
				tile.setPrefWidth(30);
				tile.setPrefHeight(30);
				tile.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
				board.getChildren().add(tile);
			}
		}
	}

	private void drawBoard() {
		for (int y = 0; y < game.getHeight(); y++) {
			for (int x = 0; x < game.getWidth(); x++) {
				board.getChildren().get(y*game.getWidth() + x).setStyle("-fx-background-color: " + getTileColor(game.getTile(x, y)) + ";");
			}
		}
	}
	
	private String getTileColor(Tile tile) {
		if (tile == game.getPlayerHead()) 
			return "#fce5cd";
		else if (tile.isPlayer())
			return "#fbbc04";
		else if (tile.isBox())
			return "#7f6000";
		else if (tile.isFinish())
			return "#ea9999";
		else if (tile.isGround())
			return "#6aa84f";
		else if (tile.isWater())
			return "#3c78d8";
		else
			return "#cfe2f3";
	}
	
	@FXML
	public void handleMoveLeft() {
		game.moveLeft();
		drawBoard();
	}
	
	@FXML
	public void handleMoveRight() {
		game.moveRight();
		drawBoard();
	}
	
	@FXML
	public void handleSwap() {
		game.swapBoxSide();
		drawBoard();
	}
	
	@FXML
	public void handleInteractWithBox() {
		game.interactWithBox();
		if (game.checkIfBoxPickedUp())
			boxInteractor.setText("Drop");
		else
			boxInteractor.setText("Pick Up");
		drawBoard();
	}
	
	
	
	

}
