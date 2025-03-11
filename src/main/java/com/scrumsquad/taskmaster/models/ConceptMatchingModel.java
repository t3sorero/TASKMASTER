package com.scrumsquad.taskmaster.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ConceptMatchingModel {
    private List<String> concepts;
    private List<String> definitions;
    private Map<String, String> correctAnswers; // Para almacenar las respuestas del usuario

    /**
     * Constructor del modelo, recibe listas de conceptos y definiciones.
     */
    public ConceptMatchingModel(List<String> concepts, List<String> definitions) {
        this.concepts = concepts;
        this.definitions = definitions;
        this.correctAnswers = new HashMap<>();
    }

    /**
     * Obtiene la lista de conceptos.
     */
    public List<String> getConcepts() {
        return concepts;
    }

    /**
     * Obtiene la lista de definiciones.
     */
    public List<String> getDefinitions() {
        return definitions;
    }

    /**
     * Guarda una respuesta del usuario.
     * @param concept   El concepto seleccionado.
     * @param definition La definición asignada por el usuario.
     */
    public void addUserAnswer(String concept, String definition) {
        correctAnswers.put(concept, definition);
    }

    /**
     * Obtiene todas las respuestas del usuario.
     */
    public Map<String, String> getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Reinicia las respuestas del usuario para comenzar una nueva partida.
     */
    public void resetUserAnswers() {
        correctAnswers.clear();
    }

    /**
     * Checkea si una respuesta es correcta o no
     */
    public boolean checkUserAnswer(String concept, String definition) {
        return correctAnswers.containsKey(concept) && correctAnswers.get(concept).equals(definition);
    }

    /**
     * Checkea las respuestas proporcionadas
     */
    public List<Boolean> checkUserAnswers(Map<String, String> userAnswers) {

        // Podriamos también lanzar excepción
        if (this.correctAnswers.size() != userAnswers.size()) { return new ArrayList<>(); }

        List<Boolean> answers = new ArrayList<>();

        for (String concept : correctAnswers.keySet()) {
            if (!userAnswers.containsKey(concept)) {
                answers.add(false);
            } else {
                answers.add(checkUserAnswer(concept, userAnswers.get(concept)));
            }
        }

        return answers;
    }
}
