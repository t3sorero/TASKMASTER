package com.scrumsquad.taskmaster.views.student.teoria;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.AppColors;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.commands.Context;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

public class TeoriaPorTemaView extends View {
    private static final int width = 960;
    private static final int height = 592;
    private int temaId;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextPane textPane;

    @Override
    public JPanel build(BuildOptions options) {
        temaId = (int) options.arguments().getOrDefault("tema", 1);
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(AppColors.secondary40);
        GridBagConstraints constraints = SwingUtils.verticalConstraints();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        JPanel loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setOpaque(false);
        JLabel loadingLabel = new JLabel(ResourceLoader.loadImageIcon("/images/loading_80x80.gif"));
        loadingPanel.add(loadingLabel, constraints);
        cardPanel.add("loading", loadingPanel);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        HTMLEditorKit kit = new HTMLEditorKit();
        textPane.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: sans-serif; margin: 4px; }");
        JScrollPane scrollPane = new JScrollPane(textPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        cardPanel.add("loaded", contentPanel);

        mainPanel.add(cardPanel, constraints);
        return mainPanel;
    }

    @Override
    public void onLoad() {
        cardLayout.show(cardPanel, "loading");
        Context ctx = new Context("teoriaGetData");
        ctx.setArgument("tema", temaId);
        AppController.getInstance().action(ctx);
    }

    @Override
    public void update(Context ctx) {
        if (ctx == null) return;
        if (CommandName.teoriaGetDataOk.equals(ctx.getCommandName())) {
            String markdown = (String) ctx.getArguments().get("markdown");
            String html = convertMarkdownToHTML(markdown);
            textPane.setText(html);
            cardLayout.show(cardPanel, "loaded");
        } else if (CommandName.teoriaGetDataKo.equals(ctx.getCommandName())) {
            textPane.setText("<html><body>Error al cargar la teoría.</body></html>");
            cardLayout.show(cardPanel, "loaded");
        }
    }

    private String convertMarkdownToHTML(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
