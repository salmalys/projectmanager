package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Formation;

import java.util.List;

public interface FormationDAO {

    // Créer une nouvelle formation
    void create(Formation formation);

    // Récupérer une formation par son ID
    Formation findById(int id);

    // Récupérer toutes les formations
    List<Formation> findAll();

    // Mettre à jour les informations d'une formation
    void update(Formation formation);

    // Supprimer une formation par son ID
    void deleteById(int id);

    // Supprimer une formation
    void delete(Formation formation);

    // Autres méthodes spécifiques à votre besoin peuvent être ajoutées ici
}
