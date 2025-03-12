package com.scrumsquad.taskmaster.lib.transactions;

import java.util.concurrent.ConcurrentHashMap;

public class TransactionManagerImp extends TransactionManager {

    private ConcurrentHashMap<Thread, Transaction> map;

    public TransactionManagerImp() {
        map = new ConcurrentHashMap<Thread, Transaction>();
    }

    public Transaction nuevaTransaccion()  {
        Transaction transaction = TransactionFactory.getInstance().newTransaction();
        if (map.contains(Thread.currentThread())) {
            throw new RuntimeException("Ya hay un hilo con una transaccion en curso");
        }

        map.put(Thread.currentThread(), transaction);

        return transaction;
    }

    public Transaction getTransaccion()  {

        Thread currentThread = Thread.currentThread();
        Transaction transaction = map.get(currentThread);

        if (transaction == null)
            System.out.println("TransactionManagerImp: No hay transaccion activa");

        return transaction;
    }

    public void eliminaTransaccion()  {

        Thread currentThread = Thread.currentThread();
        Transaction transaction = map.get(currentThread);

        if (transaction != null) {
            map.remove(currentThread);
        } else {
            System.out.println("TransactionManagerImp: Fallo al borrar transaccion");
        }
    }
}
