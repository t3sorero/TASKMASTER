package com.scrumsquad.taskmaster.services.conceptmaching;

import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.database.definicion.DefinicionDTO;

import java.util.List;
import java.util.Objects;

public class ConceptosDefinicionesTOA {

    private List<ConceptoDTO> conceptos;

    private List<DefinicionDTO> definiciones;

    public ConceptosDefinicionesTOA(List<ConceptoDTO> conceptos, List<DefinicionDTO> definiciones) {
        this.conceptos = conceptos;
        this.definiciones = definiciones;
    }

    public List<ConceptoDTO> getConceptos() {
        return conceptos;
    }

    public List<DefinicionDTO> getDefiniciones() {
        return definiciones;
    }

    public void setConceptos(List<ConceptoDTO> conceptos) {
        this.conceptos = conceptos;
    }

    public void setDefiniciones(List<DefinicionDTO> definiciones) {
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
