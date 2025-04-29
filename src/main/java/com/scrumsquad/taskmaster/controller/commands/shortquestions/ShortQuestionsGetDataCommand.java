package com.scrumsquad.taskmaster.controller.commands.shortquestions;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsService;
import com.scrumsquad.taskmaster.services.shortquestions.ShortQuestionsServiceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShortQuestionsGetDataCommand implements Command {
    @Override
    public Context execute(Context context) {
        try{
            List<ShortQuestionDTO> questions = ShortQuestionsService.getInstance().getShortQuestions((int) context.getArguments().get("tema"));
            Map<String, Object> args = new HashMap<>();
            args.put("preguntas", questions);
            return new Context(CommandName.shortQuestionsGetDataOK, args);
        } catch(Exception ex){
            ex.printStackTrace();
            return new Context(CommandName.shortQuestionsGetDataKO);
        }
    }
}
