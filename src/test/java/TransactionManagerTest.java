import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManagerImp;
import com.scrumsquad.taskmaster.lib.transactions.TransactionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionManagerTest {
    private TransactionManagerImp tm;
    private MockedStatic<TransactionFactory> factoryMock;
    private TransactionFactory mockFactory;
    private Transaction txMock;

    @BeforeEach
    void setUp() {
        tm = new TransactionManagerImp();
        factoryMock = mockStatic(TransactionFactory.class);
        mockFactory = mock(TransactionFactory.class);
        txMock = mock(Transaction.class);
        when(mockFactory.newTransaction()).thenReturn(txMock);
        factoryMock.when(TransactionFactory::getInstance).thenReturn(mockFactory);
    }

    @AfterEach
    void tearDown() {
        factoryMock.close();
    }

    @Test
    void testNuevaTransaccion_Success() {
        Transaction tx1 = tm.nuevaTransaccion();
        assertNotNull(tx1);
        assertSame(txMock, tx1);
    }

    @Test
    void testNuevaTransaccion_DuplicateThrowsException() {
        tm.nuevaTransaccion();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> tm.nuevaTransaccion());
        assertTrue(ex.getMessage().contains("Ya hay un hilo"));
    }

    @Test
    void testGetTransaccion_NoActiveTransaction() {
        assertNull(tm.getTransaccion());
    }

    @Test
    void testGetTransaccion_AfterCreation() {
        Transaction tx1 = tm.nuevaTransaccion();
        Transaction tx2 = tm.getTransaccion();
        assertSame(tx1, tx2);
    }

    @Test
    void testEliminaTransaccion() {
        tm.nuevaTransaccion();
        tm.eliminaTransaccion();
        assertNull(tm.getTransaccion());
    }
}
