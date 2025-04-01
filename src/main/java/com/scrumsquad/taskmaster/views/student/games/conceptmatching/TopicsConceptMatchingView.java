package com.scrumsquad.taskmaster.views.student.games.conceptmatching;

import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class    TopicsConceptMatchingView extends View {

    @Override
    public JPanel build(BuildOptions options) {

        JPanel panelBotones = new JPanel(new GridLayout(3,1,40,40));
        panelBotones.setOpaque(false);
        Rounded3dButton button1 = createButton(1, "Equipos de trabajo");
        Rounded3dButton button2 = createButton(2, "Metodologías de Gestión de Proyectos");
        Rounded3dButton button3 = createButton(3, "Scrum");

        panelBotones.add(button1);
        panelBotones.add(button2);
        panelBotones.add(button3);

        JPanel panelContenedor = new JPanel(new BorderLayout());
        panelContenedor.setBackground(AppColors.secondary40);
        panelContenedor.setBorder(SwingUtils.emptyBorder(60)); // Márgenes (arriba, izquierda, abajo, derecha)
        panelContenedor.add(panelBotones, BorderLayout.CENTER);
        return panelContenedor;
    }

    @Override
    public void update(Context ctx) {

    }

    private Rounded3dButton createButton(int tema, String descripcion){
        Rounded3dButton button = new Rounded3dButton("TEMA " + tema + " - " + descripcion);
        button.setLetterSpacing(2);
        button.setFont(FontUtils.lato30);
        button.setBackground(AppColors.background);
        button.setForeground(AppColors.text);

        button.addActionListener((e)->{
            Map<String, Object> arguments = new HashMap<>();
            arguments.put("tema", tema);
            Navigator.getNavigator().to(ViewRoutes.conceptMatching, arguments);
        });

        return button;
    }

}


