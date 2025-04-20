package com.scrumsquad.taskmaster.views.student.games.practicaltest;

import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PracticalTestView extends View {

    private JPanel questionsPanel;
    private List<JTextField> answerFields = new ArrayList<>();
    private List<JLabel> resultIcons = new ArrayList<>();
    private JLabel correctNumberLabel;
    private JLabel incorrectNumberLabel;

    @Override
    public JPanel build(BuildOptions options) {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppColors.secondary);

        // Título llamativo
        String nombreTema = "TEMA " + options.arguments().getOrDefault("tema", "1");
        JLabel tituloLabel = new JLabel("TEST PRÁCTICO - " + nombreTema.toUpperCase());
        tituloLabel.setFont(FontUtils.lato30);
        tituloLabel.setForeground(AppColors.text);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));

        mainPanel.add(tituloLabel, BorderLayout.NORTH);


        // Panel con preguntas
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(AppColors.secondary);

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadQuestions();

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppColors.secondaryLight);
        bottomPanel.setBorder(SwingUtils.emptyBorder(16, 32));

        // Resultados (iconos correctos/incorrectos)
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        resultPanel.setOpaque(false);

        JLabel correctIcon = new JLabel(new ImageIcon(getClass().getResource("/images/good_icon.png")));
        correctNumberLabel = new JLabel("0");
        correctNumberLabel.setFont(FontUtils.lato20);
        correctNumberLabel.setForeground(AppColors.accentText);

        JLabel incorrectIcon = new JLabel(new ImageIcon(getClass().getResource("/images/bad_icon.png")));
        incorrectNumberLabel = new JLabel("0");
        incorrectNumberLabel.setFont(FontUtils.lato20);
        incorrectNumberLabel.setForeground(AppColors.accentText);

        resultPanel.add(correctIcon);
        resultPanel.add(correctNumberLabel);
        resultPanel.add(incorrectIcon);
        resultPanel.add(incorrectNumberLabel);

        // Botones
        JButton submitButton = new JButton("ENVIAR RESPUESTAS");
        submitButton.setFont(FontUtils.lato16);
        submitButton.setBackground(AppColors.primary);
        submitButton.setForeground(AppColors.primaryText);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 36));
        submitButton.addActionListener(e -> checkAnswers());

        JButton exitButton = new JButton("SALIR");
        exitButton.setFont(FontUtils.lato16);
        exitButton.setBackground(AppColors.primary);
        exitButton.setForeground(AppColors.primaryText);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(120, 36));
        exitButton.addActionListener(e -> Navigator.getNavigator().back());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(submitButton);
        buttonsPanel.add(exitButton);

        bottomPanel.add(resultPanel, BorderLayout.WEST);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void loadQuestions() {
        questionsPanel.removeAll();
        answerFields.clear();
        resultIcons.clear();

        String[] preguntas = {
                "¿Qué es un equipo de trabajo?",
                "¿Qué papel tiene un coordinador?",
                "¿Qué significa sinergia en un equipo?",
                "¿Cuál es la ventaja principal del trabajo colaborativo?",
                "¿Qué es una metodología ágil?",
                "¿Qué representa el rol de Scrum Master?",
                "¿Qué herramienta se usa para seguimiento en Scrum?",
                "¿Qué es una retrospectiva?",
                "¿Qué significa MVP?",
                "¿Qué se hace en una Daily Meeting?"
        };

        for (int i = 0; i < preguntas.length; i++) {
            JPanel preguntaPanel = new JPanel(new BorderLayout());
            preguntaPanel.setBackground(AppColors.secondaryLight);
            preguntaPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(12, 24, 12, 24),
                    BorderFactory.createLineBorder(AppColors.secondary, 2)
            ));
            preguntaPanel.setMaximumSize(new Dimension(900, 100));

            JLabel label = new JLabel((i + 1) + ". " + preguntas[i]);
            label.setFont(FontUtils.lato20);
            label.setForeground(AppColors.text);

            JTextField textField = new JTextField();
            textField.setFont(FontUtils.lato16);

            JLabel icon = new JLabel();
            icon.setPreferredSize(new Dimension(32, 32));

            answerFields.add(textField);
            resultIcons.add(icon);

            JPanel respuestaPanel = new JPanel(new BorderLayout(8, 0));
            respuestaPanel.setOpaque(false);
            respuestaPanel.add(textField, BorderLayout.CENTER);
            respuestaPanel.add(icon, BorderLayout.EAST);

            preguntaPanel.add(label, BorderLayout.NORTH);
            preguntaPanel.add(respuestaPanel, BorderLayout.SOUTH);

            questionsPanel.add(preguntaPanel);
            questionsPanel.add(Box.createVerticalStrut(12));
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    public void checkAnswers() {
        String[] respuestasCorrectas = {
                "Conjunto de personas con objetivos comunes",
                "Organiza y guía al equipo",
                "Resultado superior al trabajo individual",
                "Mejor rendimiento y comunicación",
                "Marco de trabajo flexible y adaptativo",
                "Facilitador del proceso Scrum",
                "Tablero Kanban o herramientas como Jira",
                "Reunión para mejorar el proceso",
                "Producto mínimo viable",
                "Compartir avances y plan diario"
        };

        int correctas = 0;
        int incorrectas = 0;

        for (int i = 0; i < respuestasCorrectas.length; i++) {
            String respuestaUsuario = answerFields.get(i).getText().trim().toLowerCase();
            String respuestaCorrecta = respuestasCorrectas[i].toLowerCase();

            JLabel icon = resultIcons.get(i);
            if (respuestaUsuario.equals(respuestaCorrecta)) {
                correctas++;
                icon.setIcon(new ImageIcon(getClass().getResource("/images/good_icon.png")));
            } else {
                incorrectas++;
                icon.setIcon(new ImageIcon(getClass().getResource("/images/bad_icon.png")));
            }
        }

        correctNumberLabel.setText(String.valueOf(correctas));
        incorrectNumberLabel.setText(String.valueOf(incorrectas));
    }

    @Override
    public void update(Context ctx) {
        // Para lógica futura con servicio
    }

    @Override
    public void onLoad() {
        // Para cargar preguntas desde el backend en el futuro
    }
}
