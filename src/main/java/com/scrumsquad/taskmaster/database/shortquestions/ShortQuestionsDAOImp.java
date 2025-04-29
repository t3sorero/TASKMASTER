package com.scrumsquad.taskmaster.database.shortquestions;

import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShortQuestionsDAOImp implements ShortQuestionsDAO{

    @Override
    public List<ShortQuestionDTO> getAllQuestions(int tema) throws Exception {
        List<ShortQuestionDTO> preguntas = new ArrayList<>();
        String query = "SELECT * FROM preguntas WHERE tema = ?::varchar";
        Transaction transaction = TransactionManager.getInstance().getTransaccion();
        Connection con = transaction.getResource();

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, tema);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            preguntas.add(new ShortQuestionDTO(rs.getInt("id"), rs.getString("pregunta")));
        }
        rs.close();
        ps.close();
        return preguntas;
    }

    @Override
    public String getRespuestaById(int id) throws Exception {
        String respuesta = null;
        String query = "SELECT * FROM preguntas WHERE id = ?";
        Transaction transaction = TransactionManager.getInstance().getTransaccion();
        Connection con = transaction.getResource();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            respuesta = rs.getString("respuesta");
        }
        rs.close();
        ps.close();
        return respuesta;
    }
}
