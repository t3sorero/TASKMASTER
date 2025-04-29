package com.scrumsquad.taskmaster.views.student;

import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameSelectionView extends View {

    private int temaSeleccionado;

    @Override
    public JPanel build(BuildOptions options) {
        if (options != null && options.arguments().containsKey("tema")) {
            temaSeleccionado = (int) options.arguments().get("tema");
        }
        int rows;
        switch (temaSeleccionado) {
            case 3: {
                rows = 3;
                break;
            }
            default: {
                rows = 2;
            }
        }

        JPanel panelBotones = new JPanel(new GridLayout(rows, 1, 40, 40));
        panelBotones.setOpaque(false);

        // Botón juego de relacionar conceptos
        Rounded3dButton relacionarConceptosBtn = new Rounded3dButton("Juego: Relacionar conceptos");
        relacionarConceptosBtn.setFont(FontUtils.lato30);
        relacionarConceptosBtn.setBackground(AppColors.background);
        relacionarConceptosBtn.setForeground(AppColors.text);
        relacionarConceptosBtn.addActionListener(e -> {
            Map<String, Object> args = new HashMap<>();
            args.put("tema", temaSeleccionado);
            Navigator.getNavigator().to(ViewRoutes.conceptMatching, args);
        });

        // Botón test práctico
        Rounded3dButton testPracticoBtn = new Rounded3dButton("Test práctico");
        testPracticoBtn.setFont(FontUtils.lato30);
        testPracticoBtn.setBackground(AppColors.background);
        testPracticoBtn.setForeground(AppColors.text);
        testPracticoBtn.addActionListener(e -> {
            Map<String, Object> args = new HashMap<>();
            args.put("tema", temaSeleccionado);
            Navigator.getNavigator().to(ViewRoutes.practicalTest, args);
        });

        panelBotones.add(relacionarConceptosBtn);
        panelBotones.add(testPracticoBtn);

        // Solo si tema es 3, se añade el botón del Quiz Scrum
        if (temaSeleccionado == 3) {
            Rounded3dButton quizScrumBtn = new Rounded3dButton("Quiz Scrum");
            quizScrumBtn.setFont(FontUtils.lato30);
            quizScrumBtn.setBackground(AppColors.background);
            quizScrumBtn.setForeground(AppColors.text);
            quizScrumBtn.addActionListener(e -> {
                Map<String, Object> args = new HashMap<>();
                args.put("tema", temaSeleccionado);
                Navigator.getNavigator().to(ViewRoutes.quiz, args);
            });
            panelBotones.add(quizScrumBtn);
        }

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(AppColors.secondary40);
        contenedor.setBorder(SwingUtils.emptyBorder(60));
        contenedor.add(panelBotones, BorderLayout.CENTER);
        return contenedor;
    }

    @Override
    public void update(Context ctx) {
        // No actualiza nada dinámicamente por ahora
    }
}
