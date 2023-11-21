package eu.dauphine.idd.pm.controller;

import java.net.URL;
import java.sql.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.model.Projet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {

	/**
	 * *Partie 0: ************************ButtonHome****************************
	 * 
	 **/

	@FXML
	private Label Count_totaletudiant;
	@FXML
	private Label count_apresprojet;

	@FXML
	private Label count_avantprojet;

	@FXML
	private Label count_totalbinome;

	@FXML
	private Label count_totalprojet;
	@FXML
	private Button home_btn;

	@FXML
	private BarChart<?, ?> home_chart;
	@FXML
	private AnchorPane tmp_home;

	/**
	 * *Partie 1:
	 * ************************ButtonFormation****************************
	 * 
	 **/

	@FXML
	private Button AddFormation;

	@FXML
	private Button Clearformation;

	@FXML
	private Button Clearformation2;

	@FXML
	private TextField IdFormation;

	@FXML
	private TextField Nomformation;

	@FXML
	private ComboBox<String> PromotionList;

	@FXML
	private TextField Nomformation2;

	@FXML
	private ComboBox<String> PromotionList2;

	@FXML
	private Button RemoveFormation;

	@FXML
	private Button Updateformation;

	@FXML
	private TableColumn<Formation, Integer> col_Idformation;

	@FXML
	private TableColumn<Formation, String> col_Nomformation;

	@FXML
	private TableColumn<Formation, String> col_promotion;

	@FXML
	private ComboBox<?> filtre_formation;

	@FXML
	private Button formation_btn;

	@FXML
	private TextField search_formation;

	@FXML
	private TableView<Formation> tableFormation;

	@FXML
	private AnchorPane temp_formation;

	@FXML
	private Button btn_tmpadd;

	@FXML
	private Button btn_tmpupdate;

	@FXML
	private AnchorPane tmp_updateformation;

	@FXML
	private AnchorPane tmp_addformation;

	@FXML
	private AnchorPane tmp_btnformation;

	@FXML
	private Button Back_formation;

	@FXML
	private Button Backformation2;

	@FXML
	private Button binome_btn;

	@FXML
	private Button etudiant_btn;

	@FXML
	private Button note_btn;

	@FXML
	private Button projet_btn;

	@FXML
	private AnchorPane tmp_note;

	@FXML
	private AnchorPane tmp_projet;

	@FXML
	private AnchorPane tmp_etudiant;

	@FXML
	private AnchorPane tmp_binome;

	@FXML
	private Button Printformation;

	private FormationController formationController = new FormationController();

	private double x = 0;
	private double y = 0;

	@FXML
	public void AddFormation() {
		formationController.addFormation(IdFormation, Nomformation2, PromotionList2, search_formation, col_Idformation,
				col_Nomformation, col_promotion, tableFormation);
	}

	@FXML
	public void updateFormation() {
		formationController.updateFormation(IdFormation, Nomformation, PromotionList, search_formation, col_Idformation,
				col_Nomformation, col_promotion, tableFormation);
	}

	@FXML
	public void deleteFormation() {
		formationController.deleteFormation(IdFormation, Nomformation, PromotionList, search_formation, col_Idformation,
				col_Nomformation, col_promotion, tableFormation);
	}

	@FXML
	public void SearchFormation() {
		formationController.SearchFormation(search_formation, tableFormation);
	}

	@FXML
	public void refreshData() {
		formationController.refreshData(col_Idformation, col_Nomformation, col_promotion, tableFormation);
	}

	public void addformationshow() {
		formationController.addformationshow(col_Idformation, col_Nomformation, col_promotion, tableFormation);

	}

	public void selectFormation() {
		Formation formation = tableFormation.getSelectionModel().getSelectedItem();
		int num = tableFormation.getSelectionModel().getFocusedIndex();
		if ((num - 1) < -1) {
			return;
		}
		if (formation != null) {
			IdFormation.setText(String.valueOf(formation.getIdFormation()));
			Nomformation.setText(formation.getNom());
			Nomformation2.setText(formation.getNom());
		}

	}

	@FXML
	public void addformationReset() {
		formationController.addformationReset2(IdFormation, Nomformation, PromotionList);

	}

	@FXML
	public void addformationReset2() {
		formationController.addformationReset2(IdFormation, Nomformation2, PromotionList2);

	}

	@FXML
	public void addPromotionList() {
		formationController.addPromotionList(PromotionList);

	}

	@FXML
	public void addPromotionList2() {
		formationController.addPromotionList(PromotionList2);

	}

	@FXML
	public void tmpSwitch(ActionEvent event) {
		formationController.tmpSwitch(event, tmp_home, temp_formation, tmp_etudiant, tmp_binome, tmp_note, tmp_projet,
				home_btn, binome_btn, etudiant_btn, projet_btn, note_btn, formation_btn, btn_tmpadd, Back_formation,
				btn_tmpupdate, Backformation2, tmp_btnformation, PromotionList, search_formation, tableFormation,
				tmp_addformation, tmp_updateformation, IdFormation, Nomformation, Nomformation2, PromotionList2,
				tmp_addEtudiant, tmp_btnEtudiant, tmp_updateEtudiant, btn_tmpBackEtudient2, btn_tmpaddEtudient,
				btn_tmpbackEtudient, btn_tmpupdateEtudient);
	}

	/**
	 * *Partie 2: ************************ButtonEtudiant****************************
	 * 
	 **/
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

	private EtudiantController etudiantC = new EtudiantController();

	public void addEtudiant() {
//		etudiantC.addEtudiant(IdEtudiant, NomEtudiant, PrenomEtudiant, Formation, col_Idetudiant, col_NomEtudiant,
//				col_PrenomEtudiant, col_NomEtudiant, tableEtudiant);
	}

	private void fillFormationComboBox() {
		etudiantC.fillFormationComboBox(Formation, Formation2);
	}

	public void addEtudiantshow() {
//		etudiantC.addEtudiantShow(col_Idetudiant, col_NomEtudiant, col_PrenomEtudiant, col_NomformEtudiant,
//				col_PromotionEtudiant, tableEtudiant);

	}

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

	public void addEtudiantReset() {
		etudiantC.addEtudiantReset(IdEtudiant, NomEtudiant, PrenomEtudiant, Formation);
	}

	public void addEtudiantReset2() {
		etudiantC.addEtudiantReset(IdEtudiant, NomEtudiant2, PrenomEtudiant2, Formation2);
	}

	/**
	 * *Partie 3: ************************ButtonProjet****************************
	 * 
	 **/
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
	private Button btn_Add_projet;

	@FXML
	private Button btn_ajoutProjet;

	@FXML
	private Button btn_modifierProjet;

	@FXML
	private Button btn_supprimProjet;

	@FXML
	private Button btn_updateProjet;
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
	private TableColumn<Projet, Date> col_DateRemiseProjet;
	@FXML
	private TableColumn<Projet, String> col_SujetProjet;

	@FXML
	private TableColumn<Projet, String> col_idProjet;

	/**
	 * *Partie X: ************************Deconnexion et reglage de scene
	 * dashboard***********************
	 * 
	 **/

	@FXML
	private Button close;
	@FXML
	private AnchorPane main_form;
	@FXML
	private Label username;
	@FXML
	private Button logout;
	@FXML
	private Button minimize;

	// fonction logout permet de revenir a la scene parent, apres deconnexion
	@FXML
	public void logout() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Message");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");
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

	// recuperer Username quand on reussi a connecté et entrer dans dashbord pour
	// modifer label username dans UI Dashboard
	public void Affichersername() {
		username.setText(Data.username);
	}

	public void close() {
		System.exit(0);
	}

	public void minimize() {
		Stage stage = (Stage) main_form.getScene().getWindow();
		stage.setIconified(true);
	}

	/**
	 * *Partie UI:
	 * ************************InitialisationActionUI****************************
	 * 
	 **/
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		addformationshow();
		addEtudiantshow();
		addPromotionList();
		addPromotionList2();
		fillFormationComboBox();
		Affichersername();

	}

}
