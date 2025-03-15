import com.scrumsquad.taskmaster.lib.transactions.TransactionImp;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class TransactionTest {
    @Mock
    TransactionImp transaction;
    @Mock
    Connection con;

    @BeforeEach
    void setUp() {
        transaction = spy(new TransactionImp());
        con = mock(Connection.class);
        doReturn(con).when(transaction).getResource();
    }

    @Test
    void testStart() throws Exception {
        try (MockedStatic<DriverManager> dm = mockStatic(DriverManager.class)) {
            dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(con);
            transaction.start();
            verify(con).setAutoCommit(false);
        }
    }

    @Test
    void testCommit() throws Exception {
        doNothing().when(con).commit();
        doNothing().when(con).close();
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            TransactionManager tmImp = mock(TransactionManager.class);
            tm.when(TransactionManager::getInstance).thenReturn(tmImp);
            transaction.commit();
            verify(con).commit();
            verify(con).close();
            verify(tmImp).eliminaTransaccion();
        }
    }

    @Test
    void testRollback() throws Exception {
        doNothing().when(con).rollback();
        doNothing().when(con).close();
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            TransactionManager tmImp = mock(TransactionManager.class);
            tm.when(TransactionManager::getInstance).thenReturn(tmImp);
            transaction.rollback();
            verify(con).rollback();
            verify(con).close();
            verify(tmImp).eliminaTransaccion();
        }
    }
}
