import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.teoria.GetTeoriaCommand;
import com.scrumsquad.taskmaster.services.teoria.GetTeoriaService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

public class GetTeoriaCommandTest {

    private MockedStatic<GetTeoriaService> mockTeoriaService;

    @Test
    public void testExecute() {
        // OK: Simulación de servicio con datos válidos
        mockTeoriaService = mockStatic(GetTeoriaService.class);
        mockTeoriaService.when(GetTeoriaService::getInstance).thenReturn(new GetTeoriaServiceFake());

        Context context = new Context(CommandName.teoriaGetData);
        context.setArgument("tema", 1);
        GetTeoriaCommand command = new GetTeoriaCommand();
        Context result = command.execute(context);

        assertEquals(CommandName.teoriaGetDataOk, result.getCommandName());
        assertTrue(result.getArguments().containsKey("teoria"));
        assertEquals("Teoría de prueba", result.getArguments().get("teoria"));

        mockTeoriaService.close();
        mockTeoriaService = null;

        // KO excepcion
        mockTeoriaService = mockStatic(GetTeoriaService.class);
        mockTeoriaService.when(GetTeoriaService::getInstance).thenThrow(new RuntimeException("Error"));

        context = new Context(CommandName.teoriaGetData);
        command = new GetTeoriaCommand();
        Context resultError = command.execute(context);

        assertEquals(CommandName.teoriaGetDataKo, resultError.getCommandName());

        mockTeoriaService.close();
        mockTeoriaService = null;

        // KO teoria null
        mockTeoriaService = mockStatic(GetTeoriaService.class);
        mockTeoriaService.when(GetTeoriaService::getInstance).thenReturn(new GetTeoriaServiceNullFake());

        context = new Context(CommandName.teoriaGetData);
        command = new GetTeoriaCommand();
        Context resultNull = command.execute(context);

        assertEquals(CommandName.teoriaGetDataKo, resultNull.getCommandName());

        mockTeoriaService.close();
        mockTeoriaService = null;
    }

    class GetTeoriaServiceFake extends GetTeoriaService {
        @Override
        public String getTeoria(int tema) {
            return "Teoría de prueba";
        }
    }

    class GetTeoriaServiceNullFake extends GetTeoriaService {
        @Override
        public String getTeoria(int tema) {
            return null;
        }
    }
}
