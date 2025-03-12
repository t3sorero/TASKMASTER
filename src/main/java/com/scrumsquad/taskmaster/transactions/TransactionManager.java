package com.scrumsquad.taskmaster.transactions;

public abstract class TransactionManager {

    private static TransactionManager instance;

    public abstract Transaction nuevaTransaccion() throws Exception;

    public abstract Transaction getTransaccion() throws Exception;

    public abstract void eliminaTransaccion() throws Exception;

    public static synchronized TransactionManager getInstance() {
        if (instance == null)
            instance = new TransactionManagerImp();
        return instance;
    }
}