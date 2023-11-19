package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.service.impl.FormationServiceImpl;

public class ServiceFactory {

    private static FormationService formationService;
    //private static EtudiantService etudiantService;

    public static FormationService getFormationService() {
        if (formationService == null) {
            formationService = new FormationServiceImpl();
        }
        return formationService;
    }
}
