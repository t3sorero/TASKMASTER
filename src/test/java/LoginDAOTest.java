import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.login.LoginDAO;
import com.scrumsquad.taskmaster.database.login.LoginDAOImp;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginDAOTest {

    private MockedStatic<TransactionManager> tm;

    @BeforeEach
    void setUp() {
        tm = mockStatic(TransactionManager.class);
    }

    @AfterEach
    void tearDown() {
        tm.close();
    }

    @Test
    void testCorrectCredentials() throws Exception {

        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        LoginDAO loginDAO = DAOFactory.getLoginDAO();
        boolean isValid = loginDAO.validCredentials("usuario1@ucm.es", "contrasenia");

        assertTrue(isValid);
        verify(ps).setString(1, "usuario1@ucm.es");
        verify(ps).setString(2, "contrasenia");
        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testIncorrectCredentials() throws Exception {

        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        LoginDAO loginDAO = DAOFactory.getLoginDAO();
        boolean isValid = loginDAO.validCredentials("usuario1@ucm.es", "contraseniaMal");

        assertFalse(isValid);
        verify(ps).setString(1, "usuario1@ucm.es");
        verify(ps).setString(2, "contraseniaMal");
        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testDatabaseError() throws Exception {

        Connection con = mock(Connection.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Error en la Base de Datos"));

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        LoginDAO loginDAO = DAOFactory.getLoginDAO();

        Exception exception = assertThrows(SQLException.class, () -> {
            loginDAO.validCredentials("usuario1@ucm.es", "contrasenia");
        });

        assertEquals("Error en la Base de Datos", exception.getMessage());
    }
}