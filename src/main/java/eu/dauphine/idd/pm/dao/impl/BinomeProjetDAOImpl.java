package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.BinomeProjetDAO;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.ProjetDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BinomeProjetDAOImpl implements BinomeProjetDAO {

	private static final String INSERT_BINOME = "INSERT INTO BinomeProjet (ID_Etudiant1, ID_Etudiant2, ID_Projet,Date_Remise_Effective) VALUES (?, ?, ?, ?)";
	private static final String SELECT_BINOME_BY_ID = "SELECT * FROM BinomeProjet WHERE ID_BinomeProjet = ?";
	private static final String SELECT_ALL_BINOMES = "SELECT * FROM BinomeProjet";
	private static final String UPDATE_BINOME = "UPDATE BinomeProjet SET ID_Projet = ?, ID_Etudiant1 = ?, ID_Etudiant2 = ?, Date_Remise_Effective = ? WHERE ID_BinomeProjet = ?";
	private static final String DELETE_BINOME_BY_ID = "DELETE FROM BinomeProjet WHERE ID_BinomeProjet = ?";

	private EtudiantDAO etudiantDAO = DAOFactory.getEtudiantDAO();
	private ProjetDAO projetDAO = DAOFactory.getProjetDAO();

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void create(BinomeProjet binome) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BINOME)) {
			
			System.out.println(binome);
			preparedStatement.setInt(3, binome.getProjet().getIdProjet());
			preparedStatement.setInt(1, binome.getMembre1().getIdEtudiant());
			preparedStatement.setInt(2, binome.getMembre2().getIdEtudiant());
			preparedStatement.setNull(4, Types.DATE); // Always set to null

			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public BinomeProjet findById(int id) {
		BinomeProjet binome = null;
		int idProjet = 0;
		int idEtudiant1 = 0;
		int idEtudiant2 = 0;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BINOME_BY_ID)) {

			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				idProjet = rs.getInt("ID_Projet");
				idEtudiant1 = rs.getInt("ID_Etudiant1");
				idEtudiant2 = rs.getInt("ID_Etudiant2");

				// Check for inequality
				if (idEtudiant1 == idEtudiant2) {
					// Handle the case where membre1 equals membre2
					// You may throw an exception, log a warning, or take appropriate action
					// For now, let's print a message
					System.err.println("Warning: membre1 equals membre2");
					return null; // Return null to indicate an issue
				}

				String dateString = rs.getString("Date_Remise_Effective");

				Date date = null;
				if (dateString != null) {
					try {
						date = formatter.parse(dateString);
					} catch (ParseException e) {
						e.printStackTrace();
						// Handle the parsing exception (e.g., log a warning)
						// For now, let's print a message
						System.err.println("Warning: Error parsing Date_Remise_Effective");
						return null; // Return null to indicate an issue
					}
				} else {
					// Handle the case where the date is null
					// For now, let's print a message
					System.err.println("Warning: Date_Remise_Effective is null");
					// You can provide a default date here if needed
				}

				binome = new BinomeProjet(id, null, null, null, date);
				System.out.println(binome.getIdBinome());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (binome != null) {
			Etudiant e1 = etudiantDAO.findById(idEtudiant1);
			Etudiant e2 = etudiantDAO.findById(idEtudiant2);
			Projet p = projetDAO.findById(idProjet);

			binome.setMembre1(e1);
			binome.setMembre2(e2);
			binome.setProjet(p);

		}

		return binome;
	}

	@Override
	public ObservableList<BinomeProjet> findAll() {
	    ObservableList<BinomeProjet> binomes = FXCollections.observableArrayList();
	    Map<BinomeProjet, Integer[]> binomeMap = new HashMap<>();
	    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BINOMES)) {

	        ResultSet rs = preparedStatement.executeQuery();

	        while (rs.next()) {
	            int id = rs.getInt("ID_BinomeProjet");
	            Integer[] ids = new Integer[3];
	            ids[0] = rs.getInt("ID_Projet");
	            ids[1] = rs.getInt("ID_Etudiant1");
	            ids[2] = rs.getInt("ID_Etudiant2");
	            String dateString = rs.getString("Date_Remise_Effective");

	            Date date = dateString != null ? formatter.parse(dateString) : null;

	            BinomeProjet binome = new BinomeProjet(id, null, null, null, date);
	            binomes.add(binome);
	            binomeMap.put(binome, ids);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }

	    for (BinomeProjet binome : binomes) {
	        Projet projet = projetDAO.findById(binomeMap.get(binome)[0]);
	        Etudiant etudiant1 = etudiantDAO.findById(binomeMap.get(binome)[1]);
	        Etudiant etudiant2 = etudiantDAO.findById(binomeMap.get(binome)[2]);
	        binome.setProjet(projet);
	        binome.setMembre1(etudiant1);
	        binome.setMembre2(etudiant2);
	    }

	    return binomes;
	}


	@Override
	public void update(BinomeProjet binome) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BINOME)) {

			preparedStatement.setInt(1, binome.getProjet().getIdProjet());
			preparedStatement.setInt(2, binome.getMembre1().getIdEtudiant());
			preparedStatement.setInt(3, binome.getMembre2().getIdEtudiant());

			if (binome.getDateRemiseEffective() != null) {
				// If dateRemise is not null, update it
				String formattedDate = formatter.format(binome.getDateRemiseEffective());

				preparedStatement.setString(4, formattedDate);
			} else {
				// If dateRemise is null, set it to NULL in the database
				preparedStatement.setNull(4, Types.DATE);
			}

			preparedStatement.setInt(5, binome.getIdBinome());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BINOME_BY_ID)) {

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(BinomeProjet binome) {
		deleteById(binome.getIdBinome());
	}

	public static void main(String[] args) {
		BinomeProjetDAOImpl b = new BinomeProjetDAOImpl();
		// System.out.println(b.findById(4));
		System.out.println(b.findAll());

	}
}
