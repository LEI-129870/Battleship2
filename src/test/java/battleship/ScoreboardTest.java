package battleship;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    @Test
    @DisplayName("Valida se o método saveScore guarda a pontuação corretamente sem lançar exceções")
    void saveScore() {
        assertDoesNotThrow(() -> {
            Scoreboard.saveScore("PlayerTest", 150, 12);
        }, "Error: saveScore should not throw an exception.");
    }

    @Test
    @DisplayName("Valida se o método showScoreboard lê e exibe as pontuações sem erros")
    void showScoreboard() {
        assertDoesNotThrow(() -> {
            Scoreboard.showScoreboard();
        }, "Error: showScoreboard should not throw an exception.");
    }
}