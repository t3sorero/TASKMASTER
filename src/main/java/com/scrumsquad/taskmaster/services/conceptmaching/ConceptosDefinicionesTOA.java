package com.scrumsquad.taskmaster.services.conceptmaching;

import com.scrumsquad.taskmaster.DB.conceptmatching_concepts.ConceptoDTO;
import com.scrumsquad.taskmaster.DB.conceptmatching_matches.DefinicionesDTO;

import java.util.List;
import java.util.Objects;

public class ConceptosDefinicionesTOA {

    private List<ConceptoDTO> conceptos;

    private List<DefinicionesDTO> definiciones;

    public ConceptosDefinicionesTOA(List<ConceptoDTO> conceptos, List<DefinicionesDTO> definiciones) {
        this.conceptos = conceptos;
        this.definiciones = definiciones;
    }

    public List<ConceptoDTO> getConceptos() {
        return conceptos;
    }

    public List<DefinicionesDTO> getDefiniciones() {
        return definiciones;
    }

    public void setConceptos(List<ConceptoDTO> conceptos) {
        this.conceptos = conceptos;
    }

    public void setDefiniciones(List<DefinicionesDTO> definiciones) {
        this.definiciones = definiciones;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConceptosDefinicionesTOA that = (ConceptosDefinicionesTOA) o;
        return Objects.equals(conceptos, that.conceptos) && Objects.equals(definiciones, that.definiciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conceptos, definiciones);
    }
}
