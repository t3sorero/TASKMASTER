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

public class QuizViewTest {

    private QuizView quizView;
    private JPanel panel;

    @BeforeEach
    public void setUp() {
        quizView = new QuizView();
        panel = quizView.build(new View.BuildOptions(null, new HashMap<>()));
    }

    @Test
    public void testBuildPanelNotNull() {
        assertNotNull(panel, "El panel principal no debe ser nulo");
        assertEquals(1, panel.getComponentCount(), "Debe contener un único componente (cardPanel)");
    }

    @Test
    public void testUsesCardLayout() {
        JPanel cardPanel = (JPanel) panel.getComponent(0);
        assertTrue(cardPanel.getLayout() instanceof CardLayout, "Debe usar CardLayout como gestor de diseño");
    }

    @Test
    public void testUpdateWithOkContextAddsPreguntaPanel() {
        Map<Integer, PreguntaQuizDTO> preguntasMock = createMockPreguntas();

        Context ctx = new Context(CommandName.quizScrumGetDataOk);
        ctx.setArgument("preguntas", preguntasMock);
        quizView.update(ctx);

        JPanel cardPanel = (JPanel) panel.getComponent(0);
        boolean contienePanelPregunta = Arrays.stream(cardPanel.getComponents())
                .anyMatch(comp -> comp.getName() == null || comp.getClass().getName().contains("JPanel"));

        assertTrue(contienePanelPregunta, "Debería haberse añadido el panel de preguntas correctamente");
    }

    @Test
    public void testUpdateWithKoContextShowsError() {
        Context ctx = new Context(CommandName.quizScrumGetDataKo);
        assertDoesNotThrow(() -> quizView.update(ctx), "No debería lanzar excepción al recibir un contexto KO");
    }

    @Test
    public void testCorrectNumberOfPreguntasMock() {
        Map<Integer, PreguntaQuizDTO> preguntas = createMockPreguntas();
        assertEquals(10, preguntas.size(), "Debe haber 10 preguntas simuladas");
    }

    @Test
    public void testOpcionesDeRespuestaEstanBienFormadas() {
        Map<Integer, PreguntaQuizDTO> preguntas = createMockPreguntas();
        for (PreguntaQuizDTO pregunta : preguntas.values()) {
            assertEquals(4, pregunta.getOpciones().size(), "Cada pregunta debe tener 4 opciones");
            assertTrue(pregunta.getOpciones().contains(pregunta.getCorrecta()),
                    "Las opciones deben contener la respuesta correcta");
        }
    }

    @Test
    public void testSimulaJuegoHastaPreguntaInicial() {
        Map<Integer, PreguntaQuizDTO> preguntasMock = createMockPreguntas();
        Context ctx = new Context(CommandName.quizScrumGetDataOk);
        ctx.setArgument("preguntas", preguntasMock);
        quizView.update(ctx);

        // Accede al cardPanel y verifica que existe un componente con las opciones cargadas
        JPanel cardPanel = (JPanel) panel.getComponent(0);
        Component[] components = cardPanel.getComponents();
        boolean contieneOpciones = Arrays.stream(components)
                .flatMap(comp -> Arrays.stream(((JPanel) comp).getComponents()))
                .anyMatch(sub -> sub instanceof JPanel && ((JPanel) sub).getComponentCount() >= 4);

        assertTrue(contieneOpciones, "Deberían haberse cargado botones u opciones en el panel de preguntas");
    }

    private Map<Integer, PreguntaQuizDTO> createMockPreguntas() {
        Map<Integer, PreguntaQuizDTO> preguntas = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            List<String> opciones = Arrays.asList("A", "B", "C", "D");
            preguntas.put(i, new PreguntaQuizDTO(
                    i,
                    "Pregunta " + i,
                    i / 2 + 1,
                    "A",
                    new ArrayList<>(opciones),
                    "pista " + i
            ));
        }
        return preguntas;
    }
}
