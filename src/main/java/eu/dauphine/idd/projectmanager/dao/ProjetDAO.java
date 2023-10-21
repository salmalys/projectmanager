package eu.dauphine.idd.projectmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import eu.dauphine.idd.projectmanager.model.Etudiant;
import eu.dauphine.idd.projectmanager.model.Projet;

import java.sql.SQLException;

public class ProjetDAO {
	private Connection connexion;

	public ProjetDAO(Connection connexion) {
		this.connexion = connexion;
	}

	// Méthode pour créer la table'
	public void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS projets (" + "id SERIAL PRIMARY KEY,"
				+ "nom_matiere VARCHAR(255) NOT NULL," + "sujet VARCHAR(255) NOT NULL," + "date_remise_prevue DATE"
				+ ")";
		try (Statement statement = connexion.createStatement()) {
			statement.executeUpdate(query);
		}
	}

	public void insert(Projet projet) throws SQLException {
		String query = "INSERT INTO projets (nom_matiere, sujet, date_remise_prevue) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, projet.getNomMatiere());
			statement.setString(2, projet.getSujet());
			statement.setDate(3, new java.sql.Date(projet.getDateRemiseRapport().getTime()));

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projet.setIdProjet(generatedKeys.getInt(1));
				}
			}
		}
	}

	public void delete(int projetId) throws SQLException {
		String query = "DELETE FROM projets WHERE id = ?";

		try (PreparedStatement statement = connexion.prepareStatement(query)) {
			statement.setInt(1, projetId);
			statement.executeUpdate();
		}
	}

}
