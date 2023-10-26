package eu.dauphine.idd.pm.dao.impl;

import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAOImpl implements EtudiantDAO {

    private static final String INSERT_ETUDIANT = "INSERT INTO Etudiant (Nom, Prenom, ID_Formation) VALUES (?, ?, ?)";
    private static final String SELECT_ETUDIANT_BY_ID = "SELECT * FROM Etudiant WHERE ID_Etudiant = ?";
    private static final String SELECT_ALL_ETUDIANTS = "SELECT * FROM Etudiant";
    private static final String UPDATE_ETUDIANT = "UPDATE Etudiant SET Nom = ?, Prenom = ?, ID_Formation = ? WHERE ID_Etudiant = ?";
    private static final String DELETE_ETUDIANT_BY_ID = "DELETE FROM Etudiant WHERE ID_Etudiant = ?";

    private Connection getConnection() {
        // TODO: Retournez une connexion à la base de données
        return null;
    }

    @Override
    public void create(Etudiant etudiant) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ETUDIANT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, etudiant.getNom());
            preparedStatement.setString(2, etudiant.getPrenom());
            preparedStatement.setInt(3, etudiant.getFormation().getIdFormation()); // Supposant que Formation a une méthode getId()

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
    public Etudiant findById(int id) {
        Etudiant etudiant = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ETUDIANT_BY_ID)) {

            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                Formation formation = new Formation(); // Vous devrez probablement récupérer la formation complète ici
                formation.setIdFormation(rs.getInt("ID_Formation"));
                etudiant = new Etudiant(id, nom, prenom, formation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return etudiant;
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ETUDIANTS)) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID_Etudiant");
                String nom = rs.getString("Nom");
                String prenom = rs.getString("Prenom");
                Formation formation = new Formation(); // Encore une fois, récupérez la formation complète ici
                formation.setIdFormation(rs.getInt("ID_Formation"));
                Etudiant etudiant = new Etudiant(id, nom, prenom, formation);
                etudiants.add(etudiant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
}
