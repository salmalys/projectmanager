
package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.EtudiantService;

import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

public class EtudiantController implements Initializable {
	@FXML
	private ComboBox<String> Formation;
	@FXML
	private ComboBox<String> Formation2;
	@FXML
	private TextField IdEtudiant;
	@FXML
	private TextField NomEtudiant;

	@FXML
	private TextField NomEtudiant2;
	@FXML
	private TextField PrenomEtudiant;

	@FXML
	private TextField PrenomEtudiant2;
	@FXML
	private Button RefreshEtudiant;

	@FXML
	private TableColumn<Etudiant, String> col_PrenomEtudiant;

	@FXML
	private TableColumn<Etudiant, String> col_PromotionEtudiant;
	@FXML
	private TableColumn<Etudiant, String> col_NomEtudiant;

	@FXML
	private TableColumn<Etudiant, String> col_NomformEtudiant;
	@FXML
	private TableColumn<Etudiant, Integer> col_Idetudiant;
	@FXML
	private TextField search_Etudiant;
	@FXML
	private TableView<Etudiant> tableEtudiant;

	@FXML
	private AnchorPane tmp_addEtudiant;
	@FXML
	private AnchorPane tmp_btnEtudiant;
	@FXML
	private AnchorPane tmp_updateEtudiant;
	@FXML
	private Button btn_tmpBackEtudient2;
	@FXML
	private Button btn_tmpaddEtudient;

	@FXML
	private Button btn_tmpbackEtudient;
	@FXML
	private Button btn_tmpupdateEtudient;
	@FXML
	private ComboBox<String> filtre_etudiant;

	private FormationService formationS = ServiceFactory.getFormationService();
	private EtudiantService etudiantS = ServiceFactory.getEtudiantService();

	@FXML
	public void addEtudiant() {
		try {
			String nom = NomEtudiant.getText();
			String prenom = PrenomEtudiant.getText();
			String formationName = Formation.getSelectionModel().getSelectedItem();

			if (!isInputValid(nom, prenom, formationName)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				// Extract formation name and promotion from the combo box
				String[] formationParts = formationName.split(" - ");
				String formationNom = formationParts[0];
				String formationPromotion = formationParts[1];

				// Get the ID of the selected formation
				int idFormation = formationS.getFormationIdByNameAndPromotion(formationNom, formationPromotion);

				int result = etudiantS.createEtudiant(nom, prenom, idFormation);
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Etudiant added successfully!");
					addEtudiantshow();
					addEtudiantReset();

					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message",
							"Nom etudiant: " + nom + " Prenom: " + prenom + " already exists!");
					break;
				default:
					showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while creating the etudiant.");
					break;

				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// methode(action) qui modifie un étudiant dans l'interface graphique
	@FXML
	public void updateEtudiant() {
		try {
			String nom = NomEtudiant2.getText();
			String prenom = PrenomEtudiant2.getText();
			String formationName = Formation2.getSelectionModel().getSelectedItem();
			String idEtudiantString = IdEtudiant.getText();

			Alert alert;
			if (!isInputValid(nom, prenom, formationName)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				// Extract formation name and promotion from the combo box
				String[] formationParts = formationName.split(" - ");
				String formationNom = formationParts[0];
				String formationPromotion = formationParts[1];
				// Get the ID of the selected formation
				int idFormation = formationS.getFormationIdByNameAndPromotion(formationNom, formationPromotion);
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to Update ID Etudiant: " + idEtudiantString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					etudiantS.updateEtudiant(Integer.valueOf(idEtudiantString), nom, prenom, idFormation);

					showAlert(AlertType.INFORMATION, "Information Message", "Etudiant Updated successfully!");

					addEtudiantshow();
					addEtudiantReset2();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// methode(action) qui supprime un étudiant dans l'interface graphique
	@FXML
	public void deleteEtudiant() {
		try {

			String idEtudiantString = IdEtudiant.getText();

			Alert alert;
			if (idEtudiantString.isEmpty()) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to Delete ID Etudiant : Ligne " + idEtudiantString);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					etudiantS.deleteEtudiantById(Integer.valueOf(idEtudiantString));
					showAlert(AlertType.INFORMATION, "Information Message", "Etudiant Deleted successfully!");

					addEtudiantshow();
					addEtudiantReset();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ObservableList<Formation> allFormations;

	@FXML
	public void fillFormationComboBox() {
		allFormations = formationS.listFormations();
		ObservableList<String> formationNamesAndPromotions = FXCollections.observableArrayList();

		for (Formation formation : allFormations) {
			String nameAndPromotion = formation.getNom() + " - " + formation.getPromotion();
			formationNamesAndPromotions.add(nameAndPromotion);
		}

		Formation.setItems(formationNamesAndPromotions);
		Formation2.setItems(formationNamesAndPromotions);
	}

	private ObservableList<Etudiant> addEtudiant;

	// fonction qui affiche le tableau de Etudiant dans l'interface graphique
	@FXML
	public void addEtudiantshow() {
		allFormations = formationS.listFormations();
		addEtudiant = etudiantS.listEtudiants();
		col_Idetudiant.setCellValueFactory(new PropertyValueFactory<>("idEtudiant"));
		col_NomEtudiant.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_PrenomEtudiant.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_NomformEtudiant
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormation().getNom()));
		col_PromotionEtudiant
				.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormation().getPromotion()));
		tableEtudiant.setItems(addEtudiant);
 
	}

	// fonction qui permet de chercher et filter le tableau d'étudiants dans
	// l'interface graphique
	@FXML
	public void searchEtudiant() {
		FilteredList<Etudiant> filter = new FilteredList<>(addEtudiant, e -> true);

		search_Etudiant.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate(predData -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String searchKey = newValue.toLowerCase();
				String idEtudiant = Integer.toString(predData.getIdEtudiant());
				String nomFormation = predData.getFormation().getNom().toLowerCase();
				String promotion = predData.getFormation().getPromotion().toLowerCase();

				// Ajouter la condition pour vérifier quel filtre est sélectionné
				String selectedFilter = filtre_etudiant.getSelectionModel().getSelectedItem();

				if (selectedFilter != null) {
					// Si le filtre est sélectionné, utilisez-le pour la recherche
					if ("IdEtudiant".equals(selectedFilter) && idEtudiant.contains(searchKey)) {
						return true;
					} else if ("Nom Etudiant".equals(selectedFilter)
							&& predData.getNom().toLowerCase().contains(searchKey)) {
						return true;
					} else if ("Prenom Etudiant".equals(selectedFilter)
							&& predData.getPrenom().toLowerCase().contains(searchKey)) {
						return true;
					} else if ("Nom Formation".equals(selectedFilter) && nomFormation.contains(searchKey)) {
						return true;
					} else if ("Promotion".equals(selectedFilter) && promotion.contains(searchKey)) {
						return true;
					} else {
						return false;
					}
				} else {
					// Si aucun filtre n'est sélectionné, utilisez la logique sans filtre
					return idEtudiant.contains(searchKey) || predData.getNom().toLowerCase().contains(searchKey)
							|| predData.getPrenom().toLowerCase().contains(searchKey)
							|| nomFormation.contains(searchKey) || promotion.contains(searchKey);
				}
			});

			SortedList<Etudiant> sortedList = new SortedList<>(filter);
			sortedList.comparatorProperty().bind(tableEtudiant.comparatorProperty());

			tableEtudiant.setItems(sortedList);
		});

	}

