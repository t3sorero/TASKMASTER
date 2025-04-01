package com.scrumsquad.taskmaster.lib.transactions;

//import java.sql.SQLException;

import java.sql.Connection;

public interface Transaction {

    public void commit() throws Exception;

    public void start();

    public void rollback();

    public Connection getResource();
}