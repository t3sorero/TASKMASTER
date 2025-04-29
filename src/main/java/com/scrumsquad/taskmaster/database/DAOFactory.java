package com.scrumsquad.taskmaster.database;

import com.scrumsquad.taskmaster.database.concepto.ConceptoDAO;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDAOImp;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAOImp;
import com.scrumsquad.taskmaster.database.quiz.QuizDAO;
import com.scrumsquad.taskmaster.database.quiz.QuizDAOImp;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionsDAO;
import com.scrumsquad.taskmaster.database.shortquestions.ShortQuestionsDAOImp;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDao;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDaoImp;
import com.scrumsquad.taskmaster.database.login.LoginDAO;
import com.scrumsquad.taskmaster.database.login.LoginDAOImp;

public class DAOFactory {

    private DAOFactory() {}

    public static ConceptoDAO getConceptoDAO() {
        return new ConceptoDAOImp();
    }

    public static DefinicionDAO getDefinicionesDAO() {
        return new DefinicionDAOImp();
    }

    public static TeoriaDao getTeoriaDAO() { return new TeoriaDaoImp();}
    public static LoginDAO getLoginDAO() {return new LoginDAOImp();}

    public static QuizDAO getQuizDAO() {return new QuizDAOImp();}
    public static ShortQuestionsDAO getShortQuestionsDAO(){return new ShortQuestionsDAOImp();}

}
