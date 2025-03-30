package services.login;

import com.scrumsquad.taskmaster.database.DAOFactory;
import com.scrumsquad.taskmaster.lib.transactions.Transaction;
import com.scrumsquad.taskmaster.lib.transactions.TransactionManager;

public class LoginServiceImp extends LoginService {
    @Override
    public boolean loginValidation(String email, String password) throws Exception {
        Transaction t = TransactionManager.getInstance().nuevaTransaccion();
        try{
            t.start();
            var daoLogin = DAOFactory.getLoginDAO();
            var result = daoLogin.validCredentials(email, password);
            t.commit();
            return result;
        }
        catch (Exception e) {
            t.rollback();
            throw e;
        }
    }
}
