package com.scrumsquad.taskmaster.services;

//import com.scrumsquad.taskmaster.db.ConceptMatchingDAO;
import com.scrumsquad.taskmaster.models.ConceptMatchingModel;

import java.util.List;
import java.util.Map;

public class ConceptMatchingService {
    private final ConceptMatchingModel model;

    public ConceptMatchingService(ConceptMatchingModel model) {
        this.model = model;
    }

    /**
     * Obtiene los conceptos y definiciones desde la base de datos.
     */
    public ConceptMatchingModel getGameData() {
        return model;
    }

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public List<Boolean> checkAnswers(Map<String, String> userAnswers) {
        return model.checkUserAnswers(userAnswers);
    }

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public Boolean checkAnswer(String concept, String userAnswer) {
        return model.checkUserAnswer(concept, userAnswer);
    }
}
