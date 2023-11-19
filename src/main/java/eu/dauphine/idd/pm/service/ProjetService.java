package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.model.Projet;
import javafx.collections.ObservableList;
import java.sql.Date;

public interface ProjetService {
    int createProjet(String nomMatiere, String sujet, Date dateRemise);
    void deleteProjetById(int id);
    ObservableList<Projet> listProjets();
    void updateProjet(int id, String nomMatiere, String sujet, Date dateRemise);
}