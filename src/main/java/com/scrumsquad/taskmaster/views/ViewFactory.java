package com.scrumsquad.taskmaster.views;

import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.auth.LoginView;
import com.scrumsquad.taskmaster.views.auth.RegisterView;
import com.scrumsquad.taskmaster.views.student.MainMenuView;
import com.scrumsquad.taskmaster.views.student.StudentView;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.ConceptMatchingView;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.TopicsConceptMatchingView;
import com.scrumsquad.taskmaster.views.student.theory.TopicsTheoryView;

public class ViewFactory {
    private ViewFactory() {
    }

    public static View getView(String route) {
        return switch (route) {
            case ViewRoutes.login -> new LoginView();
            case ViewRoutes.register -> new RegisterView();
            case ViewRoutes.student -> new StudentView();
            case ViewRoutes.conceptMatching -> new ConceptMatchingView();
            case ViewRoutes.topicsConceptMatching -> new TopicsConceptMatchingView();
            case ViewRoutes.topicsTheory -> new TopicsTheoryView();
            case ViewRoutes.mainMenu -> new MainMenuView();
            default -> null;
        };
    }

}
