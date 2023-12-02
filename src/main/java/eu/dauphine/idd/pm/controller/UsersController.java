package eu.dauphine.idd.pm.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javafx.collections.ObservableList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Notes;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.BinomeProjetService;
import eu.dauphine.idd.pm.service.NotesService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UsersController implements Initializable {
	@FXML
	private Button btn_affichernote;

	@FXML
	private Button btn_affichernote1;

	@FXML
	private Button btn_backNote2;
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
	private TableColumn<BinomeProjet, String> col_dateRemisProjet;
	@FXML
	private TableView<BinomeProjet> tableviewBinomeNoteRapport;

	@FXML
	private ComboBox<String> filtre_note;

	@FXML
	private TextField id_binome;

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
	private TextField search_note;

	@FXML
	private AnchorPane tmp_optionNote;

	@FXML
	private AnchorPane tmp_showNotes;

	@FXML
	private AnchorPane tmp_tableBinomeR;
	@FXML
	private Button close;
	@FXML
	private AnchorPane main_form;

	@FXML
	private Button logout;
	@FXML
	private Button minimize;

	@FXML
	private Label username;
	private NotesService NotesS = ServiceFactory.getNotesService();
	private BinomeProjetService binomeS = ServiceFactory.getBinomeProjetService();

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

	@FXML
	public void selectBinome() {
		ObservableList<Notes> notesList = NotesS.listNotes();
		BinomeProjet selectedBinome = tableviewBinomeNoteRapport.getSelectionModel().getSelectedItem();
		Notes matchingNote = findNoteForBinome(selectedBinome, notesList);
		if (selectedBinome != null && matchingNote != null) {

			id_binome.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomeAffiche.setText(String.valueOf(selectedBinome.getIdBinome()));

		}
		if (matchingNote == null) {
			id_binome.setText(String.valueOf(selectedBinome.getIdBinome()));
			id_binomeAffiche.setText(String.valueOf(selectedBinome.getIdBinome()));
		}

	}

	@FXML
	void ClearShowNote() {
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
	private void handleBtnAfficherNote(ActionEvent event) {
		if (event.getSource() == btn_affichernote) {
			tmp_showNotes.setVisible(true);

			tmp_tableBinomeR.setVisible(true);

			tmp_optionNote.setVisible(false);
		}
	}

	@FXML
	private void handleBtnBackNote(ActionEvent event) {
		// Ajouter les conditions pour chaque bouton de retour
		if (event.getSource() == btn_backNote2) {
			showOptionNote();

		}
	}

	public void showOptionNote() {
		tmp_optionNote.setVisible(true);

		tmp_showNotes.setVisible(false);
		tmp_tableBinomeR.setVisible(true);
	}

	@FXML
	private void handleDetailsButton() {
		String idBinome = id_binomeAffiche.getText();
		Notes note = NotesS.findNoteForBinome(Integer.valueOf(idBinome));

		if (note == null) {
			showAlert(AlertType.ERROR, " Message",
					"Ce binôme n'a pas de notes.\n\nVeuillez saisir des notes pour consulter le détail");
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

		SimpleDateFormat formateur = new SimpleDateFormat("dd-MM-yyyy");
		String dateFormateeEff = formateur.format(dateRemiseEffective);
		String dateFormateeProj = formateur.format(dateRemiseProjet);

		// Calcul de la pénalité
		int penalite = calculerPenalite(dateRemiseProjet, dateRemiseEffective);
		if (!nomPrenom2.equals("")) {
			// Formater la chaîne avec les informations nécessaires
			return "\nMatière : " + nomMatiere + "\n" + "Sujet du projet : " + sujetProjet + "\n\n"
					+ "Date de remise prévue :\t" + dateFormateeProj + "\n" + "Date de remise du binôme :\t"
					+ dateFormateeEff + "\n\n" + "Détails des notes :\n\n" + "Étudiant 1 : " + nomPrenom1
					+ "\nNote Soutenance :\t" + noteSoutenance1 + "\nNote Rapport : " + noteRapport + "\nNote Finale : "
					+ noteFinal1 + "\n\nÉtudiant 2 : " + nomPrenom2 + "\nNote Soutenance : " + noteSoutenance2
					+ "\nNote Rapport : " + noteRapport + "\nNote Finale : " + noteFinal2 + "\n\nPénalité : - "
					+ penalite
					+ "\nPour chaque jour de retard, un point est retiré à la note finale de chaque étudiant.";

		} else {
			return "\nMatière : " + nomMatiere + "\nProjet : " + sujetProjet + "\n\nDate de remise prévue :\t"
					+ dateFormateeProj + "\n" + "Date de remise effective par l'étudiant :\t" + dateFormateeEff + "\n\n"
					+ "Détails des notes :\n\n" + "Étudiant 1 : " + nomPrenom1 + "\nNote Soutenance : "
					+ noteSoutenance1 + "\nNote Rapport : " + noteRapport + "\nNote Finale : " + noteFinal1
					+ "\n\nPénalité : - " + penalite
					+ "\nPour chaque jour de retard, un point est retiré à la note finale de chaque étudiant.";

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
	public void refreshData() {
		try {
			addShowNote();
			showAlert(AlertType.INFORMATION, "Refresh", "Les données ont été actualisées avec succès !");
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Erreur lors du rafraîchissement des données " + e.getMessage());
		}
	}

	// Define SimpleDateFormat as a class member
	private SimpleDateFormat newFormatter = new SimpleDateFormat("dd-MM-yyyy");
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
		col_dateRemisProjet.setCellValueFactory(cellData -> {
			BinomeProjet binomeProjet = cellData.getValue();
			java.util.Date dateRemise = binomeProjet.getDateRemiseEffective();

			if (dateRemise != null) {
				String formattedDate = newFormatter.format(dateRemise);
				return new SimpleObjectProperty<>(formattedDate);
			} else {
				return new SimpleStringProperty("_");
			}
		});
		// Ajouter les données à la table
		tableviewBinomeNoteRapport.setItems(BinomesList);
	}

	// Fonction pour trouver la note correspondante pour un binôme donné
	private Notes findNoteForBinome(BinomeProjet binome, ObservableList<Notes> notesList) {
		return notesList
				.filtered(note -> note.getBinomeProjet().getIdBinome() == binome.getIdBinome()
						&& note.getBinomeProjet().getProjet().getIdProjet() == binome.getProjet().getIdProjet())
				.stream().findFirst().orElse(null);
	}

	// Affiche les fenetre d'alerte specifique pour chaque cas
	public void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private double x = 0;
	private double y = 0;

	// fonction logout permet de revenir a la scene parent, apres deconnexion
	@FXML
	public void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Message");
		alert.setHeaderText(null);
		alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");
		Optional<ButtonType> option = alert.showAndWait();
		try {
			if (option.get().equals(ButtonType.OK)) {
				logout.getScene().getWindow().hide();
				Parent root = FXMLLoader.load(getClass().getResource("/fxml/Main.fxml"));
				Stage stage = new Stage();
				Scene scene = new Scene(root);

				root.setOnMousePressed((MouseEvent event) -> {
					x = event.getSceneX();
					y = event.getSceneY();

				});
				root.setOnMouseDragged((MouseEvent event) -> {
					stage.setX(event.getScreenX() - x);
					stage.setY(event.getScreenY() - y);
					stage.setOpacity(.8);

				});
				root.setOnMouseReleased((MouseEvent event) -> {
					stage.setOpacity(1);
				});
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.setScene(scene);
				stage.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	public void updateDateRemiseProjet() {
		try {
			String idBinomeString = id_binome.getText();

			if (idBinomeString.isEmpty()) {
				showAlert(AlertType.ERROR, "Error Message", "Remplissez tous les champs");
			} else {
				LocalDate localDate = LocalDate.now();

				java.sql.Date dateRemise = null;
				if (localDate != null) {
					dateRemise = java.sql.Date.valueOf(localDate);
				}

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Message");
				alert.setHeaderText(null);
				alert.setContentText(
						"Êtes-vous sûr de vouloir mettre à jour la date de remise pour le binôme avec l'ID "
								+ idBinomeString);

				Optional<ButtonType> option = alert.showAndWait();
				if (option.isPresent() && option.get().equals(ButtonType.OK)) {
					binomeS.updateDateRemise(Integer.valueOf(idBinomeString), dateRemise);
					addShowNote();
					showAlert(AlertType.INFORMATION, "Information Message", "Date de remise mise à jour avec succès !");
					clearIdbinome();

				}
			}
		} catch (Exception e) {
			showAlert(AlertType.ERROR, "Error", "Une erreur s'est produite : " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void clearIdbinome() {
		id_binome.clear();
		id_binomeAffiche.clear();
	}

	@FXML
	public void close() {
		System.exit(0);
	}

	@FXML
	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	public void Affichersername() {
		username.setText(Data.username);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Affichersername();
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
