package eu.dauphine.tmp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

import java.sql.SQLException;

public class FormationDAOImpl2 {
	private Connection connexion;

	public FormationDAOImpl2(Connection connexion) {
		this.connexion = connexion;
	}

	// M�thode pour cr�er la table
	public void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS formations (" + "id SERIAL PRIMARY KEY,"
				+ "nom VARCHAR(255) NOT NULL," + "promotion VARCHAR(255) NOT NULL" + ")";
		try (Statement statement = connexion.createStatement()) {
			statement.executeUpdate(query);
		}
	}
	public void insert(Formation formation) throws SQLException {
	    String query = "INSERT INTO formations (nom, promotion) VALUES (?, ?)";

	    try (PreparedStatement statement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	        statement.setString(1, formation.getNom());
	        statement.setString(2, formation.getPromotion());

	        statement.executeUpdate();

	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                formation.setIdFormation(generatedKeys.getInt(1));
	            }
	        }
	    }
	}
	public void delete(int formationId) throws SQLException {
        String query = "DELETE FROM formations WHERE id = ?";

        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setInt(1, formationId);
            statement.executeUpdate();
        }
    }

}