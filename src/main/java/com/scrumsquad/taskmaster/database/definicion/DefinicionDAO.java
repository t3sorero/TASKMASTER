package com.scrumsquad.taskmaster.database.definicion;

import java.util.List;

public interface DefinicionDAO {

    public List<DefinicionDTO> getMatches(List<Integer> ids);

    public List<DefinicionDTO> getDefinicionesByConcepto(int conceptoId) throws Exception;

    public List<DefinicionDTO> getAllDefiniciones();

    public DefinicionDTO getDefinicionById(int id);
}
