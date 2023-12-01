package eu.dauphine.idd.pm.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	@FXML
	private TextField search_note;
	@FXML
	private ComboBox<String> filtre_note;
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
			double noteS2;
			BinomeProjet b = binomeS.getBinomeProjetById(idBinome);

			// CAS OU IL Y A UN SEUL MEMBRE
			if (b.getMembre2() == null) {
				if (NoteSetudiant2Add.getText() != "") {
					showAlert(AlertType.ERROR, "Error Message",
							"This projet has no second member.\nPlease don't enter notes in the field : \n\"Note Soutenance Membre 2\".");
				} else {

					if (!isInputValid(id_binomeAdd.getText(), NoteRapportAdd.getText(), NoteSetudiant1Add.getText())) {
						showAlert(AlertType.ERROR, "Error Message", "Please enter valid numeric values.");
					} else {

						int result = NotesS.createNotes(idBinome, noteRapport, noteS1, -1);
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
									"Selected binome hasn't deliver the project yet.\nYou can't enter notes.");
							break;
						default:
							showAlert(AlertType.ERROR, "Error Message", "An unexpected error occurred.");
							break;
						}
					}

				}
			} else { // CAS OU IL Y A 2 PERSONNES
				noteS2 = Double.parseDouble(NoteSetudiant2Add.getText());
				if (!isInputValid(id_binomeAdd.getText(), NoteRapportAdd.getText(), NoteSetudiant1Add.getText())) {
					showAlert(AlertType.ERROR, "Error Message", "Please enter valid numeric values.");
				} else {
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
								"Selected binome hasn't deliver the project yet.\nYou can't enter notes.");
						break;
					default:
						showAlert(AlertType.ERROR, "Error Message", "An unexpected error occurred.");
						break;
					}
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
			double noteSoutenance2;

			int id_note = NotesS.findNoteForBinome(idBinome).getId();

			BinomeProjet b = binomeS.getBinomeProjetById(idBinome);
			if (b.getMembre2() == null) {
				if (!noteS2_mod.getText().equals("")) {
					showAlert(AlertType.ERROR, "Error Message",
							"This projet has no second member.\nPlease don't enter notes in the field : \n\"Note Soutenance Membre 2\".");
				} else {
					// Mettre à jour les notes pour une seul etudiant dans la base de données
					NotesS.updateNotes(id_note, idBinome, noteRapport, noteSoutenance1, -1);
					addShowNote();
				}
			} else {
				if (noteS2_mod.getText().equals("")) {
					showAlert(AlertType.ERROR, "Error Message",
							"This projet has a second member.\nPlease enter notes in the field : \n\"Note Soutenance Membre 2\".");
				} else {
					noteSoutenance2 = Double.parseDouble(noteS2_mod.getText());
					// Mettre à jour les notes pour deux etudians dans la base de données
					NotesS.updateNotes(id_note, idBinome, noteRapport, noteSoutenance1, noteSoutenance2);
					addShowNote();
				}
			}

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

			addShowNote();
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
	private void supprimeTousNotes() {
		ObservableList<Notes> notesList = NotesS.listNotes();

		if (!notesList.isEmpty()) {
			NotesS.deleteAll();
			addShowNote();

			showAlert(AlertType.INFORMATION, "Success", "All Notes Deleted successfully!");
		} else {
			showAlert(AlertType.INFORMATION, "Success", "Tous les notes sont deja supprimer!");

		}

	}

	private ObservableList<BinomeProjet> BinomesList;

	@FXML
	public void addShowNote() {
		BinomesList = binomeS.listBinomeProjets();
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

	@FXML
	public void searchNote() {
		// Création d'un filtre pour les notes
		FilteredList<BinomeProjet> filter = new FilteredList<>(BinomesList, n -> true);

		// Ajout d'un écouteur sur la propriété text de la barre de recherche
		search_note.textProperty().addListener((observable, oldValue, newValue) -> {
			filter.setPredicate(predData -> {
				if (newValue == null || newValue.isEmpty()) {
					// Si la recherche est vide, afficher toutes les notes
					return true;
				}

				String searchKey = newValue.toLowerCase();

				String idBibome = Integer.toString(predData.getIdBinome());
				String Etudiant1 = (predData.getMembre1() != null)
						? (predData.getMembre1().getNom() + " " + predData.getMembre1().getPrenom()).toLowerCase()
						: "";
				String Etudiant2 = (predData.getMembre2() != null)
						? (predData.getMembre2().getNom() + " " + predData.getMembre2().getPrenom()).toLowerCase()
						: "";
				String nomMatiere = (predData.getProjet() != null && predData.getProjet().getNomMatiere() != null)
						? predData.getProjet().getNomMatiere().toLowerCase()
						: "";
				String sujetProjet = (predData.getProjet() != null && predData.getProjet().getSujet() != null)
						? predData.getProjet().getSujet().toLowerCase()
						: "";

				// Ajouter la condition pour vérifier quel filtre est sélectionné
				String selectedFilter = filtre_note.getSelectionModel().getSelectedItem();

				if (selectedFilter != null && !selectedFilter.equals("Select")) {
					// Si le filtre est sélectionné, utilisez-le pour la recherche
					switch (selectedFilter) {
					case "IdBinome":
						return idBibome.contains(searchKey);
					case "Etudiant1":
						return Etudiant1.contains(searchKey);
					case "Etudian2":
						return Etudiant2.contains(searchKey);
					case "Nom Matiere":
						return nomMatiere.contains(searchKey);
					case "Sujet Projet":
						return sujetProjet.contains(searchKey);

					default:
						return false;
					}
				} else {
					// Si aucun filtre n'est sélectionné, utilisez la logique sans filtre
					return idBibome.contains(searchKey) || Etudiant1.contains(searchKey)
							|| Etudiant2.contains(searchKey) || nomMatiere.contains(searchKey)
							|| sujetProjet.contains(searchKey);

				}
			});

			// Création d'une liste triée à partir du filtre
			SortedList<BinomeProjet> sortedList = new SortedList<>(filter);

			// Liaison du comparateur de la liste triée avec le comparateur de la table
			sortedList.comparatorProperty().bind(tableviewBinomeNoteRapport.comparatorProperty());

			// Mise à jour des éléments de la table avec la liste triée
			tableviewBinomeNoteRapport.setItems(sortedList);
		});
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

			// Appeler cette fonction de calcul de la note finale
			Notes binomeNote = NotesS.findNoteForBinome(selectedBinome.getIdBinome());

			if (binomeNote != null) {
				double[] notesFinales = NotesS.calculNoteFinale(binomeNote.getId());

				// Afficher les notes finales dans les TextField
				noteF1.setText(String.valueOf(notesFinales[0]));
				if (selectedBinome.getMembre2() != null) {
					noteS2.setText(String.valueOf(binomeNote.getNoteSoutenanceMembre2()));
					noteF2.setText(String.valueOf(notesFinales[1]));
				}

				noteS1.setText(String.valueOf(binomeNote.getNoteSoutenanceMembre1()));

				note_RapportAffiche.setText(String.valueOf(binomeNote.getNoteRapport()));
			}
		}
	}

	@FXML
	public void ClearShowNote() {
		id_binomeAffiche.clear();
		nomPrenomAffiche1.clear();
		nomPrenomAffiche2.clear();
		noteF1.clear();
		noteF2.clear();
		noteS1.clear();
		noteS2.clear();
		note_RapportAffiche.clear();

	}

	@FXML
	private void handleDetailsButton() {
		String idBinome = id_binomeAffiche.getText();
		Notes note = NotesS.findNoteForBinome(Integer.valueOf(idBinome));

		if (note == null) {
			showAlert(AlertType.ERROR, " Message", "Binome has no note");
		} else {

			// Récupérez les détails que vous souhaitez afficher
			String details = getDetailsFromNotes(); // Vous devez implémenter cette méthode

			// Appelez la fonction pour afficher les détails dans une boîte de dialogue
			showDetailsAlert(details);
		}
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

		BinomeProjet BProjet = binomeS.getBinomeProjetById(Integer.valueOf(idBinome));
		Date dateRemiseProjet = BProjet.getProjet().getDateRemiseRapport();
		Date dateRemiseEffective = BProjet.getDateRemiseEffective();
		String nomMatiere = BProjet.getProjet().getNomMatiere();
		String sujetProjet = BProjet.getProjet().getSujet();

		// Calcul de la pénalité
		int penalite = calculerPenalite(dateRemiseProjet, dateRemiseEffective);
		if (!nomPrenom2.equals("")) {
			// Formater la chaîne avec les informations nécessaires
			return "Détails des notes :\n\n\n" + "Étudiant 1 : " + nomPrenom1 + ", Note Soutenance : " + noteSoutenance1
					+ ", Note Rapport : " + noteRapport + ", Note Finale :" + noteFinal1 + "\n" + "Étudiant 2 : "
					+ nomPrenom2 + ", Note Soutenance : " + noteSoutenance2 + ", Note Rapport : " + noteRapport
					+ ", Note Finale : " + noteFinal2 + "\n" + "Matière : " + nomMatiere + "\n" + "Projet : "
					+ sujetProjet + "\n" + "Date Remise Projet : " + dateRemiseProjet + "\n"
					+ "Date Remise Effective : " + dateRemiseEffective + "\n" + "Pénalité : -" + penalite
					+ " pour chaque jour de retard.";

		} else {
			return "Détails des notes :\n\n\n" + "Étudiant 1 : " + nomPrenom1 + ", Note Soutenance : " + noteSoutenance1
					+ ", Note Rapport : " + noteRapport + ", Note Finale :" + noteFinal1 + "\n" + "Matière : "
					+ nomMatiere + "\n" + "Projet : " + sujetProjet + "\n" + "Date Remise Projet : " + dateRemiseProjet
					+ "\n" + "Date Remise Effective : " + dateRemiseEffective + "\n\n" + "Pénalité : -" + penalite
					+ " pour chaque jour de retard.";

		}

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
		ObservableList<Notes> notesList = NotesS.listNotes();
		BinomeProjet selectedBinome = tableviewBinomeNoteRapport.getSelectionModel().getSelectedItem();
		Notes matchingNote = findNoteForBinome(selectedBinome, notesList);
		if (selectedBinome != null && matchingNote != null) {

			id_binomeAdd.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomeMod.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomSup.setText(String.valueOf(selectedBinome.getIdBinome()));
			noteR_mod.setText(String.valueOf(matchingNote.getNoteRapport()));
			noteS1_mod.setText(String.valueOf(matchingNote.getNoteSoutenanceMembre1()));
			if (selectedBinome.getMembre2() != null) {
				noteS2_mod.setText(String.valueOf(matchingNote.getNoteSoutenanceMembre2()));
				noteS2Sup.setText(String.valueOf(matchingNote.getNoteSoutenanceMembre2()));
			} else {
				noteS2_mod.setText("");
				noteS2Sup.setText("");
			}
			noteS1Sup.setText(String.valueOf(matchingNote.getNoteSoutenanceMembre1()));

			noteRSup.setText(String.valueOf(matchingNote.getNoteRapport()));

		} else {
			if (matchingNote == null) {
				noteS1_mod.clear();
				noteS2_mod.clear();
				noteR_mod.clear();
				noteS1Sup.clear();
				noteS2Sup.clear();
				noteRSup.clear();

				id_binomeAdd.setText(String.valueOf(selectedBinome.getIdBinome()));
				id_binomeMod.setText(String.valueOf(selectedBinome.getIdBinome()));
				id_binomSup.setText(String.valueOf(selectedBinome.getIdBinome()));
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
	public void generatePdf() {
		try {
			// Récupérez la liste de binômes depuis binomeS.listBinomeProjets()
			ObservableList<BinomeProjet> binomesList = binomeS.listBinomeProjets();
			ObservableList<Notes> notesList = NotesS.listNotes();
			String projectFolderPath = "./src/main/resources/pdf/";
			String fileName = "NoteEtudiant.pdf";
			String filePath = projectFolderPath + File.separator + fileName;
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);

			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

			// Ajoutez les en-têtes du tableau pour la première partie
			contentStream.beginText();
			contentStream.newLineAtOffset(100, 750);
			contentStream.showText("ID Binome");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Etudiant 1");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Etudiant 2");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Nom Matiere");
			contentStream.endText();

			// Ajoutez les en-têtes du tableau pour la deuxième partie
			contentStream.beginText();
			contentStream.newLineAtOffset(300, 750);
			contentStream.showText("Sujet Projet");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Note Rapport");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Note Soutenance Membre 1");
			contentStream.newLineAtOffset(100, 0);
			contentStream.showText("Note Soutenance Membre 2");
			contentStream.endText();

			int yPosition = 700;
			int xOffset = 100; // Ajustez cet offset pour l'espace entre chaque colonne

			for (BinomeProjet binome : binomesList) {
				if (binome.getProjet() != null && binome.getProjet().getNomMatiere() != null) {
					contentStream.beginText();
					contentStream.newLineAtOffset(xOffset, yPosition -= 20);
					contentStream.showText(String.valueOf(binome.getIdBinome()));
					contentStream.newLineAtOffset(xOffset, 0);
					contentStream.showText(binome.getMembre1().getNom() + " " + binome.getMembre1().getPrenom());
					contentStream.newLineAtOffset(xOffset, 0);
					contentStream.showText(binome.getMembre2() != null
							? binome.getMembre2().getNom() + " " + binome.getMembre2().getPrenom()
							: "_");
					contentStream.newLineAtOffset(xOffset, 0);
					contentStream.showText(binome.getProjet().getNomMatiere());
					contentStream.endText();

					contentStream.beginText();
					contentStream.newLineAtOffset(xOffset + 300, yPosition); // Ajustez cet offset pour aligner avec la
																				// deuxième partie
					contentStream.showText(binome.getProjet().getSujet());
					contentStream.newLineAtOffset(100, 0);

					Notes matchingNote = findNoteForBinome(binome, notesList);

					if (matchingNote != null) {
						double noteRapport = matchingNote.getNoteRapport();
						double noteSoutenance1 = matchingNote.getNoteSoutenanceMembre1();
						double noteSoutenance2 = matchingNote.getNoteSoutenanceMembre2();

						contentStream.showText(String.valueOf(noteRapport));
						contentStream.newLineAtOffset(100, 0);
						contentStream.showText(String.valueOf(noteSoutenance1));
						contentStream.newLineAtOffset(100, 0);
						contentStream.showText(String.valueOf(noteSoutenance2));
					}

					contentStream.endText();
				}

			}
			contentStream.close();

			// Enregistrez le document PDF
			String absoluteFilePath = new File(filePath).getAbsolutePath();
			document.save(absoluteFilePath);
			document.close();
			showAlert(AlertType.INFORMATION, "Print", "Data Printed successfully!");
		} catch (IOException e) {
			showAlert(AlertType.ERROR, "Error", "Failed to refresh data: " + e.getMessage());
		}
	}

	// Fonction pour trouver la note correspondante pour un binôme donné
	private Notes findNoteForBinome(BinomeProjet binome, ObservableList<Notes> notesList) {
		return notesList
				.filtered(note -> note.getBinomeProjet().getIdBinome() == binome.getIdBinome()
						&& note.getBinomeProjet().getProjet().getIdProjet() == binome.getProjet().getIdProjet())
				.stream().findFirst().orElse(null);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showOptionNote();
		addShowNote();

		tableviewBinomeNoteRapport.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {
					ClearShowNote();
					if (newSelection != null) {
						handleTableSelection();
					}

				});
		ObservableList<String> binome = FXCollections.observableArrayList("Select", "IdBinome", "Etudiant1", "Etudian2",
				"Nom Matiere", "Sujet Projet");
		filtre_note.setItems(binome);

	}

}