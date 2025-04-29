package com.scrumsquad.taskmaster.database.quiz;

import java.util.List;
import java.util.Objects;

public class PreguntaQuizDTO {
    private Integer id;
    private String pregunta;
    private int nivel; //o string, depende de BD
    private String correcta;
    private List<String> opciones;
    private String pista;
    public PreguntaQuizDTO(Integer id,String pregunta, int nivel, String correcta, List<String> opciones, String pista) {
        this.id = id;
        this.pregunta = pregunta;
        this.nivel = nivel;
        this.correcta = correcta;
        this.opciones = opciones;
        this.pista = pista;
    }

    public String getPista() {
        return pista;
    }

    public void setPista(String pista) {
        this.pista = pista;
    }

    public int getId() {
        return id;
    }
    public String getPregunta() {
        return pregunta;
    }
    public int getNivel() {
        return nivel;
    }
    public String getCorrecta() {
        return correcta;
    }
    public List<String> getOpciones() {
        return opciones;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    public void setCorrecta(String correcta) {
        this.correcta = correcta;
    }
    public void setOpciones(List<String> opciones) {
        this.opciones = opciones;
    }
    @Override
    public boolean equals(Object o) {
        if(o == null || getClass() != o.getClass()) return false;
        PreguntaQuizDTO that = (PreguntaQuizDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(pregunta, that.pregunta);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, pregunta);
    }
}
