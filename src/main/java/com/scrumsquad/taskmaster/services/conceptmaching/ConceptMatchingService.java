package com.scrumsquad.taskmaster.services.conceptmaching;

import com.scrumsquad.taskmaster.DB.DAOFactory;
import com.scrumsquad.taskmaster.DB.conceptmatching_concepts.ConceptoDTO;
import com.scrumsquad.taskmaster.DB.conceptmatching_matches.DefinicionesDTO;

import java.security.SecureRandom;
import java.util.*;

public class ConceptMatchingService {

    private static final int CONCEPTS = 4;
    private static final int DEFINITIONS = 6;
    private static ConceptMatchingService instance;

    private ConceptMatchingService() {
    }

    public static ConceptMatchingService getInstance() {
        if (instance == null) {
            instance = new ConceptMatchingService();
        }
        return instance;
    }


    /**
     * Obtiene los conceptos y definiciones desde la base de datos.
     */
    public ConceptosDefinicionesTOA getGameData() {
        var daoDefiniciones = DAOFactory.getDefinicionesDAO();
        var daoConceptos = DAOFactory.getConceptoDAO();

        var conceptos = daoConceptos.getAllConceptos();

        List<ConceptoDTO> conceptosElegidos = new ArrayList<>();
        List<DefinicionesDTO> definicionesElegidas = new ArrayList<>();
        Set<Integer> conceptosIds = new HashSet<>();
        for (int i = 0; i < DEFINITIONS; i++) {
            int random;
            do {
                random = (int) (Math.random() * conceptos.size());
            } while (conceptosIds.contains(random));
            conceptosIds.add(random);
            var concepto = conceptos.get(random);
            conceptosElegidos.add(concepto);
            var definiciones = daoDefiniciones.getDefinicionesByConcepto(concepto.getId());
            random = (int) (Math.random() * definiciones.size());
            definicionesElegidas.add(definiciones.get(random));
        }
        for (int i = 0; i < Math.abs(CONCEPTS - DEFINITIONS); i++) {
            conceptosElegidos.removeLast();
        }
        return new ConceptosDefinicionesTOA(conceptosElegidos, definicionesElegidas);
    }

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public int[] checkAnswers(Map<String, String> userAnswers) {
        return dao.validateAnswers(userAnswers);
    }
}
