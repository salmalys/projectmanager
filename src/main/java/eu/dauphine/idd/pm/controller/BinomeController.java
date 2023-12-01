package eu.dauphine.idd.pm.controller;

import java.net.URL;

import java.time.LocalDate;
import java.time.ZoneId;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.EtudiantService;
import eu.dauphine.idd.pm.service.NotesService;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.beans.property.SimpleObjectProperty;
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
	private TextField NoteRapportAdd;

	@FXML
	private TextField NoteSetudiant1Add;

	@FXML
	private TextField NoteSetudiant2Add;
	@FXML
	private TextField id_binomeAdd;
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
	@FXML
	private ComboBox<String> filtre_binome;

	private ProjetService projetS = ServiceFactory.getProjetService();
	private EtudiantService etudiantS = ServiceFactory.getEtudiantService();
	private BinomeProjetService binomeS = ServiceFactory.getBinomeProjetService();
	private NotesService noteS = ServiceFactory.getNotesService();
	private ObservableList<BinomeProjet> addBinome;

	@FXML
	public void addBinome() {
		try {
			String etudiant1 = Etudiant1List.getSelectionModel().getSelectedItem();
			String etudiant2 = Etudiant2List.getSelectionModel().getSelectedItem();
			String projet = Projet.getSelectionModel().getSelectedItem();

			if (!isInputValid(etudiant1, projet)) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				String[] etudiant1Parts = etudiant1.split(" ");
				String nomEtudiant1 = etudiant1Parts[0];
				String prenomEtudiant1 = etudiant1Parts[1];

				String[] projetParts = projet.split(" - ");
				String nomMatiere = projetParts[0];
				String sujetProjet = projetParts[1];

				int idEtudiant1 = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant1, prenomEtudiant1);
				int idProjet = projetS.getProjetIdByNomMatiereAndSujet(nomMatiere, sujetProjet);

				int result = -1;
				if (etudiant2 != null) {
					String[] etudiant2Parts = etudiant2.split(" ");
					String nomEtudiant2 = etudiant2Parts[0];
					String prenomEtudiant2 = etudiant2Parts[1];
					int idEtudiant2 = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant2, prenomEtudiant2);
					if (idEtudiant1 != idEtudiant2) {
						result = binomeS.createBinomeProjet(idEtudiant1, idEtudiant2, idProjet, null);
					} else {
						showAlert(AlertType.WARNING, "Warning Message",
								"L'étudiant 1 et l'étudiant 2 doivent être différents.\n\nPour créer un binôme avec un seul étudiant, veuillez remplir uniquement le champ \"Etudiant 1\"");
						resetBinomeFields();
						return;
					}

				} else {
					result = binomeS.createSoloProjet(idEtudiant1, idProjet, null);
				}

				switch (result) {
				case 0:
					showAlert(AlertType.INFORMATION, "Success", "Binome crée avec succès !");
					refreshBinomeTable();
					resetBinomeFields();
					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message", "Une erreur s'est produite lors de la création du binôme.");
					break;
				default:
					showAlert(AlertType.ERROR, "Error Message", "Une erreur s'est produite");
					break;
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite : " + e.getMessage());
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
			if (etudiant1Name == null) {
				etudiant1Name = Etudiant1List2.getPromptText();
			}
			if (etudiant2Name == null) {
				etudiant2Name = Etudiant2List2.getPromptText();

			}
			if (projetName == null) {
				projetName = Projet2.getPromptText();
			}
			Alert alert;
			if (!isInputValid(projetName, etudiant1Name)) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Êtes-vous sûr de vouloir mettre à jour le binôme avec l'ID  " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					String[] etudiant1Parts = etudiant1Name.split(" ");
					String nomEtudiant1 = etudiant1Parts[0];
					String prenomEtudiant1 = etudiant1Parts[1];

					String[] etudiant2Parts = etudiant2Name.split(" ");
					String nomEtudiant2 = etudiant2Parts[0];
					String prenomEtudiant2 = etudiant2Parts[1];

					String[] projetParts = projetName.split(" - ");
					String nomMatiere = projetParts[0];
					String sujetProjet = projetParts[1];

					int etudiant1Id = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant1, prenomEtudiant1);
					int etudiant2Id = etudiantS.getEtudiantIdByNameAndPrenom(nomEtudiant2, prenomEtudiant2);
					int projetId = projetS.getProjetIdByNomMatiereAndSujet(nomMatiere, sujetProjet);

					// Assuming your service method returns a boolean indicating success
					binomeS.updateBinomeProjet(Integer.valueOf(idBinomeString), etudiant1Id, etudiant2Id, projetId,
							null);

					showAlert(AlertType.INFORMATION, "Success", "Binôme mis à jour avec succès !");
					refreshBinomeTable();
					resetBinomeFields(); // Reset the input fields

				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteBinome() {
		try {

			String idBinomeString = id_binome.getText();

			Alert alert;
			if (idBinomeString.isEmpty()) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Êtes-vous sûr de vouloir supprimer le binôme avec l'ID " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					noteS.deleteNotesById(Integer.valueOf(idBinomeString));
					binomeS.deleteBinomeProjetById(Integer.valueOf(idBinomeString));
					showAlert(AlertType.INFORMATION, "Information Message", "Binôme supprimé avec succès !");

					// You might want to refresh or update your Binome TableView after the delete
					refreshBinomeTable();

					// You might want to clear the Binome fields after the delete
					resetBinomeFields();
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void updateDateRemiseProjet() {
		try {
			String idBinomeString = Id_Binome2.getText();

			if (idBinomeString.isEmpty() || DateRemise.getValue() == null) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				LocalDate localDate = DateRemise.getValue();
				java.sql.Date dateRemise = null;
				if (localDate != null) {
					dateRemise = java.sql.Date.valueOf(localDate);
				}

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText(
						"Êtes-vous sûr de vouloir mettre à jour la date de remise pour le binôme avec l'ID " + idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					binomeS.updateDateRemise(Integer.valueOf(idBinomeString), dateRemise);

					showAlert(AlertType.INFORMATION, "Information Message", "Date de remise mise à jour avec succès !");
					refreshBinomeTable();
					resetDateRemiseFields(); // Reset the date remise input fields

				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void resetDateRemiseFields() {
		Id_Binome2.clear();
		DateRemise.getEditor().clear(); // Clear the DatePicker
	}

	// Define SimpleDateFormat as a class member
	private SimpleDateFormat newFormatter = new SimpleDateFormat("dd-MM-yyyy");

	@FXML
	public void addShowBinome() {
		addBinome = binomeS.listBinomeProjets();

		col_idBinome.setCellValueFactory(new PropertyValueFactory<>("idBinome"));

		col_Etudiant1.setCellValueFactory(cellData -> {
			Etudiant membre1 = cellData.getValue().getMembre1();
			if (membre1 != null) {
				return new SimpleStringProperty(membre1.getNom() + " " + membre1.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_Etudiant2.setCellValueFactory(cellData -> {
			Etudiant membre2 = cellData.getValue().getMembre2();
			if (membre2 != null) {
				return new SimpleStringProperty(membre2.getNom() + " " + membre2.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_NomMatiere.setCellValueFactory(cellData -> {
			Projet projet = cellData.getValue().getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getNomMatiere());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_Sujtprojet.setCellValueFactory(cellData -> {
			Projet projet = cellData.getValue().getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getSujet());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_DateRemiseProjet.setCellValueFactory(cellData -> {
			BinomeProjet binomeProjet = cellData.getValue();
			java.util.Date dateRemise = binomeProjet.getDateRemiseEffective();

			if (dateRemise != null) {
				String formattedDate = newFormatter.format(dateRemise);
				return new SimpleObjectProperty<>(formattedDate);
			} else {
				return new SimpleStringProperty("_");
			}
		});

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
					String idBinome = Integer.toString(predData.getIdBinome());
					String etudiant1 = (predData.getMembre1() != null)
							? (predData.getMembre1().getNom() + " " + predData.getMembre1().getPrenom()).toLowerCase()
							: "";
					String etudiant2 = (predData.getMembre2() != null)
							? (predData.getMembre2().getNom() + " " + predData.getMembre2().getPrenom()).toLowerCase()
							: "";

					String nomMatiere = "";
					String sujetProjet = "";

					// Vérifier si le projet n'est pas null avant d'accéder à ses propriétés
					if (predData.getProjet() != null) {
						// Ajouter des vérifications supplémentaires pour les propriétés du projet
						if (predData.getProjet().getNomMatiere() != null) {
							nomMatiere = predData.getProjet().getNomMatiere().toLowerCase();
						}
						if (predData.getProjet().getSujet() != null) {
							sujetProjet = predData.getProjet().getSujet().toLowerCase();
						}
					}

					// Ajouter la condition pour vérifier quel filtre est sélectionné
					String selectedFilter = filtre_binome.getSelectionModel().getSelectedItem();

					// Vérifier si selectedFilter est null avant d'entrer dans la structure de
					// commutation
					if (selectedFilter != null) {
						switch (selectedFilter) {
						case "IdBinome":
							return idBinome.contains(searchKey);
						case "Nom Matiere":
							return nomMatiere.contains(searchKey);
						case "Sujet Projet":
							return sujetProjet.contains(searchKey);
						case "Etudiant1":
							return etudiant1.contains(searchKey);
						case "Etudiant2":
							return etudiant2.contains(searchKey);
						default:
							return false;
						}
					} else {
						// Retourne le résultat de la recherche sans filtre si selectedFilter est null
						return idBinome.contains(searchKey) || etudiant1.contains(searchKey)
								|| etudiant2.contains(searchKey) || nomMatiere.contains(searchKey)
								|| sujetProjet.contains(searchKey);
					}
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

		if (num < 0 || binome == null || binome.getMembre1() == null || binome.getProjet() == null) {
			return;
		}

		id_binome.setText(String.valueOf(binome.getIdBinome()));
		Id_Binome2.setText(String.valueOf(binome.getIdBinome()));

		Etudiant1List2.setPromptText(binome.getMembre1().getNom() + " " + binome.getMembre1().getPrenom());

		if (binome.getMembre2() != null) {
			Etudiant2List2.setPromptText(binome.getMembre2().getNom() + " " + binome.getMembre2().getPrenom());
		} else {
			Etudiant2List2.setPromptText("");
		}

		Projet2.setPromptText(binome.getProjet().getNomMatiere() + " - " + binome.getProjet().getSujet());

		// Convertir la date de java.util.Date à LocalDate pour le DatePicker
		java.util.Date dateRemiseEffectif = binome.getDateRemiseEffective();
		if (dateRemiseEffectif != null) {
			LocalDate localDateRemise = dateRemiseEffectif.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			DateRemise.setValue(localDateRemise);
		} else {
			// Gérer le cas où la date est null
			DateRemise.setValue(null);
		}
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
			fillProjetComboBox();
			fillEtudiantComboBox();

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
			fillProjetComboBox();
			fillEtudiantComboBox();
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
			fillProjetComboBox();
			fillEtudiantComboBox();
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
			fillProjetComboBox();
			fillEtudiantComboBox();

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

	// Refresh les donnees de tableau dans UI
	@FXML
	public void refreshBinomeTable() {
		try {
			fillEtudiantComboBox();
			fillProjetComboBox();
			addShowBinome();
			showAlert(AlertType.INFORMATION, "Refresh", "Les données ont été actualisées avec succès !");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Erreur lors du rafraîchissement des données " + e.getMessage());
		}
	}

	// Condition qui verefier c'est nom et prenom et formation ne sont pas vide dans
	// GUI
	public boolean isInputValid(String Etudiant1, String Projet) {

		if (Etudiant1.isEmpty() || Projet.isEmpty()) {

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
		ObservableList<String> binome = FXCollections.observableArrayList("Select", "IdBinome", "Etudiant1", "Etudian2",
				"Nom Matiere", "Sujet Projet");
		filtre_binome.setItems(binome);

	}

}
