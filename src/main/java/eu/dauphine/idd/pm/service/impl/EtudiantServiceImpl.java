package eu.dauphine.idd.pm.service.impl;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.EtudiantService;
import javafx.collections.ObservableList;

public class EtudiantServiceImpl implements EtudiantService {
	
	private EtudiantDAO etudiantDAO;
	private FormationDAO formationDAO;

	public EtudiantServiceImpl() {
		this.etudiantDAO = DAOFactory.getEtudiantDAO();
		this.formationDAO = DAOFactory.getFormationDAO();
	}
	
	@Override
	public int createEtudiant(String nom, String prenom, int idFormation) {
		Etudiant existingEtudiant = etudiantDAO.findByName(nom, prenom);

	    if (existingEtudiant != null) {
	        System.out.println("Student already exists with name: " + nom + " " + prenom);
	        return 1;
	    } else {
	    	Formation formation = formationDAO.findById(idFormation);
	        Etudiant etudiant = new Etudiant(nom, prenom, formation);
	        etudiantDAO.create(etudiant);
	        System.out.println("Student created successfully: " + etudiant.toString());
	        return 0;
	    }
	}

	@Override
	public void deleteEtudiantById(int id) {
		etudiantDAO.deleteById(id);
		System.out.println("Student with ID " + id + " succesfully removed.");
	}

	@Override
	public ObservableList<Etudiant> listEtudiants() {
		ObservableList<Etudiant> etudiants = etudiantDAO.findAll();
		return etudiants;
	}

	@Override
	public void updateEtudiant(int id, String nom, String prenom, int idFormation) {
		Formation formation = formationDAO.findById(idFormation);
		Etudiant etudiant = new Etudiant(id, nom, prenom, formation);
		etudiantDAO.update(etudiant);
		System.out.println("Student with ID "+ id + "succesfully updated.");
		System.out.println(etudiant.toString());
	}

}
