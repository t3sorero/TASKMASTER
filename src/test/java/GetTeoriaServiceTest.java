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
        // Mockeamos los métodos estáticos
        mockTM = mockStatic(TransactionManager.class);
        mockDF = mockStatic(DAOFactory.class);

        // Creamos mocks de Transaction y TeoriaDAO
        mockTransaction = mock(Transaction.class);
        mockTeoriaDAO = mock(TeoriaDao.class);

        // Simulamos que TransactionManager.getInstance() devuelve un objeto con nuevaTransaccion()
        TransactionManager mockTransactionManager = mock(TransactionManager.class);
        when(mockTransactionManager.nuevaTransaccion()).thenReturn(mockTransaction);
        mockTM.when(TransactionManager::getInstance).thenReturn(mockTransactionManager);

        // Simulamos que DAOFactory.getTeoriaDAO() devuelve nuestro mockTeoriaDAO
        mockDF.when(DAOFactory::getTeoriaDAO).thenReturn(mockTeoriaDAO);

        // Instanciamos el servicio
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

            // Simulamos que el DAO devuelve la teoría esperada
            when(mockTeoriaDAO.getTeoria(tema)).thenReturn(teoriaEsperada);

            // Ejecutamos el metodo
            String result = service.getTeoria(tema);

            // Verificamos el flujo correcto
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

        // Simulamos que el DAO lanza una excepción
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
