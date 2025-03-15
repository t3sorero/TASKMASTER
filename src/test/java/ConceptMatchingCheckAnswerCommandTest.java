import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingCheckAnswerCommand;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
public class ConceptMatchingCheckAnswerCommandTest {
    private MockedStatic<ConceptMatchingService> mockCM;
    //antes de cada Test
    @BeforeEach
    public void setUp(){
        mockCM = mockStatic(ConceptMatchingService.class);
        mockCM.when(ConceptMatchingService::getInstance).thenReturn(new ConceptMatchingServiceFake());
    }
    //despues de cada Test
    @AfterEach
    public void tearDown(){
        mockCM.close();
        mockCM = null;
    }

    @Test
    public void testExecute(){
        //OK
        Context context = new Context(CommandName.conceptMatchingCheckAnswer);
        context.setArgument("userAnswers", new HashMap<>());
        context.setArgument("conceptosIds", new HashSet<>());
        ConceptMatchingCheckAnswerCommand command = new ConceptMatchingCheckAnswerCommand();
        Context r1 = command.execute(context);
        assertTrue(r1.getCommandName() == CommandName.conceptMatchingCheckAnswerOk);
        assertTrue(r1.getArguments().containsKey("feedback"));
        assertTrue(r1.getArguments().get("feedback") instanceof HashMap<?,?>);

        //Mal, no hay conceptosIds
        context = new Context(CommandName.conceptMatchingCheckAnswer);
        context.setArgument("userAnswers", new HashMap<>());
        command = new ConceptMatchingCheckAnswerCommand();
        Context r2 = command.execute(context);
        assertTrue(r2.getCommandName() == CommandName.conceptMatchingCheckAnswerKo);

        //Mal, no hay userAnswers
        context = new Context(CommandName.conceptMatchingCheckAnswer);
        context.setArgument("conceptosIds", new HashMap<>());
        command = new ConceptMatchingCheckAnswerCommand();
        Context r3 = command.execute(context);
        assertTrue(r2.getCommandName() == CommandName.conceptMatchingCheckAnswerKo);
    }
    class ConceptMatchingServiceFake extends ConceptMatchingService{
        @Override
        public ConceptosDefinicionesTOA getGameData() throws Exception {
            return null;
        }

        @Override
        public Map<Integer, Boolean> checkAnswers(Map<Integer, Integer> userAnswers, Set<Integer> conceptosIds) throws Exception {
            return new HashMap<Integer,Boolean>();
        }
    }
}



