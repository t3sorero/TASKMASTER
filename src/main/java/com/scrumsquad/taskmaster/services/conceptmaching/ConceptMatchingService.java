package com.scrumsquad.taskmaster.services.conceptmaching;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.conceptmatching_concepts.ConceptoDTO;
import com.scrumsquad.taskmaster.database.conceptmatching_matches.DefinicionesDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

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

    private Transaction inicioTransaccion(){
        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        t.start();
        return t;
    }


    /**
     * Obtiene los conceptos y definiciones desde la base de datos.
     */
    public ConceptosDefinicionesTOA getGameData() throws Exception {

        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try {
            t.start();
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
            Collections.shuffle(definicionesElegidas);
            t.commit();
            return new ConceptosDefinicionesTOA(conceptosElegidos, definicionesElegidas);
        } catch (Exception e) {
            t.rollback();
            throw e;
        }

    }

    /**
     * Verifica las respuestas enviadas por el usuario.
     */
    public Map<Integer, Boolean> checkAnswers(Map<Integer, Integer> userAnswers, Set<Integer> conceptosIds) throws Exception {

        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try{
            t.start();
            var daoDefiniciones = DAOFactory.getDefinicionesDAO();
            var results = new HashMap<Integer, Boolean>();
            if (conceptosIds != null && !conceptosIds.isEmpty()) {
                for (var index : conceptosIds) {
                    if (userAnswers.containsKey(index)) {
                        var definicion = daoDefiniciones.getDefinicionById(userAnswers.get(index));
                        results.put(index, definicion != null && index == definicion.getConceptoId());
                    } else {
                        results.put(index, false);
                    }
                }
            } else {
                results.forEach((k, v) -> results.put(conceptosIds.iterator().next(), false));
            }
            t.commit();
            return results;
        } catch (Exception e) {
            t.rollback();
            throw e;
        }
    }
}
