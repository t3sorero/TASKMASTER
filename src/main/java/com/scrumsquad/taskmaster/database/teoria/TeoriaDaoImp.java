package com.scrumsquad.taskmaster.database.teoria;

import com.scrumsquad.taskmaster.database.DBData;
import com.scrumsquad.taskmaster.database.concepto.ConceptoDTO;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TeoriaDaoImp implements TeoriaDao {
    @Override
    public String getTeoria(int tema) throws Exception {
        String result = null;
        String query = "SELECT * FROM teoria WHERE tema = ?";
        Connection con = null;
        try {

            Transaction transaction = TransactionManager.getInstance().getTransaccion();
            con = transaction.getResource();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, tema);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getString("texto");
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }
}
