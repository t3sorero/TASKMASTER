import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.login.LoginDAO;
import com.scrumsquad.taskmaster.services.login.LoginService;
import com.scrumsquad.taskmaster.services.login.LoginServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceImpIntegrationIT {
    @Test
    void testIniciarSesion_success(){
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)){
            LoginDAO mockLoginDAO = mock(LoginDAO.class);
            factory.when(DAOFactory::getLoginDAO).thenReturn(mockLoginDAO);
            when(mockLoginDAO.validCredentials("email@test.com", "12345")).thenReturn(true);
            LoginService service = new LoginServiceImp();
            boolean result = service.loginValidation("email@test.com", "12345");
            assertTrue(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIniciarSesion_badCredentials(){
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)){
            LoginDAO mockLoginDAO = mock(LoginDAO.class);
            factory.when(DAOFactory::getLoginDAO).thenReturn(mockLoginDAO);
            when(mockLoginDAO.validCredentials("email@test.com", "1234567")).thenReturn(false);
            LoginService service = new LoginServiceImp();
            boolean result = service.loginValidation("email@test.com", "1234567");
            assertFalse(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
