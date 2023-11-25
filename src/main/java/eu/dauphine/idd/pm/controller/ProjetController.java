package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ProjetController implements Initializable {
	@FXML
	private TextField Idprojet;
	@FXML
	private TextField NomMatiereP;

	@FXML
	private TextField NomMatiereP2;
	@FXML
	private TextField SujetProjet;

	@FXML
	private TextField SujetProjet2;
	@FXML
	private Button back_projet;

	@FXML
	private Button back_projet2;

	@FXML
	private Button btn_ajoutProjet;

	@FXML
	private Button btn_modifierProjet;

	@FXML
	private Button btn_supprimProjet;

	@FXML
	private DatePicker dateRemisePorjet;

	@FXML
	private DatePicker dateRemisePorjet2;
	@FXML
	private Button refreshDataProjet;

	@FXML
	private TextField search_projet;
	@FXML
	private TableView<Projet> tableProjet;

	@FXML
	private AnchorPane tmpDeleteProjet;
	@FXML
	private AnchorPane tmp_addProjet;

	@FXML
	private AnchorPane tmp_updateProjet;

	@FXML
	private TableColumn<Projet, String> col_MatiereProjet;
	@FXML
	private TableColumn<Projet, Date> col_DateRemiseProjet;
	@FXML
	private TableColumn<Projet, String> col_SujetProjet;

	@FXML
	private TableColumn<Projet, String> col_idProjet;

	private ProjetService projetS = ServiceFactory.getProjetService();

	@FXML
	public void addProjet() {
		try {
			String nomMatiere = NomMatiereP.getText();
			String sujet = SujetProjet.getText();
			// Convertir la date du DatePicker en java.sql.Date
			java.sql.Date dateRemise = java.sql.Date.valueOf(dateRemisePorjet.getValue());

			// Check if required fields are not empty
			if (!isInputValid(nomMatiere, sujet, dateRemise)) {

				// Handle the case when some fields are empty
				showAlert(AlertType.ERROR, "Error Message", "Please fill in all required fields.");

			} else {

				// Call the ProjetService to add the project
				int result = projetS.createProjet(nomMatiere, sujet, String.valueOf(dateRemise));

				// Handle the result
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Project added successfully!");
					addProjetShow();
					resetProjetField();
					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message", "Project for this course and subject already exists!");
					break;
				default:
					showAlert(AlertType.ERROR, "Error", "An error occurred while creating the project.");
					break;
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void updateProjet() {
		try {
			String matiere = NomMatiereP2.getText();
			String sujet = SujetProjet2.getText();
			String idProjetStr = Idprojet.getText();

			// Convertir la date du DatePicker en java.sql.Date
			java.sql.Date dateRemise = java.sql.Date.valueOf(dateRemisePorjet2.getValue());

			Alert alert;
			if (!isInputValid(matiere, sujet, dateRemise)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure want to Update ID Projet : " + idProjetStr);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					projetS.updateProjet(Integer.valueOf(idProjetStr), matiere, sujet, String.valueOf(dateRemise));

					showAlert(AlertType.INFORMATION, "Information Message", "Projet Updated successfully!");

					addProjetShow();
					resetProjetField();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteProjet() {
		try {
			String idProjetStr = Idprojet.getText();

			Alert alert;
			if (idProjetStr.isEmpty()) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure want to Delete ID Projet : " + idProjetStr);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					projetS.deleteProjetById(Integer.valueOf(idProjetStr));
					showAlert(AlertType.INFORMATION, "Information Message", "Projet Deleted successfully!");

					addProjetShow();
					resetProjetField();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Liste d'éléments de projet
	private ObservableList<Projet> addprojet;

	// Fonction qui affiche le tableau de projets dans l'interface graphique
	public void addProjetShow() {
		// Récupération de la liste des projets
		addprojet = projetS.listProjets();

		// Définition des cellules des colonnes
		col_DateRemiseProjet.toString();
		col_idProjet.setCellValueFactory(new PropertyValueFactory<>("idProjet"));
		col_MatiereProjet.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
		col_DateRemiseProjet.setCellValueFactory(new PropertyValueFactory<>("dateRemiseRapport"));
		col_SujetProjet.setCellValueFactory(new PropertyValueFactory<>("sujet"));

		// Affichage des projets dans le tableau
		tableProjet.setItems(addprojet);
	}

	@FXML
	public void selectProjet() {
		Projet projet = tableProjet.getSelectionModel().getSelectedItem();
		int num = tableProjet.getSelectionModel().getFocusedIndex();
		if ((num - 1) < -1) {
			return;
		}
		if (projet != null) {
			Idprojet.setText(String.valueOf(projet.getIdProjet()));

			NomMatiereP.setText(projet.getNomMatiere());
			SujetProjet.setText(projet.getSujet());
			NomMatiereP2.setText(projet.getNomMatiere());
			SujetProjet2.setText(projet.getSujet());
//			// Convertir la date de java.sql.Date à LocalDate pour le DatePicker
//			java.sql.Date dateRemise = (Date) projet.getDateRemiseRapport();
//			if (dateRemise != null) {
//				LocalDate localDateRemise = dateRemise.toLocalDate();
//				dateRemisePorjet.setValue(localDateRemise);
//				dateRemisePorjet2.setValue(localDateRemise);
//			} else {
//				// Gérer le cas où la date est null
//				dateRemisePorjet.setValue(null);
//				dateRemisePorjet2.setValue(null);
//			}
		}
	}

	// Add a method to reset Projet fields
	@FXML
	public void resetProjetField() {
		// Clear the input fields or perform any other reset actions
		Idprojet.setText("");
		NomMatiereP.setText("");
		SujetProjet.setText("");
		dateRemisePorjet.setValue(null);
	}

	// Add a method to reset Projet fields
	@FXML
	public void resetProjetField2() {
		// Clear the input fields or perform any other reset actions
		Idprojet.setText("");
		NomMatiereP2.setText("");
		SujetProjet2.setText("");
		dateRemisePorjet2.setValue(null);
	}

	@FXML
	private void handleBtnTmpAddProjet(ActionEvent event) {
		if (event.getSource() == btn_ajoutProjet) {
			tmp_addProjet.setVisible(true);
			tmpDeleteProjet.setVisible(false);
			tmp_updateProjet.setVisible(false);
		}

	}

	// Button for going back from the add projet form

	@FXML
	private void handleBackProjet(ActionEvent event) {
		if (event.getSource() == back_projet || (event.getSource() == back_projet2)) {
			tmp_addProjet.setVisible(false);
			tmpDeleteProjet.setVisible(true);
			tmp_updateProjet.setVisible(false);

		}
	}

	// Button for switching to the update projet form
	@FXML
	private void handleBtnTmpUpdateProjet(ActionEvent event) {
		if (event.getSource() == btn_modifierProjet) {
			tmp_addProjet.setVisible(false);
			tmpDeleteProjet.setVisible(false);
			tmp_updateProjet.setVisible(true);
		}

	}

	// Add a method to show an Alert
	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	// Condition qui verefier c'est nom et promotion ne sont pas vide dans interface
	// graphique
	private boolean isInputValid(String nomMatiere, String sujet, Date dateRemise) {

		if (nomMatiere.isEmpty() || sujet.isEmpty() || dateRemise == null) {
			showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			return false;
		}
		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		addProjetShow();
	}

}