	@FXML
	public void selectEtudient() {

		Etudiant etudiant = tableEtudiant.getSelectionModel().getSelectedItem();
		int num = tableEtudiant.getSelectionModel().getFocusedIndex();
		if ((num - 1) < -1) {
			return;
		}
		if (etudiant != null) {
			IdEtudiant.setText(String.valueOf(etudiant.getIdEtudiant()));
			NomEtudiant.setText(etudiant.getNom());
			NomEtudiant2.setText(etudiant.getNom());
			PrenomEtudiant.setText(etudiant.getPrenom());
			PrenomEtudiant2.setText(etudiant.getPrenom());
		}
	}

	// Refresh les donnees de tableau dans UI
	@FXML
	public void refreshData() {
		try {
			addEtudiantshow();
			showAlert(AlertType.INFORMATION, "Refresh", "Data refreshed successfully!");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
		}
	}

	// methode qui relier l'action dans interface graphique avec le button clear
	@FXML
	public void addEtudiantReset() {
		IdEtudiant.setText("");
		NomEtudiant.setText("");
		PrenomEtudiant.setText("");
		Formation.getSelectionModel().clearSelection();

	}

	// methode qui relier l'action dans interface graphique avec le button clear
	@FXML
	public void addEtudiantReset2() {
		IdEtudiant.setText("");
		NomEtudiant2.setText("");
		PrenomEtudiant2.setText("");
		Formation2.getSelectionModel().clearSelection();

	}

	// Button pour switch vers la fenetre d'ajout d'etudiant
	@FXML
	private void handleBtnTmpAddEtudiant(ActionEvent event) {
		if (event.getSource() == btn_tmpaddEtudient) {

			tmp_addEtudiant.setVisible(true);
			tmp_btnEtudiant.setVisible(false);
			tmp_updateEtudiant.setVisible(false);
			fillFormationComboBox();
			addEtudiantReset();
		}
	}

	// Button pour retour de formulaire ajout etudiant vers la fenetre etudiant
	@FXML
	private void handleBackEtudiant(ActionEvent event) {
		if (event.getSource() == btn_tmpbackEtudient) {

			tmp_addEtudiant.setVisible(false);
			tmp_btnEtudiant.setVisible(true);
			tmp_updateEtudiant.setVisible(false);
			fillFormationComboBox();
			addEtudiantReset();
		}

	}

	// Button pour switch vers la fenetre de mise e jour d'etudiant
	@FXML
	private void handleBtnTmpUpdateEtudiant(ActionEvent event) {
		if (event.getSource() == btn_tmpupdateEtudient) {
			tmp_addEtudiant.setVisible(false);
			tmp_btnEtudiant.setVisible(false);
			tmp_updateEtudiant.setVisible(true);
			fillFormationComboBox();
			addEtudiantReset2();
		}
	}

	// Button pour retour de formulaire mise e jour etudiant vers la fenetre
	// etudiant
	@FXML
	private void handleBackEtudiant2(ActionEvent event) {
		if (event.getSource() == btn_tmpBackEtudient2) {
			tmp_addEtudiant.setVisible(false);
			tmp_btnEtudiant.setVisible(true);
			tmp_updateEtudiant.setVisible(false);
			fillFormationComboBox();
			addEtudiantReset();
		}

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

	public void showOptionEtudiant() {
		tmp_addEtudiant.setVisible(false);
		tmp_btnEtudiant.setVisible(true);
		tmp_updateEtudiant.setVisible(false);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addEtudiantshow();
		fillFormationComboBox();
		showOptionEtudiant();
		ObservableList<String> etudiant = FXCollections.observableArrayList("Select", "IdEtudiant", "Nom Etudiant",
				"Prenom Etudiant", "Nom Formation", "Promotion");

		filtre_etudiant.setItems(etudiant);

	}

}
