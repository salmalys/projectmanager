package eu.dauphine.idd.pm.controller;

import java.net.URL;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.EtudiantService;
import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ProjetService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class BinomeController implements Initializable {
	@FXML
	private Button Back_Binome1;

	@FXML
	private Button Back_Binome2;

	@FXML
	private Button Back_Binome3;

	@FXML
	private DatePicker DateRemise;

	@FXML
	private ComboBox<String> Etudiant1List;

	@FXML
	private ComboBox<String> Etudiant1List2;

	@FXML
	private ComboBox<String> Etudiant2List;
	@FXML
	private ComboBox<String> Etudiant2List2;

	@FXML
	private TextField Id_Binome2;

	@FXML
	private ComboBox<String> Projet;

	@FXML
	private ComboBox<String> Projet2;

	@FXML
	private Button RefreshBinome;

	@FXML
	private TableColumn<BinomeProjet, String> col_DateRemiseProjet;

	@FXML
	private TableColumn<BinomeProjet, String> col_Etudiant1;
	@FXML
	private TableColumn<BinomeProjet, String> col_Etudiant2;

	@FXML
	private TableColumn<BinomeProjet, String> col_NomMatiere;

	@FXML
	private TableColumn<BinomeProjet, String> col_Sujtprojet;

	@FXML
	private TableColumn<BinomeProjet, String> col_idBinome;

	@FXML
	private TextField id_binome;

	@FXML
	private TextField search_Binome;

	@FXML
	private TableView<BinomeProjet> tableBinome;
	@FXML
	private AnchorPane tmp_RemisBinome;

	@FXML
	private AnchorPane tmp_addBinome;

	@FXML
	private AnchorPane tmp_updateBinome;

	@FXML
	private Button btn_RemisBinome;

	@FXML
	private Button btn_addBinome;

	@FXML
	private Button btn_updateBinome;
	@FXML
	private AnchorPane tmp_DeleteBinome;

	private ProjetService projetS = ServiceFactory.getProjetService();
	private EtudiantService etudiantS = ServiceFactory.getEtudiantService();
	private BinomeProjetService binomeS = ServiceFactory.getBinomeProjetService();

	private ObservableList<BinomeProjet> addBinome;

	@FXML
	public void addBinome() {
		try {
			String etudiant1 = Etudiant1List.getSelectionModel().getSelectedItem();
			String etudiant2 = Etudiant2List.getSelectionModel().getSelectedItem();
			String projet = Projet.getSelectionModel().getSelectedItem();

			if (!isInputValid(etudiant1, etudiant2, projet)) {
				return;
			}

			// Extraction des noms et prénoms des étudiants à partir des ComboBox
			String[] etudiant1Parts = etudiant1.split(" ");
			String nomEtudiant1 = etudiant1Parts[0];
			String prenomEtudiant1 = etudiant1Parts[1];

			String[] etudiant2Parts = etudiant2.split(" ");
			String nomEtudiant2 = etudiant2Parts[0];
			String prenomEtudiant2 = etudiant2Parts[1];

			// Extraction du nom du sujet et de la matière du projet à partir du ComboBox
			String[] projetParts = projet.split(" - ");
			String nomMatiere = projetParts[0];
			String sujetProjet = projetParts[1];

			System.out.println(nomEtudiant1 + " " + prenomEtudiant1);
			System.out.println(nomEtudiant2 + " " + prenomEtudiant2);
			// Vous devrez probablement ajouter des méthodes pour obtenir les ID à partir
			// des noms et prénoms
			int idEtudiant1 = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant1, prenomEtudiant1);
			int idEtudiant2 = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant2, prenomEtudiant2);
			int idProjet = projetS.getProjetIdByNomMatiereAndSujet(nomMatiere, sujetProjet);
			System.out.println(idEtudiant1 + " " + idEtudiant2 + " " + idProjet);

			// Convertir la date du DatePicker en java.sql.Date
			if (DateRemise != null) {
				java.sql.Date dateRemise = java.sql.Date.valueOf(DateRemise.getValue());

				// Appeler le service pour créer le binôme
				int result = binomeS.createBinomeProjet(idEtudiant1, idEtudiant2, idProjet, dateRemise);
				System.out.println(result);

				// Gérer le résultat
				switch (result) {
				case 0: // Succès
					showAlert(AlertType.INFORMATION, "Success", "Binome added successfully!");
					addShowBinome();
					resetBinomeFields(); // Réinitialiser les champs après l'ajout
					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message", "An error occurred while creating the binome.");
					break;
				default:
					showAlert(AlertType.ERROR, "Error Message", "An unexpected error occurred.");
					break;
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void updateBinome() {
		try {

			String idBinomeString = id_binome.getText();
			String etudiant1Name = Etudiant1List2.getValue();
			String etudiant2Name = Etudiant2List2.getValue();
			String projetName = Projet2.getValue();

			Alert alert;
			if (!isInputValid(projetName, etudiant2Name, etudiant1Name)) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to Update ID Binome: " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					// Splitting the names into first name and last name
					String[] etudiant1Parts = etudiant1Name.split(" ");
					String nomEtudiant1 = etudiant1Parts[0];
					String prenomEtudiant1 = etudiant1Parts[1];

					String[] etudiant2Parts = etudiant2Name.split(" ");
					String nomEtudiant2 = etudiant2Parts[0];
					String prenomEtudiant2 = etudiant2Parts[1];

					// Splitting the projetName into nomMatiere and sujetProjet
					String[] projetParts = projetName.split(" - ");
					String nomMatiere = projetParts[0];
					String sujetProjet = projetParts[1];

					// Get the IDs of the selected Etudiants and Projet
					int etudiant1Id = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant1, prenomEtudiant1);
					int etudiant2Id = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant2, prenomEtudiant2);
					int projetId = projetS.getProjetIdByNomMatiereAndSujet(nomMatiere, sujetProjet);
					// Créez une instance de java.util.Date avec la date actuelle
					// Créez une instance de java.sql.Date avec la date actuelle
					java.sql.Date dateRemise = java.sql.Date.valueOf(DateRemise.getValue());
					// Assuming you have an updateBinome method in your binomeS service
					binomeS.updateBinomeProjet(Integer.valueOf(idBinomeString), etudiant1Id, etudiant2Id, projetId,
							dateRemise);

					showAlert(AlertType.INFORMATION, "Information Message", "Binome Updated successfully!");

					// You might want to refresh or update your Binome TableView after the update
					refreshBinomeTable();

					// You might want to clear the Binome fields after the update
					resetBinomeFields();
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteBinome() {
		try {
			String idBinomeString = id_binome.getText();

			Alert alert;
			if (idBinomeString.isEmpty()) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all blank fields");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to Delete ID Binome: " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					binomeS.deleteBinomeProjetById(Integer.valueOf(idBinomeString));
					showAlert(AlertType.INFORMATION, "Information Message", "Binome Deleted successfully!");

					// You might want to refresh or update your Binome TableView after the delete
					refreshBinomeTable();

					// You might want to clear the Binome fields after the delete
					resetBinomeFields();
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void updateDateRemiseProjet() {
		try {
			String idBinomeString = Id_Binome2.getText();

			if (idBinomeString.isEmpty() || DateRemise.getValue() == null) {
				showAlert(AlertType.ERROR, "Error Message", "Please fill all fields");
			} else {
				java.sql.Date dateRemise = java.sql.Date.valueOf(DateRemise.getValue());

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText(
						"Are you sure you want to update the date of remise for Binome ID: " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					// Call your service method to update the date of remise
					binomeS.updateDateRemise(Integer.valueOf(idBinomeString), dateRemise);

					showAlert(AlertType.INFORMATION, "Information Message", "Date of Remise Updated successfully!");

					// You might want to refresh or update your Binome TableView after the update
					refreshBinomeTable();

					// Clear the fields after the update
					resetDateRemiseFields();
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void resetDateRemiseFields() {
		Id_Binome2.clear();
		DateRemise.getEditor().clear(); // Clear the DatePicker
	}

	@FXML
	public void addShowBinome() {
		addBinome = binomeS.listBinomeProjets();

		col_idBinome.setCellValueFactory(new PropertyValueFactory<>("idBinome"));

		col_Etudiant1.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getMembre1().getNom() + " " + cellData.getValue().getMembre1().getPrenom()));

		col_Etudiant2.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getMembre1().getNom() + " " + cellData.getValue().getMembre1().getPrenom()));

		col_NomMatiere.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getProjet().getNomMatiere()));
		col_Sujtprojet
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProjet().getSujet()));

		 col_DateRemiseProjet.setCellValueFactory(null);

		tableBinome.setItems(addBinome);
	}

	@FXML
	public void searchBinome() {
		if (search_Binome != null) {
			FilteredList<BinomeProjet> filter = new FilteredList<>(addBinome, b -> true);

			search_Binome.textProperty().addListener((observable, oldValue, newValue) -> {
				filter.setPredicate(predData -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}
					String searchKey = newValue.toLowerCase();

					// Check if any of the fields contain the search keyword
					boolean idMatches = Integer.toString(predData.getIdBinome()).contains(searchKey);
					boolean etudiant1Matches = (predData.getMembre1().getNom() + " "
							+ predData.getMembre1().getPrenom()).toLowerCase().contains(searchKey);
					boolean etudiant2Matches = (predData.getMembre2().getNom() + " "
							+ predData.getMembre2().getPrenom()).toLowerCase().contains(searchKey);
					boolean projetMatches = (predData.getProjet().getNomMatiere() + " - "
							+ predData.getProjet().getSujet()).toLowerCase().contains(searchKey);
					boolean dateRemiseMatches = predData.getDateRemiseEffective().toString().contains(searchKey);

					return idMatches || etudiant1Matches || etudiant2Matches || projetMatches || dateRemiseMatches;
				});
			});

			SortedList<BinomeProjet> sortedList = new SortedList<>(filter);
			sortedList.comparatorProperty().bind(tableBinome.comparatorProperty());

			tableBinome.setItems(sortedList);
		}
	}

	@FXML
	public void selectBinome() {
		BinomeProjet binome = tableBinome.getSelectionModel().getSelectedItem();
		int num = tableBinome.getSelectionModel().getFocusedIndex();

		if ((num - 1) < -1 || binome == null) {
			return;
		}

		id_binome.setText(String.valueOf(binome.getIdBinome()));
		Id_Binome2.setText(String.valueOf(binome.getIdBinome()));

	}

	private ObservableList<Etudiant> allEtudiants;

	@FXML
	public void fillEtudiantComboBox() {
		allEtudiants = etudiantS.listEtudiants();
		ObservableList<String> etudiantNames = FXCollections.observableArrayList();

		for (Etudiant etudiant : allEtudiants) {
			String name = etudiant.getNom() + " " + etudiant.getPrenom();
			etudiantNames.add(name);
		}

		Etudiant1List.setItems(etudiantNames);
		Etudiant1List2.setItems(etudiantNames);
		Etudiant2List.setItems(etudiantNames);
		Etudiant2List2.setItems(etudiantNames);
	}

	private ObservableList<Projet> allProjets;

	@FXML
	public void fillProjetComboBox() {
		allProjets = projetS.listProjets();
		ObservableList<String> projetNames = FXCollections.observableArrayList();

		for (Projet projet : allProjets) {
			String name = projet.getNomMatiere() + " - " + projet.getSujet();
			projetNames.add(name);
		}

		Projet.setItems(projetNames);
		Projet2.setItems(projetNames);
	}

	// Button pour switch vers la fenetre d'ajout de binome
	@FXML
	private void handleBtnTmpAddBinome(ActionEvent event) {
		if (event.getSource() == btn_addBinome) {
			tmp_addBinome.setVisible(true);
			tmp_updateBinome.setVisible(false);
			tmp_DeleteBinome.setVisible(false);
			resetBinomeFields();

		}
	}

	// Button pour retour de formulaire ajout binome vers la fenetre binome
	@FXML
	private void handleBackBinome(ActionEvent event) {
		if (event.getSource() == Back_Binome1 || event.getSource() == Back_Binome2
				|| event.getSource() == Back_Binome3) {
			tmp_addBinome.setVisible(false);
			tmp_updateBinome.setVisible(false);
			tmp_DeleteBinome.setVisible(true);
			tmp_RemisBinome.setVisible(false);
			resetBinomeFields();
		}
	}

	// Button pour switch vers la fenetre de mise a jour de binome
	@FXML
	private void handleBtnTmpUpdateBinome(ActionEvent event) {
		if (event.getSource() == btn_updateBinome) {
			tmp_addBinome.setVisible(false);
			tmp_updateBinome.setVisible(true);
			tmp_DeleteBinome.setVisible(false);
			tmp_RemisBinome.setVisible(false);
			resetBinomeFields2();
		}
	}

	// Button pour retour de formulaire mise a jour binome vers la fenetre binome
	@FXML
	private void handleBtnTmpRemiseProjet(ActionEvent event) {
		if (event.getSource() == btn_RemisBinome) {
			tmp_addBinome.setVisible(false);
			tmp_updateBinome.setVisible(false);
			tmp_DeleteBinome.setVisible(false);
			tmp_RemisBinome.setVisible(true);
			resetBinomeFields2();

		}
	}

	// Ajouter cette fonction pour réinitialiser les champs après l'ajout du binôme
	@FXML
	private void resetBinomeFields() {
		// Effacer les ComboBox ou effectuer d'autres opérations de réinitialisation si
		// nécessaire
		Etudiant1List.getSelectionModel().clearSelection();
		Etudiant2List.getSelectionModel().clearSelection();
		Projet.getSelectionModel().clearSelection();
		id_binome.clear();

	}

	@FXML
	private void resetBinomeFields2() {
		// Effacer les ComboBox ou effectuer d'autres opérations de réinitialisation si
		// nécessaire
		Etudiant1List2.getSelectionModel().clearSelection();
		Etudiant2List2.getSelectionModel().clearSelection();
		Projet2.getSelectionModel().clearSelection();
		Id_Binome2.clear();
		DateRemise.setValue(null);
	}

	private void refreshBinomeTable() {
		addShowBinome();
	}

	// Condition qui verefier c'est nom et prenom et formation ne sont pas vide dans
	// GUI
	public boolean isInputValid(String Etudiant1, String Etudiant2, String Projet) {

		if (Etudiant1.isEmpty() || Etudiant2.isEmpty() || Projet.isEmpty()) {
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		fillEtudiantComboBox();
		fillProjetComboBox();
		addShowBinome();
	}

}
