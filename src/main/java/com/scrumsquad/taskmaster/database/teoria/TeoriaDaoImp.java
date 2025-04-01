package com.scrumsquad.taskmaster.database.teoria;

import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TeoriaDaoImp implements TeoriaDao {
    @Override
    public String getTeoria(int tema) {
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
            } else {
                result = "-1";
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }
}
