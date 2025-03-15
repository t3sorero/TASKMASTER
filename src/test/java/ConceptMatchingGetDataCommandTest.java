import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingGetDataCommand;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

public class ConceptMatchingGetDataCommandTest {

    private MockedStatic<ConceptMatchingService> mockCM;

    @Test
    public void testExecute(){
        //OK
        mockCM = mockStatic(ConceptMatchingService.class);
        mockCM.when(ConceptMatchingService::getInstance).thenReturn(new ConceptMatchingServiceFake());

        Context context = new Context(CommandName.conceptMatchingGetData);
        ConceptMatchingGetDataCommand command = new ConceptMatchingGetDataCommand();
        Context r1 = command.execute(context);
        assertTrue(r1.getCommandName() == CommandName.conceptMatchingGetDataOk);
        assertTrue(r1.getArguments().containsKey("toa"));
        assertTrue(r1.getArguments().get("toa") instanceof ConceptosDefinicionesTOA);

        mockCM.close();
        mockCM = null;

        //KO
        mockCM = mockStatic(ConceptMatchingService.class);
        mockCM.when(ConceptMatchingService::getInstance).thenThrow(new RuntimeException("Error"));

        context = new Context(CommandName.conceptMatchingGetData);
        command = new ConceptMatchingGetDataCommand();
        Context r2 = command.execute(context);
        assertTrue(r2.getCommandName() == CommandName.conceptMatchingGetDataKo);

        mockCM.close();
        mockCM = null;
    }

    class ConceptMatchingServiceFake extends ConceptMatchingService {
        @Override
        public ConceptosDefinicionesTOA getGameData() throws Exception {
            return new ConceptosDefinicionesTOA(new ArrayList<ConceptoDTO>(), new ArrayList<DefinicionDTO>());
        }

        @Override
        public Map<Integer, Boolean> checkAnswers(Map<Integer, Integer> userAnswers, Set<Integer> conceptosIds) throws Exception {
            return new HashMap<Integer,Boolean>();
        }
    }
}
