package com.scrumsquad.taskmaster.DB;

import com.scrumsquad.taskmaster.DB.conceptmatching_concepts.ConceptoDAO;
import com.scrumsquad.taskmaster.DB.conceptmatching_concepts.ConceptoDAOImp;
import com.scrumsquad.taskmaster.DB.conceptmatching_matches.DefinicionesDAOImp;

public class DAOFactory {

    private DAOFactory() {}

    public static ConceptoDAO getConceptoDAO() {
        return new ConceptoDAOImp();
    }

    public static DefinicionesDAOImp getDefinicionesDAO() {
        return new DefinicionesDAOImp();
    }

}
