package eu.dauphine.idd.pm.controller;

import java.net.URL;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
	public void addShowBinome() {
		addBinome = binomeS.listBinomeProjets();

		col_idBinome.setCellValueFactory(new PropertyValueFactory<>("idBinome"));

		col_Etudiant1.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getMembre1().getNom() + " " + cellData.getValue().getMembre1().getPrenom()));
		col_Etudiant1
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMembre1().getNom()));
		col_Etudiant1.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getMembre1().getPrenom()));
		col_Etudiant2.setCellValueFactory(cellData -> new SimpleStringProperty(
				cellData.getValue().getMembre1().getNom() + " " + cellData.getValue().getMembre1().getPrenom()));
		col_Etudiant2
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMembre1().getNom()));
		col_Etudiant2.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getMembre1().getPrenom()));

		col_NomMatiere.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getProjet().getNomMatiere()));
		col_Sujtprojet
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProjet().getSujet()));

		col_DateRemiseProjet.setCellValueFactory(cellData -> new SimpleStringProperty(
		// Convertir la date au format souhaité (par exemple,
		// cellData.getValue().getDateRemiseProjet().toString())
		));

		tableBinome.setItems(addBinome);
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

		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		fillEtudiantComboBox();
		fillProjetComboBox();
		addShowBinome();
	}

}
