package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.model.Formation;
import javafx.collections.ObservableList;

import java.util.List;

public class FormationService {

	private FormationDAO formationDAO;

	public FormationService() {
		this.formationDAO = DAOFactory.getFormationDAO();
	}

	public void createFormation(String nom, String promotion) {
		Formation formation = new Formation(nom, promotion);
		formationDAO.create(formation);
		System.out.println("Formation cree avec succes : " + formation);
	}

	public void deleteFormationById(int id) {
		formationDAO.deleteById(id);
		System.out.println("Formation avec l'ID " + id + " supprimee avec succes.");
	}

	public ObservableList<Formation> listFormations() {
		ObservableList<Formation> formations = formationDAO.findAll();
		System.out.println("Liste des formations : ");
		for (Formation formation : formations) {
			System.out.println(formation);
		}
		return formations;
	}

	public void update(int id, String nom, String promotion) {
		Formation formation = new Formation(id, nom, promotion);
		formationDAO.update(formation);

	}
}
