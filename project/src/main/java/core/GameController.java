package core;


import java.io.FileNotFoundException;

import fileManagement.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import levelEditor.LevelEditorController;

public class GameController {
	
	private String level;
	
	private FindingAWay game;
	
	private IFileManagement sm = new StorageManager();
	
	private LevelEditorController editorController;
	
	@FXML
	private Pane board;
	
	@FXML
	private Button boxInteractor;
	
	@FXML
	private Text levelText;
	
	@FXML
	private Text storageFeedbackText;
	
	@FXML
	private Text winText = new Text();
	
	@FXML 
	private Text loseText = new Text();
	
	@FXML
	private TextField textField;
	
	@FXML
	private void initialize() throws FileNotFoundException {
		initLevelTwo();
		sm.saveGame("level2.txt", game);
		initLevelOne();
		createBoard();
		sm.saveGame("level1.txt", game);
		drawBoard();
	}
	
	
	private void initLevelOne() {
		game = new FindingAWay(new Level(12, 14));
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
		game.addFinish(2, 1);
		
		game.addPlayer(3, 6);
		
		level = "level1.txt";
	}
	
	private void initLevelTwo() {
		game = new FindingAWay(new Level(12, 14));
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
		game.addFinish(9, 1);
		
		game.addPlayer(3, 10);

		level = "level2.txt";
	}
	
	
	private void initCurrentLevel() throws FileNotFoundException {
		String currentLevel = level;

		FindingAWay newGame = (FindingAWay) sm.loadGame(currentLevel, false);
		if (newGame != null) {
			initGame(newGame);
		}
		else {
			storageFeedbackText.setText("Error resetting");
			storageFeedbackText.setFill(Color.RED);
			throw new IllegalArgumentException("Either there is no level to reset to, or the system cant find your current level.");
		}
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
				board.getChildren().add(tile);
			}
		}
	}
	
	private boolean initGame(FindingAWay newGame) {
		if (newGame == null) {
			storageFeedbackText.setText("Error loading game");
			storageFeedbackText.setFill(Color.RED);
			return false;
		}
		else {
			game = newGame;
			storageFeedbackText.setText("Game loaded");
			storageFeedbackText.setFill(Color.BLACK);
			createBoard();
			drawBoard();
			return true;
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
			loseText.setText("You lost");
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
	public void handleReset() throws FileNotFoundException {
		if (board.getChildren().contains(winText)) 
			board.getChildren().remove(winText);
		
		if (board.getChildren().contains(loseText))
			board.getChildren().remove(loseText);
		
		initCurrentLevel();
		createBoard();
		drawBoard();
	}
	
	@FXML
	public void handleSave() {
		try {
			sm.saveGame(textField.getText().strip(), game);
			storageFeedbackText.setText("Game saved");
			storageFeedbackText.setFill(Color.BLACK);
			level = textField.getText().strip();
		}
		catch (Exception e) {
			storageFeedbackText.setText(e.getMessage());
			storageFeedbackText.setFill(Color.RED);
		}
	}
	
	@FXML
	public void handleLoad() throws FileNotFoundException {
		FindingAWay newGame = (FindingAWay) sm.loadGame(textField.getText().strip(), false);
		if (initGame(newGame))
			level = textField.getText().strip();
		
	}
	
	private Scene scene;
	private Parent oldSceneRoot, editorSceneRoot;
	
	@FXML
	public void handleLevelEditor() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/levelEditor/LevelEditor.fxml"));
		try {
			editorSceneRoot = fxmlLoader.load();
				
			//remember old UI
			scene = board.getScene();
			oldSceneRoot = scene.getRoot();
			
			editorController = fxmlLoader.getController();
			editorController.setGameController(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if (editorController != null) {
			board.getScene().setRoot(editorSceneRoot);
		}
	}
	
	public void exitLevelEditor() {
		scene.setRoot(oldSceneRoot);
	}
	
	
	

}
