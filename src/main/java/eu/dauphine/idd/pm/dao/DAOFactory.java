package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.dao.impl.FormationDAOImpl;

public class DAOFactory {
	
	
    private static FormationDAO formationDAO;


    public static FormationDAO getFormationDAO() {
        if (formationDAO == null) {
        	formationDAO = new FormationDAOImpl();
        }
        return formationDAO;
    }


}
