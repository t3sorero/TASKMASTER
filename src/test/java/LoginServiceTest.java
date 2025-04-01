import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.login.LoginDAO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.services.login.LoginServiceImp;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    private MockedStatic<TransactionManager> mockTM;
    private MockedStatic<DAOFactory> mockDF;

    private Transaction mockTransaction;
    private LoginDAO mockLoginDAO;
    private LoginServiceImp service;

    @BeforeEach
    void setUp() {

        mockTM = mockStatic(TransactionManager.class);
        mockDF = mockStatic(DAOFactory.class);


        mockTransaction = mock(Transaction.class);
        mockLoginDAO = mock(LoginDAO.class);


        TransactionManager mockTransactionManager = mock(TransactionManager.class);
        when(mockTransactionManager.nuevaTransaccion()).thenReturn(mockTransaction);
        mockTM.when(TransactionManager::getInstance).thenReturn(mockTransactionManager);


        mockDF.when(DAOFactory::getLoginDAO).thenReturn(mockLoginDAO);


        service = new LoginServiceImp();
    }

    @AfterEach
    void tearDown() {

        mockTM.close();
        mockDF.close();
    }

    @Test
    void testLoginValidation_Success_ValidCredentials() throws Exception {
        // Configurar el DAO para devolver credenciales válidas
        String email = "test@example.com";
        String password = "password123";
        when(mockLoginDAO.validCredentials(email, password)).thenReturn(true);


        boolean result = service.loginValidation(email, password);


        verify(mockTransaction).start();
        verify(mockTransaction).commit();
        assertTrue(result, "Las credenciales válidas deben devolver true");
    }

    @Test
    void testLoginValidation_Success_InvalidCredentials() throws Exception {
        // Configurar el DAO para devolver credenciales inválidas
        String email = "test@example.com";
        String password = "wrongpassword";
        when(mockLoginDAO.validCredentials(email, password)).thenReturn(false);


        boolean result = service.loginValidation(email, password);


        verify(mockTransaction).start();
        verify(mockTransaction).commit();
        assertFalse(result, "Las credenciales inválidas deben devolver false");
    }

    @Test
    void testLoginValidation_ExceptionInDAO() throws Exception {
        // Configurar el DAO para lanzar una excepción
        String email = "test@example.com";
        String password = "password123";
        when(mockLoginDAO.validCredentials(email, password)).thenThrow(new RuntimeException("Error en DAO"));


        Exception exception = assertThrows(RuntimeException.class, () -> service.loginValidation(email, password));
        assertEquals("Error en DAO", exception.getMessage());


        verify(mockTransaction).start();
        verify(mockTransaction).rollback();
    }
}
