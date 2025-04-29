package com.scrumsquad.taskmaster.database.quiz;

import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class QuizDAOImp implements QuizDAO {

    @Override
    public List<PreguntaQuizDTO> getPreguntasNvl(int nivel) throws Exception{
        ArrayList<PreguntaQuizDTO> preguntas = new ArrayList<>();
        String query = "SELECT * FROM quizscrum WHERE nivel = ?";
        Transaction t = TransactionManager.getInstance().getTransaccion();
        Connection c = t.getResource();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, nivel);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            ArrayList<String> opciones = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                if(i == 0){
                    opciones.add(rs.getString("correcta"));
                }
                else{
                    opciones.add(rs.getString("incorrecta"+i));
                }
            }
            preguntas.add(new PreguntaQuizDTO(rs.getInt("id"), rs.getString("pregunta"),
                    nivel,opciones.get(0), opciones, rs.getString("pista")));
        }
        ps.close();
        rs.close();
        return preguntas;
    }
}
