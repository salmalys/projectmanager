package eu.dauphine.idd.pm.controller;

import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Optional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;

public class FormationController {
	private FormationService formationS = ServiceFactory.getFormationService();

	public void addFormation(TextField nomFormation, ComboBox<String> promotionList) {

		
	}

	public void updateFormation(TextField nomFormation, ComboBox<String> promotionList, TextField idFormation) {

	}

	public void deleteFormation(TextField idFormation) {

	}

	private boolean isInputValid(String nom, String promotion) {

		return true;
	}

	private void showAlert(AlertType alertType, String title, String content) {

	}

}
