import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.login.StudentLoginCommand;
import com.scrumsquad.taskmaster.services.login.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentLoginCommandTest {

    private MockedStatic<LoginService> mockLoginService;

    @BeforeEach
    void setUp() {
        // Inicializar el mock estático antes de cada prueba
        mockLoginService = mockStatic(LoginService.class);
    }

    @AfterEach
    void tearDown() {
        // Cerrar el mock estático después de cada prueba
        mockLoginService.close();
    }

    @Test
    void testExecute_Success_ValidLogin() throws Exception {
        // Simular el servicio devolviendo credenciales válidas
        LoginService mockService = mock(LoginService.class);
        when(mockService.loginValidation("test@example.com", "password123")).thenReturn(true);
        mockLoginService.when(LoginService::getInstance).thenReturn(mockService);


        Context context = new Context(CommandName.login);
        context.setArgument("email", "test@example.com");
        context.setArgument("password", "password123");


        StudentLoginCommand command = new StudentLoginCommand();
        Context result = command.execute(context);


        assertEquals(CommandName.loginOk, result.getCommandName());
        assertFalse(result.getArguments().containsKey("credentials"));
    }

    @Test
    void testExecute_Failure_InvalidLogin() throws Exception {
        // Simular el servicio devolviendo credenciales inválidas
        LoginService mockService = mock(LoginService.class);
        when(mockService.loginValidation("test@example.com", "wrongpassword")).thenReturn(false);
        mockLoginService.when(LoginService::getInstance).thenReturn(mockService);


        Context context = new Context(CommandName.login);
        context.setArgument("email", "test@example.com");
        context.setArgument("password", "wrongpassword");


        StudentLoginCommand command = new StudentLoginCommand();
        Context result = command.execute(context);


        assertEquals(CommandName.loginKo, result.getCommandName());
        assertTrue(result.getArguments().containsKey("credentials"));
        assertTrue((boolean) result.getArguments().get("credentials"));
    }

    @Test
    void testExecute_Failure_MissingEmail() {
        // Crear el contexto sin el campo "email"
        Context context = new Context(CommandName.login);
        context.setArgument("password", "password123");


        StudentLoginCommand command = new StudentLoginCommand();
        Context result = command.execute(context);


        assertEquals(CommandName.loginKo, result.getCommandName());
        assertFalse(result.getArguments().containsKey("credentials"));
    }

    @Test
    void testExecute_Failure_MissingPassword() {
        // Crear el contexto sin el campo "password"
        Context context = new Context(CommandName.login);
        context.setArgument("email", "test@example.com");


        StudentLoginCommand command = new StudentLoginCommand();
        Context result = command.execute(context);


        assertEquals(CommandName.loginKo, result.getCommandName());
        assertFalse(result.getArguments().containsKey("credentials"));
    }

    @Test
    void testExecute_Exception() throws Exception {
        // Simular el servicio lanzando una excepción
        LoginService mockService = mock(LoginService.class);
        when(mockService.loginValidation("test@example.com", "password123")).thenThrow(new RuntimeException("Error en servicio"));
        mockLoginService.when(LoginService::getInstance).thenReturn(mockService);


        Context context = new Context(CommandName.login);
        context.setArgument("email", "test@example.com");
        context.setArgument("password", "password123");


        StudentLoginCommand command = new StudentLoginCommand();
        Context result = command.execute(context);


        assertEquals(CommandName.loginKo, result.getCommandName());
        assertFalse(result.getArguments().containsKey("credentials"));
    }
}
