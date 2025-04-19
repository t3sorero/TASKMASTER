package com.scrumsquad.taskmaster.database.quiz;

import java.util.List;

public interface QuizDAO {
    public List<PreguntaQuizDTO> getPreguntasNvl(int nivel) throws Exception;
}
