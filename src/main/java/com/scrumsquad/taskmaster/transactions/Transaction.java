package com.scrumsquad.taskmaster.transactions;

//import java.sql.SQLException;

public interface Transaction {

    public void commit() throws Exception;

    public void start();

    public void rollback();

    public Object getResource();
}