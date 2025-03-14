package com.scrumsquad.taskmaster.views.student.games.conceptmatching;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.lib.CommonUtils;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButton;
import com.scrumsquad.taskmaster.lib.swing.RoundedButton;
import com.scrumsquad.taskmaster.lib.swing.RoundedPanel;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ConceptMatchingView extends View {

    private static final int width = 960;
    private static final int height = 592;

    private static final Color[] selectedColors = {
            new Color(0xF8BBD0),
            new Color(0xB2E7C8),
            new Color(0xD6B3E9),
            new Color(0xFFF3B0)
    };

    private final Map<Integer, Integer> conceptoMap = new HashMap<>();
    private final Map<Integer, Integer> definicionMap = new HashMap<>();

    private JPanel gamePanel;
    private JPanel gameResultPanel;
    private JPanel buttonsConceptosPanel;
    private JPanel buttonsDefinicionesPanel;
    private final java.util.List<JButton> conceptosButtons = new ArrayList<>();
    private final java.util.List<JPanel> conceptosGood = new ArrayList<>();
    private final java.util.List<JPanel> conceptosBad = new ArrayList<>();
    private final java.util.List<JButton> definicionesButtons = new ArrayList<>();
    private final java.util.List<JPanel> definicionesGood = new ArrayList<>();
    private final java.util.List<JPanel> definicionesBad = new ArrayList<>();
    private JLabel correctNumberLabel;
    private JLabel incorrectNumberLabel;

    private JPanel actionButtonsPanel;
    private JButton sendButton;

    private Integer selectedConcepto = null;
    private Integer selectedDefinicion = null;
    private final Set<Color> colorsUsed = new HashSet<>();
    private Color selectedColor = null;

    private ConceptosDefinicionesTOA toa;

    @Override
    public JPanel build(BuildOptions options) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppColors.secondary40);
        GridBagConstraints constraints = SwingUtils.verticalConstraints();

        gamePanel = new RoundedPanel(16);
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setPreferredSize(new Dimension(width, height));
        gamePanel.setBackground(AppColors.secondary);

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(SwingUtils.emptyBorder(16, 0));
        gameResultPanel = new JPanel(new GridBagLayout());
        gameResultPanel.setOpaque(false);
        GridBagConstraints horizontalConstraints = SwingUtils.horizontalConstraints();
        horizontalConstraints.insets = new Insets(0, 8, 0, 8);
        final JPanel correctIcon = new ImagePanel("/images/good_icon.png");
        correctIcon.setPreferredSize(new Dimension(32, 32));
        correctNumberLabel = new JLabel("-");
        correctNumberLabel.setFont(FontUtils.lato16);
        correctNumberLabel.setForeground(AppColors.accentText);
        final JPanel incorrectIcon = new ImagePanel("/images/bad_icon.png");
        incorrectIcon.setPreferredSize(new Dimension(32, 32));
        incorrectNumberLabel = new JLabel("-");
        incorrectNumberLabel.setFont(FontUtils.lato16);
        incorrectNumberLabel.setForeground(AppColors.accentText);
        gameResultPanel.add(correctIcon, horizontalConstraints);
        gameResultPanel.add(correctNumberLabel, horizontalConstraints);
        gameResultPanel.add(incorrectIcon, horizontalConstraints);
        gameResultPanel.add(incorrectNumberLabel, horizontalConstraints);
        bottomPanel.add(gameResultPanel, BorderLayout.WEST);

        actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        actionButtonsPanel.setBorder(SwingUtils.emptyBorderBottom(16));
        actionButtonsPanel.setOpaque(false);
        sendButton = new RoundedButton("COMPROBAR RESPUESTAS");
        sendButton.setFont(FontUtils.lato14);
        sendButton.setBackground(AppColors.primary);
        sendButton.setForeground(AppColors.primaryText);
        sendButton.setBorder(SwingUtils.emptyBorder(64, 16));
        sendButton.addActionListener((e) -> {
            if (toa == null) return;
            sendButton.setEnabled(false);
            for (JButton button : conceptosButtons) {
                button.setEnabled(false);
            }
            for (JButton button : definicionesButtons) {
                button.setEnabled(false);
            }
            Map<Integer, Integer> data = new HashMap<>();
            Set<Integer> conceptosIds = new HashSet<>();
            for (Map.Entry<Integer, Integer> entry : conceptoMap.entrySet()) {
                data.put(
                        toa.getConceptos().get(entry.getKey()).getId(),
                        toa.getDefiniciones().get(entry.getValue()).getId());
            }
            for (ConceptoDTO concepto : toa.getConceptos()) {
                conceptosIds.add(concepto.getId());
            }
            Context ctx = new Context(CommandName.conceptMatchingCheckAnswer);
            ctx.setArgument("userAnswers", data);
            ctx.setArgument("conceptosIds", conceptosIds);
            AppController.getInstance().action(ctx);
        });
        actionButtonsPanel.add(sendButton);
        bottomPanel.add(actionButtonsPanel, BorderLayout.EAST);

        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 64, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(SwingUtils.emptyBorder(32, 0));
        buttonsConceptosPanel = new JPanel(new GridBagLayout());
        buttonsConceptosPanel.setOpaque(false);
        buttonsDefinicionesPanel = new JPanel(new GridBagLayout());
        buttonsDefinicionesPanel.setOpaque(false);
        buttonsPanel.add(buttonsConceptosPanel);
        buttonsPanel.add(buttonsDefinicionesPanel);

        gamePanel.add(buttonsPanel, BorderLayout.CENTER);

        panel.add(gamePanel, constraints);
        return panel;
    }

    @Override
    public void onLoad() {
        AppController.getInstance().action(new Context(CommandName.conceptMatchingGetData));
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;
        switch (ctx.getCommandName()) {
            case CommandName.conceptMatchingGetDataOk: {
                toa = (ConceptosDefinicionesTOA) ctx.getArguments().get("toa");
                addConceptos(toa.getConceptos());
                addDefiniciones(toa.getDefiniciones());
                buttonsConceptosPanel.revalidate();
                buttonsDefinicionesPanel.revalidate();
                break;
            }
            case CommandName.conceptMatchingCheckAnswerOk: {
                Map<Integer, Boolean> feedback = (Map<Integer, Boolean>) ctx.getArguments().get("feedback");
                setGameResults(feedback);
                break;
            }
            case CommandName.conceptMatchingGetDataKo:
            case CommandName.conceptMatchingCheckAnswerKo: {
                System.out.println("Error");
                break;
            }
        }
    }

    private void setGameResults(Map<Integer, Boolean> results) {
        if (toa == null) return;
        int correct = 0;
        int incorrect = 0;
        for (Map.Entry<Integer, Boolean> entry : results.entrySet()) {
            int index = -1;
            for (int i = 0; i < toa.getConceptos().size() && index == -1; i++) {
                if (toa.getConceptos().get(i).getId() == entry.getKey()) {
                    index = i;
                }
            }
            if (index != -1 && conceptoMap.containsKey(index)) {
                if (entry.getValue()) {
                    conceptosGood.get(index).setVisible(true);
                    definicionesGood.get(conceptoMap.get(index)).setVisible(true);
                    correct++;
                } else {
                    conceptosBad.get(index).setVisible(true);
                    definicionesBad.get(conceptoMap.get(index)).setVisible(true);
                    incorrect++;
                }
            }
        }
        correctNumberLabel.setText(correct + "");
        incorrectNumberLabel.setText(incorrect + "");
        gamePanel.revalidate();
    }

    private Color getButtonColor() {
        if (selectedColor != null) return selectedColor;
        int index = 0;
        for (; index < selectedColors.length; index++) {
            Color c = selectedColors[index];
            if (!colorsUsed.contains(c)) {
                return c;
            }
        }
        return selectedColors[selectedColors.length - 1];
    }

    private void addConceptos(java.util.List<ConceptoDTO> conceptos) {
        System.out.println(conceptos.size());
        buttonsConceptosPanel.removeAll();
        conceptosButtons.clear();
        selectedColor = null;
        conceptoMap.clear();
        colorsUsed.clear();
        GridBagConstraints constraints = SwingUtils.verticalConstraints();
        constraints.insets = new Insets(24, 0, 24, 0);
        for (int i = 0; i < conceptos.size(); i++) {
            ConceptoDTO concepto = conceptos.get(i);
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(416, 64));
            Rounded3dButton button = new Rounded3dButton(concepto.getNombre());
            button.setLetterSpacing(0);
            button.setFont(FontUtils.lato14);
            button.setBackground(AppColors.background);
            button.setForeground(AppColors.text);
            button.setBounds(0, 0, 416, 64);
            final int index = i;
            button.addActionListener((e) -> {
                // Antes -> Si existe unión se quita
                if (conceptoMap.containsKey(index)) {
                    Integer definicion = conceptoMap.remove(index);
                    definicionMap.remove(definicion);
                    final Color oldColor = button.getBackground();
                    colorsUsed.remove(oldColor);
                    button.setBackground(AppColors.background);
                    button.setForeground(AppColors.text);
                    JButton definicionButton = definicionesButtons.get(definicion);
                    definicionButton.setBackground(AppColors.background);
                    definicionButton.setForeground(AppColors.text);
                    if (selectedConcepto == null && selectedDefinicion == null) {
                        selectedColor = oldColor;
                    } else if (selectedConcepto == null) {
                        selectedColor = definicionesButtons.get(selectedDefinicion).getBackground();
                    } else {
                        selectedColor = oldColor;
                    }
                }
                // Caso 1: No hay ninguna seleccionada -> Se pone el nuevo (color y selected)
                if (selectedConcepto == null && selectedDefinicion == null) {
                    button.setBackground(getButtonColor());
                    button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                    selectedConcepto = index;
                }
                // Caso 2: concepto seleccionado -> Se quita el actual y si no es el mismo se pone el nuevo (color y selected)
                else if (selectedConcepto != null) {
                    JButton selected = conceptosButtons.get(selectedConcepto);
                    selected.setBackground(AppColors.background);
                    selected.setForeground(AppColors.text);
                    selectedColor = null;
                    if (selectedConcepto != index) {
                        button.setBackground(getButtonColor());
                        button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                        selectedConcepto = index;
                    } else {
                        selectedConcepto = null;
                    }
                }
                // Caso 3: definicion seleccionado -> Se pone el nuevo (color y selected) y se crea la unión
                else {
                    button.setBackground(getButtonColor());
                    button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                    conceptoMap.put(index, selectedDefinicion);
                    definicionMap.put(selectedDefinicion, index);
                    colorsUsed.add(getButtonColor());
                    selectedConcepto = null;
                    selectedDefinicion = null;
                    selectedColor = null;
                }
                // Bloqueamos si hemos terminado
                if (conceptoMap.size() == conceptosButtons.size()) {
                    for (int j = 0; j < definicionesButtons.size(); j++) {
                        if (!definicionMap.containsKey(j)) {
                            definicionesButtons.get(j).setEnabled(false);
                        }
                    }
                } else {
                    for (JButton b : definicionesButtons) {
                        b.setEnabled(true);
                    }
                }
            });
            JPanel goodPanel = new ImagePanel("/images/good_icon.png");
            goodPanel.setBounds(4, 4, 20, 20);
            goodPanel.setVisible(false);
            JPanel badPanel = new ImagePanel("/images/bad_icon.png");
            badPanel.setBounds(4, 4, 20, 20);
            badPanel.setVisible(false);

            conceptosButtons.add(button);
            conceptosGood.add(goodPanel);
            conceptosBad.add(badPanel);
            layeredPane.add(button, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(goodPanel, JLayeredPane.PALETTE_LAYER);
            layeredPane.add(badPanel, JLayeredPane.DRAG_LAYER);
            buttonsConceptosPanel.add(layeredPane, constraints);
        }
    }

    private void addDefiniciones(java.util.List<DefinicionDTO> definiciones) {
        System.out.println(definiciones.size());
        buttonsDefinicionesPanel.removeAll();
        definicionesButtons.clear();
        selectedColor = null;
        definicionMap.clear();
        colorsUsed.clear();
        GridBagConstraints constraints = SwingUtils.verticalConstraints();
        constraints.insets = new Insets(16, 0, 16, 0);
        for (int i = 0; i < definiciones.size(); i++) {
            DefinicionDTO definicion = definiciones.get(i);
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(416, 48));
            Rounded3dButton button = new Rounded3dButton(definicion.getDescripcion());
            button.setLetterSpacing(0);
            button.setFont(FontUtils.lato14);
            button.setBackground(AppColors.background);
            button.setForeground(AppColors.text);
            button.setBounds(0,0,416, 48);
            final int index = i;
            button.addActionListener((e) -> {
                // Antes -> Si existe unión se quita
                if (definicionMap.containsKey(index)) {
                    Integer concepto = definicionMap.remove(index);
                    conceptoMap.remove(concepto);
                    final Color oldColor = button.getBackground();
                    colorsUsed.remove(oldColor);
                    button.setBackground(AppColors.background);
                    button.setForeground(AppColors.text);
                    JButton conceptoButton = conceptosButtons.get(concepto);
                    conceptoButton.setBackground(AppColors.background);
                    conceptoButton.setForeground(AppColors.text);
                    if (selectedConcepto == null && selectedDefinicion == null) {
                        selectedColor = oldColor;
                    } else if (selectedDefinicion == null) {
                        selectedColor = conceptosButtons.get(selectedConcepto).getBackground();
                    } else {
                        selectedColor = oldColor;
                    }
                }
                // Caso 1: No hay ninguna seleccionada -> Se pone el nuevo (color y selected)
                if (selectedConcepto == null && selectedDefinicion == null) {
                    button.setBackground(getButtonColor());
                    button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                    selectedDefinicion = index;
                }
                // Caso 2: definicion seleccionado -> Se quita el actual y si no es el mismo se pone el nuevo (color y selected)
                else if (selectedDefinicion != null) {
                    JButton selected = definicionesButtons.get(selectedDefinicion);
                    selected.setBackground(AppColors.background);
                    selected.setForeground(AppColors.text);
                    selectedColor = null;
                    if (selectedDefinicion != index) {
                        button.setBackground(getButtonColor());
                        button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                        selectedDefinicion = index;
                    } else {
                        selectedDefinicion = null;
                    }
                }
                // Caso 3: concepto seleccionado -> Se pone el nuevo (color y selected) y se crea la unión
                else {
                    button.setBackground(getButtonColor());
                    button.setForeground(CommonUtils.calculateTextColor(getButtonColor()));
                    definicionMap.put(index, selectedConcepto);
                    conceptoMap.put(selectedConcepto, index);
                    colorsUsed.add(getButtonColor());
                    selectedConcepto = null;
                    selectedDefinicion = null;
                    selectedColor = null;
                }
                // Bloqueamos si hemos terminado
                if (conceptoMap.size() == conceptosButtons.size()) {
                    for (int j = 0; j < definicionesButtons.size(); j++) {
                        if (!definicionMap.containsKey(j)) {
                            definicionesButtons.get(j).setEnabled(false);
                        }
                    }
                } else {
                    for (JButton b : definicionesButtons) {
                        b.setEnabled(true);
                    }
                }
            });
            JPanel goodPanel = new ImagePanel("/images/good_icon.png");
            goodPanel.setBounds(4, 4, 16, 16);
            goodPanel.setVisible(false);
            JPanel badPanel = new ImagePanel("/images/bad_icon.png");
            badPanel.setBounds(4, 4, 16, 16);
            badPanel.setVisible(false);

            definicionesButtons.add(button);
            definicionesGood.add(goodPanel);
            definicionesBad.add(badPanel);

            layeredPane.add(button, JLayeredPane.DEFAULT_LAYER);
            layeredPane.add(goodPanel, JLayeredPane.PALETTE_LAYER);
            layeredPane.add(badPanel, JLayeredPane.DRAG_LAYER);
            buttonsDefinicionesPanel.add(layeredPane, constraints);
        }
    }
}
