package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.controller.commands.auth.LoginCommand;
import com.scrumsquad.taskmaster.controller.commands.auth.RegisterCommand;
import com.scrumsquad.taskmaster.services.ConceptMatchingService;

public class CommandFactory {
    private CommandFactory() {
    }

    public static Command getCommand(String command) {
        return switch (command) {
            case CommandName.login -> new LoginCommand();
            case CommandName.register -> new RegisterCommand();
            case CommandName.conceptMatching -> new ConceptMatchingCommand(new ConceptMatchingService(/* Provide appropriate DAO instance here */)); // Nuevo comando agregado
            default -> null;
        };
    }
}
