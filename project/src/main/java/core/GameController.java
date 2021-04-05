package core;
import java.nio.file.Path;
import java.util.List;

import fileManagement.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import levelEditor.LevelEditorController;

public class GameController {
	
	private String level;
	
	private FindingAWay game;
	
	private IFileManagement sm = new StorageManager();
	
	private IFileManagement lsm = new LevelStorageManager();
	
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
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private void initialize() {
		initLevelOne();
		createBoard();
		initChoiceBox();
		drawBoard();
	}
	
	private String getLevel() {
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
		
		level = "level1.ser";
	}
	
//	private void initLevelTwo() {
//		game = new FindingAWay(12, 14);
//		for (int y = 9; y < 11; y++) {
//			for (int x = 5; x < 9; x++) {
//				game.getTile(x, y).setGround();}}
//		for (int x = 1; x < 13; x++) {
//			game.getTile(x, 11).setGround();}
//		game.getTile(4, 10).setGround();
//		game.getTile(6, 8).setGround();
//		game.getTile(7, 8).setGround();
//		for (int x = 6; x < 11; x++) {
//			game.getTile(x, 4).setGround();}
//		for (int x = 1; x < 5; x++) {
//			game.getTile(x, 6).setGround();}
//		for (int x = 1; x < 4; x++) {
//			game.getTile(x, 7).setGround();}
//	
//		game.getTile(0, 3).setGround();
//		game.getTile(0, 4).setGround();
//		game.getTile(1, 4).setGround();
//		game.getTile(1, 5).setGround();
//		game.getTile(1, 10).setBox();
//		game.getTile(12, 10).setBox();
//		game.getTile(8, 8).setBox();
//		game.getTile(4, 5).setBox();
//		game.getTile(1, 3).setBox();
//		game.getTile(0, 2).setBox();
//		game.getTile(9, 1).setFinish();
//		
//		game.addPlayer(3, 10);
//
//		level = "level2.ser";
//	}
	
	
	private void initCurrentLevel() {
		String currentLevel = getLevel();
		
		List<String> levelsFolder = FolderReaderHelper.getItemsInFolder(Path.of(FolderReaderHelper.LEVELS_PATH));
		List<String> savesFolder = FolderReaderHelper.getItemsInFolder(Path.of(FolderReaderHelper.SAVES_PATH));
		if (levelsFolder.contains(currentLevel)) {
			FindingAWay newGame = lsm.loadGame(currentLevel);
			initGame(newGame);
		}
		else if (savesFolder.contains(currentLevel)) {
			FindingAWay newGame = sm.loadGame(currentLevel);
			initGame(newGame);
		}
		
		else {
			storageFeedbackText.setText("Error resetting");
			storageFeedbackText.setFill(Color.RED);
		}
		
	}
	
	private void initChoiceBox() {
		choiceBox.getItems().clear();
		choiceBox.getItems().addAll(FolderReaderHelper.getItemsInFolder(Path.of(FolderReaderHelper.LEVELS_PATH)));
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
	public void handleInitSelection() {
		String levelSelect = choiceBox.getValue();
		FindingAWay newGame = lsm.loadGame(levelSelect);
		
		if (initGame(newGame)) {
			level = levelSelect;
		}
	}
	
	@FXML
	public void handleSave() {
		if (!sm.saveGame(textField.getText().strip(), game)) {
			storageFeedbackText.setText("Error writing file.");
			storageFeedbackText.setFill(Color.RED);
		}
		else {
			storageFeedbackText.setText("Game saved");
			storageFeedbackText.setFill(Color.BLACK);
			level = textField.getText().strip();
		}
	}
	
	@FXML
	public void handleLoad() {
		FindingAWay newGame = sm.loadGame(textField.getText().strip());
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
		initChoiceBox();
	}
	
	
	

}
