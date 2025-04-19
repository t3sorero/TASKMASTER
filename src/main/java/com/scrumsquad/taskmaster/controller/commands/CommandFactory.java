package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.controller.commands.auth.RegisterCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingCheckAnswerCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingGetDataCommand;
import com.scrumsquad.taskmaster.controller.commands.quizscrum.QuizGetDataCommand;
import com.scrumsquad.taskmaster.controller.commands.teoria.GetTeoriaCommand;
import com.scrumsquad.taskmaster.controller.commands.login.StudentLoginCommand;

public class CommandFactory {
    private CommandFactory() {
    }

    public static Command getCommand(String command) {
        return switch (command) {
            case CommandName.login -> new StudentLoginCommand();
            case CommandName.register -> new RegisterCommand();
            case CommandName.conceptMatchingGetData -> new ConceptMatchingGetDataCommand();
            case CommandName.conceptMatchingCheckAnswer -> new ConceptMatchingCheckAnswerCommand();
            case CommandName.teoriaGetData -> new GetTeoriaCommand();
            case CommandName.quizScrumGetData -> new QuizGetDataCommand();
            default -> null;
        };
    }
}
