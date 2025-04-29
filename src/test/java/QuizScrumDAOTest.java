import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.quiz.*;
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
public class QuizScrumDAOTest {

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
    void testGetPreguntasNvlReturnsList() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);

        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(10);
        when(rs.getString("pregunta")).thenReturn("Pregunta test");
        when(rs.getString("correcta")).thenReturn("Correcta test");
        when(rs.getString("incorrecta1")).thenReturn("Incorrecta 1");
        when(rs.getString("incorrecta2")).thenReturn("Incorrecta 2");
        when(rs.getString("incorrecta3")).thenReturn("Incorrecta 3");

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        QuizDAO dao = DAOFactory.getQuizDAO();
        List<PreguntaQuizDTO> result = dao.getPreguntasNvl(1);

        assertEquals(1, result.size());
        PreguntaQuizDTO pregunta = result.get(0);
        assertEquals(10, pregunta.getId());
        assertEquals("Pregunta test", pregunta.getPregunta());
        assertEquals("Correcta test", pregunta.getCorrecta());
        assertTrue(pregunta.getOpciones().contains("Incorrecta 1"));
        assertTrue(pregunta.getOpciones().contains("Incorrecta 2"));
        assertTrue(pregunta.getOpciones().contains("Incorrecta 3"));

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetPreguntasNvlReturnsEmptyList() throws Exception {
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

        QuizDAO dao = DAOFactory.getQuizDAO();
        List<PreguntaQuizDTO> result = dao.getPreguntasNvl(999);

        assertTrue(result.isEmpty());

        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetPreguntasNvlThrowsException() throws Exception {
        Connection con = mock(Connection.class);
        Transaction tx = mock(Transaction.class);

        when(tx.getResource()).thenReturn(con);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Error en DB"));

        TransactionManager txm = mock(TransactionManager.class);
        when(txm.getTransaccion()).thenReturn(tx);
        tm.when(TransactionManager::getInstance).thenReturn(txm);

        QuizDAO dao = DAOFactory.getQuizDAO();

        Exception ex = assertThrows(SQLException.class, () -> dao.getPreguntasNvl(1));
        assertEquals("Error en DB", ex.getMessage());
    }
}