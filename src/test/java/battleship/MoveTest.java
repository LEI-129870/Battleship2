package battleship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

    private Move move;
    private List<IPosition> shots;
    private List<IGame.ShotResult> results;

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        results = new ArrayList<>();
        move = new Move(1, shots, results);
    }

    @Test
    @DisplayName("Valida o construtor, getters e representação textual (toString)")
    void testGettersAndToString() {
        assertEquals(1, move.getNumber(), "Error: Move number should be 1.");
        assertEquals(shots, move.getShots(), "Error: Shots list should match.");
        assertEquals(results, move.getShotResults(), "Error: Shot results list should match.");
        assertNotNull(move.toString(), "Error: toString should not return null.");
    }

    @Test
    @DisplayName("Valida o processamento de fogo inimigo (verbose false e true)")
    void testProcessEnemyFireAllBranches() {
        // Testar com listas vazias / comportamento base
        String jsonResultFalse = move.processEnemyFire(false);
        assertNotNull(jsonResultFalse, "Error: JSON result should not be null for verbose=false.");

        String jsonResultTrue = move.processEnemyFire(true);
        assertNotNull(jsonResultTrue, "Error: JSON result should not be null for verbose=true.");
    }
}