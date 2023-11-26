package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.model.Projet;
import javafx.collections.ObservableList;

public interface ProjetService {
    int createProjet(String nomMatiere, String sujet, String dateRemise);
    void deleteProjetById(int id);
    ObservableList<Projet> listProjets();
    void updateProjet(int id, String nomMatiere, String sujet, String dateRemise);
    int getProjetIdByNomMatiereAndSujet(String nomMatiere,String sujetProjet);
}