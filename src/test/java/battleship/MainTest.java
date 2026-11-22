package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Main.
 * Author: 129849
 * Date: 2026-11-21 23:45
 * Cyclomatic Complexity:
 * - constructor (default): 1
 * - main(String[]): 1
 */
@DisplayName("Testes unitários da classe Main")
class MainTest {

    private Main main;
    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    void setUp() {
        main = new Main();
        originalIn = System.in;
        originalOut = System.out;
        capturedOut = new ByteArrayOutputStream();
        Internacionalizacao.setLanguage("pt");
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        main = null;
        capturedOut = null;
    }

    @Test
    @DisplayName("Construtor por omissão cria uma instância não nula")
    void constructor() {
        assertNotNull(main, "Error: expected a non-null Main instance but got null");
    }

    @Test
    @DisplayName("main imprime o título, mostra o menu e termina com o comando 'desisto'")
    void main() {
        // Simula o utilizador a escrever "desisto" logo no primeiro pedido do menu
        System.setIn(new ByteArrayInputStream("desisto\n".getBytes()));
        System.setOut(new PrintStream(capturedOut, true));

        assertDoesNotThrow(() -> Main.main(new String[0]),
                "Error: expected main to finish without exceptions but one was thrown");

        String out = capturedOut.toString();
        assertAll("Saída do programa",
                () -> assertTrue(out.contains("***  Battleship  ***"),
                        "Error: expected the title '***  Battleship  ***' in the output but got '" + out + "'"),
                () -> assertTrue(out.contains(Internacionalizacao.getMessage("menu.goodbye")),
                        "Error: expected the goodbye message in the output but got '" + out + "'"));
    }
}