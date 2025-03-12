package com.scrumsquad.taskmaster.transactions;;

public abstract class TransactionFactory {

	private static TransactionFactory instance;

	public static synchronized TransactionFactory getInstance() {
		if (instance == null)
			instance = new TransactionFactoryImp();
		return instance;
	}

	public abstract Transaction newTransaction();
}