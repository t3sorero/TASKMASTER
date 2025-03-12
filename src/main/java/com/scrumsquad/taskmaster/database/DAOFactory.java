package com.scrumsquad.taskmaster.database;

import com.scrumsquad.taskmaster.database.conceptmatching_concepts.ConceptoDAO;
import com.scrumsquad.taskmaster.database.conceptmatching_concepts.ConceptoDAOImp;
import com.scrumsquad.taskmaster.database.conceptmatching_matches.DefinicionesDAOImp;

public class DAOFactory {

    private DAOFactory() {}

    public static ConceptoDAO getConceptoDAO() {
        return new ConceptoDAOImp();
    }

    public static DefinicionesDAOImp getDefinicionesDAO() {
        return new DefinicionesDAOImp();
    }

}
