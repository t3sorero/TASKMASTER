package com.scrumsquad.taskmaster.controller.commands.conceptmatching;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConceptMatchingCheckAnswerCommand implements Command {
    @Override
    public Context execute(Context ctx) {


        try {
            var userAnswer = ctx.getArguments();
            if (!userAnswer.containsKey("userAnswers")) {
                throw new Exception();
            }
            if (!userAnswer.containsKey("conceptosIds")) {
                throw new Exception();
            }
            Map<Integer, Boolean> feedback = ConceptMatchingService.getInstance().checkAnswers(
                    (Map<Integer, Integer>) userAnswer.get("userAnswers"),
                    (Set<Integer>) userAnswer.get("conceptosIds"));
            var res = new HashMap<String, Object>();
            res.put("feedback", feedback);
            return new Context(CommandName.conceptMatchingCheckAnswerOk, res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Context(CommandName.conceptMatchingCheckAnswerKo);

        }
    }
}
