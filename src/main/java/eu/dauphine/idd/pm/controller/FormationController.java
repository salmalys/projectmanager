package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.FormationService;

import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

public class FormationController implements Initializable {
	private FormationService formationS = ServiceFactory.getFormationService();
	@FXML
	private Button AddFormation;

	@FXML
	private Button Clearformation;

	@FXML
	private Button Clearformation2;

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
	private ComboBox<?> filtre_formation;

	@FXML
	private Button formation_btn;

	@FXML
	private TextField search_formation;

	@FXML
	private TableView<Formation> tableFormation;

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
	private Button Printformation;

	// methode(action) qui ajoute formtion dans l'interface graphique
	@FXML
	public void addFormation() {
		try {
			String nom = Nomformation2.getText();
			String promotion = PromotionList2.getSelectionModel().getSelectedItem();

			if (!isInputValid(nom, promotion)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				int result = formationS.createFormation(nom, promotion);
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Formation added successfully!");
					addformationshow();
					addformationReset();

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
	// 1 probleme a regler on doit avoir id formation pour chaque nom et promotion
	// correspoen
	@FXML
	public void updateFormation() {
		try {
			String nom = Nomformation.getText();
			String promotion = PromotionList.getSelectionModel().getSelectedItem();
			String IdFormatio = IdFormation.getText();

			Alert alert;
			if (!isInputValid(nom, promotion)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");

			} else if (!IdFormatio.isEmpty()) {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure want to Update ID Formation : " + IdFormatio);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {

					formationS.update(Integer.valueOf(IdFormatio), nom, promotion);

					showAlert(AlertType.INFORMATION, "Information Message", "Formation Updated successfully!");

					addformationshow();
					addformationReset2();
				}
			} else {
				showAlert(AlertType.ERROR, "Error Message", "ID Formation cannot be empty Select IDformation in table");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// methode(action) qui supprime formtion dans l'interface graphique
	@FXML
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

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Condition qui verefier c'est nom et promotion ne sont pas vide dans interface
	// graphique
	public boolean isInputValid(String nom, String promotion) {

		if (promotion == null || promotion.isEmpty() || nom.isEmpty() || nom == null) {
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
	private ObservableList<Formation> addformation = FXCollections.observableArrayList();

	// fonction qui affiche le tableau de formation dans l'interface graphique

	public void addformationshow() {
		addformation = formationS.listFormations();

		col_Idformation.setCellValueFactory(new PropertyValueFactory<>("idFormation"));
		col_Nomformation.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_promotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));

		tableFormation.setItems(addformation);

	}

	// methode qui relier l'action dans intergace graphique avec le button clear
	@FXML
	public void addformationReset2() {
		IdFormation.setText("");
		Nomformation2.setText("");
		PromotionList2.getSelectionModel().clearSelection();

	}

	// methode qui relier l'action dans intergace graphique avec le button clear
	@FXML
	public void addformationReset() {
		IdFormation.setText("");
		Nomformation.setText("");
		PromotionList.getSelectionModel().clearSelection();

	}

	// fonction qui permet de chercher et filter le tableau de formation dans GUI
	@FXML
	public void SearchFormation() {

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
			tableFormation.refresh(); // Ajoutez cette ligne pour forcer le rafraechissement
		});
	}

	// initialise la liste deroulente promotion pour liste deroulante ajouter et
	// modifier formation
	private String[] listPromotion = { "Initial", "Alternance", "Formation Continue" };

	@FXML
	public void addPromotionList() {
		if (PromotionList != null) {
			List<String> listp = new ArrayList<>();
			for (String Promot : listPromotion) {
				listp.add(Promot);
			}
			ObservableList<String> promotionL = FXCollections.observableArrayList(listp);
			PromotionList.setItems(promotionL);
		}
	}

	@FXML
	public void addPromotionList2() {
		if (PromotionList2 != null) {
			List<String> listp = new ArrayList<>();
			for (String Promot : listPromotion) {
				listp.add(Promot);
			}
			ObservableList<String> promotionL = FXCollections.observableArrayList(listp);
			PromotionList2.setItems(promotionL);
		}
	}

	@FXML
	public void selectFormation() {
		Formation formation = tableFormation.getSelectionModel().getSelectedItem();
		int num = tableFormation.getSelectionModel().getFocusedIndex();
		if ((num - 1) < -1) {
			return;
		}
		if (formation != null) {
			IdFormation.setText(String.valueOf(formation.getIdFormation()));
			Nomformation.setText(formation.getNom());
			Nomformation2.setText(formation.getNom());
		}

	}

	// Refresh les donnees de tableau dans UI
	@FXML
	public void refreshData() {
		try {
			addformationshow();
			showAlert(AlertType.INFORMATION, "Refresh", "Data refreshed successfully!");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
		}
	}

	// *******************handling button actions in a
	// UI.******************************//

	// Button for switching to the add projet form

	// Mettre vesibilite fenetre Home quand on clic sur button Home
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

	// Mettre vesibilite fenetre formation quand on clic sur button Formation
	private void handleFormationButton(AnchorPane tmp_home, AnchorPane temp_formation, AnchorPane tmp_etudiant,
			AnchorPane tmp_binome, AnchorPane tmp_note, AnchorPane tmp_projet, Button home_btn, Button binome_btn,
			Button etudiant_btn, Button projet_btn, Button note_btn, Button formation_btn) {
		tmp_home.setVisible(false);
		temp_formation.setVisible(true);
		tmp_etudiant.setVisible(false);
		tmp_binome.setVisible(false);
		tmp_note.setVisible(false);
		tmp_projet.setVisible(false);

		addPromotionList();

		formation_btn.setStyle(
				"-fx-background-color: linear-gradient(to right, rgba(0, 0, 0, 1), rgba(20, 20, 54, 1) 44%, rgba(29, 139, 162, 1) 100%);");
		home_btn.setStyle("-fx-background-color: transparent;");
		etudiant_btn.setStyle("-fx-background-color: transparent;");
		projet_btn.setStyle("-fx-background-color: transparent;");
		note_btn.setStyle("-fx-background-color: transparent;");
		binome_btn.setStyle("-fx-background-color: transparent;");
	}

	// Mettre vesibilite fenetre pour formulaire ajouter formation quand on clic sur
	// button Ajouter Formation
	@FXML
	private void handleBtnTmpAdd(ActionEvent event) {
		if (event.getSource() == btn_tmpadd) {
			tmp_addformation.setVisible(true);
			tmp_updateformation.setVisible(false);
			tmp_btnformation.setVisible(false);
			addformationReset2();
			addformationReset();
			addPromotionList2();
		}

	}

	// Button pour retour de formulaire ajoute etudiant vers la fentere formation
	@FXML
	private void handleBackFormation(ActionEvent event) {
		if (event.getSource() == Back_formation) {
			tmp_addformation.setVisible(false);
			tmp_updateformation.setVisible(false);
			tmp_btnformation.setVisible(true);
			addformationReset();
			addPromotionList();
		}
	}

	// Mettre vesibilite fenetre pour formulaire modifier formation quand on clic
	// sur
	// button Modifier Formation
	@FXML
	private void handleBtnTmpUpdate(ActionEvent event) {
		if (event.getSource() == btn_tmpupdate) {
			tmp_addformation.setVisible(false);
			tmp_updateformation.setVisible(true);
			tmp_btnformation.setVisible(false);
			addformationReset();
			addPromotionList();
		}
	}

	// Button pour retour de formulaire modifier etudiant vers la fentere formation
	@FXML
	private void handleBackFormation2(ActionEvent event) {
		if (event.getSource() == Backformation2) {
			tmp_addformation.setVisible(false);
			tmp_updateformation.setVisible(false);
			tmp_btnformation.setVisible(true);
			addformationReset();
			addPromotionList();

		}
	}

	// Fonction principale, Action qui gere tous les switch entre les different
	// fenetres dans
	// notre scene Dashboard
	public void tmpSwitch(ActionEvent event, AnchorPane tmp_home, AnchorPane temp_formation, AnchorPane tmp_etudiant,
			AnchorPane tmp_binome, AnchorPane tmp_note, AnchorPane tmp_projet, Button home_btn, Button binome_btn,
			Button etudiant_btn, Button projet_btn, Button note_btn, Button formation_btn) {
		if (event.getSource() == home_btn) {
			handleHomeButton(tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet, home_btn,
					binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn);
		} else if (event.getSource() == formation_btn) {
			handleFormationButton(tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet, home_btn,
					binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn);

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

	}

	public void showOptionFormation() {
		tmp_addformation.setVisible(false);
		tmp_updateformation.setVisible(false);
		tmp_btnformation.setVisible(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		addformationshow();
		addPromotionList();
		addPromotionList2();
		showOptionFormation();

	}

}
