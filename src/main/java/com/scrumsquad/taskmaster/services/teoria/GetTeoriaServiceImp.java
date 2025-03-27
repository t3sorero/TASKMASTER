package com.scrumsquad.taskmaster.services.teoria;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

public class GetTeoriaServiceImp extends GetTeoriaService{
    @Override
    public String getTeoria(int tema) throws Exception {
        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try {
            t.start();
            var daoTeoria = DAOFactory.getTeoriaDAO();
            String teoria = daoTeoria.getTeoria(tema);
            t.commit();
            return teoria;
        }catch(Exception e) {
            t.rollback();
            throw e;
        }
    }
}
