import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDao;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.services.teoria.GetTeoriaServiceImp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTeoriaServiceTest {

    private MockedStatic<TransactionManager> mockTM;
    private MockedStatic<DAOFactory> mockDF;

    private Transaction mockTransaction;
    private TeoriaDao mockTeoriaDAO;
    private GetTeoriaServiceImp service;

    @BeforeEach
    void setUp() {
        mockTM = mockStatic(TransactionManager.class);
        mockDF = mockStatic(DAOFactory.class);

        mockTransaction = mock(Transaction.class);
        mockTeoriaDAO = mock(TeoriaDao.class);

        TransactionManager mockTransactionManager = mock(TransactionManager.class);
        when(mockTransactionManager.nuevaTransaccion()).thenReturn(mockTransaction);
        mockTM.when(TransactionManager::getInstance).thenReturn(mockTransactionManager);

        mockDF.when(DAOFactory::getTeoriaDAO).thenReturn(mockTeoriaDAO);

        service = new GetTeoriaServiceImp();
    }

    @AfterEach
    void tearDown() {
        mockTM.close();
        mockDF.close();
        mockTM = null;
        mockDF = null;
    }

    @Test
    void testGetTeoria_Success()  {
        try{
            int tema = 1;
            String teoriaEsperada = "Teoría de prueba";

            when(mockTeoriaDAO.getTeoria(tema)).thenReturn(teoriaEsperada);

            String result = service.getTeoria(tema);

            verify(mockTransaction).start();
            verify(mockTransaction).commit();
            assertEquals(teoriaEsperada, result);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Test
    void testGetTeoria_Exception() {
        int tema = 1;

        // Simular DAO Excepcion
        try {
            when(mockTeoriaDAO.getTeoria(tema)).thenThrow(new RuntimeException("Error en DAO"));
            // Verificamos que la excepción se propaga y que se hace rollback
            Exception exception = assertThrows(RuntimeException.class, () -> service.getTeoria(tema));
            assertEquals("Error en DAO", exception.getMessage());

            verify(mockTransaction).start();
            verify(mockTransaction).rollback();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
