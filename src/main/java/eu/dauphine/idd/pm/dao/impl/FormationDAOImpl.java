package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Formation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class FormationDAOImpl implements FormationDAO {

	private static final String INSERT_FORMATION = "INSERT INTO Formation (Nom, Promotion) VALUES (?, ?)";
	private static final String SELECT_FORMATION_BY_ID = "SELECT * FROM Formation WHERE ID_Formation = ?";
	private static final String SELECT_ALL_FORMATIONS = "SELECT * FROM Formation";
	private static final String UPDATE_FORMATION = "UPDATE Formation SET Nom = ?, Promotion = ? WHERE ID_Formation = ?";
	private static final String DELETE_FORMATION_BY_ID = "DELETE FROM Formation WHERE ID_Formation = ?";

	private Connection getConnection() {
		try {
			return DatabaseConnection.getInstance().getConnection();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Connexion failed");
			return null;
		}
	}

	@Override
	public void create(Formation formation) {
		try (Connection connection = getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_FORMATION,
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, formation.getNom());
			preparedStatement.setString(2, formation.getPromotion());

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					formation.setIdFormation(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Formation findById(int id) {
		Formation formation = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FORMATION_BY_ID)) {

			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String nom = rs.getString("Nom");
				String promotion = rs.getString("Promotion");
				formation = new Formation(id, nom, promotion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return formation;
	}

	@Override
	public ObservableList<Formation> findAll() {
		ObservableList<Formation> formations = FXCollections.observableArrayList();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FORMATIONS)) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("ID_Formation");
				String nom = rs.getString("Nom");
				String promotion = rs.getString("Promotion");
				Formation formation = new Formation(id, nom, promotion);
				formations.add(formation);

			}
		} catch (SQLException e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		return formations;
	}

	@Override
	public void update(Formation formation) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FORMATION)) {

			preparedStatement.setString(1, formation.getNom());
			preparedStatement.setString(2, formation.getPromotion());
			preparedStatement.setInt(3, formation.getIdFormation());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FORMATION_BY_ID)) {

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Formation formation) {
		deleteById(formation.getIdFormation());
	}

	@Override
	public Formation findByNameAndPromotion(String nom, String promotion) {
		Formation formation = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Formation WHERE Nom = ? AND Promotion = ?")) {

			preparedStatement.setString(1, nom);
			preparedStatement.setString(2, promotion);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("ID_Formation");
				formation = new Formation(id, nom, promotion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return formation;
	}


}
