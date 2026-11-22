package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for interface IMove (método estático readMove).
 * Author: 129849
 * Date: 2026-11-21 23:45
 * Cyclomatic Complexity:
 * - readMove(int, Scanner): 2
 */
@DisplayName("Testes unitários da interface IMove")
class IMoveTest {

    private Scanner scanner;

    @BeforeEach
    void setUp() {
        // IMove é uma interface: o código a testar é o método estático readMove,
        // que lê do Scanner e devolve uma instância concreta (Move).
        scanner = null;
    }

    @AfterEach
    void tearDown() {
        if (scanner != null) {
            scanner.close();
        }
        scanner = null;
    }

    // ---------------- readMove(int, Scanner) — CC = 2 ----------------

    @Test
    @DisplayName("readMove com 0 tiros devolve uma jogada sem tiros (ciclo não executado)")
    void readMove1() {
        scanner = new Scanner("0");
        Move move = IMove.readMove(1, scanner);

        assertAll("Jogada sem tiros",
                () -> assertNotNull(move, "Error: expected a non-null Move but got null"),
                () -> assertEquals(1, move.getNumber(),
                        "Error: expected move number 1 but got " + move.getNumber()),
                () -> assertTrue(move.getShots().isEmpty(),
                        "Error: expected no shots but got " + move.getShots()),
                () -> assertTrue(move.getShotResults().isEmpty(),
                        "Error: expected no shot results but got " + move.getShotResults()));
    }

    @Test
    @DisplayName("readMove com 3 tiros lê as 3 posições pela ordem indicada (ciclo executado)")
    void readMove2() {
        scanner = new Scanner("3 1 2 3 4 5 6");
        Move move = IMove.readMove(7, scanner);

        assertAll("Jogada com 3 tiros",
                () -> assertEquals(7, move.getNumber(),
                        "Error: expected move number 7 but got " + move.getNumber()),
                () -> assertEquals(3, move.getShots().size(),
                        "Error: expected 3 shots but got " + move.getShots().size()),
                () -> assertEquals(new Position(1, 2), move.getShots().get(0),
                        "Error: expected first shot at (1,2) but got " + move.getShots().get(0)),
                () -> assertEquals(new Position(3, 4), move.getShots().get(1),
                        "Error: expected second shot at (3,4) but got " + move.getShots().get(1)),
                () -> assertEquals(new Position(5, 6), move.getShots().get(2),
                        "Error: expected third shot at (5,6) but got " + move.getShots().get(2)));
    }

    @Test
    @DisplayName("readMove lança InputMismatchException quando o número de tiros não é inteiro")
    void readMoveThrowsInputMismatchException() {
        scanner = new Scanner("abc");
        assertThrows(InputMismatchException.class, () -> IMove.readMove(1, scanner),
                "Error: expected InputMismatchException for non-numeric input but none was thrown");
    }

    @Test
    @DisplayName("readMove lança NoSuchElementException quando faltam coordenadas")
    void readMoveThrowsNoSuchElementException() {
        scanner = new Scanner("2 1 1");
        assertThrows(NoSuchElementException.class, () -> IMove.readMove(1, scanner),
                "Error: expected NoSuchElementException for missing coordinates but none was thrown");
    }
}