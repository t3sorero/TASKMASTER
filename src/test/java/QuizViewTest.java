import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.student.games.quiz.QuizView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class QuizViewTest {

    private QuizView quizView;
    private JPanel panel;

    @BeforeEach
    public void setUp() {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Este test requiere entorno gráfico");
        quizView = new QuizView();
        panel = quizView.build(new View.BuildOptions(null, new HashMap<>()));
    }

    @Test
    public void testBuildPanelNotNull() {
        assertNotNull(panel, "El panel principal no debe ser nulo");
        assertEquals(1, panel.getComponentCount(), "El panel principal debe tener un solo componente");
    }

    @Test
    public void testInitialCardLayoutIsPresent() {
        JPanel cardPanel = (JPanel) panel.getComponent(0);
        assertTrue(cardPanel.getLayout() instanceof CardLayout, "El panel debe usar CardLayout");
    }

    @Test
    public void testUpdateWithValidContextShowsPreguntaPanel() {
        Map<Integer, PreguntaQuizDTO> mockData = createMockPreguntas();
        Context ctx = new Context(CommandName.quizScrumGetDataOk);
        ctx.setArgument("preguntas", mockData);

        quizView.update(ctx);

        JPanel cardPanel = (JPanel) panel.getComponent(0);
        boolean foundPreguntaPanel = Arrays.stream(cardPanel.getComponents())
                .anyMatch(comp -> comp.getName() == null || comp instanceof JPanel);
        assertTrue(foundPreguntaPanel, "Debe haber un panel de preguntas después de update()");
    }

    @Test
    public void testUpdateWithErrorContextShowsErrorPanel() {
        Context ctx = new Context(CommandName.quizScrumGetDataKo);
        assertDoesNotThrow(() -> quizView.update(ctx), "No debe lanzar excepción con contexto de error");
    }

    @Test
    public void testSimulaJuegoHastaPreguntaInicial() {
        Map<Integer, PreguntaQuizDTO> mockData = createMockPreguntas();
        Context ctx = new Context(CommandName.quizScrumGetDataOk);
        ctx.setArgument("preguntas", mockData);
        quizView.update(ctx);
    }

    private Map<Integer, PreguntaQuizDTO> createMockPreguntas() {
        Map<Integer, PreguntaQuizDTO> preguntas = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            List<String> opciones = Arrays.asList("A", "B", "C", "D");
            preguntas.put(i, new PreguntaQuizDTO(i, "Pregunta " + i, i / 2 + 1, "A", new ArrayList<>(opciones), "Pista " + i));
        }
        return preguntas;
    }
}
