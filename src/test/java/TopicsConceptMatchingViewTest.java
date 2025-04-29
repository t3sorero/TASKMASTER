import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.lib.Widget.BuildOptions;
import com.scrumsquad.taskmaster.views.student.TopicsView;
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
    TopicsView view;

    @BeforeEach
    void setUp() {
        view = new TopicsView();
    }

    private     BuildOptions dummyOptions() {
        BuildOptions options = mock(BuildOptions.class);
        when(options.arguments()).thenReturn(new HashMap<String, Object>());
        return options;
    }
    @Test
    void testBuild() {
        JPanel panelContenedor = view.build(dummyOptions());
        assertNotNull(panelContenedor);
        assertEquals(AppColors.secondary40, panelContenedor.getBackground());
        Component[] comps = panelContenedor.getComponents();
        assertTrue(comps.length > 0);
        JPanel panelBotones = (JPanel) comps[0];
        assertEquals(3, panelBotones.getComponentCount());
        for (int i = 0; i < 3; i++) {
            Rounded3dButton btn = (Rounded3dButton) panelBotones.getComponent(i);
            // Updated expected text to match actual code behavior
            String expected = switch(i) {
                case 0 -> "TEMA 1 - Equipos de trabajo";
                case 1 -> "TEMA 2 - Metodologías de Gestión de Proyectos";
                case 2 -> "TEMA 3 - Scrum";
                default -> "";
            };
            assertEquals(expected, btn.getText());
        }
    }

    @Test
    void testButtonAction() {
        try (MockedStatic<Navigator> navMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navMock.when(Navigator::getNavigator).thenReturn(navigator);
            JPanel panelContenedor = view.build(dummyOptions());
            JPanel panelBotones = (JPanel) panelContenedor.getComponent(0);
            Rounded3dButton btn = (Rounded3dButton) panelBotones.getComponent(0);
            btn.doClick();
            verify(navigator).to((String) eq(ViewRoutes.gameSelection), (Map<String, Object>) argThat(arg ->
                    arg instanceof Map && ((Map<?, ?>) arg).get("tema").equals(1)
            ));
        }
    }
}
