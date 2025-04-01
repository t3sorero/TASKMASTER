package com.scrumsquad.taskmaster.lib.transactions;

public abstract class TransactionManager {

    private static TransactionManager instance;

    public abstract Transaction nuevaTransaccion();

    public abstract Transaction getTransaccion() throws Exception;

    public abstract void eliminaTransaccion() throws Exception;

    public static synchronized TransactionManager getInstance() {
        if (instance == null)
            instance = new TransactionManagerImp();
        return instance;
    }
}