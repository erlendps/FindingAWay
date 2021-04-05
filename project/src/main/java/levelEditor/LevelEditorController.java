package levelEditor;

import core.FindingAWay;
import core.GameController;
import core.Tile;
import fileManagement.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

public class LevelEditorController {
	
	private char tileType = ' ';
	
	private FindingAWay level;
	
	private GameController gameController;
	
	private LevelStorageManager lsm = new LevelStorageManager();
	
	@FXML
	private Pane board;
	
	
	@FXML
	private TextField textField;
	
	@FXML
	private Button setAirButton;
	
	@FXML
	private Button setBoxButton;
	
	@FXML
	private Button setGoalButton;
	
	@FXML
	private Button setGroundButton;
	
	@FXML
	private Button setPlayerButton;
	
	@FXML
	private Text storageFeedbackText;
	
	@FXML
	private TextArea editorFeedback;
	
	private Button lastButton;
	
	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}
	
	@FXML
	private void initialize() {
		initLevelEditor();
		drawBoard();
		setAirButton.underlineProperty().set(true);
		lastButton = setAirButton;
	}
	
	private void initLevelEditor() {
		level = new FindingAWay(12, 14);
		
		board.getChildren().clear();
		
		for (int y = 0; y < level.getHeight(); y++) {
			for (int x = 0; x < level.getWidth(); x++) {
				int intX = x;
				int intY = y;
				level.getTile(x, y).setAir();
				Pane tile = new Pane();
				tile.setTranslateX(x*30);
				tile.setTranslateY(y*30);
				tile.setPrefWidth(30);
				tile.setPrefHeight(30);
				tile.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
				tile.setOnMouseClicked((MouseEvent e) -> {
					if (tileType != 'o') {
						level.getTile(intX, intY).setType(tileType);
						drawBoard();
					}
					else {
						try {
							level.addPlayer(intX, intY);
							drawBoard();
						}
						catch (Exception e2) {
							editorFeedback.setText("Player already added.");
						}
					}
				});
				board.getChildren().add(tile);
			}
		}
	}
	
	@FXML
	public void handleSetAir() {
		tileType = ' ';
		lastButton.underlineProperty().set(false);
		setAirButton.underlineProperty().set(true);
		lastButton = setAirButton;
	}
	
	@FXML
	public void handleSetBox() {
		tileType = 'B';
		lastButton.underlineProperty().set(false);
		setBoxButton.underlineProperty().set(true);
		lastButton = setBoxButton;
	}
	
	@FXML
	public void handleSetGoal() {
		tileType = '*';
		lastButton.underlineProperty().set(false);
		setGoalButton.underlineProperty().set(true);
		lastButton = setGoalButton;
	}
	
	@FXML
	public void handleSetGround() {
		tileType = '#';
		lastButton.underlineProperty().set(false);
		setGroundButton.underlineProperty().set(true);
		lastButton = setGroundButton;
	}
	
	@FXML
	public void handleSetPlayer() {
		tileType = 'o';
		lastButton.underlineProperty().set(false);
		setPlayerButton.underlineProperty().set(true);
		lastButton = setPlayerButton;
	}
	
	@FXML
	public void handleRemovePlayer() {
		level.removePlayer();
		drawBoard();
	}
	
	@FXML
	public void handleReset() {
		level = new FindingAWay(12, 14);
		drawBoard();
	}
	
	@FXML
	public void handleLoad() {
		FindingAWay newGame = lsm.loadGame(textField.getText().strip());
		if (newGame != null) {
			level = newGame;
			storageFeedbackText.setText("Level loaded");
			storageFeedbackText.setFill(Color.BLACK);
			drawBoard();
		}
		else {
			storageFeedbackText.setText("Error reading file");
			storageFeedbackText.setFill(Color.RED);
		}
	}
	
	@FXML
	public void handleSave() {
		if (!LevelEditorHelper.checkIfValidLevel(level)) {
			editorFeedback.setText("Invalid level. A playermodel must be added,\n "
					+ "the tile under the player body must be a collision block\n"
					+ " (ground or box) and the level must have exactly one goal");
		}
		else {
			if (!lsm.saveGame(textField.getText().strip(), level)) {
				storageFeedbackText.setText("Error writing file.");
				storageFeedbackText.setFill(Color.RED);
				editorFeedback.setText("");
			}
			else {
				storageFeedbackText.setText("Game saved");
				storageFeedbackText.setFill(Color.BLACK);
				editorFeedback.setText("");
			}
		}
	}
	
	@FXML
	public void handleDone() {
		gameController.exitLevelEditor();
	}
	
	public void drawBoard() {
		for (int y = 0; y < level.getHeight(); y++) {
			for (int x = 0; x < level.getWidth(); x++) {
				board.getChildren().get(y*level.getWidth() + x).setStyle(
						"-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: "
						+ getTileColor(level.getTile(x, y)) + ";");
			}
		}
	}
	
	private String getTileColor(Tile tile) {
		if (tile.isPlayer())
			return "#741b47";
		else if (tile.isBox()) {
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
}
