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
	
	private int level;
	
	FindingAWay game;
	
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
	
	private int getLevel() {
		return level;
	}
	
	
	private void initLevelOne() {
		game = new FindingAWay(12, 14);
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
		
		level = 1;
	}
	
	private void initLevelTwo() {
		game = new FindingAWay(12, 14);
		for (int y = 9; y < 11; y++) {
			for (int x = 5; x < 9; x++) {
				game.getTile(x, y).setGround();}}
		for (int x = 1; x < 13; x++) {
			game.getTile(x, 11).setGround();}
		game.getTile(4, 10).setGround();
		game.getTile(6, 8).setGround();
		game.getTile(7, 8).setGround();
		for (int x = 6; x < 11; x++) {
			game.getTile(x, 4).setGround();}
		for (int x = 1; x < 5; x++) {
			game.getTile(x, 6).setGround();}
		for (int x = 1; x < 4; x++) {
			game.getTile(x, 7).setGround();}
	
		game.getTile(0, 3).setGround();
		game.getTile(0, 4).setGround();
		game.getTile(1, 4).setGround();
		game.getTile(1, 5).setGround();
		game.getTile(1, 10).setBox();
		game.getTile(12, 10).setBox();
		game.getTile(8, 8).setBox();
		game.getTile(4, 5).setBox();
		game.getTile(1, 3).setBox();
		game.getTile(0, 2).setBox();
		game.getTile(9, 1).setFinish();
		
		game.addPlayer(3, 10);

		level = 2;
	}
	
	private void initLevelThree() {
		game = new FindingAWay(12, 14);
		
		level = 3;
	}
	
	private void initCurrentLevel() {
		int currentLevel = getLevel();
		if (currentLevel == 1)
			initLevelOne();
		else if (currentLevel == 2)
			initLevelTwo();
		else if (currentLevel == 3)
			initLevelThree();
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
		
		if (game.isWon()) {
			winText.setText("You won!");
			winText.setStyle("-fx-font-size: 50px");
			winText.setFill(Color.DARKGREEN);
			winText.setTranslateX(120.0);
			winText.setTranslateY(200.0);
			board.getChildren().add(winText);
		}
		else if (game.isGameOver()) {
			loseText.setText("You Lost r-word");
			loseText.setStyle("-fx-font-size: 50px");
			loseText.setFill(Color.DARKRED);
			loseText.setTranslateX(40.0);
			loseText.setTranslateY(200.0);
			board.getChildren().add(loseText);
		}
	}
	
	private String getTileColor(Tile tile) {
		if (tile == game.getPlayerHead()) 
			return "#fce5cd";
		else if (tile.isPlayer())
			return "#741b47";
		else if (tile.isBox()) {
			if (game.getPlayerModel().contains(tile))
				return "#bf9000";
			return "#7f6000";
		}
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
	
	@FXML
	public void handleReset() {
		if (board.getChildren().contains(winText)) 
			board.getChildren().remove(winText);
		
		if (board.getChildren().contains(loseText))
			board.getChildren().remove(loseText);
		
		initCurrentLevel();
		createBoard();
		drawBoard();
	}
	
	@FXML
	public void handleGetSelection() {
		String levelSelect = choiceBox.getValue();
		
		if (levelSelect == "Level 1")
			initLevelOne();
		else if (levelSelect == "Level 2")
			initLevelTwo();
		else if (levelSelect == "Level 3")
			initLevelThree();
		
		createBoard();
		drawBoard();
		
	}
	
	
	

}
