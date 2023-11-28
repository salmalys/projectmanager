package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EtudiantDAOImpl implements EtudiantDAO {

	private static final String INSERT_ETUDIANT = "INSERT INTO Etudiant (Nom, Prenom, ID_Formation) VALUES (?, ?, ?)";
	private static final String SELECT_ETUDIANT_BY_ID = "SELECT * FROM Etudiant WHERE ID_Etudiant = ?";
	private static final String SELECT_ALL_ETUDIANTS = "SELECT * FROM Etudiant";
	private static final String UPDATE_ETUDIANT = "UPDATE Etudiant SET Nom = ?, Prenom = ?, ID_Formation = ? WHERE ID_Etudiant = ?";
	private static final String DELETE_ETUDIANT_BY_ID = "DELETE FROM Etudiant WHERE ID_Etudiant = ?";

	private FormationDAO formationDAO = DAOFactory.getFormationDAO();

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void create(Etudiant etudiant) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ETUDIANT,
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, etudiant.getNom());
			preparedStatement.setString(2, etudiant.getPrenom());
			preparedStatement.setInt(3, etudiant.getFormation().getIdFormation());
			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					etudiant.setIdEtudiant(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Etudiant findById(int idEtudiant) {
		Etudiant etudiant = null;
		int idFormation = 0;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ETUDIANT_BY_ID)) {

			preparedStatement.setInt(1, idEtudiant);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String nom = rs.getString("Nom");
				String prenom = rs.getString("Prenom");
				idFormation = rs.getInt("ID_Formation");
				etudiant = new Etudiant(idEtudiant, nom, prenom, null);

		} }catch (SQLException e) {
			System.out.println("Error while searching student");
			e.printStackTrace();
		}
		if (etudiant != null) {
			Formation formation = formationDAO.findById(idFormation);
			etudiant.setFormation(formation);

		}

		return etudiant;
	}

	@Override
	public ObservableList<Etudiant> findAll() {
		ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();

		Map<Etudiant, Integer> etudiantFormationMap = new HashMap<>();

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ETUDIANTS);
				ResultSet rs = preparedStatement.executeQuery()) {

			while (rs.next()) {
				int idEtudiant = rs.getInt("ID_Etudiant");
				String nom = rs.getString("Nom");
				String prenom = rs.getString("Prenom");
				int idFormation = rs.getInt("ID_Formation");

				Etudiant etudiant = new Etudiant(idEtudiant, nom, prenom, null);
				etudiants.add(etudiant);
				etudiantFormationMap.put(etudiant, idFormation);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		for (Etudiant etudiant : etudiants) {
			int idFormation = etudiantFormationMap.get(etudiant);
			Formation formation = formationDAO.findById(idFormation);
			etudiant.setFormation(formation);
		}

		return etudiants;
	}

	@Override
	public void update(Etudiant etudiant) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ETUDIANT)) {

			preparedStatement.setString(1, etudiant.getNom());
			preparedStatement.setString(2, etudiant.getPrenom());
			preparedStatement.setInt(3, etudiant.getFormation().getIdFormation());
			preparedStatement.setInt(4, etudiant.getIdEtudiant());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ETUDIANT_BY_ID)) {

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Etudiant etudiant) {
		deleteById(etudiant.getIdEtudiant());
	}

	@Override
	public Etudiant findByName(String nom, String prenom) {
		Etudiant etudiant = null;
		int idFormation = 0;

		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Etudiant WHERE Nom = ? AND Prenom = ?");) {
			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, prenom);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int idEtudiant = rs.getInt("ID_Etudiant");
				idFormation = rs.getInt("ID_Formation");

				etudiant = new Etudiant(idEtudiant, nom, prenom, null);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		if (etudiant != null) {
			Formation formation = formationDAO.findById(idFormation);
			etudiant.setFormation(formation);
		}

		return etudiant;
	}


	public static void main(String[] args) {
		EtudiantDAOImpl s = new EtudiantDAOImpl();

		System.out.println(s.findAll());
		//System.out.println(s.findById(1));
		System.out.println(s.findByName("SAIS", "Ilyes"));
		System.out.println(s.findById(2));
	}

}
