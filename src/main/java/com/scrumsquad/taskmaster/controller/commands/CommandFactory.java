package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.controller.commands.auth.LoginCommand;
import com.scrumsquad.taskmaster.controller.commands.auth.RegisterCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingCommand;
import com.scrumsquad.taskmaster.controller.commands.conceptmatching.ConceptMatchingGetDataCommand;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptMatchingService;

public class CommandFactory {
    private CommandFactory() {
    }

    public static Command getCommand(String command) {
        return switch (command) {
            case CommandName.login -> new LoginCommand();
            case CommandName.register -> new RegisterCommand();
            case CommandName.conceptMatchingGetData -> new ConceptMatchingGetDataCommand();
            default -> null;
        };
    }
}
