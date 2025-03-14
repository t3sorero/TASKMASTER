package com.scrumsquad.taskmaster.controller;

import com.scrumsquad.taskmaster.controller.commands.Command;
import com.scrumsquad.taskmaster.controller.commands.CommandFactory;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.CommonUtils;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.swing.TransparentButton;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.AppColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {
    private static AppController instance;

    public static AppController getInstance() {
        if (instance == null) {
            instance = new AppController();
        }
        return instance;
    }

    private final Set<AppFrame> frames;
    private final ExecutorService executor;

    private AppController() {
        frames = new HashSet<>();
        executor = Executors.newFixedThreadPool(Math.max(Runtime.getRuntime().availableProcessors() - 1, 4));
    }

    public void action(Context ctx) {
        if (ctx == null) {
            System.err.println("Context not provided");
            return;
        }
        Command command = CommandFactory.getCommand(ctx.getCommandName());
        if (command == null) {
            System.err.println("Command \"" + ctx.getCommandName() + "\" not found");
            return;
        }
        executor.submit(() -> {
            Context responseCtx = command.execute(ctx);
            for (AppFrame frame : frames) {
                frame.notifyView(responseCtx);
            }
        });
    }

    public void openFrame(String route, String title) {
        AppFrame frame = new AppFrame(title);
        frames.add(frame);
        Navigator.addFrame(frame, route);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                frames.remove(frame);
                if (frames.isEmpty()) {
                    System.exit(0);
                }
            }
        });
    }

    public static class AppFrame extends JFrame {

        private static final Dimension minimumFrameSize = new Dimension(1024, 768);
        private static final Dimension startingFrameSize = new Dimension(1024, 768);

        private final Stack<RouteView> views;
        private final AppFramePanel framePanel;

        private AppFrame(String title) {
            super(title);
            views = new Stack<>();
            // Frame options
            setMinimumSize(minimumFrameSize);
            setSize(startingFrameSize);
            setLocationRelativeTo(null);
            //setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            framePanel = new AppFramePanel(this);
            setContentPane(framePanel);
        }

        public void pushView(View view, JPanel viewBuilt) {
            views.push(new RouteView(view, viewBuilt));
            framePanel.pushView(viewBuilt);
            view.onLoad();
        }

        public void popView() {
            if (views.isEmpty()) {
                System.err.println("Views stack is empty");
                return;
            }
            JPanel viewBuilt = views.pop().viewBuilt();
            framePanel.popView(viewBuilt);
        }

        public void clearViews() {
            if (views.isEmpty()) return;
            views.clear();
            framePanel.clearViews();
        }

        public void notifyView(Context ctx) {
            if (views.isEmpty()) return;
            views.peek().view().update(ctx);
        }

        private record RouteView(View view, JPanel viewBuilt) {
        }

        private static class AppFramePanel extends JPanel {

            private final AppFrame frame;

            private static final String backButtonBlackPath = "/images/back-icon-black.png";
            private static final String backButtonWhitePath = "/images/back-icon-white.png";
            private static final Color defaultHeaderColor = AppColors.primary;
            private static final Color defaultHeaderTextColor = CommonUtils.calculateTextColor(
                    AppColors.primary,
                    Color.white,
                    Color.black);

            private final AppHeaderPanel headerPanel;
            private final JLabel headerTitleLabel;
            private final JPanel backButtonPanel;
            private final JPanel headerComponentsLabel;
            private final GridBagConstraints headerComponentsLabelGbc;
            private final JButton backButtonBlack;
            private final JButton backButtonWhite;
            private final JPanel cardsPanel;
            private final CardLayout cardLayout;
            private final JPanel basePanel;

            public AppFramePanel(AppFrame frame) {
                this.frame = frame;
                // Main frame panel
                setLayout(new BorderLayout(8, 0));
                setBackground(Color.white);
                // Header section
                headerPanel = new AppHeaderPanel(defaultHeaderColor);
                headerPanel.setLayout(new BorderLayout(16, 0));
                headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
                headerPanel.setVisible(false);
                add(headerPanel, BorderLayout.NORTH);
                // Header Back Button (+ outer panel)
                backButtonPanel = new JPanel(new BorderLayout());
                backButtonPanel.setPreferredSize(new Dimension(56, 56));
                backButtonPanel.setOpaque(false);
                backButtonPanel.setBorder(SwingUtils.emptyBorder(4));
                backButtonBlack = TransparentButton.withImage(backButtonBlackPath, 48, 48);
                backButtonBlack.addActionListener((e) -> {
                    if (frame.views.peek().view().canGoBack()) {
                        Navigator.getNavigator().back();
                    }
                });
                backButtonWhite = TransparentButton.withImage(backButtonWhitePath, 48, 48);
                backButtonWhite.addActionListener((e) -> {
                    if (frame.views.peek().view().canGoBack()) {
                        Navigator.getNavigator().back();
                    }
                });
                headerPanel.add(backButtonPanel, BorderLayout.WEST);
                // Header title
                headerTitleLabel = new JLabel();
                headerTitleLabel.setFont(FontUtils.lato20);
                headerPanel.add(headerTitleLabel, BorderLayout.CENTER);
                // Header buttons
                headerComponentsLabel = new JPanel(new GridBagLayout());
                headerComponentsLabel.setOpaque(false);
                headerComponentsLabelGbc = new GridBagConstraints();
                headerComponentsLabelGbc.gridx = GridBagConstraints.RELATIVE;
                headerComponentsLabelGbc.gridy = 0;
                headerComponentsLabelGbc.fill = GridBagConstraints.VERTICAL;
                headerComponentsLabelGbc.insets = new Insets(0, 8, 0, 8);
                headerPanel.add(headerComponentsLabel, BorderLayout.EAST);
                // Views panel
                cardLayout = new CardLayout();
                cardsPanel = new JPanel(cardLayout);
                cardsPanel.setBackground(Color.white);
                // Base empty panel
                basePanel = new JPanel();
                basePanel.setBackground(Color.white);
                cardsPanel.add("0", basePanel);
                cardLayout.show(cardsPanel, "0");
                add(cardsPanel, BorderLayout.CENTER);
            }

            public void updateFrame() {
                CardLayout layout = (CardLayout) cardsPanel.getLayout();
                if (frame.views.isEmpty()) {
                    backButtonPanel.removeAll();
                    layout.show(cardsPanel, "0");
                    headerPanel.setVisible(false);
                } else {
                    RouteView head = frame.views.peek();
                    View.ViewOptions options = head.view().getOptions();
                    boolean hasTitle = false;
                    if (options != null) {
                        headerPanel.setBackgroundColor(options.getHeaderColor() != null ? options.getHeaderColor() : defaultHeaderColor);
                        hasTitle = options.getHeaderTitle() != null && !options.getHeaderTitle().trim().isEmpty();
                        headerTitleLabel.setText(hasTitle ? options.getHeaderTitle().trim() : "");
                        headerTitleLabel.setForeground(
                                options.getHeaderTextColor() != null ? options.getHeaderTextColor() : defaultHeaderTextColor);
                    } else {
                        headerPanel.setBackgroundColor(defaultHeaderColor);
                        headerTitleLabel.setText("");
                        headerTitleLabel.setBackground(defaultHeaderTextColor);
                    }
                    boolean backButtonVisible = frame.views.size() != 1;
                    backButtonPanel.removeAll();
                    if (backButtonVisible) {
                        JButton button = (options != null && options.getHeaderTextColor() == Color.white)
                                || (options == null && defaultHeaderTextColor == Color.white) ? backButtonWhite : backButtonBlack;
                        button.setForeground(options != null ? options.getHeaderTextColor() : defaultHeaderTextColor);
                        backButtonPanel.add(button);
                    }
                    headerComponentsLabel.removeAll();
                    Set<JComponent> components = options != null ? options.getHeaderComponents() : null;
                    final boolean hasComponents = components != null && !components.isEmpty();
                    if (hasComponents) {
                        for (JComponent component : components) {
                            headerComponentsLabel.add(component, headerComponentsLabelGbc);
                        }
                    }
                    boolean headerVisible = hasTitle || backButtonVisible || hasComponents;
                    layout.show(cardsPanel, frame.views.size() + "");
                    headerPanel.setVisible(headerVisible);
                }
                revalidate();
                repaint();
            }

            public void pushView(JPanel panel) {
                cardsPanel.add(frame.views.size() + "", panel);
                updateFrame();
                cardLayout.show(cardsPanel, frame.views.size() + "");
            }

            public void popView(JPanel panel) {
                cardsPanel.remove(panel);
                updateFrame();
                cardLayout.show(cardsPanel, frame.views.size() + "");
            }

            public void clearViews() {
                cardsPanel.removeAll();
                cardsPanel.add("0", basePanel);
                updateFrame();
                cardLayout.show(cardsPanel, "0");
            }

            private static class AppHeaderPanel extends JPanel {
                private Color backgroundColor;
                private Color darker;

                private static final int divisor = 0xc;

                public AppHeaderPanel(Color backgroundColor) {
                    this.backgroundColor = backgroundColor;
                    darker = SwingUtils.blackerColor(backgroundColor, .1f);
                }

                public void setBackgroundColor(Color backgroundColor) {
                    this.backgroundColor = backgroundColor;
                    darker = SwingUtils.blackerColor(backgroundColor, .1f);
                    repaint();
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Color color1 = backgroundColor;
                    Color color2 = darker;
                    int w = getWidth(), h = getHeight();
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    LinearGradientPaint gp = new LinearGradientPaint(0, 0, 0, h,
                            new float[] {0f, .0675f, .9325f, 1f}, new Color[] {color2, color1, color1, color2});
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                    g2d.dispose();
                }
            }
        }
    }


}
