package com.scrumsquad.taskmaster.views.student.games.shortquestions;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ShortQuestionsView extends View {

    private JPanel questionsPanel;
    private List<JTextField> answerFields = new ArrayList<>();
    private List<JLabel> resultIcons = new ArrayList<>();
    private JLabel correctNumberLabel;
    private JLabel incorrectNumberLabel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel loadingPanel;
    private JPanel contentPanel;
    private int tema;
    private List<ShortQuestionDTO> preguntas;
    private Map<Integer, Integer> indexToId = new HashMap<>();

    @Override
    public JPanel build(BuildOptions options) {
        tema = (int) options.arguments().getOrDefault("tema", 1);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(AppColors.secondary);

        loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setBackground(AppColors.secondary);
        JLabel loadingIcon = new JLabel(ResourceLoader.loadImageIcon("/images/loading_80x80.gif"));
        loadingPanel.add(loadingIcon);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(AppColors.secondary);

        // Título llamativo
        String nombreTema = "TEMA " + tema;
        JLabel tituloLabel = new JLabel("TEST PRÁCTICO - " + nombreTema.toUpperCase());
        tituloLabel.setFont(FontUtils.lato30);
        tituloLabel.setForeground(AppColors.text);
        tituloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        contentPanel.add(tituloLabel, BorderLayout.NORTH);

        // Panel con preguntas
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(AppColors.secondary);

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(960, 480));
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppColors.secondaryLight);
        bottomPanel.setBorder(SwingUtils.emptyBorder(16, 32));

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
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        cardPanel.add("loading", loadingPanel);
        cardPanel.add("loaded", contentPanel);

        return cardPanel;
    }

    @Override
    public void onLoad() {
        cardLayout.show(cardPanel, "loading");
        Context ctx = new Context(CommandName.shortQuestionsGetData);
        ctx.setArgument("tema", tema);
        AppController.getInstance().action(ctx);
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;

        switch (ctx.getCommandName()) {
            case CommandName.shortQuestionsGetDataOK -> {
                preguntas = (List<ShortQuestionDTO>) ctx.getArguments().get("preguntas");
                loadQuestions();
                cardLayout.show(cardPanel, "loaded");
            }
            case CommandName.shortQuestionsGetDataKO -> {
                JOptionPane.showMessageDialog(null, "Error al cargar las preguntas", "Error", JOptionPane.ERROR_MESSAGE);
                cardLayout.show(cardPanel, "loaded");
            }
            case CommandName.shortQuestionsCheckAnswersOK -> {
                Map<Integer, Boolean> feedback = (Map<Integer, Boolean>) ctx.getArguments().get("feedback");
                showResults(feedback);
            }
        }
    }

    private void loadQuestions() {
        questionsPanel.removeAll();
        answerFields.clear();
        resultIcons.clear();
        indexToId.clear();

        for (int i = 0; i < preguntas.size(); i++) {
            ShortQuestionDTO pregunta = preguntas.get(i);
            indexToId.put(i, pregunta.getId());

            JPanel preguntaPanel = new JPanel(new BorderLayout());
            preguntaPanel.setBackground(AppColors.secondaryLight);
            preguntaPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(12, 24, 12, 24),
                    BorderFactory.createLineBorder(AppColors.secondary, 2)
            ));
            preguntaPanel.setMaximumSize(new Dimension(900, 120)); // Ajusta un poco más de altura

            // ---- Aquí el cambio importante ----
            JLabel label = new JLabel("<html>" + (i + 1) + ". " + pregunta.getPregunta() + "</html>");
            label.setFont(FontUtils.lato20);
            label.setForeground(AppColors.text);
            label.setVerticalAlignment(SwingConstants.TOP); // Para alinear bien arriba
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0)); // Margen debajo del texto

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
        Map<Integer, String> userAnswers = new HashMap<>();
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < answerFields.size(); i++) {
            int id = indexToId.get(i);
            ids.add(id);
            userAnswers.put(id, answerFields.get(i).getText());
        }

        Context ctx = new Context(CommandName.shortQuestionsCheckAnswers);
        ctx.setArgument("userAnswers", userAnswers);
        ctx.setArgument("preguntasIds", ids);
        AppController.getInstance().action(ctx);
    }

    private void showResults(Map<Integer, Boolean> feedback) {
        int correctas = 0;
        int incorrectas = 0;

        for (int i = 0; i < answerFields.size(); i++) {
            int id = indexToId.get(i);
            boolean esCorrecta = feedback.getOrDefault(id, false);
            JLabel icon = resultIcons.get(i);
            if (esCorrecta) {
                icon.setIcon(new ImageIcon(getClass().getResource("/images/good_icon.png")));
                correctas++;
            } else {
                icon.setIcon(new ImageIcon(getClass().getResource("/images/bad_icon.png")));
                incorrectas++;
            }
        }

        correctNumberLabel.setText(String.valueOf(correctas));
        incorrectNumberLabel.setText(String.valueOf(incorrectas));
    }
}
