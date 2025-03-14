package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.controller.commands.auth.LoginCommand;
import com.scrumsquad.taskmaster.controller.commands.auth.RegisterCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingCheckAnswerCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingGetDataCommand;

public class CommandFactory {
    private CommandFactory() {
    }

    public static Command getCommand(String command) {
        return switch (command) {
            case CommandName.login -> new LoginCommand();
            case CommandName.register -> new RegisterCommand();
            case CommandName.conceptMatchingGetData -> new ConceptMatchingGetDataCommand();
            case CommandName.conceptMatchingCheckAnswer -> new ConceptMatchingCheckAnswerCommand();
            default -> null;
        };
    }
}
