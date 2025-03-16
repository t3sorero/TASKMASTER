import com.scrumsquad.taskmaster.database.DBData;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingServiceImp;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

class ConceptMatchingServiceImpIntegrationTest {

    @Test
    void testGetGameData_success() throws Exception {

        // Llamar al servicio
        ConceptMatchingService conceptMatchingService = new ConceptMatchingServiceImp();
        ConceptosDefinicionesTOA result = conceptMatchingService.getGameData();

        for(ConceptoDTO c: result.getConceptos()){
            int i = 0;
            while(i < result.getDefiniciones().size() && result.getDefiniciones().get(i).getConceptoId() != c.getId()){
                i++;
            }
            assertTrue(i < result.getDefiniciones().size());
        }
        // Verificar que los conceptos y definiciones fueron obtenidos correctamente
        assertNotNull(result, "El resultado no debe ser nulo");
        assertFalse(result.getConceptos().isEmpty(), "Debe devolver al menos un concepto");
        assertFalse(result.getDefiniciones().isEmpty(), "Debe devolver al menos una definición");
        assertEquals(4, result.getConceptos().size(), "Debe seleccionar 4 conceptos");
        assertEquals(6, result.getDefiniciones().size(), "Debe seleccionar 6 definiciones");
    }

    @Test
    void testGetGameData_emptyDatabase() throws Exception { // VACIAR BASE DE DATOS PRIMERO

        // limpiarTabla("concepto");
        // limpiarTabla("definicion");

        // Base de datos vacía
        ConceptMatchingService conceptMatchingService = new ConceptMatchingServiceImp();
        ConceptosDefinicionesTOA result = conceptMatchingService.getGameData();

        // Verificar que el servicio maneje correctamente la base de datos vacía
        assertNotNull(result, "El resultado no debe ser nulo");
        assertTrue(result.getConceptos().isEmpty(), "No debe devolver conceptos si la base de datos está vacía");
        assertTrue(result.getDefiniciones().isEmpty(), "No debe devolver definiciones si la base de datos está vacía");
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
}
