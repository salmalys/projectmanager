package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.model.Etudiant;
import javafx.collections.ObservableList;

public interface EtudiantService {
	public int createEtudiant(String nom, String prenom, int idFormation);

	public void deleteEtudiantById(int id);

	public ObservableList<Etudiant> listEtudiants();

	public void updateEtudiant(int id, String nom, String prenom, int idFormation);

	public int getEtudiantIdByNameAndPrenom(String nomEtudiant, String prenomEtudiant);
}
