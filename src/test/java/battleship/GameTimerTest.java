package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameTimer.
 */
public class GameTimerTest {

    private GameTimer gameTimer;

    @BeforeEach
    void setUp() {
        gameTimer = new GameTimer();
    }

    @AfterEach
    void tearDown() {
        gameTimer = null;
    }

    @Test
    @DisplayName("Testa a instanciação correta do GameTimer")
    void testConstructor() {
        assertNotNull(gameTimer, "GameTimer instance should not be null.");
    }

    @Test
    @DisplayName("Testa o início e o fim de um turno do cronómetro sem erros")
    void testTurnLifecycle() {
        assertDoesNotThrow(() -> {
            gameTimer.startTurn();
            // Pequena pausa para simular o tempo decorrido num turno
            Thread.sleep(50);
            gameTimer.endTurn();
        }, "O fluxo de start e end do turno não deve lançar exceções.");
    }
}
