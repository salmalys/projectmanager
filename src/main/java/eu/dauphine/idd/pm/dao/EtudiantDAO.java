package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Etudiant;
import java.util.List;

public interface EtudiantDAO {

    // Créer un nouvel étudiant
    void create(Etudiant etudiant);

    // Récupérer un étudiant par son ID
    Etudiant findById(int id);

    // Récupérer tous les étudiants
    List<Etudiant> findAll();

    // Mettre à jour les informations d'un étudiant
    void update(Etudiant etudiant);

    // Supprimer un étudiant par son ID
    void deleteById(int id);

    // Supprimer un étudiant
    void delete(Etudiant etudiant);

    // Autres méthodes spécifiques à votre besoin peuvent être ajoutées ici
}
