package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProjetController implements Initializable {
	private ProjetService projetS = ServiceFactory.getProjetService();

	public void addProjet(TextField Idprojet, TextField NomMatiereP, TextField SujetProjet, DatePicker dateRemisePorjet,
			TableColumn<Projet, String> col_idProjet, TableColumn<Projet, String> col_MatiereProjet,
			TableColumn<Projet, Date> col_DateRemiseProjet, TableColumn<Projet, String> col_SujetProjet,
			TableView<Projet> tableProjet) {
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
				int result = projetS.createProjet(nomMatiere, sujet, dateRemise);

				// Handle the result
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Project added successfully!");
					addProjetShow(col_idProjet, col_MatiereProjet, col_DateRemiseProjet, col_SujetProjet, tableProjet);
					resetProjetField(Idprojet, NomMatiereP, SujetProjet, dateRemisePorjet);
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

	// Liste d'éléments de projet
	private ObservableList<Projet> addprojet;

	// Fonction qui affiche le tableau de projets dans l'interface graphique
	public void addProjetShow(TableColumn<Projet, String> col_idProjet, TableColumn<Projet, String> col_MatiereProjet,
			TableColumn<Projet, Date> col_DateRemiseProjet, TableColumn<Projet, String> col_SujetProjet,
			TableView<Projet> tableProjet) {
		// Récupération de la liste des projets
		addprojet = projetS.listProjets();

		// Définition des cellules des colonnes
		col_DateRemiseProjet.toString();
		col_idProjet.setCellValueFactory(new PropertyValueFactory<>("idProjet"));
		col_MatiereProjet.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));
		// col_DateRemiseProjet.setCellValueFactory(new
		// PropertyValueFactory<>("dateRemiseRapport"));
		col_SujetProjet.setCellValueFactory(new PropertyValueFactory<>("sujet"));

		// Affichage des projets dans le tableau
		tableProjet.setItems(addprojet);
	}

	public void updateProjet(TextField idProjet, TextField nomMatiere, TextField sujetProjet,
			DatePicker dateRemiseProjet, TableColumn<Projet, String> col_IdProjet,
			TableColumn<Projet, String> col_MatiereProjet, TableColumn<Projet, Date> col_DateRemiseProjet,
			TableColumn<Projet, String> col_SujetProjet, TableView<Projet> tableProjet) {
		try {
			String matiere = nomMatiere.getText();
			String sujet = sujetProjet.getText();
			String idProjetStr = idProjet.getText();

			// Convertir la date du DatePicker en java.sql.Date
			java.sql.Date dateRemise = java.sql.Date.valueOf(dateRemiseProjet.getValue());

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
					projetS.updateProjet(Integer.valueOf(idProjetStr), matiere, sujet, dateRemise);

					showAlert(AlertType.INFORMATION, "Information Message", "Projet Updated successfully!");

					addProjetShow(col_IdProjet, col_MatiereProjet, col_DateRemiseProjet, col_SujetProjet, tableProjet);
					resetProjetField(idProjet, nomMatiere, sujetProjet, dateRemiseProjet);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void deleteProjet(TextField idProjet, TextField nomMatiere, TextField sujetProjet,
	        DatePicker dateRemiseProjet, TableColumn<Projet, String> col_IdProjet,
	        TableColumn<Projet, String> col_MatiereProjet, TableColumn<Projet, Date> col_DateRemiseProjet,
	        TableColumn<Projet, String> col_SujetProjet, TableView<Projet> tableProjet) {
	    try {
	        String idProjetStr = idProjet.getText();

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

	                addProjetShow(col_IdProjet, col_MatiereProjet, col_DateRemiseProjet, col_SujetProjet, tableProjet);
	                resetProjetField(idProjet, nomMatiere, sujetProjet, dateRemiseProjet);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	// Add a method to reset Projet fields
	public void resetProjetField(TextField Idprojet, TextField NomMatiereP, TextField SujetProjet,
			DatePicker dateRemisePorjet) {
		// Clear the input fields or perform any other reset actions
		Idprojet.setText("");
		NomMatiereP.setText("");
		SujetProjet.setText("");
		dateRemisePorjet.setValue(null);
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
		
	}

}
