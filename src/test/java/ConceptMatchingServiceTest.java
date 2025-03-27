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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConceptMatchingServiceTest {

    private MockedStatic<TransactionManager> mockTM;
    private MockedStatic<DAOFactory> mockDF;

    private List<ConceptoDTO> conceptos;
    private List<DefinicionDTO> definiciones;

    //antes de cada Test
    @BeforeEach
    public void setUp(){
        conceptos = new ArrayList<>();
        definiciones = new ArrayList<>();
        int dId = 0;
        for (int i = 0; i < 10; i++) {
            conceptos.add(new ConceptoDTO(i, "Concepto " + i));
            definiciones.add(new DefinicionDTO(dId, "Definicion" + i, i));
            dId++;
            if (i % 2 == 0) {
                definiciones.add(new DefinicionDTO(dId, "Definicion" + i + ".2", i));
                dId++;
            }
        }
        mockTM = mockStatic(TransactionManager.class);
        mockDF = mockStatic(DAOFactory.class);
        mockTM.when(TransactionManager::getInstance).thenReturn(new TransactionManagerFake());
        mockDF.when(DAOFactory::getDefinicionesDAO).thenReturn(new DefinicionDAOFake(definiciones));
        mockDF.when(DAOFactory::getConceptoDAO).thenReturn(new ConceptoDAOFake(conceptos));
    }

    //limpiar despues de cada test
    @AfterEach
    public void tearDown(){
        mockTM.close();
        mockDF.close();
        definiciones.clear();
        conceptos.clear();
        mockTM = null;
        mockDF = null;
        definiciones = null;
        conceptos = null;
    }

    @Test
    public void testGetGameData() {
        try {
            // Simulación de la selección de un tema antes de obtener los datos
            int selectedTheme = 1; // Simulamos que se elige "Tema 1"

            ConceptosDefinicionesTOA result = ConceptMatchingService.getInstance().getGameData(selectedTheme);

            // Verificar que el resultado no es nulo
            assertNotNull(result, "El resultado no debe ser nulo");

            // Verificar que el número de conceptos y definiciones es el esperado
            assertEquals(4, result.getConceptos().size(), "Debe haber exactamente 4 conceptos seleccionados");
            assertEquals(6, result.getDefiniciones().size(), "Debe haber exactamente 6 definiciones seleccionadas");


            // Obtener los IDs de los conceptos seleccionados
            Set<Integer> conceptIds = new HashSet<>();
            for (ConceptoDTO concepto : result.getConceptos()) {
                conceptIds.add(concepto.getId());
            }

            // Verificar que los conceptos seleccionados no están duplicados
            Set<Integer> uniqueConceptIds = new HashSet<>();
            for (ConceptoDTO concepto : result.getConceptos()) {
                assertFalse(uniqueConceptIds.contains(concepto.getId()), "No debe haber conceptos duplicados");
                uniqueConceptIds.add(concepto.getId());
            }

            // Verificar que las definiciones seleccionadas no están duplicadas
            Set<Integer> uniqueDefinitionIds = new HashSet<>();
            for (DefinicionDTO definicion : result.getDefiniciones()) {
                assertFalse(uniqueDefinitionIds.contains(definicion.getId()), "No debe haber definiciones duplicadas");
                uniqueDefinitionIds.add(definicion.getId());
            }

            // Caso de tema inexistente: Se espera que devuelva listas vacías
            ConceptosDefinicionesTOA emptyResult = ConceptMatchingService.getInstance().getGameData(9999);
            assertNotNull(emptyResult);
            assertTrue(emptyResult.getConceptos().isEmpty(), "Si el tema no tiene conceptos, la lista debe estar vacía");
            assertTrue(emptyResult.getDefiniciones().isEmpty(), "Si el tema no tiene definiciones, la lista debe estar vacía");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testCheckAnswers() {
        Set<Integer> ids = new HashSet<>();
        Collections.shuffle(conceptos);
        Map<Integer, Integer> answers = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            ids.add(conceptos.get(i).getId());
        }
        ids.add(10);
        //no tiene definicion
        answers.put(10, 3);
        //respuesta correcta
        for(int j = 0; j < 2; j++){
            int i = 0;
            ConceptoDTO c = conceptos.get(j);
            while(i < definiciones.size() && definiciones.get(i).getConceptoId() != c.getId()){
                i++;
            }
            if(i < definiciones.size()){
                answers.put(c.getId(), definiciones.get(i).getId());
            }
        }
        //respuesta incorrecta concepto no coincide con definicion
        for(DefinicionDTO d: definiciones){
            if(d.getConceptoId() != conceptos.get(2).getId()){
                answers.put(conceptos.get(2).getId(), d.getId());
                break;
            }
        }
        try {
            Map<Integer, Boolean> result = ConceptMatchingService.getInstance().checkAnswers(answers,ids);
            assertNotNull(result);
            //Respuesta correcta
            assertEquals(Boolean.TRUE, result.get(conceptos.get(0).getId()));
            //Respuesta correcta
            assertEquals(Boolean.TRUE, result.get(conceptos.get(1).getId()));
            //Respuesta incorrecta incorrecta concepto no coincide con definicion
            assertEquals(Boolean.FALSE, result.get(conceptos.get(2).getId()));
            //Respuesta incorrecta no hay ninguna definicion relacionada con este
            assertEquals(Boolean.FALSE, result.get(10));
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
            for(DefinicionDTO d: definiciones){
                if(d.getId() == id){
                    return d;
                }
            }
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
        public List<ConceptoDTO> getAllConceptos(int tema) throws Exception {
            // Simulamos que solo los temas del 1 al 5 existen
            if (tema < 1 || tema > 5) {
                return Collections.emptyList();
            }

            // Filtrar solo los conceptos que pertenecen a este tema
            List<ConceptoDTO> conceptosPorTema = new ArrayList<>();
            for (ConceptoDTO c : conceptos) {
                conceptosPorTema.add(c);
            }

            return conceptosPorTema; //aqui tendria que devolver conceptosPorTema
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
