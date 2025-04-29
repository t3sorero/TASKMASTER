import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionsDAO;
import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsService;
import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShortQuestionsServiceImpIntegrationIT {

    @Test
    void testGetShortQuestions_success() throws Exception {
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            ShortQuestionsDAO mockDao = mock(ShortQuestionsDAO.class);
            factory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockDao);

            List<ShortQuestionDTO> allQuestions = new ArrayList<>();
            for (int i = 0; i < ShortQuestionsServiceImp.QUESTIONS; i++) {
                ShortQuestionDTO dto = new ShortQuestionDTO(i, "Pregunta " + i);
                allQuestions.add(dto);
            }
            when(mockDao.getAllQuestions(1)).thenReturn(allQuestions);

            ShortQuestionsService service = new ShortQuestionsServiceImp();
            List<ShortQuestionDTO> result = service.getShortQuestions(1);

            assertNotNull(result, "La lista de preguntas no debe ser nula");
            assertEquals(ShortQuestionsServiceImp.QUESTIONS, result.size(),
                    "Debe devolver exactamente " + ShortQuestionsServiceImp.QUESTIONS + " preguntas");

            assertTrue(allQuestions.containsAll(result), "El resultado debe contener las preguntas esperadas");
        }
    }

    @Test
    void testGetShortQuestions_empty() throws Exception {
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            ShortQuestionsDAO mockDao = mock(ShortQuestionsDAO.class);
            factory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockDao);
            when(mockDao.getAllQuestions(1)).thenReturn(Collections.emptyList());

            ShortQuestionsService service = new ShortQuestionsServiceImp();
            List<ShortQuestionDTO> result = service.getShortQuestions(1);

            assertNotNull(result, "La lista devuelta no debe ser nula cuando no hay preguntas");
            assertTrue(result.isEmpty(), "La lista debe estar vacía si el DAO no devuelve ninguna pregunta");
        }
    }

    @Test
    void testCheckAnswers_allCorrect() throws Exception {
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            ShortQuestionsDAO mockDao = mock(ShortQuestionsDAO.class);
            factory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockDao);

            Map<Integer, String> userAnswers = new HashMap<>();
            Set<Integer> ids = new HashSet<>(Arrays.asList(1, 2, 3));
            for (int id : ids) {
                userAnswers.put(id, "respuesta" + id);
                when(mockDao.getRespuestaById(id)).thenReturn("respuesta" + id);
            }

            ShortQuestionsService service = new ShortQuestionsServiceImp();
            Map<Integer, Boolean> results = service.checkAnswers(userAnswers, ids);

            assertNotNull(results, "El mapa de resultados no debe ser nulo");
            assertEquals(ids.size(), results.size(), "Debe evaluar todas las preguntas");
            for (Boolean correct : results.values()) {
                assertTrue(correct, "Todas las respuestas deben considerarse correctas");
            }
        }
    }

    @Test
    void testCheckAnswers_emptyIds() throws Exception {
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            ShortQuestionsDAO mockDao = mock(ShortQuestionsDAO.class);
            factory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockDao);

            Map<Integer, String> userAnswers = Map.of(1, "respuesta1");
            Set<Integer> ids = Collections.emptySet();

            ShortQuestionsService service = new ShortQuestionsServiceImp();
            Map<Integer, Boolean> results = service.checkAnswers(userAnswers, ids);

            assertNotNull(results, "El mapa devuelto no debe ser nulo si no hay IDs");
            assertTrue(results.isEmpty(), "El mapa debe estar vacío cuando no hay IDs a evaluar");
        }
    }

    @Test
    void testCheckAnswers_incorrectAndMissing() throws Exception {
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            ShortQuestionsDAO mockDao = mock(ShortQuestionsDAO.class);
            factory.when(DAOFactory::getShortQuestionsDAO).thenReturn(mockDao);

            Map<Integer, String> userAnswers = new HashMap<>();
            Set<Integer> ids = new HashSet<>(Arrays.asList(1, 2, 3));
            userAnswers.put(1, "mala");
            userAnswers.put(3, "buena3");

            when(mockDao.getRespuestaById(1)).thenReturn("buena1");
            when(mockDao.getRespuestaById(3)).thenReturn("buena3");

            ShortQuestionsService service = new ShortQuestionsServiceImp();
            Map<Integer, Boolean> results = service.checkAnswers(userAnswers, ids);

            assertNotNull(results, "El mapa de resultados no debe ser nulo");
            assertEquals(ids.size(), results.size(), "Se deben incluir todas las preguntas en los resultados");
            assertFalse(results.get(1), "La respuesta 1 debe considerarse incorrecta");
            assertFalse(results.get(2), "La respuesta 2 debe considerarse faltante y por ende incorrecta");
            assertTrue(results.get(3), "La respuesta 3 debe considerarse correcta");
        }
    }
}
