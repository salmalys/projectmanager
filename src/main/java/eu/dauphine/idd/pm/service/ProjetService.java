package eu.dauphine.idd.pm.service;

import java.util.Date;

import eu.dauphine.idd.pm.model.Projet;
import javafx.collections.ObservableList;

public interface ProjetService {
    int createProjet(String nomMatiere, String sujet, Date dateRemise);
    void deleteProjetById(int id);
    ObservableList<Projet> listProjets();
    void updateProjet(int id, String nomMatiere, String sujet, Date dateRemise);
    int getProjetIdByNomMatiereAndSujet(String nomMatiere,String sujetProjet);
}