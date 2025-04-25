import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.database.quiz.QuizDAO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.services.quizscrum.QuizService;
import com.scrumsquad.taskmaster.services.quizscrum.QuizServiceImp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    private MockedStatic<TransactionManager> mockTM;
    private MockedStatic<DAOFactory> mockDF;

    private QuizDAOFake daoFake;

    @BeforeEach
    public void setUp() {
        mockTM = mockStatic(TransactionManager.class);
        mockDF = mockStatic(DAOFactory.class);
        daoFake = new QuizDAOFake();

        mockTM.when(TransactionManager::getInstance).thenReturn(new TransactionManagerFake());
        mockDF.when(DAOFactory::getQuizDAO).thenReturn(daoFake);
    }

    @AfterEach
    public void tearDown() {
        mockTM.close();
        mockDF.close();
        daoFake = null;
    }

    @Test
    public void testGetPreguntas() throws Exception {
        QuizService service = new QuizServiceImp();
        Map<Integer, PreguntaQuizDTO> result = service.getPreguntas();

        assertNotNull(result, "El mapa de preguntas no debe ser nulo");
        assertEquals(10, result.size(), "Debe haber exactamente 10 preguntas (2 por nivel)");

        Set<Integer> ids = new HashSet<>();
        for (Map.Entry<Integer, PreguntaQuizDTO> entry : result.entrySet()) {
            PreguntaQuizDTO pregunta = entry.getValue();
            assertNotNull(pregunta.getPregunta(), "La pregunta no debe ser nula");
            assertEquals(4, pregunta.getOpciones().size(), "Debe tener 4 opciones");
            assertTrue(pregunta.getOpciones().contains(pregunta.getCorrecta()), "La respuesta correcta debe estar en las opciones");

            // Comprobamos que los IDs son únicos
            assertTrue(ids.add(pregunta.getId()), "Los IDs de las preguntas deben ser únicos");
        }
    }

    // DAO y Transaction mocks
    class TransactionManagerFake extends TransactionManager {
        @Override
        public Transaction nuevaTransaccion() {
            return new TransactionFake();
        }

        @Override
        public Transaction getTransaccion() {
            return new TransactionFake();
        }

        @Override
        public void eliminaTransaccion() {}
    }

    class TransactionFake implements Transaction {
        @Override public void start() {}
        @Override public void rollback() {}
        @Override public void commit() {}
        @Override public Connection getResource() { return null; }
    }

    class QuizDAOFake implements QuizDAO {
        @Override
        public List<PreguntaQuizDTO> getPreguntasNvl(int nivel) {
            List<PreguntaQuizDTO> lista = new ArrayList<>();
            for (int i = 1; i <= 4; i++) {
                List<String> opciones = List.of("Correcta " + i, "Incorrecta A", "Incorrecta B", "Incorrecta C");
                lista.add(new PreguntaQuizDTO(nivel * 10 + i, "Pregunta " + i + " nivel " + nivel, nivel, "Correcta " + i, opciones));
            }
            return lista;
        }
    }
}
