package eu.dauphine.idd.pm.controller;

import java.net.URL;

import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Notes;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.NotesService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class NoteController implements Initializable {

	@FXML
	private TextField id_binomeAdd;

	@FXML
	private TextField NoteRapportAdd;

	@FXML
	private TextField NoteSetudiant1Add;

	@FXML
	private TextField NoteSetudiant2Add;

	@FXML
	private Button btn_affichernote;

	@FXML
	private Button btn_backNote1;

	@FXML
	private Button btn_backNote2;

	@FXML
	private Button btn_backNote3;

	@FXML
	private Button btn_backNote4;

	@FXML
	private Button btn_modifierNote;

	@FXML
	private Button btn_saisirNote;

	@FXML
	private Button btn_supprimerNote;

	@FXML
	private TableColumn<Notes, String> col_Etudiant1NR;

	@FXML
	private TableColumn<Notes, String> col_Etudiant2NR;

	@FXML
	private TableColumn<Notes, String> col_IdbinomeNR;

	@FXML
	private TableColumn<Notes, String> col_NomMatiereNR;

	@FXML
	private TableColumn<Notes, String> col_SujetProjetNR;

	@FXML
	private TableColumn<Notes, Double> col_notRapportBinome;

	@FXML
	private TableView<Notes> tableviewBinomeNoteRapport;

	@FXML
	private AnchorPane tmp_addNote;

	@FXML
	private AnchorPane tmp_modifierNote;

	@FXML
	private AnchorPane tmp_optionNote;

	@FXML
	private AnchorPane tmp_showNotes;

	@FXML
	private AnchorPane tmp_supprimerNote;

	@FXML
	private AnchorPane tmp_tableBinomeR;


	@FXML
	private TextField id_binomeAffiche;

	@FXML
	private TextField nomPrenomAffiche1;

	@FXML
	private TextField nomPrenomAffiche2;

	@FXML
	private TextField noteF1;

	@FXML
	private TextField noteF2;

	@FXML
	private TextField noteS1;

	@FXML
	private TextField noteS2;

	@FXML
	private TextField note_RapportAffiche;

	private NotesService NotesS = ServiceFactory.getNotesService();
	private BinomeProjetService binomeS = ServiceFactory.getBinomeProjetService();

	@FXML
	public void addNote() {
		try {
			// Récupérer l'ID du binôme depuis le champ
			int idBinome = Integer.parseInt(id_binomeAdd.getText());

			// Récupérer les notes depuis les champs
			double noteRapport = Double.parseDouble(NoteRapportAdd.getText());
			double noteS1 = Double.parseDouble(NoteSetudiant1Add.getText());
			double noteS2 = Double.parseDouble(NoteSetudiant2Add.getText());
			if (!isInputValid(id_binomeAdd.getText(), NoteRapportAdd.getText(), NoteSetudiant1Add.getText(),
					NoteSetudiant2Add.getText())) {
				showAlert(AlertType.ERROR, "Error Message", "Please enter valid numeric values.");
			} else {

				// Ajouter les notes à la base de données
				int result = NotesS.createNotes(idBinome, noteRapport, noteS1, noteS2);

				switch (result) {
				case 0:
					showAlert(AlertType.INFORMATION, "Success", "Notes added successfully!");
					resetNoteFields();
					addShowNote();
					break;
				case 1:
					showAlert(AlertType.ERROR, "Error Message", "An error occurred while creating the notes.");
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
	public void addShowNote() {
		ObservableList<Notes> notesList = NotesS.listNotes();

		col_IdbinomeNR.setCellValueFactory(cellData -> {
			BinomeProjet binomeProjet = cellData.getValue().getBinomeProjet();
			if (binomeProjet != null) {
				return new SimpleStringProperty(String.valueOf(binomeProjet.getIdBinome()));
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_Etudiant1NR.setCellValueFactory(cellData -> {
			BinomeProjet binome = cellData.getValue().getBinomeProjet();
			Etudiant membre1 = binome.getMembre1();
			if (membre1 != null) {
				return new SimpleStringProperty(membre1.getNom() + " " + membre1.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_Etudiant2NR.setCellValueFactory(cellData -> {
			BinomeProjet binome = cellData.getValue().getBinomeProjet();
			Etudiant membre2 = binome.getMembre2();
			if (membre2 != null) {
				return new SimpleStringProperty(membre2.getNom() + " " + membre2.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_NomMatiereNR.setCellValueFactory(cellData -> {
			BinomeProjet binome = cellData.getValue().getBinomeProjet();
			Projet projet = binome.getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getNomMatiere());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_SujetProjetNR.setCellValueFactory(cellData -> {
			BinomeProjet binome = cellData.getValue().getBinomeProjet();
			Projet projet = binome.getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getSujet());
			} else {
				return new SimpleStringProperty("_");
			}
		});

		col_notRapportBinome.setCellValueFactory(new PropertyValueFactory<>("noteRapport"));
		tableviewBinomeNoteRapport.setItems(notesList);
	}

	@FXML
	public void selectBinome() {
		Notes selectedNote = tableviewBinomeNoteRapport.getSelectionModel().getSelectedItem();

		if (selectedNote != null) {
			BinomeProjet binome = selectedNote.getBinomeProjet();

			if (binome != null) {
				id_binomeAdd.setText(String.valueOf(binome.getIdBinome()));

			}
		}

	}

	private void resetNoteFields() {
		id_binomeAdd.clear();
		NoteRapportAdd.clear();
		NoteSetudiant1Add.clear();
		NoteSetudiant2Add.clear();
	}

	// Affiche les fenetre d'alerte specifique pour chaque cas
	public void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private boolean isInputValid(String idBinome, String noteRapport, String noteS1, String noteS2) {
		return isInteger(idBinome) && isDouble(noteRapport) && isDouble(noteS1) && isDouble(noteS2);
	}

	private boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@FXML
	private void handleBtnSaisirNote(ActionEvent event) {
		if (event.getSource() == btn_saisirNote) {
			tmp_addNote.setVisible(true);
			tmp_modifierNote.setVisible(false);
			tmp_supprimerNote.setVisible(false);
			tmp_showNotes.setVisible(false);
			tmp_optionNote.setVisible(false);

			tmp_tableBinomeR.setVisible(true);

		}
	}

	@FXML
	private void handleBtnAfficherNote(ActionEvent event) {
		if (event.getSource() == btn_affichernote) {
			tmp_showNotes.setVisible(true);
	
			tmp_tableBinomeR.setVisible(true);
			tmp_addNote.setVisible(false);
			tmp_modifierNote.setVisible(false);
			tmp_supprimerNote.setVisible(false);
			tmp_optionNote.setVisible(false);
		}
	}

	@FXML
	private void handleBtnModifierNote(ActionEvent event) {
		if (event.getSource() == btn_modifierNote) {

			tmp_modifierNote.setVisible(true);
			tmp_showNotes.setVisible(false);
			tmp_tableBinomeR.setVisible(true);
			tmp_addNote.setVisible(false);
			tmp_supprimerNote.setVisible(false);
	
			tmp_optionNote.setVisible(false);
		}
	}

	@FXML
	private void handleBtnSupprimerNote(ActionEvent event) {
		if (event.getSource() == btn_supprimerNote) {
			tmp_supprimerNote.setVisible(true);
			tmp_modifierNote.setVisible(false);
			tmp_showNotes.setVisible(false);
			tmp_tableBinomeR.setVisible(true);
			tmp_addNote.setVisible(false);
		
			tmp_optionNote.setVisible(false);
		}
	}

	@FXML
	private void handleBtnBackNote(ActionEvent event) {
		// Ajouter les conditions pour chaque bouton de retour
		if (event.getSource() == btn_backNote1) {
			showOptionNote();
		} else if (event.getSource() == btn_backNote2) {
			showOptionNote();
		} else if (event.getSource() == btn_backNote3) {
			showOptionNote();
		} else if (event.getSource() == btn_backNote4) {
			showOptionNote();
		}
	}

	private void showOptionNote() {
		tmp_optionNote.setVisible(true);
		tmp_addNote.setVisible(false);
		tmp_modifierNote.setVisible(false);
		tmp_supprimerNote.setVisible(false);
		tmp_showNotes.setVisible(false);
		tmp_tableBinomeR.setVisible(true);
	
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		showOptionNote();
		addShowNote();

	}

}
