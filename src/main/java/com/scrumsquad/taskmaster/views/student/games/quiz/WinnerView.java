package com.scrumsquad.taskmaster.views.student.games.quiz;

import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.View;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.*;
import java.net.URL;
import java.awt.BorderLayout;

public class WinnerView extends View {
    @Override
    public JPanel build(BuildOptions options) {
        JFXPanel fxPanel = new JFXPanel();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            double w = 800, h = 600;
            Pane root = new Pane();
            Rectangle leftCurtain = new Rectangle(0, 0, w/2, h);
            Rectangle rightCurtain = new Rectangle(w/2, 0, w/2, h);
            root.getChildren().addAll(leftCurtain, rightCurtain);

            TranslateTransition leftAnim = new TranslateTransition(Duration.seconds(1), leftCurtain);
            leftAnim.setByX(-w/2);
            TranslateTransition rightAnim = new TranslateTransition(Duration.seconds(1), rightCurtain);
            rightAnim.setByX(w/2);

            ParallelTransition openCurtains = new ParallelTransition(leftAnim, rightAnim);
            openCurtains.setOnFinished(e -> {
                Text msg = new Text("Enhorabuena, eres ya un SCRUM MASTER");
                msg.setFont(new Font(36));
                msg.setX((w - msg.getLayoutBounds().getWidth()) / 2);
                msg.setY(h / 2);
                root.getChildren().add(msg);
            });
            openCurtains.play();

            URL musicUrl = getClass().getResource("/audio/WinnerSound.mp3");
            MediaPlayer player = new MediaPlayer(new Media(musicUrl.toExternalForm()));
            player.play();

            fxPanel.setScene(new Scene(root, w, h));
        });

        return panel;
    }

    @Override
    public void update(Context ctx) { }
}
