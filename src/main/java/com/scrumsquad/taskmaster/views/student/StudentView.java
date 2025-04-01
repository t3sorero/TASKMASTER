package com.scrumsquad.taskmaster.views.student;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.*;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.lib.swing.RoundedButton;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class StudentView extends View {
    private static final String backgroundPath = "/images/student-background.jpg";

    public StudentView() {
        ViewOptions options = new ViewOptions();
        JPanel panel = new JPanel();
        options.addHeaderComponent(panel);
        setOptions(options);
    }

    @Override
    public JPanel build(BuildOptions options) {
        final JPanel panel = new ImagePanel(backgroundPath);
        panel.setLayout(new GridLayout(1, 2, 16, 0));
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                final int w = getWidth();
                final int h = getHeight();
                Color bg = getBackground();
                // TODO
            }
        };
        button.setOpaque(false);
        panel.add(button);
        return panel;
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;
        switch (ctx.getCommandName()) {

        }
    }
}
