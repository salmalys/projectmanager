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

            preparedStatement.setInt(1, binome.getProjet().getIdProjet());
            preparedStatement.setInt(2, binome.getMembre1().getIdEtudiant());
            preparedStatement.setInt(3, binome.getMembre2().getIdEtudiant());
            java.sql.Date date = new java.sql.Date(binome.getDateRemiseEffective().getTime());
			preparedStatement.setDate(4, date);
            		
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BinomeProjet findById(int id) {
        BinomeProjet binome = null;
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
                java.sql.Date date = rs.getDate("Date_Remise_Effective");
                binome = new BinomeProjet(id, e1, e2, p,date);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return binome;
    }

    @Override
    public ObservableList<BinomeProjet> findAll() {
    	
        ObservableList<BinomeProjet> binomes = FXCollections.observableArrayList();
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
                java.sql.Date date = rs.getDate("Date_Remise_Effective");
                binomes.add(new BinomeProjet(id, e1, e2, p, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
			java.sql.Date date = new java.sql.Date(binome.getDateRemiseEffective().getTime());
			preparedStatement.setDate(3, date);
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
	public void delete(BinomeProjet binome) {
		deleteById(binome.getIdBinome());
	}
}
