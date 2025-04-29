import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionsDAO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsServiceImp;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la LÓGICA DE NEGOCIO de ShortQuestionsService.
 * Se mockean las dependencias externas (DAO, TransactionManager) para aislar el servicio.
 */
class ShortQuestionsServiceTest {

    private ShortQuestionsDAO mockShortQuestionsDAO;
    private Transaction mockTransaction;

    private MockedStatic<DAOFactory> mockedDaoFactory;
    private MockedStatic<TransactionManager> mockedTransactionManagerStatic;

    private ShortQuestionsServiceImp shortQuestionsService; // <-- CORRECCIÓN: Usar la clase concreta

    private static final int EXPECTED_QUESTIONS_COUNT = ShortQuestionsServiceImp.QUESTIONS;

    @BeforeEach
    void setUp() throws Exception { //

        mockShortQuestionsDAO = mock(ShortQuestionsDAO.class);
        TransactionManager mockTransactionManagerInstance = mock(TransactionManager.class);
        mockTransaction = mock(Transaction.class);

        mockedDaoFactory = mockStatic(DAOFactory.class);
        mockedTransactionManagerStatic = mockStatic(TransactionManager.class);

        mockedDaoFactory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockShortQuestionsDAO);
        mockedTransactionManagerStatic.when(TransactionManager::getInstance).thenReturn(mockTransactionManagerInstance);

        when(mockTransactionManagerInstance.nuevaTransaccion()).thenReturn(mockTransaction);

        doNothing().when(mockTransaction).start();
        doNothing().when(mockTransaction).commit();
        doNothing().when(mockTransaction).rollback();

        shortQuestionsService = new ShortQuestionsServiceImp();
    }

    @AfterEach
    void tearDown() {
        mockedDaoFactory.close();
        mockedTransactionManagerStatic.close();
    }

    // =========================================================================
    // == Pruebas para la lógica de checkAnswers
    // =========================================================================

    @Test
    @DisplayName("[checkAnswers] Lógica: Respuestas correctas (insensible a mayúsculas)")
    void checkAnswers_Logic_CorrectAnswers_ReturnsTrueForAll() throws Exception { // <-- CORRECCIÓN: throws Exception
        // Arrange
        Map<Integer, String> userAnswers = Map.of(1, "RespuestaUno", 2, "RESPUESTA DOS");
        Set<Integer> preguntasIds = Set.of(1, 2);
        Map<Integer, Boolean> expectedResults = Map.of(1, true, 2, true);
        when(mockShortQuestionsDAO.getRespuestaById(1)).thenReturn("RespuestaUno");
        when(mockShortQuestionsDAO.getRespuestaById(2)).thenReturn("respuesta dos");
        // Act
        Map<Integer, Boolean> actualResults = shortQuestionsService.checkAnswers(userAnswers, preguntasIds);
        // Assert
        assertEquals(expectedResults, actualResults);
        verify(mockTransaction).start();
        verify(mockShortQuestionsDAO).getRespuestaById(1);
        verify(mockShortQuestionsDAO).getRespuestaById(2);
        verify(mockTransaction).commit();
        verify(mockTransaction, never()).rollback();
    }

    @Test
    @DisplayName("[checkAnswers] Lógica: Respuestas incorrectas")
    void checkAnswers_Logic_IncorrectAnswers_ReturnsFalseForIncorrect() throws Exception { // <-- CORRECCIÓN: throws Exception
        // Arrange
        Map<Integer, String> userAnswers = Map.of(1, "Correcta", 2, "Incorrecta");
        Set<Integer> preguntasIds = Set.of(1, 2);
        Map<Integer, Boolean> expectedResults = Map.of(1, true, 2, false);
        when(mockShortQuestionsDAO.getRespuestaById(1)).thenReturn("Correcta");
        when(mockShortQuestionsDAO.getRespuestaById(2)).thenReturn("RespuestaRealDiferente");
        // Act
        Map<Integer, Boolean> actualResults = shortQuestionsService.checkAnswers(userAnswers, preguntasIds);
        // Assert
        assertEquals(expectedResults, actualResults);
        verify(mockTransaction).commit();
    }

    @Test
    @DisplayName("[checkAnswers] Lógica: Respuesta nula del usuario resulta en false")
    void checkAnswers_Logic_UserAnswerIsNull_ReturnsFalse() throws Exception {
        // Arrange: Preparar el escenario donde el usuario envía null
        Set<Integer> preguntasIds = Set.of(1, 2); // IDs a verificar


        Map<Integer, String> userAnswers = new HashMap<>();
        userAnswers.put(1, "RespuestaValida"); // Un caso válido
        userAnswers.put(2, null);             // <-- Caso clave: Usuario envió null para ID 2

        // Resultado esperado: false para la respuesta null
        Map<Integer, Boolean> expectedResults = Map.of(
                1, true,
                2, false // Se espera false porque el valor era null
        );

        // Configurar el mock del DAO para las IDs donde se espera llamada (1 y 2)
        when(mockShortQuestionsDAO.getRespuestaById(1)).thenReturn("RespuestaValida");
        when(mockShortQuestionsDAO.getRespuestaById(2)).thenReturn("RespuestaID2");


        Map<Integer, Boolean> actualResults = shortQuestionsService.checkAnswers(userAnswers, preguntasIds);

        assertEquals(expectedResults, actualResults, "La respuesta nula del usuario no resultó en false.");

        verify(mockShortQuestionsDAO).getRespuestaById(1);
        verify(mockShortQuestionsDAO).getRespuestaById(2);
        verify(mockTransaction).commit();
        verify(mockTransaction, never()).rollback();
    }


    // =========================================================================
    // == Pruebas para la lógica de getShortQuestions
    // =========================================================================

    private List<ShortQuestionDTO> createMockQuestions(int tema, int count) {
        List<ShortQuestionDTO> questions = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            int id = (tema * 100) + i;

            String textoPregunta = "Pregunta " + id;
            questions.add(new ShortQuestionDTO(id, textoPregunta));

        }
        return questions;
    }

    @Test
    @DisplayName("[getShortQuestions] Lógica: Selección del número correcto de preguntas")
    void getShortQuestions_Logic_SelectsCorrectNumberOfQuestions() throws Exception {
        // Arrange
        int tema = 1;
        List<ShortQuestionDTO> allQuestionsFromDao = createMockQuestions(tema, EXPECTED_QUESTIONS_COUNT + 5);
        when(mockShortQuestionsDAO.getAllQuestions(tema)).thenReturn(allQuestionsFromDao);
        // Act
        List<ShortQuestionDTO> actualQuestions = shortQuestionsService.getShortQuestions(tema);
        // Assert
        assertNotNull(actualQuestions);
        assertEquals(EXPECTED_QUESTIONS_COUNT, actualQuestions.size());
        assertTrue(allQuestionsFromDao.containsAll(actualQuestions));
        verify(mockTransaction).start();
        verify(mockShortQuestionsDAO).getAllQuestions(tema);
        verify(mockTransaction).commit();
        verify(mockTransaction, never()).rollback();
    }

}
