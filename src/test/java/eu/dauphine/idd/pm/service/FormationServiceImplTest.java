package eu.dauphine.idd.pm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.impl.FormationServiceImpl;

public class FormationServiceImplTest {

    private FormationServiceImpl formationService;
    private FormationDAO formationDAO;

    @BeforeEach
    public void setUp() {
        formationDAO = mock(FormationDAO.class);
        formationService = new FormationServiceImpl(formationDAO);
    }

    @Test
    public void testCreateFormation() {
        when(formationDAO.findByNameAndPromotion(anyString(), anyString())).thenReturn(null);
        int result = formationService.createFormation("Nom", "Promotion");

        verify(formationDAO, times(1)).create(any(Formation.class));
        assertEquals(0, result);
    }
    
    @Test
    public void testCreateFormation2() {
    	Formation existingFormation = new Formation("NomExistant","PromotionExistant");
        when(formationDAO.findByNameAndPromotion(anyString(), anyString())).thenReturn(existingFormation);
        int result = formationService.createFormation("NomExistat", "PromotionExistant");

        verify(formationDAO, times(0)).create(any(Formation.class));
        assertEquals(1, result);
    }
    


    // Autres tests pour deleteFormationById, listFormations, update, getFormationIdByNameAndPromotion, etc.
}
