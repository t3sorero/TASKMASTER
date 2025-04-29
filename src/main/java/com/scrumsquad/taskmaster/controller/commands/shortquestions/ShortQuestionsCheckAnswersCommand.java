package com.scrumsquad.taskmaster.controller.commands.shortquestions;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShortQuestionsCheckAnswersCommand implements Command {
    @Override
    public Context execute(Context ctx) {
        try{
            var userAnswer = ctx.getArguments();
            if(!userAnswer.containsKey("userAnswers")){
                throw new Exception();
            }
            if(!userAnswer.containsKey("preguntasIds")){
                throw new Exception();
            }
            Map<Integer, Boolean> feedback = ShortQuestionsService.getInstance().checkAnswers((Map<Integer, String>) userAnswer.get("userAnswers"), (Set<Integer>) userAnswer.get("preguntasIds"));
            var args = new HashMap<String, Object>();
            args.put("feedback", feedback);
            return new Context(CommandName.shortQuestionsCheckAnswersOK, args);
        }catch (Exception e){
            e.printStackTrace();
            return new Context(CommandName.shortQuestionsCheckAnswersKO);
        }
    }
}
