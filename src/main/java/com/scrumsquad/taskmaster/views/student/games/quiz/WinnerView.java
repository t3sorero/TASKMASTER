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
import java.net.URL;
import java.awt.BorderLayout;
import java.util.Random;

public class WinnerView extends View {
    private MediaPlayer player;
    private Timeline confettiTimeline;
    private Timeline pulseTimeline;

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

        // Bordes dorados mejorados para la parte superior e inferior
        Rectangle topBorder = new Rectangle(50, 50, w - 100, 15);
        topBorder.setFill(Color.rgb(255, 215, 0)); // Dorado
        topBorder.setOpacity(0);

        Rectangle bottomBorder = new Rectangle(50, h - 65, w - 100, 15);
        bottomBorder.setFill(Color.rgb(255, 215, 0)); // Dorado
        bottomBorder.setOpacity(0);

        // Añadimos el panel al root
        root.getChildren().addAll(stage, topBorder, bottomBorder);

        // Animamos la aparición del panel y los bordes
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
            // Después de que aparezca el panel, mostramos el mensaje y el trofeo
            showCongratulationsMessage(root, w, h);
        });

        fadeInPanel.play();
    }

    private void showCongratulationsMessage(Pane root, double w, double h) {
        // Creamos un trofeo personalizado mejorado
        Pane trophyPane = createImprovedTrophy();
        trophyPane.setScaleX(1.3);
        trophyPane.setScaleY(1.3);
        trophyPane.setLayoutX((w - 120) / 2);  // Centrado, ajustando por escala
        trophyPane.setLayoutY(h/2 - 240);      // Colocado en la parte superior
        trophyPane.setOpacity(0); // Inicialmente invisible para animarlo

        // Añadimos un efecto de brillo
        Glow glow = new Glow();
        glow.setLevel(0.6);

        // Aplicamos un efecto de sombra proyectada para más profundidad
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GOLD);
        dropShadow.setRadius(30);
        dropShadow.setSpread(0.5);

        // Combinamos los efectos
        glow.setInput(dropShadow);
        trophyPane.setEffect(glow);

        root.getChildren().add(trophyPane);

        // Mensaje principal con estilo didáctico y motivador
        Text msg = new Text("¡Enhorabuena!");
        msg.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        msg.setFill(Color.rgb(33, 150, 243)); // Azul brillante

        // Calculamos el ancho del texto correctamente antes de posicionarlo
        double msgWidth = computeTextWidth(msg.getText(), msg.getFont());
        msg.setX((w - msgWidth) / 2);
        msg.setY(h/2 + 30);

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

        // Calculamos el ancho del texto correctamente antes de posicionarlo
        double subtitleWidth = computeTextWidth(subtitle.getText(), subtitle.getFont());
        subtitle.setX((w - subtitleWidth) / 2);
        subtitle.setY(h/2 + 80);
        subtitle.setEffect(shadow);

        // Animación para el mensaje
        msg.setOpacity(0);
        subtitle.setOpacity(0);

        // 1. Entrada del trofeo con efecto de aparición y rebote suave
        FadeTransition fadeInTrophy = new FadeTransition(Duration.seconds(1), trophyPane);
        fadeInTrophy.setFromValue(0);
        fadeInTrophy.setToValue(1);

        // Añadimos un pequeño rebote al trofeo
        TranslateTransition trophyBounce = new TranslateTransition(Duration.seconds(1), trophyPane);
        trophyBounce.setFromY(-50);
        trophyBounce.setToY(0);

        // 2. Eliminamos la rotación 3D completa que causaba problemas visuales
        // Reemplazamos con una rotación suave y limitada para mantener un efecto sutil
        RotateTransition rotateY = new RotateTransition(Duration.seconds(4), trophyPane);
        rotateY.setAxis(Rotate.Y_AXIS);
        // Rotación limitada de solo 15 grados en cada dirección
        rotateY.setFromAngle(-15);
        rotateY.setToAngle(15);
        rotateY.setCycleCount(Timeline.INDEFINITE);
        rotateY.setAutoReverse(true);

        // 3. Efecto de pulso para hacer más notoria la animación
        pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.6)),
                new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.3)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glow.levelProperty(), 0.6))
        );
        pulseTimeline.setCycleCount(Timeline.INDEFINITE);

        // Reproducimos todas las animaciones del trofeo
        ParallelTransition trophyEntrance = new ParallelTransition(fadeInTrophy, trophyBounce);
        trophyEntrance.play();

        trophyEntrance.setOnFinished(e -> {
            rotateY.play();
            pulseTimeline.play();
        });

        // Animación para el mensaje de texto
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

    // Método auxiliar para calcular el ancho real del texto
    private double computeTextWidth(String text, Font font) {
        Text helper = new Text(text);
        helper.setFont(font);
        return helper.getLayoutBounds().getWidth();
    }

    // Método para crear un trofeo mejorado con menos problemas de renderizado
    private Pane createImprovedTrophy() {
        Pane trophyPane = new Pane();
        trophyPane.setPrefSize(100, 200);

        // Colores del trofeo
        Color goldColor = Color.rgb(255, 215, 0);
        Color goldShadow = Color.rgb(218, 165, 32);
        Color goldHighlight = Color.rgb(255, 235, 100);
        Color redColor = Color.rgb(220, 20, 20);
        Color blackBase = Color.rgb(20, 20, 20);

        // Base negra del trofeo (más detallada)
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

        // Tallo dorado del trofeo (con gradiente para mejor efecto 3D)
        Rectangle stem = new Rectangle(45, 90, 10, 50);
        stem.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(0.5, goldColor),
                new Stop(1, goldShadow)));

        // Copa del trofeo con mejor detalle
        // Parte inferior de la copa
        Ellipse cupBottom = new Ellipse(50, 90, 25, 8);
        cupBottom.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(0.5, goldColor),
                new Stop(1, goldHighlight)));

        // Laterales y centro de la copa (más definidos)
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

        // Parte superior de la copa (redondeada)
        Ellipse cupTop = new Ellipse(50, 35, 25, 8);
        cupTop.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldHighlight),
                new Stop(0.5, goldColor),
                new Stop(1, goldShadow)));

        // Asas del trofeo (más definidas y con efecto 3D)
        // Asa izquierda
        Ellipse handleLeftOuter = new Ellipse(15, 60, 10, 25);
        handleLeftOuter.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldShadow),
                new Stop(1, goldColor)));

        Ellipse handleLeftInner = new Ellipse(15, 60, 5, 20);
        handleLeftInner.setFill(Color.rgb(245, 245, 245)); // Fondo que simula hueco

        // Asa derecha
        Ellipse handleRightOuter = new Ellipse(85, 60, 10, 25);
        handleRightOuter.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, goldColor),
                new Stop(1, goldShadow)));

        Ellipse handleRightInner = new Ellipse(85, 60, 5, 20);
        handleRightInner.setFill(Color.rgb(245, 245, 245)); // Fondo que simula hueco

        // Creamos un StackPane para agrupar el círculo rojo y el número
        // El círculo tendrá un radio de 18, por lo que el StackPane tendrá un diámetro de 36
        // Queremos que el centro se ubique en (50,55) => posición (50-18, 55-18)
        StackPane redCirclePane = new StackPane();
        redCirclePane.setLayoutX(50 - 18);
        redCirclePane.setLayoutY(55 - 18);
        redCirclePane.setPrefSize(36, 36);

        // Círculo rojo central ajustado para el StackPane (usando coordenadas relativas)
        Circle redCircleBg = new Circle(18, 18, 18);
        redCircleBg.setFill(Color.rgb(240, 30, 30)); // Base roja más oscura

        Circle redCircle = new Circle(18, 18, 16);
        redCircle.setFill(new RadialGradient(0, 0, 0.3, 0.3, 0.7, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 100, 100)),
                new Stop(0.8, Color.rgb(220, 20, 20)),
                new Stop(1, Color.rgb(180, 0, 0))));

        Circle redCircleBorder = new Circle(18, 18, 18);
        redCircleBorder.setFill(Color.TRANSPARENT);
        redCircleBorder.setStroke(goldColor);
        redCircleBorder.setStrokeWidth(2);

        // Texto que representa el número "1"
        Text numberOne = new Text("1");
        numberOne.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        numberOne.setFill(Color.WHITE);
        StackPane.setAlignment(numberOne, Pos.CENTER);  // Centramos el texto

        // Se añaden los nodos al contenedor. Gracias al StackPane, el número quedará centrado.
        redCirclePane.getChildren().addAll(redCircleBg, redCircle, redCircleBorder, numberOne);

        // Aplicamos efectos de iluminación más sutiles
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(3.0); // Efecto sutil

        cupMiddle.setEffect(lighting);

        // Se añaden todos los componentes al Pane principal en orden para la correcta superposición
        trophyPane.getChildren().addAll(
                baseBottom, baseMiddle, baseTop,
                stem, cupBottom,
                handleLeftOuter, handleLeftInner,
                handleRightOuter, handleRightInner,
                cupLeft, cupRight, cupMiddle, cupTop,
                redCirclePane  // Usamos el StackPane que ya contiene el círculo y el número
        );

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
        // Detener la reproducción de música y las animaciones al salir de la vista
        Platform.runLater(() -> {
            if (player != null) {
                player.stop();
                player.dispose();
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