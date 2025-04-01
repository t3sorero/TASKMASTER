package com.scrumsquad.taskmaster.lib;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;

import javax.swing.*;
import java.util.Map;

public abstract class Widget {
    public abstract JPanel build(View.BuildOptions options);

    public record BuildOptions(JFrame frame, Map<String, Object> arguments) {
    }

    public abstract void update(Context ctx);
}
