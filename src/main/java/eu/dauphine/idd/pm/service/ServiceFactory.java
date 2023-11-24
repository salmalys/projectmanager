package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.service.impl.*;

public class ServiceFactory {

    private static FormationService formationService;
    private static EtudiantService etudiantService;
    private static ProjetService projetService;
    private static BinomeProjetService binomeProjetService;
    private static NotesService notesService;

    public static FormationService getFormationService() {
        if (formationService == null) {
            formationService = new FormationServiceImpl();
        }
        return formationService;
    }
    
    public static EtudiantService getEtudiantService() {
        if (etudiantService == null) {
        	etudiantService = new EtudiantServiceImpl();
        }
        return etudiantService;
    }
    
    public static ProjetService getProjetService() {
        if (projetService == null) {
        	projetService = new ProjetServiceImpl();
        }
        return projetService;
    }
    
    public static BinomeProjetService getBinomeProjetService() {
        if (binomeProjetService == null) {
        	binomeProjetService = new BinomeProjetServiceImpl();
        }
        return binomeProjetService;
    }
    
    public static NotesService getNotesService() {
        if (notesService == null) {
        	notesService = new NotesServiceImpl();
        }
        return notesService;
    }
}
