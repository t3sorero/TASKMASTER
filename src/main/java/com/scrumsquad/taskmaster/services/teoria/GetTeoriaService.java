package com.scrumsquad.taskmaster.services.teoria;

public abstract class GetTeoriaService {

    private static GetTeoriaService instance;

    public static synchronized GetTeoriaService getInstance() {
        if (instance == null) {
            instance = new GetTeoriaServiceImp();
        }
        return instance;
    }

    public abstract String getTeoria(int tema) throws Exception;
}
