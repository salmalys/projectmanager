package eu.dauphine.idd.pm.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import eu.dauphine.idd.pm.dao.BinomeProjetDAO;
import eu.dauphine.idd.pm.dao.DAOFactory;
import eu.dauphine.idd.pm.dao.NotesDAO;
import eu.dauphine.idd.pm.model.BinomeProjet;
import eu.dauphine.idd.pm.model.Notes;
import eu.dauphine.idd.pm.model.Projet;
import eu.dauphine.idd.pm.service.NotesService;
import javafx.collections.ObservableList;

public class NotesServiceImpl implements NotesService {

	private NotesDAO notesDAO;
	private BinomeProjetDAO binomeProjetDAO;

	public NotesServiceImpl() {
		this.notesDAO = DAOFactory.getNotesDAO();
		this.binomeProjetDAO = DAOFactory.getBinomeProjetDAO();
	}

	@Override
	public int createNotes(int idBinome, double noteRapport, double noteSoutenanceMembre1,
			double noteSoutenanceMembre2) {
		BinomeProjet binome = binomeProjetDAO.findById(idBinome);
		if (binome == null) {
			System.out.println("Invalid binome ID");
			return 1;
		}
		if (binome.getDateRemiseEffective() == null) {
			System.out.println("Date Remise null");
			return 2;
		}

		Notes notes = new Notes(binome, noteRapport, noteSoutenanceMembre1, noteSoutenanceMembre2);
		notesDAO.create(notes);
		System.out.println("Note created successfully: " + notes.toString());
		return 0;
	}

	@Override
	public void deleteNotesById(int idBinome) {
		notesDAO.deleteById(idBinome);
		System.out.println("Note with ID Binome" + idBinome + " successfully removed.");
	}

	@Override
	public ObservableList<Notes> listNotes() {
		return notesDAO.findAll();
	}

	@Override
	public void updateNotes(int idNotes, int idBinome, double noteRapport, double noteSoutenanceMembre1,
			double noteSoutenanceMembre2) {
		BinomeProjet binome = binomeProjetDAO.findById(idBinome);
		if (binome == null) {
			System.out.println("Invalid binome ID");
			return;
		}

		Notes notes = new Notes(idNotes, binome, noteRapport, noteSoutenanceMembre1, noteSoutenanceMembre2);
		notesDAO.update(notes);
		System.out.println("Note with ID " + idNotes + " successfully updated.");
		System.out.println(notes.toString());
	}

	@Override
	public double[] calculNoteFinale(int idNotes) {
		Notes notes = notesDAO.findById(idNotes);

		BinomeProjet binome = notes.getBinomeProjet();
		Projet projet = binome.getProjet();

		Date dateRendu = projet.getDateRemiseRapport();
		Date dateRemiseEffective = binome.getDateRemiseEffective();

		Instant instantRemise = dateRendu.toInstant();
		Instant instantRendu = dateRemiseEffective.toInstant();

		// Calcul de la difference en millisecondes
		long diffInMillis = instantRendu.toEpochMilli() - instantRemise.toEpochMilli();

		// Calcul de la penalite
		long penalite = 0;
		if (diffInMillis > 0) {
			// Conversion en jours si retard
			penalite = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		}

		double[] notesFinales = new double[2];
		double notePartielle1 = notes.getNoteSoutenanceMembre1() * 0.5 + notes.getNoteRapport() * 0.5;
		if (notePartielle1 > penalite) {
			notesFinales[0] = notePartielle1 - penalite;
		} else {
			notesFinales[0] = 0;
		}

		double notePartielle2 = notes.getNoteSoutenanceMembre2() * 0.5 + notes.getNoteRapport() * 0.5;

		if (notePartielle2 > penalite) {
			notesFinales[1] = notePartielle2 - penalite;
		} else {
			notesFinales[1] = 0;
		}

		return notesFinales;
	}

	@Override
	public Notes findNoteForBinome(int idBinome) {
		return notesDAO.findByBinomeId(idBinome);
	}
    @Override
	public void deleteAll() {
		notesDAO.deleteAll();

	}

}