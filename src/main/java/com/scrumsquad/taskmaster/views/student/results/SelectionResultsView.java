package com.scrumsquad.taskmaster.views.student.results;

import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButtonWithTextAndImage;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;

public class SelectionResultsView extends View {
    @Override
    public JPanel build(BuildOptions options) {
        JPanel mainPanel = new JPanel();

        JPanel panelBotones = new JPanel(new GridLayout(2,1,20,20));
        panelBotones.setOpaque(false);

        Rounded3dButton exerciseButton = new Rounded3dButtonWithTextAndImage("REALIZAR EJERCICIO", "/images/ejercicios.png");
        exerciseButton.addActionListener((e) ->{
            Navigator.getNavigator().to(ViewRoutes.topicsConceptMatching);
        });
        exerciseButton.setBackground(new Color(250, 148, 118));
        exerciseButton.setForeground(Color.darkGray);
        panelBotones.add(exerciseButton);
        Rounded3dButton resultsButton = new Rounded3dButtonWithTextAndImage("CONSULTAR RESULTADOS", "/images/results-icon.png");
        resultsButton.setEnabled(false);
        resultsButton.setForeground(Color.darkGray);
        resultsButton.setBackground(new Color(244, 206, 255));
        panelBotones.add(resultsButton);

        mainPanel.setBackground(AppColors.secondary40);
        mainPanel.setBorder(SwingUtils.emptyBorder(60)); // Márgenes (arriba, izquierda, abajo, derecha)
        mainPanel.add(panelBotones, BorderLayout.CENTER);

        return mainPanel;
    }

    @Override
    public void update(Context ctx) {

    }
}
