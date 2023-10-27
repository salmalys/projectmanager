package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Etudiant;
import java.util.List;

public interface EtudiantDAO {
    void create(Etudiant etudiant);
    Etudiant findById(int id);
    List<Etudiant> findAll();
    void update(Etudiant etudiant);
    void deleteById(int id);
    void delete(Etudiant etudiant);
}
