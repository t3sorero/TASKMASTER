package com.scrumsquad.taskmaster.controller.commands;

public interface Command {
    Context execute(Context ctx);
}
