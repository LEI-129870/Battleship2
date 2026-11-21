package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class TestComments.
 * Author: 129849
 * Date: 2026-11-21 23:45
 * Cyclomatic Complexity:
 * - constructor (default): 1
 */
@DisplayName("Testes unitários da classe TestComments")
class TestCommentsTest {

    private TestComments testComments;

    @BeforeEach
    void setUp() {
        testComments = new TestComments();
    }

    @AfterEach
    void tearDown() {
        testComments = null;
    }

    @Test
    @DisplayName("Construtor por omissão cria uma instância não nula")
    void constructor() {
        assertNotNull(testComments, "Error: expected a non-null TestComments instance but got null");
    }
}