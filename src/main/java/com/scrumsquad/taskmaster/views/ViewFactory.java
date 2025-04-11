package com.scrumsquad.taskmaster.views;

import com.scrumsquad.taskmaster.lib.View;
import com.scrumsquad.taskmaster.views.auth.LoginView;
import com.scrumsquad.taskmaster.views.student.GameSelectionView;
import com.scrumsquad.taskmaster.views.student.MainMenuView;
import com.scrumsquad.taskmaster.views.student.StudentView;
import com.scrumsquad.taskmaster.views.student.games.conceptmatching.ConceptMatchingView;
import com.scrumsquad.taskmaster.views.student.TopicsView;
import com.scrumsquad.taskmaster.views.student.practicaltest.PracticalTestView;
import com.scrumsquad.taskmaster.views.student.teoria.TopicsTheoryView;
import com.scrumsquad.taskmaster.views.student.teoria.TeoriaPorTemaView;

public class ViewFactory {
    private ViewFactory() {
    }

    public static View getView(String route) {
        return switch (route) {
            case ViewRoutes.login -> new LoginView();
            case ViewRoutes.student -> new StudentView();
            case ViewRoutes.conceptMatching -> new ConceptMatchingView();
            case ViewRoutes.topics -> new TopicsView(); //temas para juegos practicos
            case ViewRoutes.topicsTheory -> new TopicsTheoryView();
            case ViewRoutes.mainMenu -> new MainMenuView();
            case ViewRoutes.teoria -> new TeoriaPorTemaView();
            case ViewRoutes.practicalTest -> new PracticalTestView();
            case ViewRoutes.gameSelection -> new GameSelectionView();
            default -> null;
        };
    }

}
