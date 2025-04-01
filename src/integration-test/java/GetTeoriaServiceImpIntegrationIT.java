import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDao;
import com.scrumsquad.taskmaster.services.teoria.GetTeoriaService;
import com.scrumsquad.taskmaster.services.teoria.GetTeoriaServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetTeoriaServiceImpIntegrationIT {

    @Test
    void testGetTeoria_success(){
        String teoria = "Teoria prueba";
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)){
            TeoriaDao mockTeoriaDAO = mock(TeoriaDao.class);
            factory.when(DAOFactory::getTeoriaDAO).thenReturn(mockTeoriaDAO);
            when(mockTeoriaDAO.getTeoria(1)).thenReturn(teoria);
            GetTeoriaService service = new GetTeoriaServiceImp();
            String result = service.getTeoria(1);
            assertNotNull(result);
            assertFalse(result.isEmpty());
            assertEquals(teoria, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetTeoria_emptyDataBase(){
        try(MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)){
            TeoriaDao mockTeoriaDAO = mock(TeoriaDao.class);
            factory.when(DAOFactory::getTeoriaDAO).thenReturn(mockTeoriaDAO);
            when(mockTeoriaDAO.getTeoria(1)).thenReturn("");
            GetTeoriaService service = new GetTeoriaServiceImp();
            String result = service.getTeoria(1);
            assertNotNull(result);
            assertTrue(result.isEmpty());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
