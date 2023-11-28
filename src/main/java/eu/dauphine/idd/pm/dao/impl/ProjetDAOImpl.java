package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.controller.Data;
import eu.dauphine.idd.pm.dao.ProjetDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Projet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class ProjetDAOImpl implements ProjetDAO {

	private static final String INSERT_PROJET = "INSERT INTO Projet (Nom_Matiere, Sujet, Date_Remise_Prevue) VALUES (?, ?, ?)";
	private static final String SELECT_PROJET_BY_ID = "SELECT * FROM Projet WHERE ID_Projet = ?";
	private static final String SELECT_ALL_PROJETS = "SELECT * FROM Projet";
	private static final String UPDATE_PROJET = "UPDATE Projet SET Nom_Matiere = ?, Sujet = ?, Date_Remise_Prevue = ? WHERE ID_Projet = ?";
	private static final String DELETE_PROJET_BY_ID = "DELETE FROM Projet WHERE ID_Projet = ?";
	private static final String COUNT_NBPROJET = "SELECT COUNT(ID_Projet) AS totalProjets FROM Projet";

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
	public void create(Projet projet) {
		try (Connection connection = getConnection();

				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJET,
						Statement.RETURN_GENERATED_KEYS)) {

			preparedStatement.setString(1, projet.getNomMatiere());
			preparedStatement.setString(2, projet.getSujet());
			String formattedDate = formatter.format(projet.getDateRemiseRapport());
			preparedStatement.setString(3, formattedDate);

			preparedStatement.executeUpdate();

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					projet.setIdProjet(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Projet findById(int id) {
		Projet projet = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJET_BY_ID)) {

			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {

				String nomMatiere = rs.getString("Nom_Matiere");
				String sujet = rs.getString("Sujet");
				Date date = formatter.parse(rs.getString("Date_Remise_Prevue"));
				
				projet = new Projet(id, nomMatiere, sujet, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (ParseException e) {
            e.printStackTrace();
        }
		return projet;
	}

	@Override
	public ObservableList<Projet> findAll() {
		ObservableList<Projet> projets = FXCollections.observableArrayList();
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROJETS)) {

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("ID_Projet");
				String nomMatiere = rs.getString("Nom_Matiere");
				String sujet = rs.getString("Sujet");
				Date date = formatter.parse(rs.getString("Date_Remise_Prevue"));
				
				Projet projet = new Projet(id, nomMatiere, sujet, date);
				projets.add(projet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		catch (ParseException e) {
            e.printStackTrace();
        }
		return projets;
	}

	@Override
	public void update(Projet projet) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJET)) {

			preparedStatement.setString(1, projet.getNomMatiere());
			preparedStatement.setString(2, projet.getSujet());
			String formattedDate = formatter.format(projet.getDateRemiseRapport());
			preparedStatement.setString(3, formattedDate);
			preparedStatement.setInt(4, projet.getIdProjet());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteById(int id) {
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJET_BY_ID)) {

			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(Projet projet) {
		deleteById(projet.getIdProjet());
	}

	@Override
	public Projet findByCourseSubject(String nomMatiere, String sujet) {
		Projet projet = null;
		try (Connection connection = getConnection();
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM Projet WHERE Nom_Matiere = ? AND Sujet = ?")) {

			preparedStatement.setString(1, nomMatiere);
			preparedStatement.setString(2, sujet);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				int idProjet = rs.getInt("ID_Projet");
				Date date = formatter.parse(rs.getString("Date_Remise_Prevue"));
				projet = new Projet(idProjet, nomMatiere, sujet, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		catch (ParseException e) {
            e.printStackTrace();
        }
		return projet;
	}

	public int getTotalProjets() {
	    int totalProjets = 0;

	    try (Connection connection = getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(COUNT_NBPROJET);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        if (resultSet.next()) {
	            totalProjets = resultSet.getInt("totalProjets");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return totalProjets;
	}
	
	public static void main(String[] args) {
		ProjetDAOImpl l=new ProjetDAOImpl();
		System.out.println(l.findAll());
	}


}
