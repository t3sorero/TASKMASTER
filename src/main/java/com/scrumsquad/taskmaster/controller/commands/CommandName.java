package com.scrumsquad.taskmaster.controller.commands;

public class CommandName {
    private CommandName() {
    }

    public static final String login = "login";
    public static final String loginOk = "loginOk";
    public static final String loginKo = "loginKo";

    public static final String register = "register";
    public static final String registerOk = "registerOk";
    public static final String registerKo = "registerKo";

    // Nuevo comando para el juego de relación de conceptos
    public static final String conceptMatching = "conceptMatching";
    public static final String conceptMatchingOk = "conceptMatchingOk";
    public static final String conceptMatchingKo = "conceptMatchingKo";
    public static final String conceptMatchingGetData = "conceptMatchingGetData";
    public static final String conceptMatchingGetDataOk = "conceptMatchingGetDataOk";
    public static final String conceptMatchingGetDataKo = "conceptMatchingGetDataKo";
}
