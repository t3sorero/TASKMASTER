package com.scrumsquad.taskmaster.database.login;

import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAOImp implements LoginDAO {

    @Override
    public boolean validCredentials(String email, String password) throws Exception{
        Boolean valid = false;
        String query = "SELECT * FROM public.usuarios where correo = ? and contraseña = crypt(?,contraseña);";
        Transaction transaction = TransactionManager.getInstance().getTransaccion();
        Connection con = transaction.getResource();
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            valid = true;
        }
        ps.close();
        rs.close();
        return valid;
    }
}
