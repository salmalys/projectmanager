package eu.dauphine.idd.pm.service;

public class ServiceFactory {

    private static FormationService formationService;

    public static FormationService getFormationService() {
        if (formationService == null) {
            formationService = new FormationService();
        }
        return formationService;
    }
}
