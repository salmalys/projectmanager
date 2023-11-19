package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.BinomeProjetDAO;
import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.NotesDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Notes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class NotesDAOImpl implements NotesDAO {
	private static final String INSERT_NOTE = "INSERT INTO Notes(ID_BinomeProjet, Note_Rapport, Note_Soutenance_Etudiant1, Note_Soutenance_Etudiant2) VALUES (?, ?, ?, ?)";
	private static final String UPDATE_NOTE = "UPDATE Notes SET ID_BinomeProjet=?, Note_Rapport=?, Note_Soutenance_Etudiant1=?, Note_Soutenance_Etudiant2=? WHERE ID_Notes=?";
	private static final String DELETE_NOTE = "DELETE FROM Notes WHERE ID_Notes =?";
	private static final String FIND_BY_ID = "SELECT * FROM Notes WHERE ID_Notes =?";
	private static final String FIND_ALL_NOTES = "SELECT * FROM Notes";

	private BinomeProjetDAO binomeProjetDAO = DAOFactory.getBinomeProjetDAO();
	
	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void create(Notes note) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NOTE,
						Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setInt(1, note.getBinomeProjet().getIdBinome()); 
			preparedStatement.setDouble(2, note.getNoteRapport());
			preparedStatement.setDouble(3, note.getNoteSoutenanceMembre1());
			preparedStatement.setDouble(4, note.getNoteSoutenanceMembre2());
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
	public void update(Notes note) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NOTE)) {
			preparedStatement.setInt(1, note.getBinomeProjet().getIdBinome());
			preparedStatement.setDouble(2, note.getNoteRapport());
			preparedStatement.setDouble(3, note.getNoteSoutenanceMembre1());
			preparedStatement.setDouble(3, note.getNoteSoutenanceMembre2());
			preparedStatement.setInt(6, note.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Notes findById(int idNotes) {
		Notes note = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
			preparedStatement.setInt(1, idNotes);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs.next()) {
				int idBinomeProjet = rs.getInt("ID_BinomeProjet");
				BinomeProjet b = binomeProjetDAO.findById(idBinomeProjet);
				
				double noteR = rs.getDouble("Note_Rapport");
				double noteS1 = rs.getDouble("Note_Soutenance_Etudiant1");
				double noteS2 = rs.getDouble("Note_Soutenance_Etudiant2");
				note = new Notes(idNotes, b, noteR, noteS1, noteS2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return note;
	}

	@Override
	public ObservableList<Notes> findAll() {
		Connection connection = getConnection();
		ObservableList<Notes> notes = FXCollections.observableArrayList();
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(FIND_ALL_NOTES)) {
			while (rs.next()) {
				int idNotes = rs.getInt("ID_Notes");
				int idBinomeProjet = rs.getInt("ID_BinomeProjet");
				BinomeProjet b = binomeProjetDAO.findById(idBinomeProjet);
				
				double noteR = rs.getDouble("Note_Rapport");
				double noteS1 = rs.getDouble("Note_Soutenance_Etudiant1");
				double noteS2 = rs.getDouble("Note_Soutenance_Etudiant2");
				notes.add(new Notes(idNotes, b, noteR, noteS1, noteS2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notes;
	}
	
	@Override
	public void deleteById(int idNotes) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_NOTE)) {
			preparedStatement.setInt(1, idNotes);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void delete(Notes note) {
		deleteById(note.getId());
	}
}
