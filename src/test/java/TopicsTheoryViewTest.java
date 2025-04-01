import com.scrumsquad.taskmaster.views.student.teoria.TopicsTheoryView;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.controller.Navigator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicsTheoryViewTest {
    private TopicsTheoryView view;

    @BeforeEach
    void setUp() {
        view = new TopicsTheoryView();
        view.build(mock(com.scrumsquad.taskmaster.lib.Widget.BuildOptions.class));
    }

    @Test
    void testPanelContenedorCreated() {
        JPanel panelContenedor = view.build(mock(com.scrumsquad.taskmaster.lib.Widget.BuildOptions.class));
        assertNotNull(panelContenedor);
        assertEquals(BorderLayout.class, panelContenedor.getLayout().getClass());
        assertEquals(com.scrumsquad.taskmaster.views.AppColors.secondary40, panelContenedor.getBackground());
    }

    @Test
    void testButtonsCreated() {
        JPanel panelBotones = (JPanel) view.build(null).getComponent(0);
        assertEquals(3, panelBotones.getComponentCount());

        for (Component component : panelBotones.getComponents()) {
            assertTrue(component instanceof Rounded3dButton);
        }
    }

    @Test
    void testButtonNavigation() {
        try (MockedStatic<Navigator> navigatorMock = mockStatic(Navigator.class)){
            Navigator navigator = mock(Navigator.class);
            when(Navigator.getNavigator()).thenReturn(navigator);

            Rounded3dButton button1 = TestUtils.getPrivateField(view, "button1", Rounded3dButton.class);
            Rounded3dButton button2 = TestUtils.getPrivateField(view, "button2", Rounded3dButton.class);
            Rounded3dButton button3 = TestUtils.getPrivateField(view, "button3", Rounded3dButton.class);

            button1.doClick();
            verify(navigator).to(Mockito.<String>any(), Mockito.<Map<String, Object>>any());

            button2.doClick();
            verify(navigator, times(2)).to(Mockito.<String>any(), Mockito.<Map<String, Object>>any());

            button3.doClick();
            verify(navigator, times(3)).to(Mockito.<String>any(), Mockito.<Map<String, Object>>any());
        }
    }
}