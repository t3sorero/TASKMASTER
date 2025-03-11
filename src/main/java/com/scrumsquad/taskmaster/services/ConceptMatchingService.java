package com.scrumsquad.taskmaster.services;

import com.scrumsquad.taskmaster.db.ConceptMatchingDAO;
import com.scrumsquad.taskmaster.models.ConceptMatchingModel;

import java.util.List;
import java.util.Map;

public class ConceptMatchingService {
    private final ConceptMatchingDAO dao;

    public ConceptMatchingService(ConceptMatchingDAO dao) {
        this.dao = dao;
    }

    /**
     * Obtiene los conceptos y definiciones desde la base de datos.
     */
    public ConceptMatchingModel getGameData() {
        List<String> concepts = dao.getConcepts();
        List<String> definitions = dao.getDefinitions();
        return new ConceptMatchingModel(concepts, definitions);
    }

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public int[] checkAnswers(Map<String, String> userAnswers) {
        return dao.validateAnswers(userAnswers);
    }
}
