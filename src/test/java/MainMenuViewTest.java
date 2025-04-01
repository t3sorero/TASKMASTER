import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.views.student.MainMenuView;
import com.scrumsquad.taskmaster.views.ViewRoutes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MainMenuViewTest {
    MainMenuView view;

    @BeforeEach
    void setUp() {
        view = new MainMenuView();
        view.build(null);
    }

    @Test
    void testMainPanelCreated() {
        JPanel mainPanel = TestUtils.getPrivateField(view, "mainPanel", JPanel.class);
        assertNotNull(mainPanel);
        assertEquals(BorderLayout.class, mainPanel.getLayout().getClass());
    }

    @Test
    void testButtonsCreated() {
        JButton teoriaButton = TestUtils.getPrivateField(view, "teoriaButton", JButton.class);
        JButton practicarButton = TestUtils.getPrivateField(view, "practicarButton", JButton.class);
        assertNotNull(teoriaButton);
        assertNotNull(practicarButton);
        String texto = TestUtils.getPrivateField(teoriaButton, "bottomText", String.class);
        assertEquals("TEORÍA", texto);
        texto = TestUtils.getPrivateField(practicarButton, "bottomText", String.class);
        assertEquals("PRACTICAR", texto);
    }

    @Test
    void testBuild() {
        JPanel panel = view.build(null);
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void testTeoriaButtonAction() {
        try (MockedStatic<Navigator> navigatorMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navigatorMock.when(Navigator::getNavigator).thenReturn(navigator);
            JButton teoriaButton = TestUtils.getPrivateField(view, "teoriaButton", JButton.class);
            teoriaButton.doClick();
            verify(navigator).to(any());
        }
    }


    @Test
    void testPracticarButtonAction() {
        try (MockedStatic<Navigator> navigatorMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navigatorMock.when(Navigator::getNavigator).thenReturn(navigator);
            JButton practicarButton = TestUtils.getPrivateField(view, "practicarButton", JButton.class);
            practicarButton.doClick();
            verify(navigator).to(any());
        }
    }
}