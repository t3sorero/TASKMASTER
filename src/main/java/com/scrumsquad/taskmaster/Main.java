package com.scrumsquad.taskmaster;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.lib.CommonUtils;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {

    private static final String firstRoute = ViewRoutes.conceptMatching;

    private static final String[] imagesToPreload = {
            "/images/back-icon-white.png",
            "/images/login-background.jpg",
            "/images/student-background.jpg",
            "/images/back-icon-black.png",
    };

    public static void main(String[] args) {
        int javaVersion = CommonUtils.getJavaMainVersion();
        if (javaVersion < 8) {
            JOptionPane.showMessageDialog(null, "Es necesario una versión de Java 8 o superior");
        } else if (javaVersion == 8) {
            System.setProperty("sun.java2d.dpiaware", "true");
            System.setProperty("sun.java2d.uiScale", "2");
        } else {
            System.setProperty("sun.java2d.uiScale", "1");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            //new ResourcePreLoaderFrame(() -> {
                AppController.getInstance().openFrame(firstRoute, "TaskMaster");
            //});
        });
    }

    private interface ResourcePreLoaderOnComplete {
        void onComplete();
    }

    private static class ResourcePreLoaderFrame extends JDialog {

        public ResourcePreLoaderFrame(ResourcePreLoaderOnComplete onComplete) {
            // TODO gui
            try {
                final JPanel panel = new JPanel(new BorderLayout());
                panel.setBackground(Color.black);
                panel.setBorder(SwingUtils.emptyBorder(4));
                final JPanel centerPanel = new ImagePanel("/images/taskmaster_intro.png");
                centerPanel.setPreferredSize(new Dimension(298, 180));
                panel.add(centerPanel, BorderLayout.CENTER);
                final JLabel loading = new JLabel(ResourceLoader.loadImageIcon("/images/loading_intro.gif"));
                panel.add(loading, BorderLayout.SOUTH);
                setContentPane(panel);
                setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                Dimension windowSize = new Dimension(298, 220);
                setMinimumSize(windowSize);
                setSize(windowSize);
                setResizable(false);
                setUndecorated(true);
                setLocationRelativeTo(null);
                setVisible(true);
                java.util.List<CompletableFuture<Void>> tasks = new ArrayList<>(imagesToPreload.length);
                for (String path : imagesToPreload) {
                    tasks.add(CompletableFuture.runAsync(() -> {
                        ResourceLoader.loadImage(path);
                    }));
                }
                // Convert the list to an array and wait for all tasks to complete
                CompletableFuture<Void> allTasks = CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]));

                // Action to be executed when all tasks are completed
                allTasks.thenAccept(v -> {
                    this.dispose();
                    onComplete.onComplete();
                });
            } catch (Exception e) {
                this.dispose();
                JOptionPane.showMessageDialog(null,
                        "Ocurrió un error cargando la aplicación. Si el error persiste, reinstale la aplicación.");
                System.exit(1);
            }
        }
    }
}
