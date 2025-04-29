package com.scrumsquad.taskmaster.services.quizscrum;


import com.scrumsquad.taskmaster.database.quiz.PreguntaQuizDTO;

import java.util.Map;

public abstract class QuizService {
    private static QuizService instance;

    public static QuizService getInstance() {
        if (instance == null) {
            instance = new QuizServiceImp();
        }
        return instance;
    }

    public abstract Map<Integer, PreguntaQuizDTO> getPreguntas() throws Exception;
}
