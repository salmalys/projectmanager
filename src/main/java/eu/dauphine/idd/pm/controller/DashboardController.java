package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.*;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.MenuItem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import eu.dauphine.idd.pm.dao.impl.FormationDAOImpl;
import eu.dauphine.idd.pm.model.Formation;
import javafx.scene.control.MenuButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;

public class DashboardController implements Initializable {

	@FXML
	private AnchorPane main_form;
	@FXML
	private Button AddFormation;

	@FXML
	private Button Clearformation;

	@FXML
	private Label Count_totaletudiant;

	@FXML
	private TextField IdFormation;

	@FXML
	private TextField Nomformation;

	@FXML
	private MenuButton PromotionList;

	@FXML
	private Button RemoveFormation;

	@FXML
	private Button Updateformation;

	@FXML
	private TableColumn<?, ?> col_Idformation;

	@FXML
	private TableColumn<?, ?> col_Nomformation;

	@FXML
	private TableColumn<?, ?> col_promotion;

	@FXML
	private Label count_apresprojet;

	@FXML
	private Label count_avantprojet;

	@FXML
	private Label count_totalbinome;

	@FXML
	private Label count_totalprojet;

	@FXML
	private ComboBox<?> filtre_formation;

	@FXML
	private Button formation_btn;

	@FXML
	private Button home_btn;

	@FXML
	private BarChart<?, ?> home_chart;

	@FXML
	private Button logout;

	@FXML
	private TextField search_formation;

	@FXML
	private TableView<?> tableFormation;

	@FXML
	private AnchorPane temp_formation;

	@FXML
	private AnchorPane tmp_home;

	@FXML
	private Label username;

	@FXML
	private Button close;

	@FXML
	private Button minimize;

	@FXML
	private MenuItem initiale;

	@FXML
	private MenuItem continueF;

	@FXML
	private MenuItem alternance;

	private FormationService formationS = ServiceFactory.getFormationService();

	public void close() {
		System.exit(0);
	}

	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	private double x = 0;
	private double y = 0;

	public void AddFormation() {
		// Get values from the interface
		try {
			String nom = Nomformation.getText();
			String promotion = PromotionList.getText();
			System.out.println(promotion);
			if (promotion == null || nom == null) {
				throw new Exception("empty nom/promotion");

			}
			formationS.createFormation(nom, promotion);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Optional: Display a message or perform any other actions after the insertion
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success");
		alert.setHeaderText(null);
		alert.setContentText("Formation added successfully!");
		alert.showAndWait();

		// Optional: Clear the input fields or perform any other actions
		Nomformation.clear();
		// PromotionList
	}

	public void Affichersername() {
		username.setText(MainController.getData);
	}

	
	public void initialize1() {
		setMenuButtonText(initiale.getText());

	}

	
	public void initialize2() {

		setMenuButtonText(continueF.getText());

	}

	
	public void initialize3() {

		setMenuButtonText(alternance.getText());

	}

	@FXML
	public void setMenuButtonText(String text) {
		PromotionList.setText(text);

	}

	@FXML
	public void tmpSwitch(ActionEvent event) {
		if (event.getSource() == home_btn) {
			tmp_home.setVisible(true);
			temp_formation.setVisible(false);

			home_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			formation_btn.setStyle("-fx-background-color: transparent;");

		} else if (event.getSource() == formation_btn) {
			tmp_home.setVisible(false);
			temp_formation.setVisible(true);
			formation_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			home_btn.setStyle("-fx-background-color: transparent;");
		}

	}

	// fonction logout permet de revenir a la scene parent, apres deconnexion
	public void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Message");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
		Optional<ButtonType> option = alert.showAndWait();
		try {
			if (option.get().equals(ButtonType.OK)) {
				logout.getScene().getWindow().hide();
				Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(root);

				root.setOnMousePressed((MouseEvent event) -> {
					x = event.getSceneX();
					y = event.getSceneY();

				});
				root.setOnMouseDragged((MouseEvent event) -> {
					stage.setX(event.getScreenX() - x);
					stage.setY(event.getScreenY() - y);
					stage.setOpacity(.8);

				});
				root.setOnMouseReleased((MouseEvent event) -> {
					stage.setOpacity(1);
				});
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.setScene(scene);
				stage.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// throw new UnsupportedOperationException("Not supported yet");

	}

}
