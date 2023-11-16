package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.BinomeDAO;
import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.ProjetDAO;
import eu.dauphine.idd.pm.jdbc.DatabaseConnection;
import eu.dauphine.idd.pm.model.Binome;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.model.Etudiant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BinomeDAOImpl implements BinomeDAO {

    private static final String INSERT_BINOME = "INSERT INTO Binome (ID_Projet, ID_Etudiant1, ID_Etudiant2) VALUES (?, ?, ?)";
    private static final String SELECT_BINOME_BY_ID = "SELECT * FROM Binome WHERE ID_Binome = ?";
    private static final String SELECT_ALL_BINOMES = "SELECT * FROM Binome";
    private static final String UPDATE_BINOME = "UPDATE Binome SET ID_Projet = ?, ID_Etudiant1 = ?, ID_Etudiant2 = ? WHERE ID_Binome = ?";
    private static final String DELETE_BINOME_BY_ID = "DELETE FROM Binome WHERE ID_Binome = ?";

	private EtudiantDAO etudiantDAO = DAOFactory.getEtudiantDAO();
	private ProjetDAO projetDAO = DAOFactory.getProjetDAO();
	
    private Connection getConnection() {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void create(Binome binome) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BINOME)) {

            preparedStatement.setInt(1, binome.getProjet().getIdProjet());
            preparedStatement.setInt(2, binome.getMembre1().getIdEtudiant());
            preparedStatement.setInt(3, binome.getMembre2().getIdEtudiant());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Binome findById(int id) {
        Binome binome = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BINOME_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int idProjet = rs.getInt("ID_Projet");
                int idEtudiant1 = rs.getInt("ID_Etudiant1");
                int idEtudiant2 = rs.getInt("ID_Etudiant2");
                Etudiant e1 = etudiantDAO.findById(idEtudiant1);
                Etudiant e2 = etudiantDAO.findById(idEtudiant2);
                Projet p = projetDAO.findById(idProjet);
                binome = new Binome(id, e1, e2, p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return binome;
    }

    @Override
    public ObservableList<Binome> findAll() {
    	
        ObservableList<Binome> binomes = FXCollections.observableArrayList();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BINOMES)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID_Binome");
                int idProjet = rs.getInt("ID_Projet");
                int idEtudiant1 = rs.getInt("ID_Etudiant1");
                int idEtudiant2 = rs.getInt("ID_Etudiant2");
                Etudiant e1 = etudiantDAO.findById(idEtudiant1);
                Etudiant e2 = etudiantDAO.findById(idEtudiant2);
                Projet p = projetDAO.findById(idProjet);
                binomes.add(new Binome(id, e1, e2, p));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return binomes;
    }

    @Override
    public void update(Binome binome) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BINOME)) {

            preparedStatement.setInt(1, binome.getProjet().getIdProjet());
            preparedStatement.setInt(2, binome.getMembre1().getIdEtudiant());
            preparedStatement.setInt(3, binome.getMembre2().getIdEtudiant());
            preparedStatement.setInt(4, binome.getIdBinome());

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
	public void delete(Binome binome) {
		deleteById(binome.getIdBinome());
	}
}
