package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class NoteController implements Initializable {

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
	private TableColumn<?, ?> col_Etudiant1NR;

	@FXML
	private TableColumn<?, ?> col_Etudiant2NR;

	@FXML
	private TableColumn<?, ?> col_IdbinomeNR;

	@FXML
	private TableColumn<?, ?> col_NomMatiereNR;

	@FXML
	private TableColumn<?, ?> col_SujetProjetNR;

	@FXML
	private TableColumn<?, ?> col_notRapportBinome;

	@FXML
	private TableView<?> tableviewBinomeNoteRapport;

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
	private AnchorPane tmp_tableshowNotes;

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
			tmp_tableshowNotes.setVisible(true);
			tmp_tableBinomeR.setVisible(false);
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
			tmp_tableBinomeR.setVisible(false);
			tmp_addNote.setVisible(false);
			tmp_supprimerNote.setVisible(false);
			tmp_tableshowNotes.setVisible(true);
			tmp_optionNote.setVisible(false);
		}
	}

	@FXML
	private void handleBtnSupprimerNote(ActionEvent event) {
		if (event.getSource() == btn_supprimerNote) {
			tmp_supprimerNote.setVisible(true);
			tmp_modifierNote.setVisible(false);
			tmp_showNotes.setVisible(false);
			tmp_tableBinomeR.setVisible(false);
			tmp_addNote.setVisible(false);
			tmp_tableshowNotes.setVisible(true);
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
		tmp_tableshowNotes.setVisible(false);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		showOptionNote();

	}

}
