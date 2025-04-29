import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.lib.Widget.BuildOptions;
import com.scrumsquad.taskmaster.views.student.games.shortquestions.ShortQuestionsView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShortQuestionsViewTest {

    private ShortQuestionsView view;
    private JPanel panel;

    @BeforeEach
    void setUp() {
        view = new ShortQuestionsView();
        BuildOptions options = new BuildOptions(null, Map.of("tema", 1));
        panel = view.build(options);
    }

    @Test
    void testPanelBuildsCorrectly() {
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void testExitButtonAction() {
        try (MockedStatic<Navigator> navigatorMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navigatorMock.when(Navigator::getNavigator).thenReturn(navigator);

            JButton exitButton = TestUtils.findButtonByText(panel, "SALIR");
            assertNotNull(exitButton);
            exitButton.doClick();

            verify(navigator, times(1)).back();
        }
    }

    @Test
    void testSafeUpdateAndOnLoad() {
        assertDoesNotThrow(() -> view.update(null));
        assertDoesNotThrow(() -> view.update(new Context("unknownCommand")));
        assertDoesNotThrow(() -> view.onLoad());
    }

    @Test
    void testUpdateLoadsQuestionsCorrectly() {
        List<ShortQuestionDTO> preguntas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            preguntas.add(new ShortQuestionDTO(i + 1, "Pregunta " + (i + 1)));
        }
        Context ctx = new Context(CommandName.shortQuestionsGetDataOK);
        ctx.setArgument("preguntas", preguntas);

        view.update(ctx);
        List<JTextField> fields = TestUtils.getPrivateField(view, "answerFields", List.class);
        assertEquals(10, fields.size());
    }

    @Test
    void testAllCorrectAnswers() {
        simulateLoadQuestions();
        simulateCheckAnswers(Map.of(
                1, true, 2, true, 3, true, 4, true, 5, true,
                6, true, 7, true, 8, true, 9, true, 10, true
        ));

        JLabel correctLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("10", correctLabel.getText());
        assertEquals("0", incorrectLabel.getText());
    }

    @Test
    void testAllWrongAnswers() {
        simulateLoadQuestions();
        simulateCheckAnswers(Collections.nCopies(10, false).stream()
                .collect(HashMap::new, (m, v) -> m.put(m.size() + 1, false), Map::putAll));

        JLabel correctLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("0", correctLabel.getText());
        assertEquals("10", incorrectLabel.getText());
    }

    @Test
    void testHalfCorrectAnswers() {
        simulateLoadQuestions();
        Map<Integer, Boolean> feedback = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            feedback.put(i, i % 2 == 0); // even = correct
        }
        simulateCheckAnswers(feedback);

        JLabel correctLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);

        assertEquals("5", correctLabel.getText());
        assertEquals("5", incorrectLabel.getText());
    }

    @Test
    void testSubmitAndExitButtonsExist() {
        JButton submitButton = TestUtils.findButtonByText(panel, "ENVIAR RESPUESTAS");
        JButton exitButton = TestUtils.findButtonByText(panel, "SALIR");
        assertNotNull(submitButton);
        assertNotNull(exitButton);
    }


    private void simulateLoadQuestions() {
        List<ShortQuestionDTO> preguntas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            preguntas.add(new ShortQuestionDTO(i + 1, "Pregunta " + (i + 1)));
        }
        Context ctx = new Context(CommandName.shortQuestionsGetDataOK);
        ctx.setArgument("preguntas", preguntas);
        view.update(ctx);

        List<JTextField> fields = TestUtils.getPrivateField(view, "answerFields", List.class);
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setText("respuesta " + (i + 1));
        }
    }

    private void simulateCheckAnswers(Map<Integer, Boolean> feedback) {
        Context ctx = new Context(CommandName.shortQuestionsCheckAnswersOK);
        ctx.setArgument("feedback", feedback);
        view.update(ctx);
    }
}
