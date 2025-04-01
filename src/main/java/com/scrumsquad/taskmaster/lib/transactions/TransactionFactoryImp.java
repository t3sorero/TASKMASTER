package com.scrumsquad.taskmaster.lib.transactions;

public class TransactionFactoryImp extends TransactionFactory {

    public Transaction newTransaction() {
        return new TransactionImp();
    }
}