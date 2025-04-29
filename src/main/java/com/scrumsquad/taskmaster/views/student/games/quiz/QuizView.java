package com.scrumsquad.taskmaster.views.student.games.quiz;

import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.Navigator;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.lib.FontUtils;
import com.scrumsquad.taskmaster.lib.ResourceLoader;
import com.scrumsquad.taskmaster.lib.SwingUtils;
import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.lib.swing.GradientRoundedPanel;
import com.scrumsquad.taskmaster.lib.swing.ImagePanel;
import com.scrumsquad.taskmaster.lib.swing.RoundedPanel;
import com.scrumsquad.taskmaster.views.AppColors;
import com.scrumsquad.taskmaster.views.ViewRoutes;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class QuizView extends View {

    private static final int minWidth = 1500;
    private static final int minHeight = 800;

    private static final String backgroundPath = "/images/quiz_background.jpg";
    private static final int totalQuestions = 5;
    private static final Color progresoRemainingColor = AppColors.secondaryLight;
    private static final Color progresoDoneColor = AppColors.primary;
    private static final Color progresoCurrentColor = new Color(0xFF9D33);

    private static final Color preguntaColor1 = new Color(0x192C72);
    private static final Color preguntaColor2 = new Color(0x25569A);
    private static final Color correctColor = new Color(0x3FDF10);
    private static final Color incorrectColor = new Color(0xD61313);

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private java.util.List<JPanel> progresoQuestions = new ArrayList<>(totalQuestions);
    private JPanel pregunta;
    private JPanel pista;
    private java.util.List<GradientRoundedPanel> respuestaButtons = new ArrayList<>(4);
    private JPanel reintentarButton;

    private HashMap<Integer,PreguntaQuizDTO> preguntas;
    private int correctQuestion;
    private int currentQuestion = 0;

    private boolean enabled = true;
    private Timer timer;
    private Dimension currentMinimumSize;
    private JFrame frame;

    @Override
    public JPanel build(BuildOptions options) {
        frame = options.frame();
        currentMinimumSize = frame.getMinimumSize();
        frame.setMinimumSize(new Dimension(minWidth, minHeight));
        frame.setLocationRelativeTo(null);
        //llamada al comando
        Context ctx = new Context(CommandName.quizScrumGetData);
        AppController.getInstance().action(ctx);

        JPanel mainPanel = new ImagePanel(backgroundPath, ImagePanel.CENTER);
        mainPanel.setLayout(new GridLayout(1, 1));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        cardPanel.add("progreso", createProgresoPanel());
        cardPanel.add("error", createErrorPanel());

        mainPanel.add(cardPanel);
        return mainPanel;
    }

    private JPanel createErrorPanel(){
        GridBagConstraints errorConstraints = SwingUtils.verticalConstraints();
        errorConstraints.insets = new Insets(8, 0, 8, 0);
        JPanel errorPanel = new JPanel(new GridBagLayout());
        errorPanel.setOpaque(false);
        JLabel errorIconPanel = new JLabel(ResourceLoader.loadImageIcon("/images/error_icon.png"));
        JLabel errorMessagePanel = new JLabel("Error al cargar de la base de datos");
        JPanel aux = new JPanel();
        aux.setLayout(new FlowLayout());
        errorMessagePanel.setFont(FontUtils.lato30);
        errorMessagePanel.setForeground(AppColors.error);
        aux.add(errorMessagePanel);
        errorPanel.add(errorIconPanel, errorConstraints);
        errorPanel.add(aux, errorConstraints);
        return errorPanel;
    }

    private JPanel createProgresoPanel() {
        JPanel progresoPanel = new JPanel(new GridLayout(totalQuestions, 1, 16, 16));
        progresoPanel.setOpaque(false);
        //progresoPanel.setBorder(SwingUtils.emptyBorder(64));
        Stack<JPanel> stack = new Stack<>();
        for (int i = totalQuestions; i > 0; i--) {
            JPanel question = new RoundedPanel(4);
            question.setLayout(new GridBagLayout());
            GridBagConstraints constraints = SwingUtils.verticalConstraints();
            question.setBackground(progresoRemainingColor);
            question.setEnabled(false);
            JLabel label = new JLabel(i + "");
            label.setForeground(AppColors.text);
            label.setFont(FontUtils.lato30);
            question.add(label, constraints);
            stack.add(question);
            progresoPanel.add(question);
        }
        while (!stack.isEmpty()) {
            progresoQuestions.add(stack.pop());
        }

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel aux = new JPanel();
        aux.setBorder(SwingUtils.emptyBorder(64));
        aux.setLayout(new BoxLayout(aux, BoxLayout.Y_AXIS));
        aux.setOpaque(false);
        aux.add(progresoPanel);
        aux.add(Box.createVerticalStrut(10));

        GradientRoundedPanel reintentar = new GradientRoundedPanel(
                preguntaColor1, preguntaColor2,
                Color.white, 20);
        reintentar.setLayout(new GridBagLayout());
        JLabel texto = new JLabel("Reintentar");
        texto.setForeground(Color.white);
        texto.setFont(FontUtils.lato30);
        reintentar.add(texto);
        reintentar.setMaximumSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 9));
        reintentar.setPreferredSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 9));
        aux.add(reintentar, SwingUtils.verticalConstraints());
        reintentar.setVisible(false);
        reintentar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                Navigator.getNavigator().to(ViewRoutes.quiz,args -> Navigator.getNavigator().back());
            }
        });;
        this.reintentarButton = reintentar;

        return aux;
    }

    private void showProgresoPanel() {
        /*
        Cambia el color de los fondos de las pregunas en la vista del progreso
        Se aprovecha para ir creando la vista de la pregunta y usar el parpadeo como pantalla de carga
        */
        int i = 0;
        for (JPanel question : progresoQuestions) {
            if (currentQuestion > i) {
                question.setBackground(progresoDoneColor);
            } else if (currentQuestion == i) {
                question.setBackground(progresoCurrentColor);
            } else {
                question.setBackground(progresoRemainingColor);
            }
            i++;
        }
        cardLayout.show(cardPanel, "progreso");

        showPreguntaPanel(0); //hace el efecto de parpadeo
    }

    private void showPreguntaPanel(int n) {
        /*
        Se crea un timer que itera 7 veces para mostrar el efecto de parpadeo
        Se le pasa el SwingWorker para que espere la creación del panel con la pregunta
         */
        int cont = Math.max(0, n);
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = null;
        timer = new Timer(300, (e) -> {
            if (cont < 7) {
                progresoQuestions
                        .get(currentQuestion)
                        .setBackground(cont % 2 == 0 ? progresoCurrentColor : progresoRemainingColor);
                showPreguntaPanel(cont + 1);
            } else {
                cardLayout.show(cardPanel, "pregunta");
            }
        });
        timer.setRepeats(false); //Sin esta linea no para de salir el panel de las preguntas
        timer.start();
    }

    private void updateQuestionPanel (int current){
        JLabel textoPregunta = (JLabel)pregunta.getComponents()[0];
        textoPregunta.setText(preguntas.get(current+1).getPregunta());
        Collections.shuffle(preguntas.get(current+1).getOpciones());

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        this.pista.setVisible(false);
        JLabel textoPista = (JLabel)pista.getComponents()[0];
        textoPista.setText(preguntas.get(current+1).getPista());
        textoPista = (JLabel)pista.getComponents()[0];
        this.pista.setMaximumSize(new Dimension(textoPista.getPreferredSize().width+50, pantalla.height / 20));
        this.pista.setPreferredSize(new Dimension(textoPista.getPreferredSize().width+50, pantalla.height / 20));

        for (int i = 0; i < 4; i++) {
            GradientRoundedPanel panel = respuestaButtons.get(i);
            panel.setColor(preguntaColor1, preguntaColor2);
            panel.setVisible(true);
            JLabel textoRespuesta = (JLabel)panel.getComponents()[0];
            textoRespuesta.setText(preguntas.get(current+1).getOpciones().get(i));

            if (textoRespuesta.getText().equals(preguntas.get(current+1).getCorrecta())) {
                correctQuestion = i;
            }
        }
        enabled = true;
    }

    private JPanel createQuestionsPanel(int current) {

        //auxiliar (saca la dimensión del panel y marca el numero de la correcta)
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();

        //principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(SwingUtils.emptyBorder(64));
        enabled = true;

        //comodines
        mainPanel.add(createComodines(current));

        //separador
        mainPanel.add(Box.createVerticalStrut(10));

        //pregunta
        JPanel pregunta = new GradientRoundedPanel(preguntaColor1, preguntaColor2, Color.white, 20);
        pregunta.setLayout(new GridBagLayout());
        pregunta.setOpaque(false);
        JLabel texto = new JLabel(preguntas.get(current+1).getPregunta());
        texto.setForeground(Color.white);
        texto.setFont(FontUtils.lato30);
        pregunta.add(texto);
        pregunta.setMaximumSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 10));
        pregunta.setPreferredSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 10));
        mainPanel.add(pregunta);
        this.pregunta = pregunta;

        //separador
        mainPanel.add(Box.createVerticalStrut(10));

        //respuesta
        JPanel respuestas = new JPanel(new GridLayout(2, 2, 10, 10));
        respuestaButtons = new ArrayList<>();
        Collections.shuffle(preguntas.get(current+1).getOpciones());
        for (int i = 0; i < 4; i++) {
            respuestas.add(createResponse(i, current));
        }
        for (int i = 0; i < 4; i++){
            String aux = preguntas.get(current+1).getOpciones().get(i);
            if (aux.equals(preguntas.get(current+1).getCorrecta())) {
                correctQuestion = i;
                break;
            }
        }
        respuestas.setOpaque(false);
        respuestas.setPreferredSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 5));
        respuestas.setMaximumSize(new Dimension(Integer.MAX_VALUE, pantalla.height / 5));
        mainPanel.add(respuestas);

        return mainPanel;
    }

    private JPanel createComodines(int current){
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel comodinesAux = new JPanel();
        comodinesAux.setLayout(new BoxLayout(comodinesAux, BoxLayout.X_AXIS));
        comodinesAux.setOpaque(false);
        JPanel comodines = new JPanel();
        comodines.setLayout(new BoxLayout(comodines, BoxLayout.X_AXIS));
        comodines.setOpaque(false);

        GradientRoundedPanel cincuentaCincuenta = comodinesAux("50/50");
        GradientRoundedPanel preguntaExtra = comodinesAux("Sprint Extra");
        GradientRoundedPanel pista = comodinesAux("Pregunta Al Scrum Master");

        createCincuentaCincuenta(cincuentaCincuenta);
        createPreguntaExtra(preguntaExtra);
        createPista(pista);

        comodines.add(cincuentaCincuenta);
        comodines.add(Box.createHorizontalStrut(10));
        comodines.add(preguntaExtra);
        comodines.add(Box.createHorizontalStrut(10));
        comodines.add(pista);
        comodinesAux.add(comodines);

        comodinesAux.add(Box.createHorizontalGlue());

        GradientRoundedPanel pistaPanel = new GradientRoundedPanel(preguntaColor1, preguntaColor2, Color.white, 20);
        pistaPanel.setLayout(new GridBagLayout());
        pistaPanel.setOpaque(false);
        pistaPanel.setVisible(false);
        this.pista = pistaPanel;
        JLabel textoPista = new JLabel(preguntas.get(current+1).getPista());
        textoPista.setFont(FontUtils.lato20);
        textoPista.setForeground(Color.white);
        pistaPanel.add(textoPista);
        pistaPanel.setMaximumSize(new Dimension(textoPista.getPreferredSize().width+50, pantalla.height / 20));
        pistaPanel.setPreferredSize(new Dimension(textoPista.getPreferredSize().width+50, pantalla.height / 20));
        comodinesAux.add(pistaPanel);

        comodines.setMaximumSize(new Dimension(pantalla.width / 4, pantalla.height / 20));
        comodines.setPreferredSize(new Dimension(pantalla.width / 4, pantalla.height / 20));
        return comodinesAux;
    }

    private GradientRoundedPanel comodinesAux(String texto) {
        GradientRoundedPanel panel = new GradientRoundedPanel
                (preguntaColor1, preguntaColor2, Color.white, 20);
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        JLabel textoComodin = new JLabel(texto);
        textoComodin.setFont(FontUtils.lato20);
        textoComodin.setForeground(Color.white);
        panel.add(textoComodin);
        panel.setPreferredSize(textoComodin.getPreferredSize());
        return panel;
    }

    private void createCincuentaCincuenta(GradientRoundedPanel panel) {

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (enabled) {
                    panel.removeMouseListener(this);
                    panel.setColor(progresoRemainingColor, progresoRemainingColor);
                    panel.repaint();

                    System.out.println("Se ha pulsado el comodin");

                    ArrayList<Integer> opciones = new ArrayList<>();
                    for (int j = 0; j < 4; j++){
                        if (j != correctQuestion)
                            opciones.add(j);
                    }
                    Collections.shuffle(opciones);
                    List<Integer> descartados = opciones.subList(0, 2);
                    respuestaButtons.get(descartados.get(0)).setVisible(false);
                    respuestaButtons.get(descartados.get(1)).setVisible(false);
                }
            }
        });
    }

    private void createPreguntaExtra(GradientRoundedPanel panel) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (enabled) {
                    panel.removeMouseListener(this);
                    panel.setColor(progresoRemainingColor, progresoRemainingColor);
                    panel.repaint();
                    updateQuestionPanel(currentQuestion*2+1);
                }
            }
        });
    }

    private void createPista(GradientRoundedPanel panel) {
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (enabled) {
                    panel.removeMouseListener(this);
                    panel.setColor(progresoRemainingColor, progresoRemainingColor);
                    panel.repaint();
                    pista.setVisible(true);
                }
            }
        });
    }

    private JPanel createResponse(int i, int current) {

        JLabel textoRespuesta = new JLabel(preguntas.get(current+1).getOpciones().get(i));
        textoRespuesta.setForeground(Color.white);
        textoRespuesta.setFont(FontUtils.lato30);

        GradientRoundedPanel panel = new GradientRoundedPanel(preguntaColor1, preguntaColor2, Color.white, 20);
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.add(textoRespuesta);
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (enabled) {
                    enabled = false;
                    System.out.println("Se ha pulsado el boton" + i);
                    responseMade(i);
                }
            }
        });
        respuestaButtons.add(panel);
        return panel;
    }

    private void responseMade(int i) {
        respuestaButtons.get(correctQuestion).setColor(correctColor, correctColor);
        respuestaButtons.get(correctQuestion).repaint();

        Timer timer = null;
        if (i != correctQuestion) { //respuesta incorrecta
            respuestaButtons.get(i).setColor(incorrectColor, incorrectColor);
            respuestaButtons.get(i).repaint();
            progresoQuestions.get(currentQuestion).setBackground(incorrectColor);
            timer = new Timer (2000, (e) -> {
                cardLayout.show(cardPanel, "progreso");
                this.reintentarButton.setVisible(true);
                /*Navigator.getNavigator().to(ViewRoutes.quiz,
                        args -> Navigator.getNavigator().back());*/
            });
        }
        else if (currentQuestion != 4){ //respuesta correcta
            timer = new Timer(2000, (e) -> {
                currentQuestion++;
                updateQuestionPanel(currentQuestion*2);
                showProgresoPanel();
            });
        }
        else { //juego terminado
            timer = new Timer(5000, (e) -> {
                Navigator.getNavigator().to(ViewRoutes.quizWinner,
                        args -> Navigator.getNavigator().back());
            });
        }
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onDispose() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = null;
        frame.setMinimumSize(currentMinimumSize);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void update(Context ctx) {
        System.out.println("ha llegado el contexto");
        if (ctx ==  null) return;
        if (ctx.getCommandName().equals(CommandName.quizScrumGetDataOk)) {
            preguntas = (HashMap<Integer, PreguntaQuizDTO>) ctx.getArguments().get("preguntas");
            cardPanel.add("pregunta", createQuestionsPanel(currentQuestion*2));
            showProgresoPanel();
        }
        else {
            cardLayout.show(cardPanel, "error");
        }
    }
}
