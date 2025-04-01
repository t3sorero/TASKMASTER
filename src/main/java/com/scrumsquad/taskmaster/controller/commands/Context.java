package com.scrumsquad.taskmaster.controller.commands;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private String commandName;
    private final Map<String, Object> arguments;

    public Context(String commandName) throws IllegalArgumentException {
        this(commandName, new HashMap<>());
    }

    public Context(String commandName, Map<String, Object> arguments) throws IllegalArgumentException {
        if (commandName == null) throw new IllegalArgumentException();
        this.commandName = commandName;
        this.arguments = arguments != null ? arguments : new HashMap<>();
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public String getCommandName() {
        return commandName;
    }
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
    public void setArgument(String key, Object value) {
        arguments.put(key, value);
    }

}
