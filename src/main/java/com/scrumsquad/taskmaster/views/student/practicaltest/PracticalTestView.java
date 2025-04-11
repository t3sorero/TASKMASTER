package com.scrumsquad.taskmaster.views.student.practicaltest;

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
        mainPanel.setBackground(AppColors.secondaryLight);

        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(AppColors.secondaryLight);
        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setPreferredSize(new Dimension(800, 480));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        loadQuestions();

        // Panel inferior con resultados y botones
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(SwingUtils.emptyBorder(16, 32));

        // Resultados (correctos/incorrectos)
        JPanel resultPanel = new JPanel();
        resultPanel.setOpaque(false);
        resultPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));

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

        // Botón de salir
        JButton exitButton = new JButton("SALIR");
        exitButton.setFont(FontUtils.lato14);
        exitButton.setBackground(AppColors.primary);
        exitButton.setForeground(AppColors.primaryText);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(120, 36));
        exitButton.addActionListener(e -> Navigator.getNavigator().back());

        JButton submitButton = new JButton("ENVIAR RESPUESTAS");
        submitButton.setFont(FontUtils.lato14);
        submitButton.setBackground(AppColors.primary);
        submitButton.setForeground(AppColors.primaryText);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(180, 36));
        submitButton.addActionListener(e -> checkAnswers());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(submitButton);
        buttonsPanel.add(exitButton);

        bottomPanel.add(resultPanel, BorderLayout.WEST);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void loadQuestions() {
        questionsPanel.removeAll();
        answerFields.clear();
        resultIcons.clear();

        // Simulamos 10 preguntas
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

        // Simulamos las respuestas correctas
        String[] respuestas = {
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

        for (int i = 0; i < preguntas.length; i++) {
            JPanel preguntaPanel = new JPanel(new BorderLayout());
            preguntaPanel.setBackground(AppColors.secondaryLight);
            preguntaPanel.setBorder(SwingUtils.emptyBorder(16, 0));
            preguntaPanel.setPreferredSize(new Dimension(700, 72));

            JLabel preguntaLabel = new JLabel((i + 1) + ". " + preguntas[i]);
            preguntaLabel.setFont(FontUtils.lato14);
            preguntaLabel.setPreferredSize(new Dimension(400, 24));

            JTextField respuestaField = new JTextField();
            respuestaField.setFont(FontUtils.lato14);

            JLabel iconLabel = new JLabel();
            iconLabel.setPreferredSize(new Dimension(32, 32));

            answerFields.add(respuestaField);
            resultIcons.add(iconLabel);

            JPanel respuestaPanel = new JPanel(new BorderLayout(8, 0));
            respuestaPanel.setOpaque(false);
            respuestaPanel.add(respuestaField, BorderLayout.CENTER);
            respuestaPanel.add(iconLabel, BorderLayout.EAST);

            preguntaPanel.add(preguntaLabel, BorderLayout.NORTH);
            preguntaPanel.add(respuestaPanel, BorderLayout.SOUTH);

            questionsPanel.add(preguntaPanel);
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    private void checkAnswers() {
        // Simulamos las respuestas correctas
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
        // cuando se conecte al servicio, aquí se gestionarán los resultados
    }

    @Override
    public void onLoad() {
        // aquí se podrá cargar las preguntas desde el servicio
    }
}
