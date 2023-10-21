package eu.dauphine.idd.projectmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import eu.dauphine.idd.projectmanager.model.Binome;
import eu.dauphine.idd.projectmanager.model.Etudiant;

import java.sql.SQLException;

public class BinomeDAO {
	private Connection connexion;

	public BinomeDAO(Connection connexion) {
		this.connexion = connexion;
	}

	// Méthode pour créer la table
	public void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS binomes (" + "id SERIAL PRIMARY KEY," + "id_projet INT,"
				+ "id_etudiant1 INT," + "id_etudiant2 INT," + "FOREIGN KEY (id_projet) REFERENCES projets(id),"
				+ "FOREIGN KEY (id_etudiant1) REFERENCES etudiants(id),"
				+ "FOREIGN KEY (id_etudiant2) REFERENCES etudiants(id)" + ")";
		try (Statement statement = connexion.createStatement()) {
			statement.executeUpdate(query);
		}
	}

	public void insert(Binome binome) throws SQLException {
		String query = "INSERT INTO binomes (numero, id_projet, id_etudiant1, id_etudiant2) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, binome.getProjet().getIdProjet());
			statement.setInt(2, binome.getMembre1().getIdEtudiant());
			statement.setInt(3, binome.getMembre2().getIdEtudiant());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					binome.setIdBinome(generatedKeys.getInt(1));
				}
			}
		}
	}

	public void delete(int binomeId) throws SQLException {
		String query = "DELETE FROM binomes WHERE id = ?";

		try (PreparedStatement statement = connexion.prepareStatement(query)) {
			statement.setInt(1, binomeId);
			statement.executeUpdate();
		}
	}

}