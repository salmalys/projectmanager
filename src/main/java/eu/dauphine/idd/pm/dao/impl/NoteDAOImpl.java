package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.NoteDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class NoteDAOImpl implements NoteDAO {
	private static final String INSERT_NOTE = "INSERT INTO Notes(binome, etudiant, noteRapport, noteSoutenance, dateRemiseEffective) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_NOTE = "UPDATE Notes SET binome=?, etudiant=?, noteRapport=?, noteSoutenance=?, dateRemiseEffective=? WHERE id=?";
	private static final String DELETE_NOTE = "DELETE FROM Notes WHERE id=?";
	private static final String FIND_BY_ID = "SELECT * FROM Notes WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM Notes";

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void create(Note note) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NOTE,
						Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, note.getBinome().getIdBinome()); // Assumant que Binome a une m�thode getId()
			preparedStatement.setInt(2, note.getEtudiant().getIdEtudiant()); // Assumant que Etudiant a une m�thode
																				// getId()
			preparedStatement.setDouble(3, note.getNoteRapport());
			preparedStatement.setDouble(4, note.getNoteSoutenance());
			//A MODIFIER 
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					note.setId(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Note note) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NOTE)) {
			preparedStatement.setInt(1, note.getBinome().getIdBinome());
			preparedStatement.setInt(2, note.getEtudiant().getIdEtudiant());
			preparedStatement.setDouble(3, note.getNoteRapport());
			preparedStatement.setDouble(4, note.getNoteSoutenance());
			//A MODIFIER
			preparedStatement.setInt(6, note.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_NOTE)) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Note findById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				return mapToNote(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ObservableList<Note> findAll() {
		Connection connection = getConnection();
		ObservableList<Note> notes = FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(FIND_ALL)) {
			while (rs.next()) {
				notes.add(mapToNote(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notes;
	}

	private Note mapToNote(ResultSet rs) throws SQLException {
		Note note = new Note();
		note.setId(rs.getInt("id"));
		// Pour simplifier, je cr�e de nouveaux objets Binome et Etudiant uniquement
		// avec leurs IDs.
		// Dans une impl�mentation compl�te, vous souhaiterez peut-�tre r�cup�rer toutes
		// les informations
		// de ces objets depuis la base de donn�es.
		// note.setBinome(new Binome(rs.getInt("binome")));
		// note.setEtudiant(new Etudiant(rs.getInt("etudiant")));
		note.setNoteRapport(rs.getDouble("noteRapport"));
		note.setNoteSoutenance(rs.getDouble("noteSoutenance"));
		//A MODIFIER
		return note;
	}
	
	@Override
	public void delete(Note note) {
		deleteById(note.getId());
	}
}
