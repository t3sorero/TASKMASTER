package com.scrumsquad.taskmaster.transactions;

public class TransactionFactoryImp extends TransactionFactory {

    public Transaction newTransaction() {
        return new TransactionImp();
    }
}