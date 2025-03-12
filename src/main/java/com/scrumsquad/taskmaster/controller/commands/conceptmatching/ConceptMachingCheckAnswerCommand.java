package com.scrumsquad.taskmaster.controller.commands.conceptmatching;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConceptMachingCheckAnswerCommand implements Command {
    @Override
    public Context execute(Context ctx) {


        try {
            var userAnswer = ctx.getArguments();
            Map<Integer, Boolean> feedback = ConceptMatchingService.getInstance().checkAnswers((Map<Integer, Integer>) userAnswer.get("userAnswers"), (Set<Integer>) userAnswer.get("correctAnswers"));
            var res = new HashMap<String, Object>();
            res.put("feedback", feedback);
            return new Context(CommandName.conceptMatchingCheckUserAnswerOk, res);
        } catch (Exception e) {
            e.printStackTrace();
            return new Context(CommandName.conceptMatchingCheckUserAnswerKo);

        }
    }
}
