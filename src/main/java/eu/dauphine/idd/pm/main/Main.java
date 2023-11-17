package eu.dauphine.idd.pm.main;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private double x = 0;
	private double y = 0;

	@Override
	public void start(Stage primaryStage) {
		try {
			StackPane root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
			Scene scene = new Scene(root, 600, 400);

			root.setOnMousePressed((MouseEvent event) -> {
				x = event.getSceneX();
				y = event.getSceneY();

			});
			root.setOnMouseDragged((MouseEvent event) -> {
				primaryStage.setX(event.getScreenX() - x);
				primaryStage.setY(event.getScreenY() - y);
				primaryStage.setOpacity(.8);

			});
			root.setOnMouseReleased((MouseEvent event) -> {
				primaryStage.setOpacity(1);
			});
			primaryStage.initStyle(StageStyle.TRANSPARENT);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Gestion de Projets des etudiants");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String dbPath = "./src/main/resources/sample.db" ;
		
		//Si argument en ligne de commande on recupere le chemin de la base de donnees
		if (args.length > 0) {
			dbPath = args[0]; 
	    }
		System.out.println("Data Base Path: " + dbPath);
		
		//Injection du chemin de la BDD pour la connexion
		DatabaseConnection.setDatabasePath(dbPath);
		
		launch(args);
	}
}
