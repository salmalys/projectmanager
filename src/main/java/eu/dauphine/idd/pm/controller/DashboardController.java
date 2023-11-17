package eu.dauphine.idd.pm.controller;

import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

	@FXML
	private AnchorPane main_form;

	@FXML
	private Button AddFormation;

	@FXML
	private Button Clearformation;
	@FXML
	private Button Clearformation2;

	@FXML
	private Label Count_totaletudiant;

	@FXML
	private TextField IdFormation;

	@FXML
	private TextField Nomformation;

	@FXML
	private ComboBox<String> PromotionList;

	@FXML
	private TextField Nomformation2;

	@FXML
	private ComboBox<String> PromotionList2;

	@FXML
	private Button RemoveFormation;

	@FXML
	private Button Updateformation;

	@FXML
	private TableColumn<Formation, Integer> col_Idformation;

	@FXML
	private TableColumn<Formation, String> col_Nomformation;

	@FXML
	private TableColumn<Formation, String> col_promotion;

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
	private TableView<Formation> tableFormation;

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

	@FXML
	private Button btn_tmpadd;

	@FXML
	private Button btn_tmpupdate;

	@FXML
	private AnchorPane tmp_updateformation;

	@FXML
	private AnchorPane tmp_addformation;

	@FXML
	private AnchorPane tmp_btnformation;
	@FXML
	private Button Back_formation;

	@FXML
	private Button Backformation2;

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
	private Button Printformation;

	// DATABASE TOOLS
	private Connection connection;
	private PreparedStatement prepare;
	private ResultSet result;
	private Statement statement;
	private double x = 0;
	private double y = 0;

	private FormationService formationS = ServiceFactory.getFormationService();

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void close() {
		System.exit(0);
	}

	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	public void AddFormation() {
		try {
			String nom = Nomformation2.getText();
			String promotion = PromotionList2.getSelectionModel().getSelectedItem();

			if (!isInputValid(nom, promotion)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				String check = "SELECT Nom,Promotion FROM Formation WHERE Nom='" + nom + "' and Promotion='" + promotion
						+ "'";
				connection = getConnection();
				statement = connection.createStatement();
				result = statement.executeQuery(check);

				if (result.next()) {
					showAlert(AlertType.ERROR, "Error Message",
							"Nom formation: " + nom + " Promotion: " + promotion + " already exists!");
				} else {
					formationS.createFormation(nom, promotion);
					showAlert(AlertType.INFORMATION, "Success", "Formation added successfully!");
					addformationshow();
					addformationReset2();
					SearchFormation() ;
				}
			}

		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateFormation() {
		try {
			String nom = Nomformation.getText();
			String promotion = PromotionList.getSelectionModel().getSelectedItem();
			String IdFormatio = IdFormation.getText();

			Alert alert;
			if (!isInputValid(nom, promotion)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");

			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure want to Update ID Formation : " + IdFormatio);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					formationS.update(Integer.valueOf(IdFormatio), nom, promotion);

					showAlert(AlertType.INFORMATION, "Information Message", "Formation Updated successfully!");

					addformationshow();
					addformationReset();
					SearchFormation() ;

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteFormation() {
		try {

			String IdFormatio = IdFormation.getText();

			Alert alert;
			if (IdFormatio.isEmpty()) {

				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");

			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure want to Delete ID Formation : Ligne " + IdFormatio);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					formationS.deleteFormationById(Integer.valueOf(IdFormatio));
					showAlert(AlertType.INFORMATION, "Information Message", "Formation Deleted successfully!");

					addformationshow();
					addformationReset();
					SearchFormation();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void SearchFormation() {
	    FilteredList<Formation> filter = new FilteredList<>(addformation, e -> true);
	    search_formation.textProperty().addListener((observable, oldValue, newValue) -> {
	        filter.setPredicate(predData -> {
	            if (newValue == null || newValue.isEmpty()) {
	                return true;
	            }
	            String searchKey = newValue.toLowerCase();

	            // Check if any of the fields contain the search keyword
	            boolean idMatches = Integer.toString(predData.getIdFormation()).equalsIgnoreCase(searchKey);


	            boolean nomMatches = predData.getNom().toLowerCase().contains(searchKey);
	            boolean promotionMatches = predData.getPromotion().toLowerCase().contains(searchKey);

	            System.out.println("ID: " + predData.getIdFormation() + ", Nom: " + predData.getNom() + ", Promotion: " + predData.getPromotion());
	            System.out.println("ID Match: " + idMatches + ", Nom Match: " + nomMatches + ", Promotion Match: " + promotionMatches);

	            return idMatches || nomMatches || promotionMatches;
	        });
	    });

	    SortedList<Formation> sortedList = new SortedList<>(filter);
	    sortedList.comparatorProperty().bind(tableFormation.comparatorProperty());

	    System.out.println(filter.toString());
	}
	@FXML
	public void refreshData() {
		try {
			addformationshow();
			showAlert(AlertType.INFORMATION, "Refresh", "Data refreshed successfully!");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
		}
	}

	private ObservableList<Formation> addformation;

	public void addformationshow() {
		addformation = formationS.listFormations();
		col_Idformation.setCellValueFactory(new PropertyValueFactory<>("idFormation"));
		col_Nomformation.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_promotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));

		tableFormation.setItems(addformation);

	}

	public void selectFormation() {
		Formation formation = tableFormation.getSelectionModel().getSelectedItem();
		int num = tableFormation.getSelectionModel().getFocusedIndex();
		if ((num - 1) < -1) {
			return;
		}

		IdFormation.setText(String.valueOf(formation.getIdFormation()));
		Nomformation.setText(formation.getNom());
		Nomformation2.setText(formation.getNom());

	}

	public void addformationReset() {
		IdFormation.setText("");
		Nomformation.setText("");
		PromotionList.getSelectionModel().clearSelection();

	}

	public void addformationReset2() {
		IdFormation.setText("");
		Nomformation2.setText("");
		PromotionList2.getSelectionModel().clearSelection();

	}

	private String[] listPromotion = { "Initial", "Alternance", "Formation Continue" };
	private String[] listPromotion2 = { "Initial", "Alternance", "Formation Continue" };

	public void addPromotionList() {
		List<String> listp = new ArrayList<>();
		for (String Promot : listPromotion) {
			listp.add(Promot);
		}
		ObservableList promotionL = FXCollections.observableArrayList(listp);
		PromotionList.setItems(promotionL);

	}

	public void addPromotionList2() {
		List<String> listp = new ArrayList<>();
		for (String Promot : listPromotion2) {
			listp.add(Promot);
		}
		ObservableList promotionL = FXCollections.observableArrayList(listp);
		PromotionList2.setItems(promotionL);

	}

	public void Affichersername() {
		username.setText(Data.username);
	}

	public void tmpSwitch(ActionEvent event) {
		if (event.getSource() == home_btn) {
			handleHomeButton();
		} else if (event.getSource() == formation_btn) {
			handleFormationButton();

		} else if (event.getSource() == etudiant_btn) {
			tmp_home.setVisible(false);
			temp_formation.setVisible(false);
			tmp_etudiant.setVisible(true);
			tmp_binome.setVisible(false);
			tmp_note.setVisible(false);
			tmp_projet.setVisible(false);

			etudiant_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			home_btn.setStyle("-fx-background-color: transparent;");
			binome_btn.setStyle("-fx-background-color: transparent;");
			projet_btn.setStyle("-fx-background-color: transparent;");
			note_btn.setStyle("-fx-background-color: transparent;");
			formation_btn.setStyle("-fx-background-color: transparent;");

		} else if (event.getSource() == projet_btn) {
			tmp_home.setVisible(false);
			temp_formation.setVisible(false);
			tmp_etudiant.setVisible(false);
			tmp_binome.setVisible(false);
			tmp_note.setVisible(false);
			tmp_projet.setVisible(true);

			projet_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			home_btn.setStyle("-fx-background-color: transparent;");
			etudiant_btn.setStyle("-fx-background-color: transparent;");
			binome_btn.setStyle("-fx-background-color: transparent;");
			note_btn.setStyle("-fx-background-color: transparent;");
			formation_btn.setStyle("-fx-background-color: transparent;");

		} else if (event.getSource() == note_btn) {
			tmp_home.setVisible(false);
			temp_formation.setVisible(false);
			tmp_etudiant.setVisible(false);
			tmp_binome.setVisible(false);
			tmp_note.setVisible(true);
			tmp_projet.setVisible(false);

			note_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			home_btn.setStyle("-fx-background-color: transparent;");
			etudiant_btn.setStyle("-fx-background-color: transparent;");
			projet_btn.setStyle("-fx-background-color: transparent;");
			binome_btn.setStyle("-fx-background-color: transparent;");
			formation_btn.setStyle("-fx-background-color: transparent;");
		} else if (event.getSource() == binome_btn) {
			tmp_home.setVisible(false);
			temp_formation.setVisible(false);
			tmp_etudiant.setVisible(false);
			tmp_binome.setVisible(true);
			tmp_note.setVisible(false);
			tmp_projet.setVisible(false);

			binome_btn.setStyle(
					"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
			home_btn.setStyle("-fx-background-color: transparent;");
			etudiant_btn.setStyle("-fx-background-color: transparent;");
			projet_btn.setStyle("-fx-background-color: transparent;");
			note_btn.setStyle("-fx-background-color: transparent;");
			formation_btn.setStyle("-fx-background-color: transparent;");
		}

		else if (event.getSource() == btn_tmpadd && temp_formation.isVisible()) {
			handleBtnTmpAdd();

		} else if (event.getSource() == Back_formation && temp_formation.isVisible()) {
			handleBackFormation();

		} else if (event.getSource() == btn_tmpupdate && temp_formation.isVisible()) {
			handleBtnTmpUpdate();

		} else if (event.getSource() == Backformation2 && temp_formation.isVisible()) {
			handleBackFormation2();

		}
	}

	private void handleHomeButton() {
		tmp_home.setVisible(true);
		temp_formation.setVisible(false);
		tmp_etudiant.setVisible(false);
		tmp_binome.setVisible(false);
		tmp_note.setVisible(false);
		tmp_projet.setVisible(false);
		home_btn.setStyle(
				"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
		binome_btn.setStyle("-fx-background-color: transparent;");
		etudiant_btn.setStyle("-fx-background-color: transparent;");
		projet_btn.setStyle("-fx-background-color: transparent;");
		note_btn.setStyle("-fx-background-color: transparent;");
		formation_btn.setStyle("-fx-background-color: transparent;");
	}

	private void handleFormationButton() {
		tmp_home.setVisible(false);
		temp_formation.setVisible(true);
		tmp_etudiant.setVisible(false);
		tmp_binome.setVisible(false);
		tmp_note.setVisible(false);
		tmp_projet.setVisible(false);
		tmp_btnformation.setVisible(true);
		addPromotionList();
		SearchFormation();
		formation_btn.setStyle(
				"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
		home_btn.setStyle("-fx-background-color: transparent;");
		etudiant_btn.setStyle("-fx-background-color: transparent;");
		projet_btn.setStyle("-fx-background-color: transparent;");
		note_btn.setStyle("-fx-background-color: transparent;");
		binome_btn.setStyle("-fx-background-color: transparent;");
	}

	private void handleBtnTmpAdd() {
		tmp_addformation.setVisible(true);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(false);
		addformationReset();
		addformationReset2();
		addPromotionList2();

	}

	private void handleBackFormation() {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(true);
		addformationReset();
		addPromotionList();
	}

	private void handleBtnTmpUpdate() {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(true);
		tmp_btnformation.setVisible(false);
		addformationReset();
		addPromotionList();
	}

	private void handleBackFormation2() {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(true);
		addformationReset();

		addPromotionList();
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private boolean isInputValid(String nom, String promotion) {
		if (promotion == null || promotion.isEmpty() || nom.isEmpty()) {
			showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			return false;
		}
		return true;
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

		addformationshow();
		addPromotionList();
		addPromotionList2();
		SearchFormation();

	}

}
