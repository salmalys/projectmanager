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
    public int createNotes(int idBinome, double noteRapport, double noteSoutenanceMembre1, double noteSoutenanceMembre2) {
        BinomeProjet binome = binomeProjetDAO.findById(idBinome);
        if (binome == null) {
            System.out.println("Invalid binome ID");
            return 1;
        }

        Notes notes = new Notes(binome, noteRapport, noteSoutenanceMembre1, noteSoutenanceMembre2);
        notesDAO.create(notes);
        System.out.println("Note created successfully: " + notes.toString());
        return 0;
    }

    @Override
    public void deleteNotesById(int idNotes) {
        notesDAO.deleteById(idNotes);
        System.out.println("Note with ID " + idNotes + " successfully removed.");
    }

    @Override
    public ObservableList<Notes> listNotes() {
        return notesDAO.findAll();
    }

    @Override
    public void updateNotes(int idNotes, int idBinome, double noteRapport, double noteSoutenanceMembre1, double noteSoutenanceMembre2) {
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

        if (notes == null) {
            System.out.println("Invalid notes ID");
            return new double[]{0, 0}; // Ou lancez une exception appropriée
        }

        BinomeProjet binome = notes.getBinomeProjet();
        Projet projet = binome.getProjet();

        Date dateRendu = projet.getDateRemiseRapport();
        Date dateRemiseEffective = binome.getDateRemiseEffective();

        // Calcul de la différence en millisecondes
        long diffInMillis = dateRemiseEffective.toInstant().toEpochMilli() - dateRendu.toInstant().toEpochMilli();

        // Calcul de la pénalité
        long penalite = Math.max(0, TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS));

        // Calcul des notes finales
        double noteMembre1 = notes.getNoteSoutenanceMembre1() * 0.5 + notes.getNoteRapport() * 0.5 - penalite;
        double noteMembre2 = notes.getNoteSoutenanceMembre2() * 0.5 + notes.getNoteRapport() * 0.5 - penalite;

        return new double[]{noteMembre1, noteMembre2};
    }
    

}
