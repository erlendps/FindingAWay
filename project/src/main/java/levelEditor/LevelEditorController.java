package levelEditor;

import java.io.FileNotFoundException;

import core.GameController;
import core.Level;
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
	
	private LevelEditorGame editor;
	
	private GameController gameController;
	
	private StorageManager sm = new StorageManager();
	
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
		editor = new LevelEditorGame(new Level(12, 14));
		
		board.getChildren().clear();
		
		for (int y = 0; y < editor.getHeight(); y++) {
			for (int x = 0; x < editor.getWidth(); x++) {
				int intX = x;
				int intY = y;
				editor.getTile(x, y).setAir();
				Pane tile = new Pane();
				tile.setTranslateX(x*30);
				tile.setTranslateY(y*30);
				tile.setPrefWidth(30);
				tile.setPrefHeight(30);
				tile.setOnMouseClicked((MouseEvent e) -> {
					try {
						editor.setTile(intX, intY);
						drawBoard();
					}
					catch (Exception e1) {
						editorFeedback.setText(e1.getMessage());
					}
				});
				board.getChildren().add(tile);
			}
		}
	}
	
	@FXML
	public void handleSetAir() {
		editor.setType(' ');
		lastButton.underlineProperty().set(false);
		setAirButton.underlineProperty().set(true);
		lastButton = setAirButton;
	}
	
	@FXML
	public void handleSetBox() {
		editor.setType('B');
		lastButton.underlineProperty().set(false);
		setBoxButton.underlineProperty().set(true);
		lastButton = setBoxButton;
	}
	
	@FXML
	public void handleSetGoal() {
		editor.setType('*');
		lastButton.underlineProperty().set(false);
		setGoalButton.underlineProperty().set(true);
		lastButton = setGoalButton;
	}
	
	@FXML
	public void handleSetGround() {
		editor.setType('#');
		lastButton.underlineProperty().set(false);
		setGroundButton.underlineProperty().set(true);
		lastButton = setGroundButton;
	}
	
	@FXML
	public void handleSetPlayer() {
		editor.setType('o');
		lastButton.underlineProperty().set(false);
		setPlayerButton.underlineProperty().set(true);
		lastButton = setPlayerButton;
	}
	
	@FXML
	public void handleRemovePlayer() {
		editor.removePlayer();
		drawBoard();
	}
	
	@FXML
	public void handleRemoveFinish() {
		editor.removeFinish();
		drawBoard();
	}
	
	@FXML
	public void handleReset() {
		editor = new LevelEditorGame(new Level(12, 14));
		drawBoard();
	}
	
	@FXML
	public void handleLoad() throws FileNotFoundException {
		LevelEditorGame newEditor = (LevelEditorGame) sm.loadGame(textField.getText().strip(), true);
		if (newEditor != null) {
			editor = newEditor;
			storageFeedbackText.setText("Level loaded");
			storageFeedbackText.setFill(Color.BLACK);
			drawBoard();
		}
		else {
			storageFeedbackText.setText("Error reading file. It could be a path \n"
					+ "issue or the level is not valid.");
			storageFeedbackText.setFill(Color.RED);
		}
	}
	
	@FXML
	public void handleSave() {
		try {
			sm.saveGame(textField.getText().strip(), editor);
			storageFeedbackText.setText("Game saved");
			storageFeedbackText.setFill(Color.BLACK);
			editorFeedback.setText("");
		}
		catch (Exception e) {
			storageFeedbackText.setText(e.getMessage());
			storageFeedbackText.setFill(Color.RED);
		}
	}
	
	@FXML
	public void handleDone() {
		gameController.exitLevelEditor();
	}
	
	public void drawBoard() {
		for (int y = 0; y < editor.getHeight(); y++) {
			for (int x = 0; x < editor.getWidth(); x++) {
				board.getChildren().get(y*editor.getWidth() + x).setStyle(
						"-fx-border-color: black; -fx-border-width: 1px; -fx-background-color: "
						+ getTileColor(editor.getTile(x, y)) + ";");
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
		else
			return "#cfe2f3";
	}
}
