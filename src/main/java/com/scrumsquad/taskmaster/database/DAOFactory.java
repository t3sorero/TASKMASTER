package com.scrumsquad.taskmaster.database;

import com.scrumsquad.taskmaster.database.concepto.ConceptoDAO;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDAOImp;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDAOImp;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDao;
import com.scrumsquad.taskmaster.database.teoria.TeoriaDaoImp;

public class DAOFactory {

    private DAOFactory() {}

    public static ConceptoDAO getConceptoDAO() {
        return new ConceptoDAOImp();
    }

    public static DefinicionDAO getDefinicionesDAO() {
        return new DefinicionDAOImp();
    }

    public static TeoriaDao getTeoriaDAO() { return new TeoriaDaoImp();}

}
