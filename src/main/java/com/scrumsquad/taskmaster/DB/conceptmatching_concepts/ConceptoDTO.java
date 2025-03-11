package com.scrumsquad.taskmaster.DB.conceptmatching_concepts;

import java.util.Objects;

public class ConceptoDTO {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private int id;
    private String nombre;

    public ConceptoDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ConceptoDTO that = (ConceptoDTO) o;
        return id == that.id && Objects.equals(nombre, that.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }
}
