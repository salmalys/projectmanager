package eu.dauphine.idd.pm.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.ProjetDAO;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.ProjetService;
import javafx.collections.ObservableList;
import java.util.Date;

public class ProjetServiceImpl implements ProjetService {

	private ProjetDAO projetDAO;

	public ProjetServiceImpl() {
		this.projetDAO = DAOFactory.getProjetDAO();
	}
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

	@Override
	public int createProjet(String nomMatiere, String sujet, Date dateRemise) {
		Projet existingProjet = projetDAO.findByCourseSubject(nomMatiere, sujet);

		if (existingProjet != null) {
			System.out.println("For this course: " + nomMatiere + " Project already exists with subject: " + sujet);
			return 1;
		} else {

			Projet projet = new Projet(nomMatiere, sujet, dateRemise);

			projetDAO.create(projet);
			System.out.println("Project created successfully: " + projet.toString());
			return 0;
		}
	}

	@Override
	public void deleteProjetById(int id) {
		projetDAO.deleteById(id);
		System.out.println("Project with ID " + id + " successfully removed.");
	}

	@Override
	public ObservableList<Projet> listProjets() {
		ObservableList<Projet> projets = projetDAO.findAll();
		return projets;
	}

	@Override
	public void updateProjet(int id, String nomMatiere, String sujet, String dateRemise) {
		java.util.Date date = null;

        try {
            date = formatter.parse(dateRemise);
        } catch (ParseException e) {
            e.printStackTrace();
        }

		Projet projet = new Projet(id, nomMatiere, sujet, date);
		projetDAO.update(projet);
		System.out.println("Project with ID " + id + " successfully updated.");
		System.out.println(projet.toString());
	}
	// Count

	@Override
	public int getProjetIdByNomMatiereAndSujet(String nomMatiere, String sujetProjet) {
		return projetDAO.findByCourseSubject(nomMatiere, sujetProjet).getIdProjet();
	}
}
