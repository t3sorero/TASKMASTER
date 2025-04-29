package com.scrumsquad.taskmaster.database.shortquestions;

import java.util.List;

public interface ShortQuestionsDAO {

    public List<ShortQuestionDTO> getAllQuestions(int tema)throws Exception;

    public String getRespuestaById(int id) throws Exception;
}
