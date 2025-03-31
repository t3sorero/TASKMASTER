import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.lib.Widget.BuildOptions;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.TopicsConceptMatchingView;
import com.scrumsquad.taskmaster.views.ViewRoutes;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.views.AppColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class TopicsConceptMatchingViewTest {
    TopicsConceptMatchingView view;

    @BeforeEach
    void setUp() {
        view = new TopicsConceptMatchingView();
    }

    private     BuildOptions dummyOptions() {
        BuildOptions options = mock(BuildOptions.class);
        when(options.arguments()).thenReturn(new HashMap<String, Object>());
        return options;
    }
    @Test
    void testBuild() {
        JPanel panel = view.build(null);
        assertNotNull(panel);
        Component[] conts = panel.getComponents();
        assertTrue(conts.length > 0);
        JPanel panelContenedor = (JPanel) conts[0];
        assertEquals(AppColors.secondary40, panelContenedor.getBackground());
        Component[] comps = panelContenedor.getComponents();
        assertTrue(comps.length > 0);
        JPanel panelBotones = (JPanel) comps[0];
        assertEquals(3, panelBotones.getComponentCount());
        for (int i = 0; i < 3; i++) {
            Rounded3dButton btn = (Rounded3dButton) panelBotones.getComponent(i);
            assertEquals("TEMA " + (i + 1), btn.getText());
        }
    }

    @Test
    void testButtonAction() {
        try (MockedStatic<Navigator> navMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navMock.when(Navigator::getNavigator).thenReturn(navigator);
            JPanel panel = view.build(dummyOptions());
            JPanel panelContenedor = (JPanel) panel.getComponent(0);
            JPanel panelBotones = (JPanel) panelContenedor.getComponent(0);
            Rounded3dButton btn = (Rounded3dButton) panelBotones.getComponent(0);
            btn.doClick();
            verify(navigator).to((String) eq(ViewRoutes.conceptMatching), (Map<String, Object>) argThat(arg ->
                    arg instanceof Map && ((Map<?, ?>)arg).get("tema").equals(1)
            ));
        }
    }
}
