package eu.dauphine.idd.pm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.dauphine.idd.pm.dao.EtudiantDAO;
import eu.dauphine.idd.pm.dao.FormationDAO;
import eu.dauphine.idd.pm.model.Etudiant;
import eu.dauphine.idd.pm.model.Formation;
import eu.dauphine.idd.pm.service.impl.EtudiantServiceImpl;

public class EtudiantServiceImplTest {

    private EtudiantServiceImpl etudiantService;
    private EtudiantDAO etudiantDAO;
    private FormationDAO formationDAO;

    @BeforeEach
    public void setUp() {
        etudiantDAO = mock(EtudiantDAO.class);
        formationDAO = mock(FormationDAO.class);
        etudiantService = new EtudiantServiceImpl(etudiantDAO, formationDAO);
    }

    @Test
    public void testCreateEtudiant() {
        // Remplacer avec les méthodes et paramètres appropriés
        when(etudiantDAO.findByName(anyString(),anyString())).thenReturn(null);
        
        Formation f = new Formation("CAP","Initial");
        when(formationDAO.findById(anyInt())).thenReturn(f);
       int result = etudiantService.createEtudiant("Nom", "Prénom", 1);

        verify(etudiantDAO, times(1)).create(any(Etudiant.class));
        assertEquals(0, result);
    }

    @Test
    public void testCreateEtudiantExisting() {
        // Remplacer avec les méthodes et paramètres appropriés
        Etudiant existingEtudiant = new Etudiant("NomExistant", "PrénomExistant", new Formation());
        when(etudiantDAO.findByName(anyString(),anyString())).thenReturn(existingEtudiant);
        int result = etudiantService.createEtudiant("NomExistant", "PrénomExistant", 1);

        verify(etudiantDAO, times(0)).create(any(Etudiant.class));
        assertEquals(1, result);
    }

    // Autres tests pour les méthodes comme deleteEtudiantById, listEtudiants, updateEtudiant, getEtudiantDetails, etc.
}
