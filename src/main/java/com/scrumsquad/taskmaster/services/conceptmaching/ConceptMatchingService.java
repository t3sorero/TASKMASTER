package com.scrumsquad.taskmaster.services.conceptmaching;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.util.*;

public abstract class ConceptMatchingService {

    private static ConceptMatchingService instance;

    public static synchronized ConceptMatchingService getInstance() {
        if (instance == null) {
            instance = new ConceptMatchingServiceImp();
        }
        return instance;
    }

    /**
     * Obtiene los conceptos y definiciones desde la base de datos.
     */
    public abstract ConceptosDefinicionesTOA getGameData() throws Exception;

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public abstract Map<Integer, Boolean> checkAnswers(Map<Integer, Integer> userAnswers, Set<Integer> conceptosIds) throws Exception;
}
