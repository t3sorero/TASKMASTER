package com.scrumsquad.taskmaster.services.shortquestions;

import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ShortQuestionsService {

    private static ShortQuestionsService instance;

    public static synchronized  ShortQuestionsService getInstance(){
        if(instance == null){
            instance = new ShortQuestionsServiceImp();
        }
        return instance;
    }

    //Obtener las preguntas de la base de datos
    public abstract List<ShortQuestionDTO> getShortQuestions(int tema) throws Exception;


    //Verificar las respuestas enviadas por el usuario
    public abstract Map<Integer, Boolean> checkAnswers(Map<Integer, String> userAnswers, Set<Integer> preguntasIds) throws Exception;

}
