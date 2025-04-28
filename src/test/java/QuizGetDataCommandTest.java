import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.quizscrum.QuizGetDataCommand;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.services.quizscrum.QuizService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

public class QuizGetDataCommandTest {

    private MockedStatic<QuizService> mockQuiz;

    @BeforeEach
    public void setUp() {
        mockQuiz = mockStatic(QuizService.class);
    }

    @AfterEach
    public void tearDown() {
        mockQuiz.close();
    }

    @Test
    public void testExecute_ok() {
        // Datos simulados para el fake
        Map<Integer, PreguntaQuizDTO> preguntas = new HashMap<>();
        preguntas.put(1, new PreguntaQuizDTO(1, "¿Qué es Scrum?", 1, "Marco de trabajo ágil",
                List.of("Marco de trabajo ágil", "Lenguaje de programación", "Base de datos", "Sistema operativo"), "pista"));
        preguntas.put(2, new PreguntaQuizDTO(2, "¿Quién es el Product Owner?", 1, "Responsable del valor del producto",
                List.of("Responsable del valor del producto", "Scrum Master", "Cliente", "CEO"), "pista"));

        // Inyectar el fake
        mockQuiz.when(QuizService::getInstance).thenReturn(new QuizServiceFake(preguntas));

        Context ctx = new Context(CommandName.quizScrumGetData);
        QuizGetDataCommand command = new QuizGetDataCommand();

        Context result = command.execute(ctx);

        assertTrue(result.getCommandName() == CommandName.quizScrumGetDataOk);
        assertTrue(result.getArguments().containsKey("preguntas"));
        assertTrue(result.getArguments().get("preguntas") instanceof Map);
    }

    @Test
    public void testExecute_ko() {
        mockQuiz.when(QuizService::getInstance).thenThrow(new RuntimeException("Error de servicio"));

        Context ctx = new Context(CommandName.quizScrumGetData);
        QuizGetDataCommand command = new QuizGetDataCommand();

        Context result = command.execute(ctx);

        assertTrue(result.getCommandName() == CommandName.quizScrumGetDataKo);
    }

    static class QuizServiceFake extends QuizService {
        private final Map<Integer, PreguntaQuizDTO> preguntasMock;

        public QuizServiceFake(Map<Integer, PreguntaQuizDTO> preguntasMock) {
            this.preguntasMock = preguntasMock;
        }

        @Override
        public Map<Integer, PreguntaQuizDTO> getPreguntas() throws Exception {
            return preguntasMock;
        }
    }
}
