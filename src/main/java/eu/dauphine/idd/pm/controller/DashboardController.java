package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import eu.dauphine.idd.pm.service.ServiceFactory;
import eu.dauphine.idd.pm.service.UtilService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

	/**
	 * *Partie 0: ************************ButtonHome****************************
	 * 
	 **/

	@FXML
	private Label Count_totaletudiant;
	@FXML
	private Label count_apresprojet;

	@FXML
	private Label count_avantprojet;

	@FXML
	private Label count_totalbinome;

	@FXML
	private Label count_totalprojet;
	@FXML
	private Button home_btn;

	@FXML
	private BarChart<?, ?> home_chart;
	@FXML
	private Button formation_btn;

	@FXML
	private AnchorPane temp_formation;

	@FXML
	private Button binome_btn;

	@FXML
	private Button etudiant_btn;

	@FXML
	private Button note_btn;

	@FXML
	private Button projet_btn;

	@FXML
	private AnchorPane tmp_note;

	@FXML
	private AnchorPane tmp_projet;

	@FXML
	private AnchorPane tmp_etudiant;

	@FXML
	private AnchorPane tmp_binome;
	@FXML
	private AnchorPane tmp_home;
	@FXML
	private BarChart<String, Number> barChart;

	private UtilService utilS = ServiceFactory.getUtilService();

	public void home_countEtudiant() {
		Count_totaletudiant.setText(String.valueOf(utilS.getTotalEtudiants()));
	}

	public void home_countProjet() {
		count_totalprojet.setText(String.valueOf(utilS.getTotalProjets()));
	}

	public void home_countAvantProjet() {
		count_avantprojet.setText(String.valueOf(utilS.getNbprojetRemisAvantDate()));
	}

	public void home_countApresProjet() {
		count_apresprojet.setText(String.valueOf(utilS.getNbprojetRemisApresDate()));
	}

	public void home_countBinome() {
		count_totalbinome.setText(String.valueOf(utilS.getNbBinome()));
	}

	private FormationController formationController = new FormationController();

	@FXML
	public void tmpSwitch(ActionEvent event) {
		formationController.tmpSwitch(event, tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet,
				home_btn, binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn);
		home_countEtudiant();
		home_countProjet();
		home_countAvantProjet();
		home_countApresProjet();
		home_countBinome();
	}

	/**
	 * *Partie X: ************************Deconnexion et reglage de scene
	 * dashboard***********************
	 * 
	 **/

	@FXML
	private Button close;
	@FXML
	private AnchorPane main_form;
	@FXML
	private Label username;
	@FXML
	private Button logout;
	@FXML
	private Button minimize;

	private double x = 0;
	private double y = 0;

	// fonction logout permet de revenir a la scene parent, apres deconnexion
	@FXML
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

	public void BarChartDonnee() {
		HashMap<String, Double> moyennes = utilS.getMoyenneParProjet();

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName("Moyenne des Notes");

		for (Map.Entry<String, Double> entry : moyennes.entrySet()) {
			series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
		}

		barChart.getData().add(series);

	}

	// recuperer Username quand on reussi a connecté et entrer dans dashbord pour
	// modifer label username dans UI Dashboard
	public void Affichersername() {
		username.setText(Data.username);
	}

	public void close() {
		System.exit(0);
	}

	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	/**
	 * *Partie UI:
	 * ************************InitialisationActionUI****************************
	 * 
	 **/
	@FXML
	private BinomeController binomeController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Affichersername();
		home_countEtudiant();
		home_countProjet();
		home_countAvantProjet();
		home_countApresProjet();
		home_countBinome();
		// Création d'une série de données
		BarChartDonnee();

	}

}
