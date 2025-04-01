package com.scrumsquad.taskmaster.lib.transactions;

import com.scrumsquad.taskmaster.database.DBData;

import java.sql.Connection;
import java.sql.DriverManager;

public class TransactionImp implements Transaction {

    private Connection connection;

    public void commit() {

        Connection con = getResource();
        if (con == null) return;
        try {
            con.commit();
            con.close();
            TransactionManager.getInstance().eliminaTransaccion();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void start() {
        try {
            connection = DriverManager.getConnection(DBData.DB_URL, DBData.DB_USER, DBData.DB_PASSWORD);
            connection.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void rollback() {
        Connection con = getResource();
        if (con == null) return;
        try {
            con.rollback();
            con.close();
            TransactionManager.getInstance().eliminaTransaccion();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public Connection getResource() {
        return connection;
    }
}