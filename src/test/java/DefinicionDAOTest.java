import com.scrumsquad.taskmaster.database.definicion.DefinicionDAO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAOImp;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
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
public class DefinicionDAOTest {

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
    void testGetMatches() throws Exception {
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
        when(rs.getString("definicion")).thenReturn("Def 1", "Def 2");
        when(rs.getInt("n_concepto")).thenReturn(10, 20);
        DefinicionDAO dao = new DefinicionDAOImp();
        List<DefinicionDTO> list = dao.getMatches(ids);
        assertEquals(2, list.size());
        verify(rs).close();
        verify(ps).close();
        verify(con).close();
    }

    @Test
    void testGetDefinicionesByConcepto() throws Exception {
        Connection txCon = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        Transaction tx = mock(Transaction.class);
        when(tx.getResource()).thenReturn(txCon);
        when(txCon.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(3);
        when(rs.getString("descripcion")).thenReturn("Desc");
        when(rs.getInt("id_concepto")).thenReturn(30);
        try (MockedStatic<TransactionManager> tm = mockStatic(TransactionManager.class)) {
            TransactionManager tmImp = mock(TransactionManager.class);
            when(tmImp.getTransaccion()).thenReturn(tx);
            tm.when(TransactionManager::getInstance).thenReturn(tmImp);
            DefinicionDAO dao = new DefinicionDAOImp();
            List<DefinicionDTO> list = dao.getDefinicionesByConcepto(30);
            assertEquals(1, list.size());
            assertEquals(3, list.get(0).getId());
        }
        verify(rs).close();
        verify(ps).close();
    }

    @Test
    void testGetAllDefiniciones() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(7);
        when(rs.getString("definicion")).thenReturn("AllDef");
        when(rs.getInt("n_concepto")).thenReturn(70);
        DefinicionDAO dao = new DefinicionDAOImp();
        List<DefinicionDTO> list = dao.getAllDefiniciones();
        assertEquals(1, list.size());
        assertEquals(7, list.get(0).getId());
        verify(rs).close();
        verify(ps).close();
        verify(con).close();
    }

    @Test
    void testGetDefinicionById() throws Exception {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        dm.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(con);
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("id")).thenReturn(5);
        when(rs.getString("descripcion")).thenReturn("DefById");
        when(rs.getInt("id_concepto")).thenReturn(50);
        DefinicionDAO dao = new DefinicionDAOImp();
        DefinicionDTO dto = dao.getDefinicionById(5);
        assertNotNull(dto);
        assertEquals(5, dto.getId());
        verify(rs).close();
        verify(ps).close();
        verify(con).close();
    }
}

