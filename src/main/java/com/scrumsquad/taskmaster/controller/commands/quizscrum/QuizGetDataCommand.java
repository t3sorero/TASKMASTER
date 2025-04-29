package com.scrumsquad.taskmaster.controller.commands.quizscrum;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.services.quizscrum.QuizService;

public class QuizGetDataCommand implements Command {
    @Override
    public Context execute(Context ctx) {
        try {
            var preguntasQuiz = QuizService.getInstance().getPreguntas();
            if (preguntasQuiz == null || preguntasQuiz.isEmpty()) {
                return new Context(CommandName.quizScrumGetDataKo);
            }
            ctx.getArguments().put("preguntas", preguntasQuiz);
            return new Context(CommandName.quizScrumGetDataOk, ctx.getArguments());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Context(CommandName.quizScrumGetDataKo);
        }
    }
}
