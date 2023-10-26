package eu.dauphine.idd.pm.service;

public class ServiceFactory {

    private static FormationService formationService;

    // Instance de tout autre service que vous pourriez ajouter plus tard
    // private static AutreService autreService;

    public static FormationService getFormationService() {
        if (formationService == null) {
            formationService = new FormationService();
        }
        return formationService;
    }

    // Getter pour tout autre service que vous pourriez ajouter plus tard
    // public static AutreService getAutreService() {
    //     if (autreService == null) {
    //         autreService = new AutreService();
    //     }
    //     return autreService;
    // }
}
