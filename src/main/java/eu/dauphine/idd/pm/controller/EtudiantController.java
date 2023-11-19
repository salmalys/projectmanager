

package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

public class EtudiantController {
	private FormationService formationS = ServiceFactory.getFormationService();
	private FormationController formationController = new FormationController();

//	public void addEtudiant(TextField idEtudiant, TextField nomEtudiant, TextField prenomEtudiant,
//			ComboBox<String> Formation, TableColumn<Etudiant, Integer> col_IdEtudiant,
//			TableColumn<Etudiant, String> col_NomEtudiant, TableColumn<Etudiant, String> col_PrenomEtudiant,
//			TableColumn<Etudiant, String> col_Formation, TableView<Etudiant> tableEtudiant) {
//		try {
//			String nom = nomEtudiant.getText();
//			String prenom = prenomEtudiant.getText();
//			String formationName = Formation.getSelectionModel().getSelectedItem();
//
//			if (!isInputValid(nom, prenom, formationName)) {
//				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
//			} else {
//				// Extract formation name and promotion from the combo box
//				String[] formationParts = formationName.split(" - ");
//				String formationNom = formationParts[0];
//				String formationPromotion = formationParts[1];
//
//				// Get the ID of the selected formation
//				int idFormation = formationS.getFormationIdByNameAndPromotion(formationNom, formationPromotion);
//
//			//	int result = etudiantS.createEtudiant(nom, prenom, idFormation);
//				switch (result) {
//				case 0: // Success
//					showAlert(AlertType.INFORMATION, "Success", "Etudiant added successfully!");
//			//		addEtudiantShow(col_IdEtudiant, col_NomEtudiant, col_PrenomEtudiant, col_Formation, tableEtudiant);
//					addEtudiantReset(idEtudiant, nomEtudiant, prenomEtudiant, Formation);
//
//				//	SearchEtudiant(search_etudiant, tableEtudiant);
//
//					break;
//				case 1:
//					showAlert(AlertType.ERROR, "Error Message",
//							"Nom etudiant: " + nom + " Prenom: " + prenom + " already exists!");
//					break;
//				default:
//					showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the etudiant.");
//					break;
//
//				}
//			}
//		} catch (Exception e) {
//			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}

	private ObservableList<Formation> allFormations;

	public void fillFormationComboBox(ComboBox<String> Formation, ComboBox<String> Formation2) {
		allFormations = formationS.listFormations();
		ObservableList<String> formationNamesAndPromotions = FXCollections.observableArrayList();

		for (Formation formation : allFormations) {
			String nameAndPromotion = formation.getNom() + " - " + formation.getPromotion();
			formationNamesAndPromotions.add(nameAndPromotion);
		}

		Formation.setItems(formationNamesAndPromotions);
		Formation2.setItems(formationNamesAndPromotions);
	}
	

	// fonction qui affiche le tableau de Etudiant dans l'interface graphique
	public void addEtudiantShow(TableColumn<Etudiant, Integer> col_IdEtudiant, TableColumn<Etudiant, String> col_NomEtudiant, TableColumn<Etudiant, String> col_PrenomEtudiant,TableColumn<Etudiant, String> col_NomformEtudiant, TableColumn<Etudiant, String> col_PromotionEtudiant,  TableView<Etudiant> tableEtudiant) {
		allFormations = formationS.listFormations();
		col_IdEtudiant.setCellValueFactory(new PropertyValueFactory<>("idEtudiant"));
		col_NomEtudiant.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_PrenomEtudiant.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_NomformEtudiant.setCellValueFactory(new PropertyValueFactory<>("formation.nom"));
		col_PromotionEtudiant.setCellValueFactory(new PropertyValueFactory<>("formation.promotion"));
	//	tableEtudiant.setItems(allFormations);

	}


	// methode qui relier l'action dans intergace graphique avec le button clear
	public void addEtudiantReset(TextField idEtudiant, TextField nomEtudiant, TextField prenomEtudiant,
			ComboBox<String> Formation) {
		idEtudiant.setText("");
		nomEtudiant.setText("");
		prenomEtudiant.setText("");
		Formation.getSelectionModel().clearSelection();

	}

	// Condition qui verefier c'est nom et prenom et formation ne sont pas vide dans
	// GUI
	public boolean isInputValid(String nom, String prenom, String formation) {

		if (prenom.isEmpty() || formation.isEmpty() || nom.isEmpty()) {
			showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			return false;
		}
		return true;
	}

	// Affiche les fenetre d'alerte specifique pour chaque cas
	public void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

