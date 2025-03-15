import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDAO;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDAOImp;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAOImp;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionImp;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManagerImp;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConceptMatchingServiceTest {

    @Test
    public void testGetGameData() {
        List<ConceptoDTO> conceptos = new ArrayList<>();
        List<DefinicionDTO> definiciones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            conceptos.add(new ConceptoDTO(i, "Concepto " + i));
            definiciones.add(new DefinicionDTO(i, "Definicion" + i, i));
            if (i % 2 == 0) {
                definiciones.add(new DefinicionDTO(i, "Definicion" + i + ".2", i));
            }
        }

        try(MockedStatic<TransactionManager> mockTM = mockStatic(TransactionManager.class)){
            mockTM.when(TransactionManager::getInstance).thenReturn(new TransactionManagerFake());
        }

        try(MockedStatic<DAOFactory> mockDF = mockStatic(DAOFactory.class)){
            mockDF.when(DAOFactory::getDefinicionesDAO).thenReturn(new DefinicionDAOFake(definiciones));
            mockDF.when(DAOFactory::getConceptoDAO).thenReturn(new ConceptoDAOFake(conceptos));
        }
        try {
            ConceptosDefinicionesTOA result = ConceptMatchingService.getInstance().getGameData();
            assertNotNull(result);
            assertEquals(4, result.getConceptos().size());
            assertEquals(6, result.getDefiniciones().size());
            for(ConceptoDTO c: result.getConceptos()){
                int i = 0;
                while(i < result.getDefiniciones().size() && result.getDefiniciones().get(i).getConceptoId() != c.getId()){
                    i++;
                }
                assertTrue(i < result.getDefiniciones().size());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    class TransactionManagerFake extends TransactionManager {
        @Override
        public Transaction nuevaTransaccion() {
            return new TransactionImpFake();
        }

        @Override
        public Transaction getTransaccion() {
            return new TransactionImpFake();
        }

        @Override
        public void eliminaTransaccion() throws Exception {

        }
    }

    class TransactionImpFake implements Transaction {
        @Override
        public void start() {
        }

        @Override
        public void rollback() {

        }

        @Override
        public Connection getResource() {
            return null;
        }

        @Override
        public void commit() {
        }
    }

    class DefinicionDAOFake implements DefinicionDAO {
        private List<DefinicionDTO> definiciones;

        public DefinicionDAOFake(List<DefinicionDTO> definiciones) {
            this.definiciones = definiciones;
        }

        @Override
        public List<DefinicionDTO> getMatches(List<Integer> ids) {
            return List.of();
        }

        @Override
        public List<DefinicionDTO> getDefinicionesByConcepto(int conceptoId) throws Exception {
            List<DefinicionDTO> d = new ArrayList<>();
            for (DefinicionDTO def : definiciones) {
                if (def.getConceptoId() == conceptoId) {
                    d.add(def);
                }
            }
            return d;
        }

        @Override
        public List<DefinicionDTO> getAllDefiniciones() {
            return List.of();
        }

        @Override
        public DefinicionDTO getDefinicionById(int id) {
            return null;
        }
    }

    class ConceptoDAOFake implements ConceptoDAO {

        private List<ConceptoDTO> conceptos;
        public ConceptoDAOFake(List<ConceptoDTO> conceptos) {
            this.conceptos = conceptos;
        }

        @Override
        public List<ConceptoDTO> getConcepts(List<Integer> ids) {
            return List.of();
        }

        @Override
        public List<ConceptoDTO> getAllConceptos() throws Exception {
            return conceptos;
        }

        @Override
        public ConceptoDTO getConceptoById(Integer key) {
            for(ConceptoDTO c: conceptos){
                if(c.getId() == key){
                    return c;
                }
            }
            return null;
        }
    }
}
