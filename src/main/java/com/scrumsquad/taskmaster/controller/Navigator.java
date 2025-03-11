package com.scrumsquad.taskmaster.controller;

import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.ViewFactory;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Navigator {

    private static Map<AppController.AppFrame, Navigator> navigators = new HashMap<>();
    private static AppController.AppFrame focusedWindow;

    public static void addFrame(AppController.AppFrame frame, String route) {
        if (navigators.containsKey(frame)) return;
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                focusedWindow = frame;
                super.windowGainedFocus(e);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                navigators.remove(frame);
                focusedWindow = null;
                super.windowClosed(e);
            }
        });
        focusedWindow = frame;
        Navigator navigator = new Navigator(frame);
        navigators.put(frame, navigator);
        navigator.to(route);
    }

    public static Navigator getNavigator() {
        return navigators.get(focusedWindow);
    }

    private final AppController.AppFrame frame;
    private final Stack<NavigatorRoute> routesStack;

    private Navigator(AppController.AppFrame frame) {
        routesStack = new Stack<>();
        navigators = new HashMap<>();
        this.frame = frame;
    }

    /**
     * Elimina todas las vistas de la ventana y añade la vista route
     *
     * @param route
     */
    public void offTo(String route) {
        offTo(route, null, null);
    }

    /**
     * Elimina todas las vistas de la ventana y añade la vista route
     *
     * @param route
     * @param arguments
     */
    public void offTo(String route, Map<String, Object> arguments) {
        offTo(route, arguments, null);
    }

    /**
     * Elimina todas las vistas de la ventana y añade la vista route
     *
     * @param route
     * @param onPop
     */
    public void offTo(String route, NavigatorOnPopListener onPop) {
        offTo(route, null, onPop);
    }

    /**
     * Elimina todas las vistas de la ventana y añade la vista route
     *
     * @param route
     * @param arguments
     * @param onPop
     */
    public void offTo(String route, Map<String, Object> arguments, NavigatorOnPopListener onPop) {
        View view = ViewFactory.getView(route);
        if (view == null) {
            System.err.println("View \"" + route + "\" not found");
            return;
        }
        JPanel viewBuilt = view.build(new View.BuildOptions(frame, arguments));
        routesStack.clear();
        routesStack.push(new NavigatorRoute(route, view, onPop));
        frame.clearViews();
        frame.pushView(view, viewBuilt);
    }

    public void to(String route) {
        to(route, null, null);
    }

    public void to(String route, Map<String, Object> arguments) {
        to(route, arguments, null);
    }

    public void to(String route, NavigatorOnPopListener onPop) {
        to(route, null, onPop);
    }

    public void to(String route, Map<String, Object> arguments, NavigatorOnPopListener onPop) {
        View view = ViewFactory.getView(route);
        if (view == null) {
            System.err.println("View \"" + route + "\" not found");
            return;
        }
        JPanel viewBuilt = view.build(new View.BuildOptions(frame, arguments));
        routesStack.push(new NavigatorRoute(route, view, onPop));
        frame.pushView(view, viewBuilt);
    }

    public void back() {
        back(null);
    }

    public void back(Map<String, Object> arguments) {
        if (routesStack.isEmpty()) {
            System.err.println("Navigator stack is empty");
            return;
        }
        NavigatorOnPopListener onPop = routesStack.pop().onPop;
        frame.popView();
        if (onPop != null) {
            onPop.onPop(arguments);
        }
    }

    public interface NavigatorOnPopListener {
        void onPop(Map<String, Object> args);
    }

    private record NavigatorRoute(String route, View view, NavigatorOnPopListener onPop) {
    }

}
