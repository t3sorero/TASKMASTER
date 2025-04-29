package com.scrumsquad.taskmaster.views.student.games.quiz;

import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.View;
import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Random;

public class WinnerView extends View {
    private MediaPlayer introPlayer;
    private MediaPlayer winnerPlayer;
    private Timeline confettiTimeline;
    private Timeline pulseTimeline;
    private double sceneWidth;
    private double sceneHeight;

    @Override
    public JPanel build(BuildOptions options) {
        JFXPanel fxPanel = new JFXPanel();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            // We'll use a responsive design approach to handle resizing
            sceneWidth = 800;
            sceneHeight = 600;

            // Create a main container that will act as a responsive wrapper
            StackPane mainContainer = new StackPane();
            Pane root = new Pane();
            mainContainer.getChildren().add(root);

            // Set minimum size to preserve layout
            mainContainer.setMinSize(sceneWidth, sceneHeight);

            // Initial background
            Rectangle background = new Rectangle(0, 0, sceneWidth, sceneHeight);
            background.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(25, 118, 210)), // Educational blue
                    new Stop(1, Color.rgb(21, 101, 192))));

            // Make the background responsive
            background.widthProperty().bind(mainContainer.widthProperty());
            background.heightProperty().bind(mainContainer.heightProperty());
            root.getChildren().add(background);

            // First, play intro music during the curtain animation
            URL introMusicUrl = getClass().getResource("/audio/IntroSound.mp3");
            if (introMusicUrl != null) {
                introPlayer = new MediaPlayer(new Media(introMusicUrl.toExternalForm()));
                introPlayer.play();
            } else {
                System.err.println("Error: Could not find the audio file IntroSound.mp3");
            }

            // Create responsive curtains
            createResponsiveCurtains(root, mainContainer);

            // Create the scene with the responsive container
            Scene scene = new Scene(mainContainer, sceneWidth, sceneHeight);

            // Make the scene respond to size changes
            scene.widthProperty().addListener((obs, oldVal, newVal) -> {
                sceneWidth = newVal.doubleValue();
                adjustLayout(root);
            });

            scene.heightProperty().addListener((obs, oldVal, newVal) -> {
                sceneHeight = newVal.doubleValue();
                adjustLayout(root);
            });

            fxPanel.setScene(scene);
        });

        return panel;
    }

    private void adjustLayout(Pane root) {
        // This method would contain any specific adjustments needed for resizing
        // Most of our components will use relative positioning through property bindings
    }

    private void createResponsiveCurtains(Pane root, StackPane container) {
        // Create responsive curtains that scale with the window
        int numFolds = 8;

        // Colors for curtains: bright educational red
        Color curtainColor = Color.rgb(220, 53, 69); // Vibrant red
        Color curtainShadow = Color.rgb(187, 45, 59); // Shadow for folds

        // Containers for curtains
        Pane leftCurtainPane = new Pane();
        Pane rightCurtainPane = new Pane();

        // Make sure curtains resize with the container
        leftCurtainPane.prefWidthProperty().bind(container.widthProperty().divide(2));
        leftCurtainPane.prefHeightProperty().bind(container.heightProperty());
        rightCurtainPane.prefWidthProperty().bind(container.widthProperty().divide(2));
        rightCurtainPane.prefHeightProperty().bind(container.heightProperty());

        // Create the curtain folds that resize with the parent pane
        for (int i = 0; i < numFolds; i++) {
            final int foldIndex = i;  // Need final for lambda

            // Left fold
            Rectangle leftFold = new Rectangle();
            leftFold.widthProperty().bind(leftCurtainPane.prefWidthProperty().divide(numFolds));
            leftFold.heightProperty().bind(leftCurtainPane.prefHeightProperty());
            leftFold.xProperty().bind(leftFold.widthProperty().multiply(foldIndex));
            leftFold.setFill(i % 2 == 0 ? curtainColor : curtainShadow);
            leftCurtainPane.getChildren().add(leftFold);

            // Right fold
            Rectangle rightFold = new Rectangle();
            rightFold.widthProperty().bind(rightCurtainPane.prefWidthProperty().divide(numFolds));
            rightFold.heightProperty().bind(rightCurtainPane.prefHeightProperty());
            rightFold.xProperty().bind(rightFold.widthProperty().multiply(foldIndex));
            rightFold.setFill(i % 2 == 0 ? curtainColor : curtainShadow);
            rightCurtainPane.getChildren().add(rightFold);
        }

        // Position curtains
        leftCurtainPane.setLayoutX(0);
        rightCurtainPane.layoutXProperty().bind(container.widthProperty().divide(2));

        // Add curtains to root
        root.getChildren().addAll(leftCurtainPane, rightCurtainPane);

        // Animation to open curtains
        TranslateTransition leftAnim = new TranslateTransition(Duration.seconds(2.5), leftCurtainPane);
        leftAnim.toXProperty().bind(container.widthProperty().divide(2).negate());

        TranslateTransition rightAnim = new TranslateTransition(Duration.seconds(2.5), rightCurtainPane);
        rightAnim.toXProperty().bind(container.widthProperty().divide(2));

        // Create a parallel animation for the curtains
        ParallelTransition openCurtains = new ParallelTransition(leftAnim, rightAnim);
        openCurtains.setOnFinished(e -> {
            // IMPORTANT: Stop intro music and start winner music when curtains are open
            if (introPlayer != null) {
                introPlayer.stop();
                introPlayer.dispose();
            }

            // Now play the winner music
            URL winnerMusicUrl = getClass().getResource("/audio/WinnerSound.mp3");
            if (winnerMusicUrl != null) {
                winnerPlayer = new MediaPlayer(new Media(winnerMusicUrl.toExternalForm()));
                winnerPlayer.play();
            } else {
                System.err.println("Error: Could not find the audio file WinnerSound.mp3");
            }

            // IMPORTANT: create celebration panel AFTER curtains open
            createCelebrationPanel(root, container);
            startConfettiAnimation(root, container);
        });

        // Delay curtain opening for a more theatrical effect
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> openCurtains.play()));
        delay.play();
    }

    private void createCelebrationPanel(Pane root, StackPane container) {
        // Create responsive celebration panel
        Rectangle stage = new Rectangle();
        stage.xProperty().bind(container.widthProperty().multiply(0.0625)); // 50/800 = 0.0625
        stage.yProperty().bind(container.heightProperty().multiply(0.0833)); // 50/600 = 0.0833
        stage.widthProperty().bind(container.widthProperty().multiply(0.875)); // (800-100)/800 = 0.875
        stage.heightProperty().bind(container.heightProperty().multiply(0.8333)); // (600-100)/600 = 0.8333
        stage.setFill(Color.rgb(245, 245, 245));
        stage.setArcWidth(20);
        stage.setArcHeight(20);
        stage.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));
        stage.setOpacity(0); // Start with opacity 0 for animation

        // Enhanced gold borders for top and bottom
        Rectangle topBorder = new Rectangle();
        topBorder.xProperty().bind(stage.xProperty());
        topBorder.yProperty().bind(stage.yProperty());
        topBorder.widthProperty().bind(stage.widthProperty());
        topBorder.heightProperty().bind(container.heightProperty().multiply(0.025)); // 15/600 = 0.025
        topBorder.setFill(Color.rgb(255, 215, 0)); // Gold
        topBorder.setOpacity(0);

        Rectangle bottomBorder = new Rectangle();
        bottomBorder.xProperty().bind(stage.xProperty());
        bottomBorder.yProperty().bind(container.heightProperty().subtract(topBorder.heightProperty().add(stage.yProperty())));
        bottomBorder.widthProperty().bind(stage.widthProperty());
        bottomBorder.heightProperty().bind(topBorder.heightProperty());
        bottomBorder.setFill(Color.rgb(255, 215, 0)); // Gold
        bottomBorder.setOpacity(0);

        // Add panel to root
        root.getChildren().addAll(stage, topBorder, bottomBorder);

        // Animate panel appearance
        Timeline fadeInPanel = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(stage.opacityProperty(), 0),
                        new KeyValue(topBorder.opacityProperty(), 0),
                        new KeyValue(bottomBorder.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.8),
                        new KeyValue(stage.opacityProperty(), 1),
                        new KeyValue(topBorder.opacityProperty(), 1),
                        new KeyValue(bottomBorder.opacityProperty(), 1))
        );

        fadeInPanel.setOnFinished(e -> {
            // After panel appears, show congratulations message and trophy
            showCongratulationsMessage(root, container);
        });

        fadeInPanel.play();
    }

    private void showCongratulationsMessage(Pane root, StackPane container) {
        // Create improved trophy
        Pane trophyPane = createImprovedTrophy();
        trophyPane.setScaleX(1.3);
        trophyPane.setScaleY(1.3);

        // Position trophy responsively
        trophyPane.layoutXProperty().bind(container.widthProperty().divide(2).subtract(60)); // Centered, adjusted for scale
        trophyPane.layoutYProperty().bind(container.heightProperty().multiply(0.5).subtract(240)); // Upper part of screen
        trophyPane.setOpacity(0); // Initially invisible for animation

        // Add glow effect
        Glow glow = new Glow();
        glow.setLevel(0.6);

        // Add drop shadow for depth
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GOLD);
        dropShadow.setRadius(30);
        dropShadow.setSpread(0.5);

        // Combine effects
        glow.setInput(dropShadow);
        trophyPane.setEffect(glow);

        root.getChildren().add(trophyPane);

        // Main message with educational style
        Text msg = new Text("¡Enhorabuena!");
        msg.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        msg.setFill(Color.rgb(33, 150, 243)); // Bright blue

        // Position text responsively
        msg.xProperty().bind(container.widthProperty().divide(2).subtract(computeTextWidth(msg.getText(), msg.getFont())/2));
        msg.yProperty().bind(container.heightProperty().multiply(0.5).add(30));

        // Add shadow effect for text
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        msg.setEffect(shadow);

        // Secondary message
        Text subtitle = new Text("Eres ya un SCRUM MASTER");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        subtitle.setFill(Color.rgb(76, 175, 80)); // Educational green

        // Position subtitle responsively
        subtitle.xProperty().bind(container.widthProperty().divide(2).subtract(computeTextWidth(subtitle.getText(), subtitle.getFont())/2));
        subtitle.yProperty().bind(container.heightProperty().multiply(0.5).add(80));
        subtitle.setEffect(shadow);

        // Set initial opacity for animation
        msg.setOpacity(0);
        subtitle.setOpacity(0);

        // 1. Trophy entrance with fade and bounce effect
        FadeTransition fadeInTrophy = new FadeTransition(Duration.seconds(1), trophyPane);
        fadeInTrophy.setFromValue(0);
        fadeInTrophy.setToValue(1);

        // Add a gentle bounce to the trophy
        TranslateTransition trophyBounce = new TranslateTransition(Duration.seconds(1), trophyPane);
        trophyBounce.setFromY(-50);
        trophyBounce.setToY(0);

        // 2. Replace full 3D rotation with gentle rotation
        RotateTransition rotateY = new RotateTransition(Duration.seconds(4), trophyPane);
        rotateY.setAxis(Rotate.Y_AXIS);
        // Limited rotation of only 15 degrees in each direction
        rotateY.setFromAngle(-15);
        rotateY.setToAngle(15);
        rotateY.setCycleCount(Timeline.INDEFINITE);
        rotateY.setAutoReverse(true);

        // 3. Pulse effect to enhance animation
        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.6)),
                new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.3)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glow.levelProperty(), 0.6))
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);

        // Play all trophy animations
        ParallelTransition trophyEntrance = new ParallelTransition(fadeInTrophy, trophyBounce);
        trophyEntrance.play();

        trophyEntrance.setOnFinished(e -> {
            rotateY.play();
            pulseTimeline.play();
        });

        // Text animation
        Timeline fadeInText = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(msg.opacityProperty(), 0),
                        new KeyValue(subtitle.opacityProperty(), 0)
                ),
                new KeyFrame(Duration.seconds(1.5),
                        new KeyValue(msg.opacityProperty(), 1),
                        new KeyValue(subtitle.opacityProperty(), 1)
                )
        );

        root.getChildren().addAll(msg, subtitle);
        fadeInText.play();
    }

    // Helper method to calculate actual text width
    private double computeTextWidth(String text, Font font) {
        Text helper = new Text(text);
        helper.setFont(font);
        return helper.getLayoutBounds().getWidth();
    }

    // Method to create improved trophy with better rendering
    private Pane createImprovedTrophy() {
        Pane trophyPane = new Pane();
        trophyPane.setPrefSize(100, 200);

        // Trophy colors
        Color goldColor = Color.rgb(255, 215, 0);
        Color goldShadow = Color.rgb(218, 165, 32);
        Color goldHighlight = Color.rgb(255, 235, 100);
        Color blackBase = Color.rgb(20, 20, 20);

        // Black trophy base (more detailed)
        Rectangle baseBottom = new Rectangle(5, 170, 90, 15);
        baseBottom.setFill(blackBase);
        baseBottom.setArcWidth(5);
        baseBottom.setArcHeight(5);

        Rectangle baseMiddle = new Rectangle(15, 155, 70, 15);
        baseMiddle.setFill(blackBase);
        baseMiddle.setArcWidth(5);
        baseMiddle.setArcHeight(5);

        Rectangle baseTop = new Rectangle(25, 140, 50, 15);
        baseTop.setFill(blackBase);
        baseTop.setArcWidth(5);
        baseTop.setArcHeight(5);

        // Golden stem with gradient for 3D effect
        Rectangle stem = new Rectangle(45, 90, 10, 50);
        stem.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(0.5, goldColor),
                new Stop(1, goldShadow)));

        // Trophy cup with better detail
        // Bottom part of cup
        Ellipse cupBottom = new Ellipse(50, 90, 25, 8);
        cupBottom.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(0.5, goldColor),
                new Stop(1, goldHighlight)));

        // Cup sides and center (more defined)
        Rectangle cupLeft = new Rectangle(25, 35, 5, 55);
        cupLeft.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(1, goldColor)));

        Rectangle cupRight = new Rectangle(70, 35, 5, 55);
        cupRight.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldColor),
                new Stop(1, goldShadow)));

        Rectangle cupMiddle = new Rectangle(30, 35, 40, 55);
        cupMiddle.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(0.5, goldColor),
                new Stop(1, goldShadow)));

        // Rounded top of cup
        Ellipse cupTop = new Ellipse(50, 35, 25, 8);
        cupTop.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldHighlight),
                new Stop(0.5, goldColor),
                new Stop(1, goldShadow)));

        // Trophy handles (more defined with 3D effect)
        // Left handle
        Ellipse handleLeftOuter = new Ellipse(15, 60, 10, 25);
        handleLeftOuter.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(1, goldColor)));

        Ellipse handleLeftInner = new Ellipse(15, 60, 5, 20);
        handleLeftInner.setFill(Color.rgb(245, 245, 245)); // Background to simulate hole

        // Right handle
        Ellipse handleRightOuter = new Ellipse(85, 60, 10, 25);
        handleRightOuter.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldColor),
                new Stop(1, goldShadow)));

        Ellipse handleRightInner = new Ellipse(85, 60, 5, 20);
        handleRightInner.setFill(Color.rgb(245, 245, 245)); // Background to simulate hole

        // Create StackPane for red circle and number
        StackPane redCirclePane = new StackPane();
        redCirclePane.setLayoutX(50 - 18);
        redCirclePane.setLayoutY(55 - 18);
        redCirclePane.setPrefSize(36, 36);

        // Red circle adjusted for StackPane (using relative coordinates)
        Circle redCircleBg = new Circle(18, 18, 18);
        redCircleBg.setFill(Color.rgb(240, 30, 30)); // Darker red base

        Circle redCircle = new Circle(18, 18, 16);
        redCircle.setFill(new RadialGradient(0, 0, 0.3, 0.3, 0.7, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 100, 100)),
                new Stop(0.8, Color.rgb(220, 20, 20)),
                new Stop(1, Color.rgb(180, 0, 0))));

        Circle redCircleBorder = new Circle(18, 18, 18);
        redCircleBorder.setFill(Color.TRANSPARENT);
        redCircleBorder.setStroke(goldColor);
        redCircleBorder.setStrokeWidth(2);

        // Number "1" text
        Text numberOne = new Text("1");
        numberOne.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        numberOne.setFill(Color.WHITE);
        StackPane.setAlignment(numberOne, Pos.CENTER);  // Center the text

        // Add nodes to container. With StackPane, number will be centered.
        redCirclePane.getChildren().addAll(redCircleBg, redCircle, redCircleBorder, numberOne);

        // Apply subtle lighting effects
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(3.0); // Subtle effect

        cupMiddle.setEffect(lighting);

        // Add all components to main Pane in order for correct overlapping
        trophyPane.getChildren().addAll(
                baseBottom, baseMiddle, baseTop,
                stem, cupBottom,
                handleLeftOuter, handleLeftInner,
                handleRightOuter, handleRightInner,
                cupLeft, cupRight, cupMiddle, cupTop,
                redCirclePane  // Use the StackPane containing circle and number
        );

        return trophyPane;
    }

    private void startConfettiAnimation(Pane root, StackPane container) {
        Random random = new Random();
        confettiTimeline = new Timeline(
                new KeyFrame(Duration.millis(50), event -> {
                    for (int i = 0; i < 5; i++) {
                        // Create confetti of different shapes and educational colors
                        double size = 5 + random.nextDouble() * 10;
                        javafx.scene.Node confetti;

                        // Alternate between circles and rectangles
                        if (random.nextBoolean()) {
                            confetti = new Circle(size);
                            ((Circle) confetti).setFill(getRandomEducationalColor());
                        } else {
                            confetti = new Rectangle(size, size);
                            ((Rectangle) confetti).setFill(getRandomEducationalColor());
                            ((Rectangle) confetti).setRotate(random.nextDouble() * 360);
                        }

                        // Initial position - spread across the whole width
                        confetti.setLayoutX(random.nextDouble() * container.getWidth());
                        confetti.setLayoutY(-10);

                        root.getChildren().add(confetti);

                        // More realistic animation with rotation
                        TranslateTransition fall = new TranslateTransition(
                                Duration.seconds(2 + random.nextDouble() * 3), confetti);
                        fall.byYProperty().bind(container.heightProperty().add(20));

                        // Oscillating X movement to simulate real falling
                        double amplitude = (random.nextDouble() - 0.5) * 200;
                        Timeline oscillate = new Timeline(
                                new KeyFrame(Duration.ZERO,
                                        new KeyValue(confetti.translateXProperty(), 0)),
                                new KeyFrame(Duration.seconds(0.5),
                                        new KeyValue(confetti.translateXProperty(), amplitude/2)),
                                new KeyFrame(Duration.seconds(1.0),
                                        new KeyValue(confetti.translateXProperty(), 0)),
                                new KeyFrame(Duration.seconds(1.5),
                                        new KeyValue(confetti.translateXProperty(), -amplitude/2)),
                                new KeyFrame(Duration.seconds(2.0),
                                        new KeyValue(confetti.translateXProperty(), 0))
                        );
                        oscillate.setCycleCount(Timeline.INDEFINITE);

                        // Rotation
                        RotateTransition rotate = new RotateTransition(
                                Duration.seconds(random.nextDouble() * 2 + 1), confetti);
                        rotate.setByAngle(360);
                        rotate.setCycleCount(Timeline.INDEFINITE);

                        // Start animations
                        fall.play();
                        oscillate.play();
                        rotate.play();

                        // Remove confetti when done falling
                        fall.setOnFinished(e -> {
                            oscillate.stop();
                            rotate.stop();
                            root.getChildren().remove(confetti);
                        });
                    }
                })
        );
        confettiTimeline.setCycleCount(100);
        confettiTimeline.play();
    }

    private Color getRandomEducationalColor() {
        Random random = new Random();
        // Vibrant educational colors
        Color[] colors = {
                Color.rgb(33, 150, 243),  // Primary blue
                Color.rgb(76, 175, 80),   // Success green
                Color.rgb(255, 193, 7),   // Warning yellow
                Color.rgb(156, 39, 176),  // Creative purple
                Color.rgb(244, 67, 54),   // Energy red
                Color.rgb(0, 188, 212),   // Info cyan
                Color.rgb(255, 152, 0),   // Motivation orange
                Color.rgb(233, 30, 99)    // Fun pink
        };
        return colors[random.nextInt(colors.length)];
    }

    @Override
    public void update(Context ctx) { }

    @Override
    public void onDispose() {
        // Stop music playback and animations when leaving the view
        Platform.runLater(() -> {
            if (introPlayer != null) {
                introPlayer.stop();
                introPlayer.dispose();
            }
            if (winnerPlayer != null) {
                winnerPlayer.stop();
                winnerPlayer.dispose();
            }
            if (confettiTimeline != null) {
                confettiTimeline.stop();
            }
            if (pulseTimeline != null) {
                pulseTimeline.stop();
            }
        });
    }
}