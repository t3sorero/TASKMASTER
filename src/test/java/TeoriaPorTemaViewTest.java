import com.scrumsquad.taskmaster.controller.AppController;
import com.scrumsquad.taskmaster.controller.commands.CommandName;
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.Widget;
import com.scrumsquad.taskmaster.views.student.teoria.TeoriaPorTemaView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.swing.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeoriaPorTemaViewTest {
    TeoriaPorTemaView view;

    @BeforeEach
    void setUp() {
        view = new TeoriaPorTemaView();
        Widget.BuildOptions options = new Widget.BuildOptions(null, Map.of("tema", 1));
        view.build(options);
    }

    @Test
    void testBuild() {
        Widget.BuildOptions options = new Widget.BuildOptions(null, Map.of("tema", 1));
        JPanel panel = view.build(options);
        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }
}
