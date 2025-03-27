package com.scrumsquad.taskmaster.views.student;

import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.swing.Rounded3dButtonWithTextAndImage;
import com.scrumsquad.taskmaster.lib.*;
import com.scrumsquad.taskmaster.views.ViewRoutes;


import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainMenuView extends View {

    private JPanel mainPanel;

    @Override
    public JPanel build(BuildOptions options) {
        mainPanel = new BackgroundPanel("/images/main-background.jpg");
        mainPanel.setLayout(new BorderLayout());

        // Panel superior con logo
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        Image logoImage = ResourceLoader.loadImage("/images/logo.png");
        Image scaledLogo = logoImage.getScaledInstance(400, 280, Image.SCALE_SMOOTH); // Ajusta tamaño
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(logoLabel);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(1, 2, 40, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JButton teoriaButton = createStyledButton("TEORÍA", new Color(173, 216, 230), "/images/teoria.png");
        teoriaButton.addActionListener(e -> {
            Navigator.getNavigator().to(ViewRoutes.teoria, new HashMap<String, Object>() {{
                put("tema", 1);
            }});
        });
        JButton practicarButton = createStyledButton("PRACTICAR", new Color(255, 150, 120), "/images/ejercicios.png");
        practicarButton.addActionListener(e -> {
            Navigator.getNavigator().to(ViewRoutes.topicsConceptMatching);
        });        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));

        buttonPanel.add(teoriaButton);
        buttonPanel.add(practicarButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Eslogan
        JLabel sloganLabel = new JLabel("Si no puedes con la presión, abandona", SwingConstants.CENTER);
        sloganLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        sloganLabel.setForeground(Color.WHITE);
        sloganLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(sloganLabel, BorderLayout.SOUTH);

        return mainPanel;
    }

    @Override
    public void update(Context ctx) {

    }

    private JButton createStyledButton(String text, Color bgColor, String imagePath) {
        JButton button = new Rounded3dButtonWithTextAndImage(text, imagePath);
        button.setBackground(bgColor);
        button.setForeground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    // Clase para imagen de fondo
    static class BackgroundPanel extends JPanel {
        private final Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = ResourceLoader.loadImage(imagePath);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}
