import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.shortquestions.*;
import com.scrumsquad.taskmaster.lib.transactions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortQuestionsDAOTest {

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
    void testGetAllQuestionsReturnsList() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("pregunta")).thenReturn("¿Qué es Java?");

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();
        List<ShortQuestionDTO> result = dao.getAllQuestions(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("¿Qué es Java?", result.get(0).getPregunta());

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetAllQuestionsReturnsEmptyList() throws Exception {
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

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();
        List<ShortQuestionDTO> result = dao.getAllQuestions(99);

        assertTrue(result.isEmpty());

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetAllQuestionsThrowsException() throws Exception {
        Connection con = mock(Connection.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Error DB"));

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();

        Exception ex = assertThrows(SQLException.class, () -> dao.getAllQuestions(1));
        assertEquals("Error DB", ex.getMessage());
    }

    @Test
    void testGetRespuestaByIdReturnsAnswer() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("respuesta")).thenReturn("Una clase es una plantilla para objetos.");

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();
        String respuesta = dao.getRespuestaById(2);

        assertEquals("Una clase es una plantilla para objetos.", respuesta);

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetRespuestaByIdReturnsNull() throws Exception {
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

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();
        String respuesta = dao.getRespuestaById(999);

        assertNull(respuesta);

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetRespuestaByIdThrowsException() throws Exception {
        Connection con = mock(Connection.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Error inesperado"));

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        ShortQuestionsDAO dao = DAOFactory.getShortQuestionsDAO();

        Exception ex = assertThrows(SQLException.class, () -> dao.getRespuestaById(5));
        assertEquals("Error inesperado", ex.getMessage());
    }
}