package com.scrumsquad.taskmaster.DB.conceptmatching_matches;

import java.util.Objects;

public class DefinicionesDTO {
    private int id;
    private String descripcion;
    private int conceptoId;

    public DefinicionesDTO(int id, String definicion, int nConcepto) {
        this.id = id;
        this.descripcion = definicion;
        this.conceptoId = nConcepto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getConceptoId() {
        return conceptoId;
    }

    public void setConceptoId(int conceptoId) {
        this.conceptoId = conceptoId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DefinicionesDTO that = (DefinicionesDTO) o;
        return id == that.id && conceptoId == that.conceptoId && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descripcion, conceptoId);
    }
}
