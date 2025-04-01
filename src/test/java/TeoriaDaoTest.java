
import com.scrumsquad.taskmaster.database.teoria.TeoriaDaoImp;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TeoriaDaoTest {
    TeoriaDaoImp dao;
    @Mock Transaction mockedTransaction;
    @Mock Connection mockedConnection;
    @Mock PreparedStatement mockedStatement;
    @Mock ResultSet mockedResultSet;
    @Mock TransactionManager mockedTransactionManager;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        dao = new TeoriaDaoImp();
        when(mockedTransaction.getResource()).thenReturn(mockedConnection);
        when(mockedConnection.prepareStatement(anyString())).thenReturn(mockedStatement);
        when(mockedStatement.executeQuery()).thenReturn(mockedResultSet);
    }

    @Test
    void testGetTeoriaReturnsText() throws Exception {
        String expected = "Test Theory";
        when(mockedResultSet.next()).thenReturn(true);
        when(mockedResultSet.getString("texto")).thenReturn(expected);
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            tm.when(TransactionManager::getInstance).thenReturn(mockedTransactionManager);
            when(mockedTransactionManager.getTransaccion()).thenReturn(mockedTransaction);
            String result = dao.getTeoria(1);
            assertEquals(expected, result);
        }
        verify(mockedStatement).setInt(eq(1), eq(1));
        verify(mockedStatement).executeQuery();
        verify(mockedResultSet).close();
        verify(mockedStatement).close();
    }

    @Test
    void testGetTeoriaNoResult() throws Exception {
        when(mockedResultSet.next()).thenReturn(false);
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            tm.when(TransactionManager::getInstance).thenReturn(mockedTransactionManager);
            when(mockedTransactionManager.getTransaccion()).thenReturn(mockedTransaction);
            String result = dao.getTeoria(1);
            assertEquals("-1", result);
        }
    }

    @Test
    void testGetTeoriaException() throws Exception {
        when(mockedConnection.prepareStatement(anyString())).thenThrow(new RuntimeException("Error"));
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            tm.when(TransactionManager::getInstance).thenReturn(mockedTransactionManager);
            when(mockedTransactionManager.getTransaccion()).thenReturn(mockedTransaction);
            String result = dao.getTeoria(1);
            assertNull(result);
        }
    }
}
