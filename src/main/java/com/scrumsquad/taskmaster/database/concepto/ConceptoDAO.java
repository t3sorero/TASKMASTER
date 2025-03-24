package com.scrumsquad.taskmaster.database.concepto;

import java.util.List;

public interface ConceptoDAO {

    public List<ConceptoDTO> getConcepts(List<Integer> ids);

    public List<ConceptoDTO> getAllConceptos(int tema) throws Exception;


    public ConceptoDTO getConceptoById(Integer key);
}
