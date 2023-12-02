package eu.dauphine.idd.pm.controller;

import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
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

public class LoginController implements Initializable {

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
	    String adminQuery = "SELECT * FROM admin WHERE username = ? AND password = ?";
	    String userQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
	    connection = getConnection();

	    try {
	        // Check admin login
	        if (loginUser(adminQuery)) {
	            return;
	        }

	        // Check user login
	        if (loginUser(userQuery)) {
	            return;
	        }

	        // If no match found, create a new user
	        createUser();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private boolean loginUser(String query) {
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
	            return false;
	        } else if (result.next()) {
	            Data.username = username.getText();

	            alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Information Message");
	            alert.setHeaderText(null);
	            alert.setContentText("Successfully Login");
	            alert.showAndWait();

	            Stage currentStage = (Stage) login.getScene().getWindow();
	            currentStage.close();

	            if (query.contains("admin")) {
	                // Admin login, open dashboard.fxml
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
	                // Regular user login, open NotesUser.fxml
	                Parent root = FXMLLoader.load(getClass().getResource("/fxml/NoteUsers.fxml"));
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
	            }

	            return true;
	        }

	    } catch (SQLException | IOException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	public void createUser() {
	    String etudiantQuery = "SELECT * FROM Etudiant WHERE Nom = ? AND Prenom = ?";
	    String insertUserQuery = "INSERT INTO users(username, password) VALUES (?, ?)";
	    
	    connection = getConnection();

	    try {
	        // Check if the corresponding student exists
	        prepare = connection.prepareStatement(etudiantQuery);
	        prepare.setString(1, username.getText()); // Nom
	        prepare.setString(2, password.getText()); // Prenom
	        result = prepare.executeQuery();

	        if (result.next()) {
	            // Student exists, insert into the users table
	            prepare = connection.prepareStatement(insertUserQuery);
	            prepare.setString(1, username.getText()); // Nom as username
	            prepare.setString(2, password.getText()); // Prenom as password

	            // Execute the update
	            int userResult = prepare.executeUpdate();

	            if (userResult > 0) {
	                Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Information Message");
	                alert.setHeaderText(null);
	                alert.setContentText("Successfully Created User");
	                alert.showAndWait();
	            } else {
	                Alert alert = new Alert(AlertType.ERROR);
	                alert.setTitle("Error Message");
	                alert.setHeaderText(null);
	                alert.setContentText("Failed to Create User");
	                alert.showAndWait();
	            }
	        } else {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setTitle("Error Message");
	            alert.setHeaderText(null);
	            alert.setContentText("Admin/Etudiant n'existe pas ");
	            alert.showAndWait();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}



	public void close() {

		System.exit(0);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// throw new UnsupportedOperationException("Not supported yet");

	}

}
