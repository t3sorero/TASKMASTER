    import com.scrumsquad.taskmaster.database.DAOFactory;
    import com.scrumsquad.taskmaster.database.DBData;
    import com.scrumsquad.taskmaster.database.concepto.ConceptoDAO;
    import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
    import com.scrumsquad.taskmaster.database.definicion.DefinicionDAO;
    import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
    import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;
    import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingServiceImp;
    import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.MockedStatic;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.util.Arrays;
    import java.util.Collections;
    import java.util.List;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    public class ConceptMatchingServiceImpIntegrationIT {

        @Test
        void testGetGameData_success() throws Exception {
            List<ConceptoDTO> conceptos = Arrays.asList(
                    new ConceptoDTO(1, "C1"),
                    new ConceptoDTO(2, "C2"),
                    new ConceptoDTO(3, "C3"),
                    new ConceptoDTO(4, "C4"),
                    new ConceptoDTO(5, "C5"),
                    new ConceptoDTO(6, "C6")
            );
            try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
                ConceptoDAO mockConceptoDAO = mock(ConceptoDAO.class);
                DefinicionDAO mockDefinicionDAO = mock(DefinicionDAO.class);
                factory.when(DAOFactory::getConceptoDAO).thenReturn(mockConceptoDAO);
                factory.when(DAOFactory::getDefinicionesDAO).thenReturn(mockDefinicionDAO);
                when(mockConceptoDAO.getAllConceptos(1)).thenReturn(conceptos);
                when(mockDefinicionDAO.getDefinicionesByConcepto(anyInt())).thenAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    return List.of(new DefinicionDTO(id * 10, "Definition for " + id, id));
                });
                ConceptMatchingService service = new ConceptMatchingServiceImp();
                ConceptosDefinicionesTOA result = service.getGameData(1);
                for (ConceptoDTO c : result.getConceptos()) {
                    assertTrue(result.getDefiniciones().stream().anyMatch(d -> d.getConceptoId() == c.getId()));
                }
                assertNotNull(result);
                assertFalse(result.getConceptos().isEmpty());
                assertFalse(result.getDefiniciones().isEmpty());
                assertEquals(4, result.getConceptos().size());
                assertEquals(6, result.getDefiniciones().size());
            }
        }

        @Test
        void testGetGameData_emptyDatabase() throws Exception {
            try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
                ConceptoDAO mockConceptoDAO = mock(ConceptoDAO.class);
                DefinicionDAO mockDefinicionDAO = mock(DefinicionDAO.class);
                factory.when(DAOFactory::getConceptoDAO).thenReturn(mockConceptoDAO);
                factory.when(DAOFactory::getDefinicionesDAO).thenReturn(mockDefinicionDAO);
                when(mockConceptoDAO.getAllConceptos(1)).thenReturn(Collections.emptyList());
                when(mockDefinicionDAO.getDefinicionesByConcepto(anyInt())).thenReturn(Collections.emptyList());
                ConceptMatchingService service = new ConceptMatchingServiceImp();
                ConceptosDefinicionesTOA result = service.getGameData(1);
                assertNotNull(result);
                assertTrue(result.getConceptos().isEmpty());
                assertTrue(result.getDefiniciones().isEmpty());
            }
        }
    }
