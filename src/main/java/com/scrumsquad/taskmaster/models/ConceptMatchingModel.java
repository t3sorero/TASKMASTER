package com.scrumsquad.taskmaster.models;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ConceptMatchingModel {
    private List<String> concepts;
    private List<String> definitions;
    private Map<String, String> userAnswers; // Para almacenar las respuestas del usuario

    /**
     * Constructor del modelo, recibe listas de conceptos y definiciones.
     */
    public ConceptMatchingModel(List<String> concepts, List<String> definitions) {
        this.concepts = concepts;
        this.definitions = definitions;
        this.userAnswers = new HashMap<>();
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
        userAnswers.put(concept, definition);
    }

    /**
     * Obtiene todas las respuestas del usuario.
     */
    public Map<String, String> getUserAnswers() {
        return userAnswers;
    }

    /**
     * Reinicia las respuestas del usuario para comenzar una nueva partida.
     */
    public void resetUserAnswers() {
        userAnswers.clear();
    }
}
