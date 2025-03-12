package com.scrumsquad.taskmaster.transactions;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.Thread;

public class TransactionManagerImp extends TransactionManager {

    private ConcurrentHashMap<Thread, Transaction> map;

    public TransactionManagerImp() {
        map = new ConcurrentHashMap<Thread, Transaction>();
    }

    public Transaction nuevaTransaccion() throws Exception {
        Transaction trans = null;
        Thread thread = Thread.currentThread();
        if(map==null) throw new Exception("TransactionManager no inicializado correctamente");
        if (map.get(thread) != null) {
            throw new Exception("Se esta intentando anidar transacciones");
        } else {
            trans = TransactionFactory.getInstance().newTransaction();
            map.put(thread, trans);
        }
        return trans;
    }

    public Transaction getTransaccion() throws Exception {

        Transaction trans = null;
        Thread thread = Thread.currentThread();
        if(map==null) throw new Exception("TransactionManager no inicializado correctamente");
        if (map.get(thread) == null) {
            throw new Exception("Transaccion inexistente");
        } else {
            trans = map.get(Thread.currentThread());
        }
        return trans;
    }

    public void eliminaTransaccion() throws Exception {

        Thread thread = Thread.currentThread();
        if(map==null) throw new Exception("TransactionManager no inicializado correctamente");
        if (map.get(thread) == null) {
            throw new Exception("Transaccion inexistente");
        }
    }
}
