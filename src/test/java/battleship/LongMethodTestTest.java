package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class LongMethodTest.
 * Author: 129849
 * Date: 2026-11-21 23:45
 * Cyclomatic Complexity:
 * - constructor (default): 1
 * - testarLongMethod(): 1
 * - primeiraParte() [private]: 1
 * - segundaParte() [private]: 1
 * - calcularNumeros(): 1
 * - calcularPontos() [private]: 1
 */
@DisplayName("Testes unitários da classe LongMethodTest")
class LongMethodTestTest {

    private LongMethodTest longMethodTest;
    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    void setUp() {
        longMethodTest = new LongMethodTest();
        originalOut = System.out;
        capturedOut = new ByteArrayOutputStream();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        longMethodTest = null;
        capturedOut = null;
    }

    /** Invoca um método privado sem argumentos por reflexão. */
    private Object invokePrivate(String name) throws Exception {
        Method method = LongMethodTest.class.getDeclaredMethod(name);
        method.setAccessible(true);
        return method.invoke(longMethodTest);
    }

    @Test
    @DisplayName("Construtor por omissão cria uma instância não nula")
    void constructor() {
        assertNotNull(longMethodTest, "Error: expected a non-null LongMethodTest instance but got null");
    }

    @Test
    @DisplayName("testarLongMethod imprime a soma de 1 a 34 (595)")
    void testarLongMethod() {
        System.setOut(new PrintStream(capturedOut, true));
        longMethodTest.testarLongMethod();

        String out = capturedOut.toString().trim();
        assertEquals("595", out, "Error: expected '595' to be printed but got '" + out + "'");
    }

    @Test
    @DisplayName("primeiraParte (privado) devolve a soma de 1 a 17 (153)")
    void primeiraParte() throws Exception {
        Object result = invokePrivate("primeiraParte");
        assertEquals(153, result, "Error: expected 153 but got " + result);
    }

    @Test
    @DisplayName("segundaParte (privado) devolve a soma de 18 a 34 (442)")
    void segundaParte() throws Exception {
        Object result = invokePrivate("segundaParte");
        assertEquals(442, result, "Error: expected 442 but got " + result);
    }

    @Test
    @DisplayName("calcularNumeros devolve 10")
    void calcularNumeros() {
        int result = longMethodTest.calcularNumeros();
        assertEquals(10, result, "Error: expected 10 but got " + result);
    }

    @Test
    @DisplayName("calcularPontos (privado) termina sem lançar exceções")
    void calcularPontos() {
        assertDoesNotThrow(() -> invokePrivate("calcularPontos"),
                "Error: expected calcularPontos to run without exceptions but one was thrown");
    }
}