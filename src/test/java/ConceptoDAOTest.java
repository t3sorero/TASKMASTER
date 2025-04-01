import com.scrumsquad.taskmaster.database.concepto.ConceptoDAO;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDAOImp;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConceptoDAOTest {

    private MockedStatic<DriverManager> dm;

    @BeforeEach
    void setUp() {
        dm = mockStatic(DriverManager.class);
    }

    @AfterEach
    void tearDown() {
        dm.close();
    }

    @Test
    void testGetConcepts() throws Exception {
        List<Integer> ids = List.of(1, 2);
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Array sqlArray = mock(Array.class);
        dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(con.createArrayOf(eq("integer"), any())).thenReturn(sqlArray);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, true, false);
        when(rs.getInt("id")).thenReturn(1, 2);
        when(rs.getString("nombre")).thenReturn("Concepto 1", "Concepto 2");
        ConceptoDAO dao = new ConceptoDAOImp();
        List<ConceptoDTO> list = dao.getConcepts(ids);
        assertEquals(2, list.size());
        verify(rs).close();
        verify(ps).close();
        verify(con).close();
    }

    @Test
    void testGetConceptoById() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(5);
        when(rs.getString("nombre")).thenReturn("Test");
        ConceptoDAO dao = new ConceptoDAOImp();
        ConceptoDTO dto = dao.getConceptoById(5);
        assertNotNull(dto);
        assertEquals(5, dto.getId());
        assertEquals("Test", dto.getNombre());
        verify(rs).close();
        verify(ps).close();
        verify(con).close();
    }

    @Test
    void testGetAllConceptos() throws Exception {
        Connection txCon = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);
        when(tx.getResource()).thenReturn(txCon);
        when(txCon.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(10);
        when(rs.getString("nombre")).thenReturn("TxConcepto");
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            TransactionManager tmImp = mock(TransactionManager.class);
            when(tmImp.getTransaccion()).thenReturn(tx);
            tm.when(TransactionManager::getInstance).thenReturn(tmImp);
            ConceptoDAO dao = new ConceptoDAOImp();
            List<ConceptoDTO> list = dao.getAllConceptos(1);
            assertEquals(1, list.size());
            assertEquals(10, list.get(0).getId());
        }
        verify(rs).close();
        verify(ps).close();
    }
}

