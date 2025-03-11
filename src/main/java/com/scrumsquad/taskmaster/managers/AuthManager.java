package com.scrumsquad.taskmaster.managers;

public class AuthManager {

    private String token;

    public boolean isLoggedIn() {
        if (token == null) return false;
        // TODO DAO call
        return true;
    }

    public void save(String token) {
        this.token = token;
    }

    public void clear() {
        token = null;
    }
}
