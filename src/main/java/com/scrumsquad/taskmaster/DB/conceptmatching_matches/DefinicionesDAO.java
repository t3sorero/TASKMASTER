package com.scrumsquad.taskmaster.DB.conceptmatching_matches;

import java.util.Collection;
import java.util.List;

public interface DefinicionesDAO {

    public List<DefinicionesDTO> getMatches(List<Integer> ids);

    public List<DefinicionesDTO> getDefinicionesByConcepto(int conceptoId);

    public List<DefinicionesDTO> getAllDefiniciones();
}
