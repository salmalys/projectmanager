package eu.dauphine.idd.pm.controller;

import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ResourceBundle;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController implements Initializable {

	@FXML
	private Button close;

	@FXML
	private Button login;

	@FXML
	private AnchorPane main_form;

	@FXML
	private PasswordField password;

	@FXML
	private TextField username;

	// DATABASE TOOLS
	private Connection connection;
	private PreparedStatement prepare;
	private ResultSet result;
	private double x = 0;
	private double y = 0;

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loginAdmin() {
		String query = "SELECT * FROM admin WHERE username =? and password=? ";
		connection = getConnection();
		try {
			prepare = connection.prepareStatement(query);
			prepare.setString(1, username.getText());
			prepare.setString(2, password.getText());
			result = prepare.executeQuery();
			Alert alert;
			if (username.getText().isEmpty() || password.getText().isEmpty()) {
				alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Message");
				alert.setHeaderText(null);
				alert.setContentText("Please fill all blank fields");
				alert.showAndWait();
			} else {
				if (result.next()) {
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Message");
					alert.setHeaderText(null);
					alert.setContentText("Successfully Login");
					alert.showAndWait();
					Stage currentStage = (Stage) login.getScene().getWindow();

					currentStage.close();
					Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
					Stage stage = new Stage();
					Scene scene = new Scene(root);

					root.setOnMousePressed((MouseEvent event) -> {
						x = event.getSceneX();
						y = event.getSceneY();

					});
					root.setOnMouseDragged((MouseEvent event) -> {
						stage.setX(event.getScreenX() - x);
						stage.setY(event.getScreenY() - y);

					});
					stage.initStyle(StageStyle.TRANSPARENT);

					stage.setScene(scene);
					stage.show();

				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Message");
					alert.setHeaderText(null);
					alert.setContentText("Wrong Username/Password");
					alert.showAndWait();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {

		System.exit(0);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
