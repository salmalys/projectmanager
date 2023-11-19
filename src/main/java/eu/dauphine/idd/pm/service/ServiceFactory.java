package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.service.impl.*;

public class ServiceFactory {

    private static FormationService formationService;
    private static EtudiantService etudiantService;
    private static ProjetService projetService;

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
}
