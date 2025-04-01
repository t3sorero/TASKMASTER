package com.scrumsquad.taskmaster.services.login;

public abstract class LoginService {
    private static LoginService instance;

    public static LoginService getInstance() {
        if (instance == null) {
            instance = new LoginServiceImp();
        }
        return instance;
    }

    public abstract boolean loginValidation(String email, String password) throws Exception;
}
