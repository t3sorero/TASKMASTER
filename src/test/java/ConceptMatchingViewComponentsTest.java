import com.scrumsquad.taskmaster.lib.Widget;
import com.scrumsquad.taskmaster.lib.swing.RoundedPanel;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.ConceptMatchingView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConceptMatchingViewComponentsTest {

    private ConceptMatchingView view;
    private JFrame frame;

    @BeforeEach
    void setUp() {
        view = new ConceptMatchingView();
        frame = new JFrame();
        frame.setContentPane(view.build(new Widget.BuildOptions(frame, new HashMap<>())));
    }
    @AfterEach
    void tearDown() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
        view = null;
    }
    @Test
    void testGamePanelCreated() {
        RoundedPanel gamePanel = TestUtils.getPrivateField(view, "gamePanel", RoundedPanel.class);
        assertNotNull(gamePanel);
        assertEquals(AppColors.secondary, gamePanel.getBackground());
        assertEquals(new Dimension(960, 592), gamePanel.getPreferredSize());
    }

    @Test
    void testActionButtonsAndSendButton() {
        JPanel actionPanel = TestUtils.getPrivateField(view, "actionButtonsPanel", JPanel.class);
        assertNotNull(actionPanel);
        JButton sendButton = TestUtils.getPrivateField(view, "sendButton", JButton.class);
        assertNotNull(sendButton);
        assertEquals("COMPROBAR RESPUESTAS", sendButton.getText());
    }

    @Test
    void testResultPanelComponents() {
        JPanel gameResultPanel = TestUtils.getPrivateField(view, "gameResultPanel", JPanel.class);
        assertNotNull(gameResultPanel);
        // Expect at least 4 components (2 icons and 2 labels)
        assertTrue(gameResultPanel.getComponentCount() >= 4);
        JLabel correctLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);
        assertNotNull(correctLabel);
        assertNotNull(incorrectLabel);
        assertEquals("-", correctLabel.getText());
        assertEquals("-", incorrectLabel.getText());
    }

    @Test
    void testButtonsPanelsCreated() {
        JPanel botonesConceptos = TestUtils.getPrivateField(view, "buttonsConceptosPanel", JPanel.class);
        JPanel botonesDefiniciones = TestUtils.getPrivateField(view, "buttonsDefinicionesPanel", JPanel.class);
        assertNotNull(botonesConceptos);
        assertNotNull(botonesDefiniciones);
    }
}
