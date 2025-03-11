package com.scrumsquad.taskmaster.controller.commands;

import com.scrumsquad.taskmaster.controller.commands.auth.LoginCommand;
import com.scrumsquad.taskmaster.controller.commands.auth.RegisterCommand;

public class CommandFactory {
    private CommandFactory() {
    }

    public static Command getCommand(String command) {
        return switch (command) {
            case CommandName.login -> new LoginCommand();
            case CommandName.register -> new RegisterCommand();
            default -> null;
        };
    }
}
