package com.scrumsquad.taskmaster.DB.conceptmatching_concepts;

import java.util.List;

public interface ConceptoDAO {

    public List<ConceptoDTO> getConcepts(List<Integer> ids);

    public List<ConceptoDTO> getAllConceptos();


    public ConceptoDTO getConceptoById(Integer key);
}
