package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
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
import eu.dauphine.idd.pm.model.Formation;

public class FormationController {
	private FormationService formationS = ServiceFactory.getFormationService();

	// methode(action) qui ajoute formtion dans l'interface graphique
	public void addFormation(TextField IdFormation, TextField nomFormation, ComboBox<String> promotionList,
			TextField search_formation, TableColumn<Formation, Integer> col_Idformation,
			TableColumn<Formation, String> col_Nomformation, TableColumn<Formation, String> col_promotion,
			TableView<Formation> tableFormation) {
		try {
			String nom = nomFormation.getText();
			String promotion = promotionList.getSelectionModel().getSelectedItem();

			if (!isInputValid(nom, promotion)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				int result = formationS.createFormation(nom, promotion);
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Formation added successfully!");
					addformationshow(col_Idformation, col_Nomformation, col_promotion, tableFormation);
					addformationReset2(IdFormation, nomFormation, promotionList);

					SearchFormation(search_formation, tableFormation);

					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message",
							"Nom formation: " + nom + " Promotion: " + promotion + " already exists!");
					break;
				default:
					showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the formation.");
					break;

				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}

	}

	// methode(action) qui modifier formtion dans l'interface graphique
	public void updateFormation(TextField idFormation, TextField nomFormation, ComboBox<String> promotionList,
			TextField search_formation, TableColumn<Formation, Integer> col_Idformation,
			TableColumn<Formation, String> col_Nomformation, TableColumn<Formation, String> col_promotion,
			TableView<Formation> tableFormation) {
		try {
			String nom = nomFormation.getText();
			String promotion = promotionList.getSelectionModel().getSelectedItem();
			String IdFormatio = idFormation.getText();

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

					addformationshow(col_Idformation, col_Nomformation, col_promotion, tableFormation);
					addformationReset2(idFormation, nomFormation, promotionList);
					SearchFormation(search_formation, tableFormation);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// methode(action) qui supprime formtion dans l'interface graphique
	public void deleteFormation(TextField idFormation, TextField nomFormation, ComboBox<String> promotionList,
			TextField search_formation, TableColumn<Formation, Integer> col_Idformation,
			TableColumn<Formation, String> col_Nomformation, TableColumn<Formation, String> col_promotion,
			TableView<Formation> tableFormation) {
		try {

			String IdFormatio = idFormation.getText();

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

					addformationshow(col_Idformation, col_Nomformation, col_promotion, tableFormation);
					addformationReset2(idFormation, nomFormation, promotionList);
					SearchFormation(search_formation, tableFormation);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Condition qui verefier c'est nom et promotion ne sont pas vide dans interface
	// graphique
	public boolean isInputValid(String nom, String promotion) {

		if (promotion == null || promotion.isEmpty() || nom.isEmpty()) {
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

	// Liste element de formation
	private ObservableList<Formation> addformation;

	// fonction qui affiche le tableau de formation dans l'interface graphique
	public void addformationshow(TableColumn<Formation, Integer> col_Idformation,
			TableColumn<Formation, String> col_Nomformation, TableColumn<Formation, String> col_promotion,
			TableView<Formation> tableFormation) {
		addformation = formationS.listFormations();
		col_Idformation.setCellValueFactory(new PropertyValueFactory<>("idFormation"));
		col_Nomformation.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_promotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));

		tableFormation.setItems(addformation);

	}

	// methode qui relier l'action dans intergace graphique avec le button clear
	public void addformationReset2(TextField IdFormation, TextField Nomformation2, ComboBox<String> PromotionList2) {
		IdFormation.setText("");
		Nomformation2.setText("");
		PromotionList2.getSelectionModel().clearSelection();

	}

	// fonction qui permet de chercher et filter le tableau de formation dans GUI
	public void SearchFormation(TextField search_formation, TableView<Formation> tableFormation) {
		   FilteredList<Formation> filter = new FilteredList<>(addformation, e -> true);
		    search_formation.textProperty().addListener((Observable, oldValue, newValue) -> {
		        filter.setPredicate(predData -> {
		            if (newValue == null || newValue.isEmpty()) {
		                return true;
		            }
		            String searchKey = newValue.toLowerCase();
		            String idformation = String.valueOf(predData.getIdFormation());

		            if (idformation.contains(searchKey)) {
		                return true;
		            } else if (predData.getNom().toLowerCase().contains(searchKey)) {
		                return true;
		            } else if (predData.getPromotion().toLowerCase().contains(searchKey)) {
		                return true;
		            } else
		                return false;
		        });
		        
		        SortedList<Formation> sortList = new SortedList<>(filter);
		        sortList.comparatorProperty().bind(tableFormation.comparatorProperty());
		        tableFormation.setItems(sortList);
		        tableFormation.refresh();  // Ajoutez cette ligne pour forcer le rafra�chissement
		    });
	}

	// initialis� la liste deroulente promotion pour liste deroulante ajouter et
	// modifier formation
	private String[] listPromotion = { "Initial", "Alternance", "Formation Continue" };

	public void addPromotionList(ComboBox<String> PromotionList) {
		List<String> listp = new ArrayList<>();
		for (String Promot : listPromotion) {
			listp.add(Promot);
		}
		ObservableList<String> promotionL = FXCollections.observableArrayList(listp);
		PromotionList.setItems(promotionL);

	}

	// Refresh les donn�es de tableau dans UI
	public void refreshData(TableColumn<Formation, Integer> col_Idformation,
			TableColumn<Formation, String> col_Nomformation, TableColumn<Formation, String> col_promotion,
			TableView<Formation> tableFormation) {
		try {
			addformationshow(col_Idformation, col_Nomformation, col_promotion, tableFormation);
			showAlert(AlertType.INFORMATION, "Refresh", "Data refreshed successfully!");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
		}
	}

	// *******************handling button actions in a
	// UI.******************************//

	// Button pour switch vers la fenetre d'ajout d'�tudiant
	private void handleBtnTmpAddEtudiant(AnchorPane tmp_addEtudiant, AnchorPane tmp_btnEtudiant,
			AnchorPane tmp_updateEtudiant) {
		tmp_addEtudiant.setVisible(true);
		tmp_btnEtudiant.setVisible(false);
		tmp_updateEtudiant.setVisible(false);
	}

	// Button pour retour de formulaire ajout �tudiant vers la fen�tre �tudiant
	private void handleBackEtudiant(Button btn_tmpbackEtudient, AnchorPane tmp_addEtudiant, AnchorPane tmp_btnEtudiant,
			AnchorPane tmp_updateEtudiant) {
		tmp_addEtudiant.setVisible(false);
		tmp_btnEtudiant.setVisible(true);
		tmp_updateEtudiant.setVisible(false);

	}

	// Button pour switch vers la fen�tre de mise � jour d'�tudiant
	private void handleBtnTmpUpdateEtudiant(AnchorPane tmp_addEtudiant, AnchorPane tmp_btnEtudiant,
			AnchorPane tmp_updateEtudiant) {
		tmp_addEtudiant.setVisible(false);
		tmp_btnEtudiant.setVisible(false);
		tmp_updateEtudiant.setVisible(true);
	}

	// Button pour retour de formulaire mise � jour �tudiant vers la fen�tre
	// �tudiant
	private void handleBackEtudiant2(Button btn_tmpBackEtudient2, AnchorPane tmp_addEtudiant,
			AnchorPane tmp_btnEtudiant, AnchorPane tmp_updateEtudiant) {
		tmp_addEtudiant.setVisible(false);
		tmp_btnEtudiant.setVisible(true);
		tmp_updateEtudiant.setVisible(false);

	}

	// Mettre vesibilit� fenetre Home quand on clic sur button Home
	private void handleHomeButton(AnchorPane tmp_home, AnchorPane temp_formation, AnchorPane tmp_etudiant,
			AnchorPane tmp_binome, AnchorPane tmp_note, AnchorPane tmp_projet, Button home_btn, Button binome_btn,
			Button etudiant_btn, Button projet_btn, Button note_btn, Button formation_btn) {
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

	// Mettre vesibilit� fenetre formation quand on clic sur button Formation
	private void handleFormationButton(AnchorPane tmp_home, AnchorPane temp_formation, AnchorPane tmp_etudiant,
			AnchorPane tmp_binome, AnchorPane tmp_note, AnchorPane tmp_projet, AnchorPane tmp_btnformation,
			Button home_btn, Button binome_btn, Button etudiant_btn, Button projet_btn, Button note_btn,
			Button formation_btn, ComboBox<String> PromotionList, TextField search_formation,
			TableView<Formation> tableFormation) {
		tmp_home.setVisible(false);
		temp_formation.setVisible(true);
		tmp_etudiant.setVisible(false);
		tmp_binome.setVisible(false);
		tmp_note.setVisible(false);
		tmp_projet.setVisible(false);
		tmp_btnformation.setVisible(true);
		addPromotionList(PromotionList);
		SearchFormation(search_formation, tableFormation);
		formation_btn.setStyle(
				"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
		home_btn.setStyle("-fx-background-color: transparent;");
		etudiant_btn.setStyle("-fx-background-color: transparent;");
		projet_btn.setStyle("-fx-background-color: transparent;");
		note_btn.setStyle("-fx-background-color: transparent;");
		binome_btn.setStyle("-fx-background-color: transparent;");
	}

	// Mettre vesibilit� fenetre pour formulaire ajouter formation quand on clic sur
	// button Ajouter Formation
	private void handleBtnTmpAdd(AnchorPane tmp_addformation, AnchorPane tmp_updateformation,
			AnchorPane tmp_btnformation, TextField IdFormation, TextField Nomformation, ComboBox<String> PromotionList,
			TextField Nomformation2, ComboBox<String> PromotionList2) {
		tmp_addformation.setVisible(true);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(false);
		addformationReset2(IdFormation, Nomformation, PromotionList);
		addformationReset2(IdFormation, Nomformation2, PromotionList2);
		addPromotionList(PromotionList2);

	}

	// Button pour retour de formulaire ajoute etudiant vers la fentere formation
	private void handleBackFormation(AnchorPane tmp_addformation, AnchorPane tmp_updateformation,
			AnchorPane tmp_btnformation, TextField IdFormation, TextField Nomformation,
			ComboBox<String> PromotionList) {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(true);
		addformationReset2(IdFormation, Nomformation, PromotionList);
		addPromotionList(PromotionList);
	}

	// Mettre vesibilit� fenetre pour formulaire modifier formation quand on clic
	// sur
	// button Modifier Formation
	private void handleBtnTmpUpdate(AnchorPane tmp_addformation, AnchorPane tmp_updateformation,
			AnchorPane tmp_btnformation, TextField IdFormation, TextField Nomformation,
			ComboBox<String> PromotionList) {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(true);
		tmp_btnformation.setVisible(false);
		addformationReset2(IdFormation, Nomformation, PromotionList);
		addPromotionList(PromotionList);
	}

	// Button pour retour de formulaire modifier etudiant vers la fentere formation
	private void handleBackFormation2(AnchorPane tmp_addformation, AnchorPane tmp_updateformation,
			AnchorPane tmp_btnformation, TextField IdFormation, TextField Nomformation,
			ComboBox<String> PromotionList) {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(true);
		addformationReset2(IdFormation, Nomformation, PromotionList);
		addPromotionList(PromotionList);
	}

	// Fonction principale, Action qui gere tous les switch entre les different
	// fenetres dans
	// notre scene Dashboard
	public void tmpSwitch(ActionEvent event, AnchorPane tmp_home, AnchorPane temp_formation, AnchorPane tmp_etudiant,
			AnchorPane tmp_binome, AnchorPane tmp_note, AnchorPane tmp_projet, Button home_btn, Button binome_btn,
			Button etudiant_btn, Button projet_btn, Button note_btn, Button formation_btn, Button btn_tmpadd,
			Button Back_formation, Button btn_tmpupdate, Button Backformation2, AnchorPane tmp_btnformation,
			ComboBox<String> PromotionList, TextField search_formation, TableView<Formation> tableFormation,
			AnchorPane tmp_addformation, AnchorPane tmp_updateformation, TextField IdFormation, TextField Nomformation,
			TextField Nomformation2, ComboBox<String> PromotionList2, AnchorPane tmp_addEtudiant,
			AnchorPane tmp_btnEtudiant, AnchorPane tmp_updateEtudiant, Button btn_tmpBackEtudient2,
			Button btn_tmpaddEtudient, Button btn_tmpbackEtudient, Button btn_tmpupdateEtudient) {
		if (event.getSource() == home_btn) {
			handleHomeButton(tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet, home_btn,
					binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn);
		} else if (event.getSource() == formation_btn) {
			handleFormationButton(tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet,
					tmp_btnformation, home_btn, binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn,
					PromotionList, search_formation, tableFormation);
			SearchFormation(search_formation, tableFormation);

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
			handleBtnTmpAdd(tmp_addformation, tmp_updateformation, tmp_btnformation, IdFormation, Nomformation,
					PromotionList, Nomformation2, PromotionList2);

		} else if (event.getSource() == Back_formation && temp_formation.isVisible()) {
			handleBackFormation(tmp_addformation, tmp_updateformation, tmp_btnformation, IdFormation, Nomformation2,
					PromotionList2);

		} else if (event.getSource() == btn_tmpupdate && temp_formation.isVisible()) {
			handleBtnTmpUpdate(tmp_addformation, tmp_updateformation, tmp_btnformation, IdFormation, Nomformation,
					PromotionList);

		} else if (event.getSource() == Backformation2 && temp_formation.isVisible()) {
			handleBackFormation2(tmp_addformation, tmp_updateformation, tmp_btnformation, IdFormation, Nomformation,
					PromotionList);

		} else if (event.getSource() == btn_tmpaddEtudient) {
			handleBtnTmpAddEtudiant(tmp_addEtudiant, tmp_btnEtudiant, tmp_updateEtudiant);
		} else if (event.getSource() == btn_tmpbackEtudient) {
			handleBackEtudiant(btn_tmpbackEtudient, tmp_addEtudiant, tmp_btnEtudiant, tmp_updateEtudiant);
		} else if (event.getSource() == btn_tmpupdateEtudient) {
			handleBtnTmpUpdateEtudiant(tmp_addEtudiant, tmp_btnEtudiant, tmp_updateEtudiant);
		} else if (event.getSource() == btn_tmpBackEtudient2) {
			handleBackEtudiant2(btn_tmpBackEtudient2, tmp_addEtudiant, tmp_btnEtudiant, tmp_updateEtudiant);
		}
	}

}
