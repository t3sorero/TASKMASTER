package com.scrumsquad.taskmaster.database.login;

public interface LoginDAO {
    public boolean validCredentials(String email, String password) throws Exception;
}
