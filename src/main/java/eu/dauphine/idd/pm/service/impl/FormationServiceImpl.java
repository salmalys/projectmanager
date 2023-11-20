package eu.dauphine.idd.pm.service.impl;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.FormationService;
import javafx.collections.ObservableList;


public class FormationServiceImpl implements FormationService{

	private FormationDAO formationDAO;

	public FormationServiceImpl() {
		this.formationDAO = DAOFactory.getFormationDAO();
	}
	
	//Constructeur pour l'injection de dependance (Test unitaire)
    public FormationServiceImpl(FormationDAO formationDAO) {
        this.formationDAO = formationDAO;
    }
	

	@Override
	public int createFormation(String nom, String promotion) {
	    Formation existingFormation = formationDAO.findByNameAndPromotion(nom, promotion);

	    if (existingFormation != null) {
	        System.out.println("Formation already exists with name: " + nom + " and promotion: " + promotion);
	        return 1;
	    } else {
	        Formation formation = new Formation(nom, promotion);
	        formationDAO.create(formation);
	        System.out.println("Formation created successfully: " + formation.toString());
	        return 0;
	    }
	}

	@Override
	public void deleteFormationById(int id) {
		formationDAO.deleteById(id);
		System.out.println("Formation with ID " + id + " succesfully removed.");
	}

	@Override
	public ObservableList<Formation> listFormations() {
		ObservableList<Formation> formations = formationDAO.findAll();
		return formations;
	}

	@Override
	public void update(int id, String nom, String promotion) {
		Formation formation = new Formation(id, nom, promotion);
		formationDAO.update(formation);
		System.out.println("Formation with ID "+ id + "succesfully updated.");
		System.out.println(formation.toString());
	}
	public int getFormationIdByNameAndPromotion(String formationNom, String formationPromotion) {
		return formationDAO.findByNameAndPromotion(formationNom, formationPromotion).getIdFormation();
	}

}
