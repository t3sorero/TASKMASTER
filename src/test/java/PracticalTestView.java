import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.Widget.BuildOptions;
import com.scrumsquad.taskmaster.views.student.games.practicaltest.PracticalTestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PracticalTestViewTest {
    private PracticalTestView view;
    private JPanel panel;

    @BeforeEach
    void setUp() {
        view = new PracticalTestView();
        BuildOptions options = new BuildOptions(null, new HashMap<>());
        panel = view.build(options);
    }

    @Test
    void testBuildNotNull() {
        assertNotNull(panel, "Panel debería haberse creado correctamente");
        assertTrue(panel.getComponentCount() > 0, "Panel debería contener componentes");
    }

    @Test
    void testComponentsExistence() {
        JButton submitButton = TestUtils.findButtonByText(panel, "ENVIAR RESPUESTAS");
        JButton exitButton = TestUtils.findButtonByText(panel, "SALIR");
        assertNotNull(submitButton, "Botón de enviar respuestas debe existir");
        assertNotNull(exitButton, "Botón de salir debe existir");
    }

    @Test
    void testSendAnswersAllCorrect() {
        List<JTextField> fields = TestUtils.getPrivateField(view, "answerFields", List.class);
        String[] correctAnswers = {
                "Conjunto de personas con objetivos comunes",
                "Organiza y guía al equipo",
                "Resultado superior al trabajo individual",
                "Mejor rendimiento y comunicación",
                "Marco de trabajo flexible y adaptativo",
                "Facilitador del proceso Scrum",
                "Tablero Kanban o herramientas como Jira",
                "Reunión para mejorar el proceso",
                "Producto mínimo viable",
                "Compartir avances y plan diario"
        };
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setText(correctAnswers[i]);
        }

        view.checkAnswers();

        JLabel correctNumberLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectNumberLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("10", correctNumberLabel.getText(), "Todas deberían estar correctas");
        assertEquals("0", incorrectNumberLabel.getText(), "Ninguna debería estar incorrecta");
    }

    @Test
    void testSendAnswersNoneCorrect() {
        List<JTextField> fields = TestUtils.getPrivateField(view, "answerFields", List.class);
        fields.forEach(field -> field.setText("Respuesta incorrecta"));

        view.checkAnswers();

        JLabel correctNumberLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectNumberLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("0", correctNumberLabel.getText(), "Ninguna debería estar correcta");
        assertEquals("10", incorrectNumberLabel.getText(), "Todas deberían estar incorrectas");
    }

    @Test
    void testSendAnswersHalfCorrect() {
        List<JTextField> fields = TestUtils.getPrivateField(view, "answerFields", List.class);
        String[] halfCorrectAnswers = {
                "Conjunto de personas con objetivos comunes",
                "incorrecto",
                "Resultado superior al trabajo individual",
                "incorrecto",
                "Marco de trabajo flexible y adaptativo",
                "incorrecto",
                "Tablero Kanban o herramientas como Jira",
                "incorrecto",
                "Producto mínimo viable",
                "incorrecto"
        };
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setText(halfCorrectAnswers[i]);
        }

        view.checkAnswers();

        JLabel correctNumberLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectNumberLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("5", correctNumberLabel.getText(), "5 deberían estar correctas");
        assertEquals("5", incorrectNumberLabel.getText(), "5 deberían estar incorrectas");
    }

    @Test
    void testExitButtonNavigation() {
        try (MockedStatic<Navigator> navigatorMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navigatorMock.when(Navigator::getNavigator).thenReturn(navigator);

            JButton exitButton = TestUtils.findButtonByText(panel, "SALIR");
            assertNotNull(exitButton, "Botón SALIR no debería ser nulo");

            exitButton.doClick();
            verify(navigator, times(1)).back();
        }
    }

    @Test
    void testUpdateWithNullContext() {
        assertDoesNotThrow(() -> view.update(null), "Actualizar con contexto nulo no debe arrojar excepción");
    }

    @Test
    void testUpdateWithEmptyContext() {
        Context emptyCtx = new Context("unknownCommand");
        assertDoesNotThrow(() -> view.update(emptyCtx), "Actualizar con contexto desconocido no debe arrojar excepción");
    }

    @Test
    void testOnLoad() {
        assertDoesNotThrow(() -> view.onLoad(), "onLoad no debe arrojar excepción");
    }
}
