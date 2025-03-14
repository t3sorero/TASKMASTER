package com.scrumsquad.taskmaster.lib.transactions;

import com.scrumsquad.taskmaster.database.DBData;

import java.sql.Connection;
import java.sql.DriverManager;

public class TransactionImp implements Transaction {

    private Connection connection;

    public void commit() throws Exception {

        if (connection == null) return;
        try {
            connection.commit();
            connection.close();
            TransactionManager.getInstance().eliminaTransaccion();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
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
        // begin-user-code
        if (connection == null) return;
        try {
            connection.rollback();
            connection.close();
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