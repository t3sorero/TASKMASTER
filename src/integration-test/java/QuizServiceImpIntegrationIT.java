import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.database.quiz.QuizDAO;
import com.scrumsquad.taskmaster.services.quizscrum.QuizServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceImpIntegrationIT {

    @Test
    void testGetPreguntasQuiz() throws Exception {
        // Arrange
        QuizDAO mockDao = mock(QuizDAO.class);
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            factory.when(DAOFactory::getQuizDAO).thenReturn(mockDao);

            // Simular preguntas por nivel
            for (int nivel = 1; nivel <= 5; nivel++) {
                List<PreguntaQuizDTO> preguntasNivel = new ArrayList<>();
                preguntasNivel.add(new PreguntaQuizDTO(2*nivel-1, "Pregunta A nivel " + nivel, nivel, "A",
                        List.of("A", "B", "C", "D"), "Pista A"));
                preguntasNivel.add(new PreguntaQuizDTO(2*nivel, "Pregunta B nivel " + nivel, nivel, "B",
                        List.of("A", "B", "C", "D"), "Pista B"));
                when(mockDao.getPreguntasNvl(nivel)).thenReturn(preguntasNivel);
            }

            QuizServiceImp service = new QuizServiceImp();
            Map<Integer, PreguntaQuizDTO> resultado = service.getPreguntas();
            //Comprobar que se devuelven 10 preguntas
            assertEquals(10, resultado.size(), "Debe haber exactamente 10 preguntas");

            // Verificar que hay 2 preguntas por nivel
            Map<Integer, Integer> conteoPorNivel = new HashMap<>();
            for (PreguntaQuizDTO pregunta : resultado.values()) {
                conteoPorNivel.put(pregunta.getNivel(), conteoPorNivel.getOrDefault(pregunta.getNivel(), 0) + 1);
            }

            for (int nivel = 1; nivel <= 5; nivel++) {
                int cantidad = conteoPorNivel.getOrDefault(nivel, 0);
                assertEquals(2, cantidad, "El nivel " + nivel + " debe tener exactamente 2 preguntas");
            }

            // Verificar llamadas al DAO
            for (int nivel = 1; nivel <= 5; nivel++) {
                verify(mockDao).getPreguntasNvl(nivel);
            }
        }
    }


    @Test
    void testGetPreguntas_databaseEmpty() throws Exception {
        QuizDAO mockDao = mock(QuizDAO.class);
        try (MockedStatic<DAOFactory> factory = mockStatic(DAOFactory.class)) {
            factory.when(DAOFactory::getQuizDAO).thenReturn(mockDao);

            for (int nivel = 1; nivel <= 5; nivel++) {
                when(mockDao.getPreguntasNvl(nivel)).thenReturn(Collections.emptyList());
            }

            QuizServiceImp service = new QuizServiceImp();

            assertThrows(Exception.class, service::getPreguntas);
        }
    }

}

