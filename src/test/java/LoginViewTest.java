import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.Widget;
import com.scrumsquad.taskmaster.views.auth.LoginView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginViewTest {

    private LoginView view;
    private Widget.BuildOptions options;

    @BeforeEach
    void setUp() {
        view = new LoginView();
        options = new Widget.BuildOptions(null, new HashMap<>());
    }

    @Test
    void testBuild() {
        JPanel panel = view.build(options);
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }

    @Test
    void testInvalidEmailValidation() {
        view.build(options);
        boolean isValid = view.validateLogin("notAnEmail", "password123");
        assertFalse(isValid);
    }

    @Test
    void testEmptyPasswordValidation() {
        view.build(options);
        boolean isValid = view.validateLogin("test@example.com", "");
        assertFalse(isValid);
    }

    @Test
    void testValidLoginValidation() {
        view.build(options);
        boolean isValid = view.validateLogin("test@example.com", "password123");
        assertTrue(isValid);
    }

    @Test
    void testSendLogin_DisabledButton() {
        view.build(options);
        JButton loginButton = getLoginButton(view);
        loginButton.setEnabled(false);

        try (MockedStatic<AppController> appMock = mockStatic(AppController.class)) {
            AppController controller = mock(AppController.class);
            appMock.when(AppController::getInstance).thenReturn(controller);
            loginButton.doClick();
            verify(controller, never()).action(any());
        }
    }

    @Test
    void testUpdate_LoginOk() {
        view.build(options);
        Context ctx = new Context(CommandName.loginOk);
        try (MockedStatic<Navigator> navMock = mockStatic(Navigator.class)) {
            Navigator navigator = mock(Navigator.class);
            navMock.when(Navigator::getNavigator).thenReturn(navigator);
            view.update(ctx);
            verify(navigator).to(any());
        }
    }

    @Test
    void testUpdate_LoginKo() {
        view.build(options);
        Context ctx = new Context(CommandName.loginKo);
        ctx.setArgument("credentials", true);
        view.update(ctx);

        JLabel errorLabel = getErrorResponseLabel(view);
        assertTrue(errorLabel.isVisible());
        assertEquals("Usuario o contraseña incorrectos", errorLabel.getText());
    }

    // Utils
    private JButton getLoginButton(LoginView view) {
        return TestUtils.getPrivateField(view, "loginButton", JButton.class);
    }

    private JLabel getErrorResponseLabel(LoginView view) {
        return TestUtils.getPrivateField(view, "errorResponseLabel", JLabel.class);
    }
}
