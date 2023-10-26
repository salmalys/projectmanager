package eu.dauphine.idd.pm.main;

import eu.dauphine.idd.pm.service.FormationService;
import eu.dauphine.idd.pm.service.ServiceFactory;

public class App {

    public static void main(String[] args) {
      //  FormationService formationService = new FormationService();
        FormationService formationService = ServiceFactory.getFormationService();


        // Créer quelques formations
        System.out.println("Création de formations...");
        formationService.createFormation("Informatique", "2023");
        formationService.createFormation("Mathématiques", "2023");
        formationService.createFormation("Physique", "2022");

        // Lister les formations
        System.out.println("\nListe des formations :");
        formationService.listFormations();

        // Supprimer une formation par ID
        // NOTE: Assurez-vous que l'ID que vous utilisez existe réellement dans votre base de données.
        // Pour cet exemple, je suppose qu'il y a une formation avec l'ID 2.
    //    System.out.println("\nSuppression de la formation avec l'ID 2...");
    //    formationService.deleteFormationById(2);

        // Lister à nouveau les formations pour vérifier que la formation a été supprimée
      //  System.out.println("\nListe des formations après suppression :");
      //  formationService.listFormations();
    }
}
