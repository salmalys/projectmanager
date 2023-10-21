package eu.dauphine.idd.projectmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import eu.dauphine.idd.projectmanager.model.Note;

import java.sql.SQLException;

public class NoteDAO {
	private Connection connexion;

	public NoteDAO(Connection connexion) {
		this.connexion = connexion;
	}

	// Méthode pour créer la table
	public void createTable() throws SQLException {
		String query = "CREATE TABLE IF NOT EXISTS notes (" + "id SERIAL PRIMARY KEY,"
				+ "note_rapport DOUBLE precision," + "note_soutenance DOUBLE precision," + "date_remise_effective DATE,"
				+ "id_binome INT," + "id_etudiant INT," + "FOREIGN KEY (id_binome) REFERENCES binomes(id),"
				+ "FOREIGN KEY (id_etudiant) REFERENCES etudiants(id)" +

				")";
		try (Statement statement = connexion.createStatement()) {
			statement.executeUpdate(query);
		}
	}

	public void insert(Note note) throws SQLException {
		String query = "INSERT INTO notes (note_rapport, note_soutenance, date_remise_effective, id_binome) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connexion.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setDouble(1, note.getNoteRapport());
			statement.setDouble(2, note.getNoteSoutenance());
			statement.setDate(3, new java.sql.Date(note.getDate_remise_effective().getTime()));
			statement.setInt(4, note.getBinome().getIdBinome());

			statement.executeUpdate();

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					note.setId(generatedKeys.getInt(1));
				}
			}
		}
	}

	public void delete(int noteId) throws SQLException {
		String query = "DELETE FROM notes WHERE id = ?";

		try (PreparedStatement statement = connexion.prepareStatement(query)) {
			statement.setInt(1, noteId);
			statement.executeUpdate();
		}
	}
}
