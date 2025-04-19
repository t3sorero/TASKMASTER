package com.scrumsquad.taskmaster.views.student.games.quiz;

import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.lib.swing.RoundedButton;
import com.scrumsquad.taskmaster.lib.swing.RoundedPanel;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class QuizView extends View {

    private static final String backgroundPath = "/images/quiz_background.jpg";
    private static final int totalQuestions = 6;
    private static final Color progresoRemainingColor = AppColors.secondaryLight;
    private static final Color progresoDoneColor = AppColors.primary;
    private static final Color progresoCurrentColor = new Color(0xFF9D33);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private java.util.List<JPanel> progresoQuestions = new ArrayList<>(totalQuestions);

    private int currentQuestion = 0;
    private Timer timer;

    @Override
    public JPanel build(BuildOptions options) {

        JPanel mainPanel = new ImagePanel(backgroundPath, ImagePanel.CENTER);
        mainPanel.setLayout(new GridLayout(1, 1));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        cardPanel.add("progreso", createProgresoPanel());
        cardPanel.add("pregunta", createPreguntaPanel());
        // TODO resto de paneles

        mainPanel.add(cardPanel);

        showProgresoPanel();

        return mainPanel;
    }

    private JPanel createProgresoPanel() {
        JPanel progresoPanel = new JPanel(new GridLayout(totalQuestions, 1, 16, 16));
        progresoPanel.setOpaque(false);
        progresoPanel.setBorder(SwingUtils.emptyBorder(64));
        Stack<JPanel> stack = new Stack<>();
        for (int i = totalQuestions; i > 0; i--) {
            JPanel question = new RoundedPanel(4);
            question.setLayout(new GridBagLayout());
            GridBagConstraints constraints = SwingUtils.verticalConstraints();
            question.setBackground(progresoRemainingColor);
            question.setEnabled(false);
            JLabel label = new JLabel(i + "");
            label.setForeground(AppColors.text);
            label.setFont(FontUtils.lato30);
            question.add(label, constraints);
            stack.add(question);
            progresoPanel.add(question);
        }
        while (!stack.isEmpty()) {
            progresoQuestions.add(stack.pop());
        }
        return progresoPanel;
    }

    private void showProgresoPanel() {
        int i = 0;
        for (JPanel question : progresoQuestions) {
            if (currentQuestion > i) {
                question.setBackground(progresoDoneColor);
            } else if (currentQuestion == i) {
                question.setBackground(progresoCurrentColor);
            } else {
                question.setBackground(progresoRemainingColor);
            }
            i++;
        }
        cardLayout.show(cardPanel, "progreso");
        showPreguntaPanel(0);
    }

    private void showPreguntaPanel(int n) {
        int cont = Math.max(0, n);
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = null;
        timer = new Timer(400, (e) -> {
            if (cont < 7) {
                progresoQuestions.get(currentQuestion)
                        .setBackground(cont % 2 == 0 ? progresoCurrentColor : progresoRemainingColor);
                showPreguntaPanel(cont + 1);
            } else {
                // TODO mostrar pregunta concreta
                cardLayout.show(cardPanel, "pregunta");
            }
        });
        timer.start();
    }

    private JPanel createPreguntaPanel() {
        JPanel preguntaPanel = new JPanel();
        preguntaPanel.setOpaque(false);
        return preguntaPanel;
    }

    private void showPreguntaPanel() {
        cardLayout.show(cardPanel, "pregunta");
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDispose() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = null;
    }

    @Override
    public void update(Context ctx) {

    }
}
