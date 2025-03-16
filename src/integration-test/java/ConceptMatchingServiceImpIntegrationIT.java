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
                when(mockConceptoDAO.getAllConceptos()).thenReturn(conceptos);
                when(mockDefinicionDAO.getDefinicionesByConcepto(anyInt())).thenAnswer(invocation -> {
                    Integer id = invocation.getArgument(0);
                    return List.of(new DefinicionDTO(id * 10, "Definition for " + id, id));
                });
                ConceptMatchingService service = new ConceptMatchingServiceImp();
                ConceptosDefinicionesTOA result = service.getGameData();
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
                when(mockConceptoDAO.getAllConceptos()).thenReturn(Collections.emptyList());
                when(mockDefinicionDAO.getDefinicionesByConcepto(anyInt())).thenReturn(Collections.emptyList());
                ConceptMatchingService service = new ConceptMatchingServiceImp();
                ConceptosDefinicionesTOA result = service.getGameData();
                assertNotNull(result);
                assertTrue(result.getConceptos().isEmpty());
                assertTrue(result.getDefiniciones().isEmpty());
            }
        }

        private void limpiarTabla(String nombreTabla) {

                try {
                    Connection c = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD );
                    PreparedStatement ps;

                    ps = c.prepareStatement("DELETE FROM " + nombreTabla);
                    ps.execute();

                    ps.close();
                    c.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }

        private void insertarDatos() {

            // Crear conceptos
            ConceptoDTO[] conceptos = new ConceptoDTO[]{
                    new ConceptoDTO(3, "MBTI"),
                    new ConceptoDTO(4, "DISC"),
                    new ConceptoDTO(5, "Roles de Belbin"),
                    new ConceptoDTO(6, "Roles Mentales"),
                    new ConceptoDTO(7, "Roles de Acción"),
                    new ConceptoDTO(8, "Roles Sociales"),
                    new ConceptoDTO(9, "Coordinador"),
                    new ConceptoDTO(10, "Cerebro"),
                    new ConceptoDTO(11, "Monitor Evaluador"),
                    new ConceptoDTO(12, "Impulsor"),
                    new ConceptoDTO(13, "Especialista"),
                    new ConceptoDTO(14, "Implementador"),
                    new ConceptoDTO(15, "Finalizador"),
                    new ConceptoDTO(16, "Investigador de recursos"),
                    new ConceptoDTO(17, "Cohesionador"),
                    new ConceptoDTO(18, "Modelo de Tuckman")
            };

            // Crear definiciones
            DefinicionDTO[] definiciones = new DefinicionDTO[]{
                    new DefinicionDTO(1, "Indicador de personalidad del que podemos obtener 16 tipos de personalidad diferentes", 3),
                    new DefinicionDTO(2, "Representa en un círculo cuatro cuadrantes que representan 4 estilos de personalidad", 4),
                    new DefinicionDTO(3, "Mentales, de Acción y Sociales", 5),
                    new DefinicionDTO(4, "Roles orientados hacia el mundo de las ideas", 6),
                    new DefinicionDTO(5, "Cerebro, Monitor Evaluador y Especialista", 6),
                    new DefinicionDTO(6, "Roles orientados al desempeño de tareas", 7),
                    new DefinicionDTO(7, "Impulsor, Implementador y Finalizador", 7),
                    new DefinicionDTO(8, "Roles orientados hacia las relaciones con las personas", 8),
                    new DefinicionDTO(9, "Coordinador, Investigador de recursos y Cohesionador", 8),
                    new DefinicionDTO(10, "Necesario para dirigir y desarrollar a los miembros del equipo", 9),
                    new DefinicionDTO(11, "Aglomera al equipo entorno al objetivo, pero no lo hace todo", 9),
                    new DefinicionDTO(12, "Genera nuevas propuestas y resuelve problemas complejos", 10),
                    new DefinicionDTO(13, "Clave en puestos de planificación y estrategia", 11),
                    new DefinicionDTO(14, "Evalúa las ideas y sugerencias, analizando pros y contras", 11),
                    new DefinicionDTO(15, "Inyecta energía al equipo en situaciones de estancamiento o crisis", 12),
                    new DefinicionDTO(16, "Ayuda al equipo de manera puntual", 13),
                    new DefinicionDTO(17, "Hacen lo que se necesite hacer, independientemente de que les guste o no", 14),
                    new DefinicionDTO(18, "Aportan un sentido de urgencia al equipo", 15),
                    new DefinicionDTO(19, "Bueno cumpliendo plazos", 15),
                    new DefinicionDTO(20, "El mejor para iniciar contactos externos, para hallar recursos fuera del grupo", 16),
                    new DefinicionDTO(21, "Escucha e impide los enfrentamientos", 17),
                    new DefinicionDTO(22, "Facilita el dialogo y crea una buena atmosfera de trabajo", 17),
                    new DefinicionDTO(23, "Formación, Conflicto, Normalización, Desempeño y Disolución", 18),
                    new DefinicionDTO(24, "Describe cómo los equipos evolucionan a lo largo del tiempo en 5 fases", 18)
            };

            // Conectar a la base de datos y realizar las inserciones
            try (Connection connection = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD)) {
                // Inserción de conceptos
                String insertConceptoSQL = "INSERT INTO concepto (id, nombre) VALUES (?, ?)";
                try (PreparedStatement psConcepto = connection.prepareStatement(insertConceptoSQL)) {
                    for (ConceptoDTO concepto : conceptos) {
                        psConcepto.setInt(1, concepto.getId());
                        psConcepto.setString(2, concepto.getNombre());
                        psConcepto.executeUpdate();
                    }
                }

                // Inserción de definiciones
                String insertDefinicionSQL = "INSERT INTO definicion (id, descripcion, id_concepto) VALUES (?, ?, ?)";
                try (PreparedStatement psDefinicion = connection.prepareStatement(insertDefinicionSQL)) {
                    for (DefinicionDTO definicion : definiciones) {
                        psDefinicion.setInt(1, definicion.getId());
                        psDefinicion.setString(2, definicion.getDescripcion());
                        psDefinicion.setInt(3, definicion.getConceptoId());
                        psDefinicion.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
