package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class FindingAWayApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("FindingAWay.fxml"));
		stage.setTitle("Finding a Way");
		stage.setScene(new Scene(parent));
        stage.show();
	}

	public static void main(String[] args) {
		launch(FindingAWayApp.class, args);
	}
}
