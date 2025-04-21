package com.scrumsquad.taskmaster.services.shortquestions;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.util.*;

public class ShortQuestionsServiceImp extends ShortQuestionsService{

    public static final int QUESTIONS = 10;

    @Override
    public List<ShortQuestionDTO> getShortQuestions(int tema) throws Exception {

        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try{
            t.start();
            var daoPreguntas = DAOFactory.getShortQuestionsDAO();

            var preguntas = daoPreguntas.getAllQuestions(tema);
            if(preguntas.isEmpty()){
                t.commit();
                return new ArrayList<>();
            }
            Collections.shuffle(preguntas);
            List<ShortQuestionDTO> preguntasElegidas = new ArrayList<>();
            for(int j = 0; j < QUESTIONS; j++){
                preguntasElegidas.add(preguntas.get(j));
            }
            t.commit();
            return preguntasElegidas;
        }catch(Exception e){
            t.rollback();
            throw e;
        }
    }

    @Override
    public Map<Integer, Boolean> checkAnswers(Map<Integer, String> userAnswers, Set<Integer> preguntasIds) throws Exception {
        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try{
            t.start();
            var daoPreguntas = DAOFactory.getShortQuestionsDAO();
            var results = new HashMap<Integer, Boolean>();
            if(preguntasIds != null && !preguntasIds.isEmpty()){
                for(var index : preguntasIds){
                    if(userAnswers.containsKey(index)){
                        String respuesta = daoPreguntas.getRespuestaById(index);
                        if(respuesta.equalsIgnoreCase(userAnswers.get(index))){
                            results.put(index, true);
                        } else{
                            results.put(index, false);
                        }
                    } else{
                        results.put(index, false);
                    }
                }
            }
            t.commit();
            return results;
        }catch(Exception e){
            t.rollback();
            throw e;
        }
    }
}
