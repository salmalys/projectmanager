package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Notes;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.NotesService;
import eu.dauphine.idd.pm.service.ProjetService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
	private TableColumn<BinomeProjet, String> col_Etudiant1NR;

	@FXML
	private TableColumn<BinomeProjet, String> col_Etudiant2NR;

	@FXML
	private TableColumn<BinomeProjet, String> col_IdbinomeNR;

	@FXML
	private TableColumn<BinomeProjet, String> col_NomMatiereNR;

	@FXML
	private TableColumn<BinomeProjet, String> col_SujetProjetNR;

	@FXML
	private TableColumn<BinomeProjet, String> col_notRapportBinome;

	@FXML
	private TableView<BinomeProjet> tableviewBinomeNoteRapport;

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
	@FXML
	private TextField id_binomeMod;

	@FXML
	private TextField noteR_mod;

	@FXML
	private TextField noteS1_mod;

	@FXML
	private TextField noteS2_mod;
	@FXML
	private TextField noteS1Sup;
	@FXML
	private TextField noteRSup;
	@FXML
	private TextField noteS2Sup;
	@FXML
	private TextField id_binomSup;

	private NotesService NotesS = ServiceFactory.getNotesService();
	private BinomeProjetService binomeS = ServiceFactory.getBinomeProjetService();
	private ProjetService projetS = ServiceFactory.getProjetService();

	@FXML
	public void addNote() {
		try {
			// Récupérer l'ID du binôme depuis le champ
			int idBinome = Integer.parseInt(id_binomeAdd.getText());

			// Récupérer les notes depuis les champs
			double noteRapport = Double.parseDouble(NoteRapportAdd.getText());
			double noteS1 = Double.parseDouble(NoteSetudiant1Add.getText());
			double noteS2;
			if (NoteSetudiant2Add.getText() == "") {
				noteS2 = 0;
			} else {
				noteS2 = Double.parseDouble(NoteSetudiant2Add.getText());
			}
			if (!isInputValid(id_binomeAdd.getText(), NoteRapportAdd.getText(), NoteSetudiant1Add.getText())) {
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
					showAlert(AlertType.ERROR, "Error Message", "Selected binome doesn't exist");
					break;
				case 2:
					showAlert(AlertType.ERROR, "Error Message",
							"Selected binome hasn't deliver the project yet. You can't enter notes.");
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
	private void modifierNotes() {
		try {
			// Récupérer les valeurs depuis les champs de texte
			int idBinome = Integer.parseInt(id_binomeMod.getText());
			double noteRapport = Double.parseDouble(noteR_mod.getText());
			double noteSoutenance1 = Double.parseDouble(noteS1_mod.getText());
			double noteSoutenance2 = Double.parseDouble(noteS2_mod.getText());
			int id_note = NotesS.findNoteForBinome(idBinome).getId();
			// Mettre à jour les notes dans la base de données
			NotesS.updateNotes(id_note, idBinome, noteRapport, noteSoutenance1, noteSoutenance2);

			// Rafraîchir la table après la modification
			tableviewBinomeNoteRapport.refresh();

			// Effacer les champs de texte après la modification
			id_binomeMod.clear();
			noteR_mod.clear();
			noteS1_mod.clear();
			noteS2_mod.clear();

			// Afficher un message de succès ou utiliser une alerte pour informer
			// l'utilisateur
			System.out.println("Notes mises à jour avec succès.");

		} catch (NumberFormatException e) {
			// Gérer l'exception si la conversion échoue (par exemple, si l'utilisateur
			// entre du texte au lieu de nombres)
			System.out.println("Veuillez entrer des valeurs numériques valides pour les notes.");
		}
	}

	@FXML
	private void supprimerNote() {
		try {
			// Récupérer l'ID du binôme depuis le champ de texte
			int idBinome = Integer.parseInt(id_binomSup.getText());

			// Supprimer la note de la base de données
			NotesS.deleteNotesById(idBinome);

			// Rafraîchir la table après la suppression
			tableviewBinomeNoteRapport.refresh();

			// Effacer les champs de texte après la suppression
			id_binomSup.clear();
			noteRSup.clear();
			noteS1Sup.clear();
			noteS2Sup.clear();

			// Afficher un message de succès ou utiliser une alerte pour informer
			// l'utilisateur
			System.out.println("Note supprimée avec succès.");

		} catch (NumberFormatException e) {
			// Gérer l'exception si la conversion échoue (par exemple, si l'utilisateur
			// entre du texte au lieu d'un nombre)
			System.out.println("Veuillez entrer une valeur numérique valide pour l'ID du binôme.");
		}
	}

	@FXML
	public void addShowNote() {
		ObservableList<BinomeProjet> BinomesList = binomeS.listBinomeProjets();
		ObservableList<Notes> notesList = NotesS.listNotes();

		// Configurer les colonnes du TableView
		col_IdbinomeNR.setCellValueFactory(new PropertyValueFactory<>("idBinome"));
		col_Etudiant1NR.setCellValueFactory(cellData -> {
			Etudiant membre1 = cellData.getValue().getMembre1();
			if (membre1 != null) {
				return new SimpleStringProperty(membre1.getNom() + " " + membre1.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_Etudiant2NR.setCellValueFactory(cellData -> {
			Etudiant membre2 = cellData.getValue().getMembre2();
			if (membre2 != null) {
				return new SimpleStringProperty(membre2.getNom() + " " + membre2.getPrenom());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_NomMatiereNR.setCellValueFactory(cellData -> {
			Projet projet = cellData.getValue().getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getNomMatiere());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_SujetProjetNR.setCellValueFactory(cellData -> {
			Projet projet = cellData.getValue().getProjet();
			if (projet != null) {
				return new SimpleStringProperty(projet.getSujet());
			} else {
				return new SimpleStringProperty("_");
			}
		});
		col_notRapportBinome.setCellValueFactory(cellData -> {
			BinomeProjet binome = cellData.getValue();
			Notes matchingNote = findNoteForBinome(binome, notesList);

			if (matchingNote != null) {
				return new SimpleStringProperty(String.valueOf(matchingNote.getNoteRapport()));
			} else {
				return new SimpleStringProperty("_");
			}
		});
		// Ajouter les données à la table
		tableviewBinomeNoteRapport.setItems(BinomesList);
	}

	private Notes findNoteForBinome(BinomeProjet binomeProjet, List<Notes> notesList) {
		for (Notes note : notesList) {
			if (note.getBinomeProjet().getIdBinome() == binomeProjet.getIdBinome()) {

				return note;
			}
		}
		return null; // Retourne null si aucune note n'est trouvée pour le binôme
	}

	@FXML
	private void handleTableSelection() {
		// Récupérer l'élément sélectionné dans le TableView
		BinomeProjet selectedBinome = tableviewBinomeNoteRapport.getSelectionModel().getSelectedItem();

		if (selectedBinome != null) {
			// Afficher les informations dans les TextField correspondants
			id_binomeAffiche.setText(String.valueOf(selectedBinome.getIdBinome()));
			nomPrenomAffiche1
					.setText(selectedBinome.getMembre1().getNom() + " " + selectedBinome.getMembre1().getPrenom());
			if (selectedBinome.getMembre2() != null) {
				nomPrenomAffiche2
						.setText(selectedBinome.getMembre2().getNom() + " " + selectedBinome.getMembre2().getPrenom());
			}

			Notes binomeNote = NotesS.findNoteForBinome(selectedBinome.getIdBinome());

			if (binomeNote != null) {
				// Appeler cette fonction de calcul de la note finale
				double[] notesFinales = NotesS.calculNoteFinale(binomeNote.getId());
                
				// Afficher les notes finales dans les TextField
				noteF1.setText(String.valueOf(notesFinales[0]));

				noteF2.setText(String.valueOf(notesFinales[1]));
				noteS1.setText(String.valueOf(binomeNote.getNoteSoutenanceMembre1()));
				noteS2.setText(String.valueOf(binomeNote.getNoteSoutenanceMembre2()));
				note_RapportAffiche.setText(String.valueOf(binomeNote.getNoteRapport()));
			}
		}
	}

	@FXML
	private void handleDetailsButton() {
		// Récupérez les détails que vous souhaitez afficher
		String details = getDetailsFromNotes(); // Vous devez implémenter cette méthode

		// Appelez la fonction pour afficher les détails dans une boîte de dialogue
		showDetailsAlert(details);
	}

	private void showDetailsAlert(String details) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Détails des Notes");
		alert.setHeaderText(null);
		alert.setContentText(details);
		alert.showAndWait();
	}

	private String getDetailsFromNotes() {
		// Récupérer les valeurs depuis les champs texte
		String idBinome = id_binomeAffiche.getText();
		String nomPrenom1 = nomPrenomAffiche1.getText();
		String nomPrenom2 = nomPrenomAffiche2.getText();
		String noteSoutenance1 = noteS1.getText();
		String noteSoutenance2 = noteS2.getText();
		String noteRapport = note_RapportAffiche.getText();
		String noteFinal1 = noteF1.getText();
		String noteFinal2 = noteF2.getText();

		// Récupérer les autres informations nécessaires (à partir des bases de données,
		// etc.)
		// Replacez ces valeurs factices par les valeurs réelles que vous récupérez
		String nomMatiere = "Nom Matière";
		String sujetProjet = "Sujet Projet";

		BinomeProjet BProjet = binomeS.getBinomeProjetById(Integer.valueOf(idBinome));
		Date dateRemiseProjet = BProjet.getProjet().getDateRemiseRapport();
		Date dateRemiseEffective = BProjet.getDateRemiseEffective();

		// Calcul de la pénalité
		int penalite = calculerPenalite(dateRemiseProjet, dateRemiseEffective);

		// Formater la chaîne avec les informations nécessaires
		return "Détails des notes :\n\n\n" + "Étudiant 1 : " + nomPrenom1 + ", Note Soutenance : " + noteSoutenance1
				+ ", Note Rapport : " + noteRapport + ", Note Finale :" + noteFinal1 + "\n" + "Étudiant 2 : "
				+ nomPrenom2 + ", Note Soutenance : " + noteSoutenance2 + ", Note Rapport : " + noteRapport
				+ ", Note Finale : " + noteFinal2 + "\n" + "Matière : " + nomMatiere + "\n" + "Projet : " + sujetProjet
				+ "\n" + "Date Remise Projet : " + dateRemiseProjet + "\n" + "Date Remise Effective : "
				+ dateRemiseEffective + "\n" + "Pénalité : -" + penalite + " pour chaque jour de retard.";
	}

	private int calculerPenalite(Date dateRemiseProjet, Date dateRemiseEffective) {
		long differenceEnJours = calculerDifferenceEnJours(dateRemiseProjet, dateRemiseEffective);

		// Appliquer la pénalité de -1 pour chaque jour de retard
		return (int) Math.max(differenceEnJours, 0) * -1;
	}

	private long calculerDifferenceEnJours(Date dateRemiseProjet, Date dateRemiseEffective) {
		// Calculer la différence en jours
		long diffInMillis = dateRemiseEffective.getTime() - dateRemiseProjet.getTime();
		return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
	}

	@FXML
	public void selectBinome() {
		BinomeProjet selectedBinome = tableviewBinomeNoteRapport.getSelectionModel().getSelectedItem();

		if (selectedBinome != null) {

			id_binomeAdd.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomeMod.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomSup.setText(String.valueOf(selectedBinome.getIdBinome()));

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

	private boolean isInputValid(String idBinome, String noteRapport, String noteS1) {
		return isInteger(idBinome) && isDouble(noteRapport) && isDouble(noteS1);
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

	// Refresh les donnees de tableau dans UI
	@FXML
	public void refreshData() {
		try {
			addShowNote();
			showAlert(AlertType.INFORMATION, "Refresh", "Data refreshed successfully!");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
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

	public void showOptionNote() {
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
		// Associer cette fonction à l'événement de sélection du TableView
		tableviewBinomeNoteRapport.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					if (newSelection != null) {
						handleTableSelection();
					}
				});

	}

}
