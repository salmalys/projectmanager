package eu.dauphine.tmp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import eu.dauphine.idd.pm.model.Etudiant;

import java.sql.SQLException;

public class EtudiantDAOImpl2 {
	private Connection connexion;

	public EtudiantDAOImpl2(Connection connexion) {
		this.connexion = connexion;
	}

	public EtudiantDAOImpl2() {
		// TODO Auto-generated constructor stub
	}

	// M�thode pour cr�er la table
	public void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS etudiants (" + "id SERIAL PRIMARY KEY,"
				+ "nom VARCHAR(255) NOT NULL," + "prenom VARCHAR(255) NOT NULL," + "id_formation INT,"
				+ "FOREIGN KEY (id_formation) REFERENCES formations(id)" + ")";
		try (Statement statement = connexion.createStatement()) {
			statement.executeUpdate(query);
		}
	}

	public void insert(Etudiant etudiant) throws SQLException {
		String query = "INSERT INTO etudiants (nom, prenom, id_formation) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, etudiant.getNom());
			statement.setString(2, etudiant.getPrenom());
			statement.setInt(3, etudiant.getFormation().getIdFormation());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					etudiant.setIdEtudiant(generatedKeys.getInt(1));
				}
			}
		}
	}

	public void delete(int etudiantId) throws SQLException {
		String query = "DELETE FROM etudiants WHERE id = ?";

		try (PreparedStatement statement = connexion.prepareStatement(query)) {
			statement.setInt(1, etudiantId);
			statement.executeUpdate();
		}
	}

}
