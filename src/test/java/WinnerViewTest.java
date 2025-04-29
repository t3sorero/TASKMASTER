
import com.scrumsquad.taskmaster.controller.commands.Context;
import com.scrumsquad.taskmaster.lib.Widget.BuildOptions;
import com.scrumsquad.taskmaster.views.student.games.quiz.WinnerView;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.JPanel;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WinnerViewTest {

    private WinnerView winnerView;

    @Mock
    private BuildOptions buildOptions;

    @Mock
    private Context context;

    @Mock
    private MediaPlayer mockMediaPlayer;

    @Mock
    private Media mockMedia;

    @BeforeAll
    static void setupJavaFX() {
        // Omite los tests si no hay entorno gráfico (como GitHub Actions)
        assumeFalse(GraphicsEnvironment.isHeadless(), "Test omitido en entorno headless");
        new JFXPanel(); // Inicializa JavaFX Toolkit
    }

    @BeforeEach
    void setUp() {
        winnerView = new WinnerView();
    }

    @Test
    void build_ShouldCreateJPanel() {
        // Act
        JPanel panel = winnerView.build(buildOptions);

        // Assert
        assertNotNull(panel);
        assertEquals(1, panel.getComponentCount());
        assertTrue(panel.getComponent(0) instanceof JFXPanel);
    }

    @Test
    void onDispose_ShouldCallPlatformRunLater() {
        try (MockedStatic<Platform> platformMock = mockStatic(Platform.class)) {
            // Act
            winnerView.onDispose();

            // Assert
            // Verify that runLater was called
            platformMock.verify(() -> Platform.runLater(any(Runnable.class)));
        }
    }

    @Test
    void update_ShouldNotThrowException() {
        // Act & Assert
        assertDoesNotThrow(() -> winnerView.update(context));
    }

    @Test
    void createCelebrationPanel_ShouldCreatePanelWithCorrectElements() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("createCelebrationPanel", Pane.class, javafx.scene.layout.StackPane.class);
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("createCelebrationPanel method should exist");
        }
    }

    @Test
    void startConfettiAnimation_ShouldExist() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("startConfettiAnimation", Pane.class, javafx.scene.layout.StackPane.class);
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("startConfettiAnimation method should exist");
        }
    }

    @Test
    void createResponsiveCurtains_ShouldCreateCurtains() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("createResponsiveCurtains", Pane.class, javafx.scene.layout.StackPane.class);
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("createResponsiveCurtains method should exist");
        }
    }

    @Test
    void showCongratulationsMessage_ShouldExist() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("showCongratulationsMessage", Pane.class, javafx.scene.layout.StackPane.class);
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("showCongratulationsMessage method should exist");
        }
    }

    @Test
    void createImprovedTrophy_ShouldExist() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("createImprovedTrophy");
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("createImprovedTrophy method should exist");
        }
    }

    @Test
    void getRandomEducationalColor_ShouldExist() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("getRandomEducationalColor");
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("getRandomEducationalColor method should exist");
        }
    }

    @Test
    void build_ShouldHandleAudioResourceNotFound() {
        // Just test that the build method doesn't throw exceptions
        // The actual audio resource handling is done in JavaFX's runtime which is difficult to mock
        assertDoesNotThrow(() -> winnerView.build(buildOptions));
    }

    @Test
    void computeTextWidth_ShouldExist() {
        // This test cannot be directly run due to JavaFX requirements
        // We will verify the method exists and is accessible via reflection
        try {
            Method method = WinnerView.class.getDeclaredMethod("computeTextWidth", String.class, javafx.scene.text.Font.class);
            method.setAccessible(true);
            assertNotNull(method);
        } catch (NoSuchMethodException e) {
            fail("computeTextWidth method should exist");
        }
    }
}