package com.scrumsquad.taskmaster.services.quizscrum;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;
import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;
import com.scrumsquad.taskmaster.services.conceptmaching.ConceptosDefinicionesTOA;

import java.util.*;

public class QuizServiceImp extends QuizService{
    @Override
    public Map<Integer, PreguntaQuizDTO> getPreguntas() throws Exception {
        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try {
            t.start();
            Map<Integer, PreguntaQuizDTO> preguntas = new HashMap<>();
            var daoQuiz = DAOFactory.getQuizDAO();
            for(int i = 1; i < 6; i++){
                List<PreguntaQuizDTO> preguntasNvl = daoQuiz.getPreguntasNvl(i);
                Collections.shuffle(preguntasNvl);
                //preguntas 1,2->nivel 1, preguntas 2,3->nivel 2, etc.
                preguntas.put(2*i-1, preguntasNvl.get(0));
                preguntas.put(2*i, preguntasNvl.get(1));
            }
            t.commit();
           return preguntas;
        } catch (Exception e) {
            t.rollback();
            throw e;
        }
    }
}
