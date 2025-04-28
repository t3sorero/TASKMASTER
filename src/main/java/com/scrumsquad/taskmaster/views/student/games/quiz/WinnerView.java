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
import javafx.animation.RotateTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.KeyValue;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Bloom;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import javax.swing.*;
import java.net.URL;
import java.awt.BorderLayout;
import java.util.Random;

public class WinnerView extends View {
    private MediaPlayer player;
    private Timeline confettiTimeline;

    @Override
    public JPanel build(BuildOptions options) {
        JFXPanel fxPanel = new JFXPanel();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
            double w = 800, h = 600;
            Pane root = new Pane();

            // Fondo con degradado azul educativo
            Rectangle background = new Rectangle(0, 0, w, h);
            background.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(25, 118, 210)), // Azul educativo
                    new Stop(1, Color.rgb(21, 101, 192))));
            root.getChildren().add(background);

            // Creamos las cortinas con textura y pliegues
            createCurtains(root, w, h);

            // Reproducir música de ganador
            URL musicUrl = getClass().getResource("/audio/WinnerSound.mp3");
            if (musicUrl != null) {
                player = new MediaPlayer(new Media(musicUrl.toExternalForm()));
                player.play();
            } else {
                System.err.println("Error: No se pudo encontrar el archivo de audio WinnerSound.mp3");
            }

            fxPanel.setScene(new Scene(root, w, h));
        });

        return panel;
    }

    private void createCurtains(Pane root, double w, double h) {
        // Creamos múltiples pliegues para cada cortina para dar efecto más realista
        int numFolds = 8;
        double foldWidth = w / (numFolds * 2);

        // Colores para las cortinas: rojo brillante didáctico
        Color curtainColor = Color.rgb(220, 53, 69); // Rojo vibrante
        Color curtainShadow = Color.rgb(187, 45, 59); // Sombra para los pliegues

        // Contenedores para las cortinas
        Pane leftCurtainPane = new Pane();
        Pane rightCurtainPane = new Pane();

        // Creamos los pliegues de las cortinas
        for (int i = 0; i < numFolds; i++) {
            // Pliegue izquierdo
            Rectangle leftFold = new Rectangle(i * foldWidth, 0, foldWidth, h);
            leftFold.setFill(i % 2 == 0 ? curtainColor : curtainShadow);
            leftCurtainPane.getChildren().add(leftFold);

            // Pliegue derecho
            Rectangle rightFold = new Rectangle(i * foldWidth, 0, foldWidth, h);
            rightFold.setFill(i % 2 == 0 ? curtainColor : curtainShadow);
            rightCurtainPane.getChildren().add(rightFold);
        }

        // Posicionamos las cortinas
        leftCurtainPane.setLayoutX(0);
        rightCurtainPane.setLayoutX(w / 2);

        // Añadimos las cortinas al root
        root.getChildren().addAll(leftCurtainPane, rightCurtainPane);

        // Efecto de borde dorado para las cortinas (típico de teatro)
        Rectangle leftBorder = new Rectangle(w/2 - 10, 0, 10, h);
        leftBorder.setFill(Color.rgb(255, 215, 0)); // Dorado
        Rectangle rightBorder = new Rectangle(w/2, 0, 10, h);
        rightBorder.setFill(Color.rgb(255, 215, 0)); // Dorado
        root.getChildren().addAll(leftBorder, rightBorder);

        // Animación para abrir las cortinas
        TranslateTransition leftAnim = new TranslateTransition(Duration.seconds(2.5), leftCurtainPane);
        leftAnim.setByX(-w/2);

        TranslateTransition rightAnim = new TranslateTransition(Duration.seconds(2.5), rightCurtainPane);
        rightAnim.setByX(w/2);

        // Crear una animación paralela para las cortinas
        ParallelTransition openCurtains = new ParallelTransition(leftAnim, rightAnim);
        openCurtains.setOnFinished(e -> {
            // IMPORTANTE: Aquí creamos el panel de celebración DESPUÉS de que se abran las cortinas
            createCelebrationPanel(root, w, h);
            startConfettiAnimation(root, w, h);
        });

        // Retrasamos un poco la apertura de las cortinas para dar un efecto más teatral
        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1), e -> openCurtains.play()));
        delay.play();
    }

    private void createCelebrationPanel(Pane root, double w, double h) {
        // Creamos el panel de celebración (solo después de que las cortinas se abren)
        Rectangle stage = new Rectangle(50, 50, w - 100, h - 100);
        stage.setFill(Color.rgb(245, 245, 245));
        stage.setArcWidth(20);
        stage.setArcHeight(20);
        stage.setEffect(new DropShadow(20, Color.rgb(0, 0, 0, 0.5)));
        stage.setOpacity(0); // Empezamos con opacidad 0 para animarlo

        // Añadimos el panel al root
        root.getChildren().add(stage);

        // Animamos la aparición del panel
        Timeline fadeInPanel = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(stage.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.8), new KeyValue(stage.opacityProperty(), 1))
        );

        fadeInPanel.setOnFinished(e -> {
            // Después de que aparezca el panel, mostramos el mensaje y el trofeo
            showCongratulationsMessage(root, w, h);
        });

        fadeInPanel.play();
    }

    private void showCongratulationsMessage(Pane root, double w, double h) {
        // Intentamos cargar un trofeo SVG o PNG
        try {
            // Creamos un trofeo dibujado con JavaFX (ya que puede no existir el archivo)
            Pane trophyPane = createTrophyGraphic();
            trophyPane.setLayoutX((w - 100) / 2);  // Centrado, asumiendo ancho de 100
            trophyPane.setLayoutY(h/2 - 200);      // Colocado en la parte superior

            // Añadimos un efecto de brillo
            Bloom bloom = new Bloom();
            bloom.setThreshold(0.3);
            trophyPane.setEffect(bloom);

            // Rotación suave para el trofeo
            RotateTransition rt = new RotateTransition(Duration.seconds(4), trophyPane);
            rt.setByAngle(10);
            rt.setAxis(Rotate.Z_AXIS);
            rt.setAutoReverse(true);
            rt.setCycleCount(Timeline.INDEFINITE);
            rt.play();

            root.getChildren().add(trophyPane);
        } catch (Exception e) {
            System.err.println("Error creando el trofeo: " + e.getMessage());
        }

        // Mensaje principal con estilo didáctico y motivador
        Text msg = new Text("¡Enhorabuena!");
        msg.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        msg.setFill(Color.rgb(33, 150, 243)); // Azul brillante
        msg.setX((w - msg.getLayoutBounds().getWidth()) / 2);
        msg.setY(h/2);

        // Efecto de sombra para el texto
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        msg.setEffect(shadow);

        // Mensaje secundario
        Text subtitle = new Text("Eres ya un SCRUM MASTER");
        subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        subtitle.setFill(Color.rgb(76, 175, 80)); // Verde didáctico
        subtitle.setX((w - subtitle.getLayoutBounds().getWidth()) / 2);
        subtitle.setY(h/2 + 50);
        subtitle.setEffect(shadow);

        // Animación para el mensaje
        msg.setOpacity(0);
        subtitle.setOpacity(0);

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

    // Método para crear un trofeo gráficamente con JavaFX
    private Pane createTrophyGraphic() {
        Pane trophyPane = new Pane();
        trophyPane.setPrefSize(100, 150);

        // Copa del trofeo
        Rectangle cup = new Rectangle(25, 0, 50, 60);
        cup.setArcWidth(25);
        cup.setArcHeight(25);
        cup.setFill(Color.rgb(255, 215, 0)); // Dorado

        // Base del trofeo
        Rectangle base = new Rectangle(15, 100, 70, 20);
        base.setFill(Color.rgb(205, 127, 50)); // Bronce

        // Pie del trofeo
        Rectangle stem = new Rectangle(45, 60, 10, 40);
        stem.setFill(Color.rgb(255, 215, 0)); // Dorado

        // Base inferior
        Rectangle bottomBase = new Rectangle(25, 120, 50, 10);
        bottomBase.setFill(Color.rgb(205, 127, 50)); // Bronce

        // Añadimos todos los elementos al panel
        trophyPane.getChildren().addAll(cup, stem, base, bottomBase);

        // Efecto de brillo para el trofeo
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(255, 255, 0, 0.8));
        glow.setWidth(20);
        glow.setHeight(20);
        trophyPane.setEffect(glow);

        return trophyPane;
    }

    private void startConfettiAnimation(Pane root, double width, double height) {
        Random random = new Random();
        confettiTimeline = new Timeline(
                new KeyFrame(Duration.millis(50), event -> {
                    for (int i = 0; i < 5; i++) {
                        // Crear confeti de diferentes formas y colores educativos
                        double size = 5 + random.nextDouble() * 10;
                        javafx.scene.Node confetti;

                        // Alternamos entre círculos y rectángulos
                        if (random.nextBoolean()) {
                            confetti = new Circle(size);
                            ((Circle) confetti).setFill(getRandomEducationalColor());
                        } else {
                            confetti = new Rectangle(size, size);
                            ((Rectangle) confetti).setFill(getRandomEducationalColor());
                            ((Rectangle) confetti).setRotate(random.nextDouble() * 360);
                        }

                        // Posición inicial
                        confetti.setLayoutX(random.nextDouble() * width);
                        confetti.setLayoutY(-10);

                        root.getChildren().add(confetti);

                        // Animación más realista con rotación
                        TranslateTransition fall = new TranslateTransition(
                                Duration.seconds(2 + random.nextDouble() * 3), confetti);
                        fall.setByY(height + 20);

                        // Movimiento oscilante en X para simular caída real
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

                        // Rotación
                        RotateTransition rotate = new RotateTransition(
                                Duration.seconds(random.nextDouble() * 2 + 1), confetti);
                        rotate.setByAngle(360);
                        rotate.setCycleCount(Timeline.INDEFINITE);

                        // Iniciamos las animaciones
                        fall.play();
                        oscillate.play();
                        rotate.play();

                        // Removemos el confeti cuando termina de caer
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
        // Colores educativos vibrantes
        Color[] colors = {
                Color.rgb(33, 150, 243),  // Azul primario
                Color.rgb(76, 175, 80),   // Verde éxito
                Color.rgb(255, 193, 7),   // Amarillo atención
                Color.rgb(156, 39, 176),  // Púrpura creatividad
                Color.rgb(244, 67, 54),   // Rojo energía
                Color.rgb(0, 188, 212),   // Cian información
                Color.rgb(255, 152, 0),   // Naranja motivación
                Color.rgb(233, 30, 99)    // Rosa diversión
        };
        return colors[random.nextInt(colors.length)];
    }

    @Override
    public void update(Context ctx) { }

    @Override
    public void onDispose() {
        // Detener la reproducción de música y la animación al salir de la vista
        Platform.runLater(() -> {
            if (player != null) {
                player.stop();
                player.dispose();
            }
            if (confettiTimeline != null) {
                confettiTimeline.stop();
            }
        });
    }
}