package eu.dauphine.idd.pm.service.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.service.UtilService;

public class UtilServiceImpl implements UtilService {

	private static final String COUNT_NBPROJET = "SELECT COUNT(ID_Projet) AS totalProjets FROM Projet";
	private static final String COUNT_NBETUDIANT = "SELECT COUNT(ID_Etudiant) AS totalEtudiants FROM Etudiant";
	private static final String COUNT_REMISEAVANTDATE = "SELECT COUNT(*) AS NombreProjetsRemisATemps FROM BinomeProjet BP JOIN Projet P ON BP.ID_Projet = P.ID_Projet WHERE SUBSTR(BP.Date_Remise_Effective, 7, 4) || '-' || SUBSTR(BP.Date_Remise_Effective, 4, 2) || '-' || SUBSTR(BP.Date_Remise_Effective, 1, 2) <= SUBSTR(P.Date_Remise_Prevue, 7, 4) || '-' || SUBSTR(P.Date_Remise_Prevue, 4, 2) || '-' || SUBSTR(P.Date_Remise_Prevue, 1, 2);";
	private static final String COUNT_REMISEAPRESDATE = "SELECT COUNT(*) AS NombreProjetsRemisRetard FROM BinomeProjet BP JOIN Projet P ON BP.ID_Projet = P.ID_Projet WHERE SUBSTR(BP.Date_Remise_Effective, 7, 4) || '-' || SUBSTR(BP.Date_Remise_Effective, 4, 2) || '-' || SUBSTR(BP.Date_Remise_Effective, 1, 2)  > SUBSTR(P.Date_Remise_Prevue, 7, 4) || '-' || SUBSTR(P.Date_Remise_Prevue, 4, 2) || '-' || SUBSTR(P.Date_Remise_Prevue, 1, 2);";
	private static final String COUNT_NBBINOME = "SELECT COUNT(ID_BinomeProjet) AS totalBinomes FROM BinomeProjet";
	private static final String AVG_BYPROJET = "SELECT p.Nom_Matiere, p.Sujet, AVG((n.Note_Rapport + n.Note_Soutenance_Etudiant1 + "
			+ "COALESCE(n.Note_Soutenance_Etudiant2, 0)) / (CASE WHEN n.Note_Soutenance_Etudiant2 IS NULL THEN 2 ELSE 3 END)) AS Moyenne FROM Projet"
			+ " p JOIN BinomeProjet bp ON p.ID_Projet = bp.ID_Projet JOIN Notes"
			+ " n ON bp.ID_BinomeProjet = n.ID_BinomeProjet " + "GROUP BY p.Nom_Matiere, p.Sujet";


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
	public int getNbprojetRemisAvantDate() {
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
	public int getNbprojetRemisApresDate() {
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

	@Override
	public int getNbBinome() {
		int totalBinome = 0;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(COUNT_NBBINOME);
				ResultSet resultSet = preparedStatement.executeQuery()) {

			if (resultSet.next()) {
				totalBinome = resultSet.getInt("totalBinomes");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return totalBinome;
	}

	@Override
	public HashMap<String, Double> getMoyenneParProjet() {
		HashMap<String, Double> moyennes = new HashMap<>();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(AVG_BYPROJET)) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String nomMatiere = rs.getString("Nom_Matiere");
				String sujet = rs.getString("Sujet");
				double moyenne = rs.getDouble("Moyenne");
				moyennes.put(nomMatiere + " - " + sujet, moyenne);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return moyennes;
	}

}
