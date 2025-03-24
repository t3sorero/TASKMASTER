
import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.ConceptMatchingView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConceptMatchingViewTest {
    ConceptMatchingView view;
    @BeforeEach
    void setUp() {
        view = new ConceptMatchingView();
        view.build(null);
    }
    @Test
    void testBuild() {
        JPanel panel = view.build(null);
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }
    @Test
    void testOnLoad() {
        try (MockedStatic<AppController> appMock = mockStatic(AppController.class)) {
            AppController controller = mock(AppController.class);
            appMock.when(AppController::getInstance).thenReturn(controller);
            view.onLoad();
            verify(controller).action(any());
        }
    }
    @Test
    void testUpdate_GetDataOk() {
        List<ConceptoDTO> conceptos = List.of(new ConceptoDTO(1, "C1"), new ConceptoDTO(2, "C2"));
        List<DefinicionDTO> definiciones = List.of(new DefinicionDTO(10, "D1", 1), new DefinicionDTO(20, "D2", 2));
        ConceptosDefinicionesTOA toa = new ConceptosDefinicionesTOA(conceptos, definiciones);
        Context ctx = new Context(CommandName.conceptMatchingGetDataOk);
        ctx.setArgument("toa", toa);
        view.build(null);
        view.update(ctx);
        JPanel buttonsPanel = TestUtils.getPrivateField(view, "buttonsConceptosPanel", JPanel.class);
        assertNotNull(buttonsPanel);
        assertEquals(conceptos.size(), buttonsPanel.getComponentCount());
    }
    @Test
    void testUpdate_CheckAnswerOk() {
        List<ConceptoDTO> conceptos = List.of(new ConceptoDTO(1, "C1"), new ConceptoDTO(2, "C2"));
        List<DefinicionDTO> definiciones = List.of(new DefinicionDTO(10, "D1", 1), new DefinicionDTO(20, "D2", 2));
        ConceptosDefinicionesTOA toa = new ConceptosDefinicionesTOA(conceptos, definiciones);
        Context ctxData = new Context(CommandName.conceptMatchingGetDataOk);
        ctxData.setArgument("toa", toa);
        view.build(null);
        view.update(ctxData);
        // Simulate mapping: index 0 -> 0, index 1 -> 1
        TestUtils.setPrivateField(view, "conceptoMap", Map.of(0, 0, 1, 1));
        Map<Integer, Boolean> feedback = Map.of(1, true, 2, false);
        Context ctxFeedback = new Context(CommandName.conceptMatchingCheckAnswerOk);
        ctxFeedback.setArgument("feedback", feedback);
        view.update(ctxFeedback);
        JLabel correctLabel = TestUtils.getPrivateField(view, "correctNumberLabel", JLabel.class);
        JLabel incorrectLabel = TestUtils.getPrivateField(view, "incorrectNumberLabel", JLabel.class);
        assertEquals("1", correctLabel.getText());
        assertEquals("1", incorrectLabel.getText());
    }

    @Test
    void testSendButtonAction() {
        try (MockedStatic<AppController> appMock = mockStatic(AppController.class)) {
            AppController controller = mock(AppController.class);
            appMock.when(AppController::getInstance).thenReturn(controller);
            JButton sendButton = TestUtils.getPrivateField(view, "sendButton", JButton.class);
            sendButton.doClick();
            view.onLoad();
            verify(controller).action(any());
        }
    }

    @Test
    void testPanelData() {
        List<ConceptoDTO> conceptos = List.of(new ConceptoDTO(1, "Concepto 1"), new ConceptoDTO(2, "Concepto 2"));
        List<DefinicionDTO> definiciones = List.of(new DefinicionDTO(10, "Definicion 1", 1), new DefinicionDTO(20, "Definicion 2", 2));
        ConceptosDefinicionesTOA toa = new ConceptosDefinicionesTOA(conceptos, definiciones);
        Context ctx = new Context(CommandName.conceptMatchingGetDataOk);
        ctx.setArgument("toa", toa);
        view.build(null);
        view.update(ctx);
        JPanel conceptosPanel = TestUtils.getPrivateField(view, "buttonsConceptosPanel", JPanel.class);
        JPanel definicionesPanel = TestUtils.getPrivateField(view, "buttonsDefinicionesPanel", JPanel.class);
        assertNotNull(conceptosPanel);
        assertNotNull(definicionesPanel);
        assertEquals(conceptos.size(), conceptosPanel.getComponentCount());
        assertEquals(definiciones.size(), definicionesPanel.getComponentCount());
    }

}
