package eu.dauphine.idd.pm.controller;

import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.ProjetService;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
	private TableColumn<Projet, String> col_DateRemiseProjet;
	@FXML
	private TableColumn<Projet, String> col_SujetProjet;

	@FXML
	private TableColumn<Projet, String> col_idProjet;
	@FXML
	private ComboBox<String> filtre_Projet;

	private ProjetService projetS = ServiceFactory.getProjetService();

	@FXML
	public void addProjet() {
		try {
			String nomMatiere = NomMatiereP.getText();
			String sujet = SujetProjet.getText();
			// Convertir la date du DatePicker en java.sql.Date
			LocalDate localDate = dateRemisePorjet.getValue();
			java.sql.Date dateRemise = null;

			if (localDate != null) {
				dateRemise = java.sql.Date.valueOf(localDate);
			}
			// Check if required fields are not empty
			if (!isInputValid(nomMatiere, sujet, dateRemise)) {

				// Handle the case when some fields are empty
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");

			} else {

				// Call the ProjetService to add the project
				int result = projetS.createProjet(nomMatiere, sujet, dateRemise);

				// Handle the result
				switch (result) {
				case 0: // Success
					showAlert(AlertType.INFORMATION, "Success", "Projet ajouté avec succès !");
					addProjetShow();
					resetProjetField();
					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message", "Ce sujet de projet existe déjà pour cette matière");
					break;
				default:
					showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite lors de la création du projet");
					break;
				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite : " + e.getMessage());
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
			LocalDate localDate = dateRemisePorjet2.getValue();
			System.out.println(localDate.toString());
			java.sql.Date dateRemise = null;

			if (localDate != null) {
				dateRemise = java.sql.Date.valueOf(localDate);
			}
			Alert alert;
			if (!isInputValid(matiere, sujet, dateRemise)) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Êtes-vous sûr de vouloir mettre à jour le projet avec l'ID " + idProjetStr);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					projetS.updateProjet(Integer.valueOf(idProjetStr), matiere, sujet, dateRemise);

					showAlert(AlertType.INFORMATION, "Information Message", "Projet mis à jour avec succès !");

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
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText("Êtes-vous sûr de vouloir supprimer le projet avec l'ID " + idProjetStr);
				Optional<ButtonType> option = alert.showAndWait();
				if (option.get().equals(ButtonType.OK)) {
					projetS.deleteProjetById(Integer.valueOf(idProjetStr));
					showAlert(AlertType.INFORMATION, "Information Message", "Projet supprimé avec succès !");

					addProjetShow();
					resetProjetField();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void searchProjet() {
		if (search_projet != null) {
			FilteredList<Projet> filter = new FilteredList<>(addprojet, e -> true);

			search_projet.textProperty().addListener((observable, oldValue, newValue) -> {
				filter.setPredicate(predData -> {
					if (newValue == null || newValue.isEmpty()) {
						return true;
					}

					String searchKey = newValue.toLowerCase();
					String idProjet = String.valueOf(predData.getIdProjet());
					String nomMatiere = predData.getNomMatiere().toLowerCase();
					String sujet = predData.getSujet().toLowerCase();

					// Ajouter la condition pour vérifier quel filtre est sélectionné
					String selectedFilter = filtre_Projet.getSelectionModel().getSelectedItem();

					if (selectedFilter != null && !selectedFilter.equals("Select")) {
						// Si le filtre est sélectionné, utilisez-le pour la recherche
						if ("IdProjet".equals(selectedFilter) && idProjet.contains(searchKey)) {
						
							return true;
						} else if ("Nom Matiere".equals(selectedFilter) && nomMatiere.contains(searchKey)) {
							
							return true;
						} else if ("Sujet Projet".equals(selectedFilter) && sujet.contains(searchKey)) {
						
							return true;
						} else {
							return false;
						}
					} else {
						// Si aucun filtre n'est sélectionné, utilisez la logique sans filtre
					
						return idProjet.contains(searchKey) || nomMatiere.contains(searchKey)
								|| sujet.contains(searchKey);
					}
				});
			});

			SortedList<Projet> sortedList = new SortedList<>(filter);
			sortedList.comparatorProperty().bind(tableProjet.comparatorProperty());

			tableProjet.setItems(sortedList);

		}
	}

	// Liste d'éléments de projet
	private ObservableList<Projet> addprojet;
	// Define SimpleDateFormat as a class member
	private SimpleDateFormat newFormatter = new SimpleDateFormat("dd-MM-yyyy");

	// Fonction qui affiche le tableau de projets dans l'interface graphique
	public void addProjetShow() {
		// Récupération de la liste des projets
		addprojet = projetS.listProjets();

		// Définition des cellules des colonnes

		col_idProjet.setCellValueFactory(new PropertyValueFactory<>("idProjet"));
		col_MatiereProjet.setCellValueFactory(new PropertyValueFactory<>("nomMatiere"));

		// Use a StringConverter to format the date
		col_DateRemiseProjet.setCellValueFactory(cellData -> {
			Projet projet = cellData.getValue();
			String formattedDate = newFormatter.format(projet.getDateRemiseRapport());
			return new SimpleObjectProperty<>(formattedDate);
		});

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

			// Convertir la date de java.util.Date à LocalDate pour le DatePicker
			java.util.Date dateRemise = projet.getDateRemiseRapport();
			if (dateRemise != null) {
				LocalDate localDateRemise = dateRemise.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dateRemisePorjet.setValue(localDateRemise);
				dateRemisePorjet2.setValue(localDateRemise);
			} else {
				// Gérer le cas où la date est null
				dateRemisePorjet.setValue(null);
				dateRemisePorjet2.setValue(null);
			}
		}
	}

	// Refresh les donnees de tableau dans UI
	@FXML
	public void refreshData() {
		try {
			addProjetShow();
			showAlert(AlertType.INFORMATION, "Refresh", "Les données ont été actualisées avec succès !");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Erreur lors du rafraîchissement des données " + e.getMessage());
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
	
			return false;
		}
		return true;
	}

	public void showOptionProjet() {
		tmp_addProjet.setVisible(false);
		tmpDeleteProjet.setVisible(true);
		tmp_updateProjet.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showOptionProjet();
		addProjetShow();
		ObservableList<String> projet = FXCollections.observableArrayList("Select", "IdProjet", "Nom Matiere",
				"Sujet Projet");

		filtre_Projet.setItems(projet);
	}

}
