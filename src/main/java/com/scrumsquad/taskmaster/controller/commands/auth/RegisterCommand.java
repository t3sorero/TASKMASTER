package com.scrumsquad.taskmaster.controller.commands.auth;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;

import java.util.HashMap;
import java.util.Map;

public class RegisterCommand implements Command {
    @Override
    public Context execute(Context ctx) {
        try {
            Map<String, Object> args = new HashMap<>();
            args.put("emailTaken", true);
            return new Context(CommandName.registerKo, args);
        } catch (Exception ex) {
            return new Context(CommandName.registerKo);
        }
    }
}
