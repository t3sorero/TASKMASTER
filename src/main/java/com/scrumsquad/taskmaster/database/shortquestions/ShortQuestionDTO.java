package com.scrumsquad.taskmaster.database.shortquestions;

import java.util.Objects;

public class ShortQuestionDTO {

    private Integer id;
    private String pregunta;

    public ShortQuestionDTO(int id, String pregunta){
        this.id = id;
        this.pregunta = pregunta;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShortQuestionDTO that = (ShortQuestionDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(pregunta, that.pregunta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pregunta);
    }
}
