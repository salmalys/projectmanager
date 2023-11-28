package eu.dauphine.idd.pm.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.service.UtilService;

public class UtilServiceImpl implements UtilService {
	
	private static final String COUNT_NBPROJET = "SELECT COUNT(ID_Projet) AS totalProjets FROM Projet";
	private static final String COUNT_NBETUDIANT = "SELECT COUNT(ID_Etudiant) AS totalEtudiants FROM Etudiant";
	private static final String COUNT_REMISEAVANTDATE = "SELECT COUNT(*) AS NombreProjetsRemisATemps FROM BinomeProjet BP JOIN Projet P ON BP.ID_Projet = P.ID_Projet WHERE SUBSTR(BP.Date_Remise_Effective, 7, 4) || '-' || SUBSTR(BP.Date_Remise_Effective, 4, 2) || '-' || SUBSTR(BP.Date_Remise_Effective, 1, 2) <= SUBSTR(P.Date_Remise_Prevue, 7, 4) || '-' || SUBSTR(P.Date_Remise_Prevue, 4, 2) || '-' || SUBSTR(P.Date_Remise_Prevue, 1, 2);";
	private static final String COUNT_REMISEAPRESDATE = "SELECT COUNT(*) AS NombreProjetsRemisRetard FROM BinomeProjet BP JOIN Projet P ON BP.ID_Projet = P.ID_Projet WHERE SUBSTR(BP.Date_Remise_Effective, 7, 4) || '-' || SUBSTR(BP.Date_Remise_Effective, 4, 2) || '-' || SUBSTR(BP.Date_Remise_Effective, 1, 2)  > SUBSTR(P.Date_Remise_Prevue, 7, 4) || '-' || SUBSTR(P.Date_Remise_Prevue, 4, 2) || '-' || SUBSTR(P.Date_Remise_Prevue, 1, 2);";

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public int getTotalEtudiants() {
		int totalEtudiants = 0;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(COUNT_NBETUDIANT);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			if (resultSet.next()) {
				totalEtudiants = resultSet.getInt("totalEtudiants");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return totalEtudiants;
	}

	@Override
	public int getTotalProjets() {
	    int totalProjets = 0;

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(COUNT_NBPROJET);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        if (resultSet.next()) {
	            totalProjets = resultSet.getInt("totalProjets");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return totalProjets;
	}
	
	@Override
	public int getNbprojetRemisAvantDate(){
	    int totalProjets = 0;

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(COUNT_REMISEAVANTDATE);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        if (resultSet.next()) {
	            totalProjets = resultSet.getInt("NombreProjetsRemisATemps");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return totalProjets;
	}
	
	@Override
	public int getNbprojetRemisApresDate(){
	    int totalProjets = 0;

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(COUNT_REMISEAPRESDATE);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        if (resultSet.next()) {
	            totalProjets = resultSet.getInt("NombreProjetsRemisRetard");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return totalProjets;
	}
	
}
