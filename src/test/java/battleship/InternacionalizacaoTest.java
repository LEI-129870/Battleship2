package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for class Internacionalizacao.
 * Author: 129849
 * Date: 2026-11-20 13:40
 * Cyclomatic Complexity:
 * - constructor (default): 1
 * - static initializer: 1
 * - setLanguage(String): 1
 * - calcularNumeros() [private]: 1
 * - getMessage(String, Object...): 3
 * - getCurrentLocale(): 1
 */
@DisplayName("Testes unitários da classe Internacionalizacao")
class InternacionalizacaoTest {

    private Internacionalizacao internacionalizacao;

    @BeforeEach
    void setUp() {
        internacionalizacao = new Internacionalizacao();
        // O estado da classe é estático: garantir que cada teste começa em português
        Internacionalizacao.setLanguage("pt");
    }

    @AfterEach
    void tearDown() {
        // Repor o idioma por omissão para não afetar outras classes de teste (ex.: Game, Tasks)
        Internacionalizacao.setLanguage("pt");
        internacionalizacao = null;
    }

    // ---------------------------------------------------------------------
    // constructor / static initializer
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Construtor por omissão cria uma instância não nula")
    void constructor() {
        assertNotNull(internacionalizacao,
                "Error: expected a non-null Internacionalizacao instance but got null");
    }

    @Test
    @DisplayName("Inicializador estático define o português como idioma por omissão")
    void staticInitializer() throws Exception {
        // O bloco static já correu quando a classe foi carregada; verificamos o seu efeito
        Field messagesField = Internacionalizacao.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        ResourceBundle bundle = (ResourceBundle) messagesField.get(null);

        assertAll("Estado inicial em português",
                () -> assertNotNull(bundle,
                        "Error: expected a loaded ResourceBundle but got null"),
                () -> assertEquals("pt", Internacionalizacao.getCurrentLocale().getLanguage(),
                        "Error: expected default language 'pt' but got '"
                                + Internacionalizacao.getCurrentLocale().getLanguage() + "'"),
                () -> assertEquals("Bons ventos!", Internacionalizacao.getMessage("menu.goodbye"),
                        "Error: expected the Portuguese message 'Bons ventos!' but got '"
                                + Internacionalizacao.getMessage("menu.goodbye") + "'"));
    }

    // ---------------------------------------------------------------------
    // setLanguage(String) — CC = 1
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("setLanguage(\"en\") muda o locale e as mensagens para inglês")
    void setLanguage() {
        Internacionalizacao.setLanguage("en");

        assertAll("Mudança para inglês",
                () -> assertEquals(Locale.forLanguageTag("en"), Internacionalizacao.getCurrentLocale(),
                        "Error: expected locale 'en' but got '" + Internacionalizacao.getCurrentLocale() + "'"),
                () -> assertEquals("Fair winds!", Internacionalizacao.getMessage("menu.goodbye"),
                        "Error: expected the English message 'Fair winds!' but got '"
                                + Internacionalizacao.getMessage("menu.goodbye") + "'"));
    }

    @Test
    @DisplayName("setLanguage(null) lança NullPointerException")
    void setLanguageThrowsNullPointerException() {
        assertThrows(NullPointerException.class,
                () -> Internacionalizacao.setLanguage(null),
                "Error: expected NullPointerException for a null language code but none was thrown");
    }

    @Test
    @DisplayName("setLanguage com idioma sem ficheiro de mensagens lança MissingResourceException")
    void setLanguageThrowsMissingResourceException() {
        // Sem messages.properties base, o fallback é o Locale por omissão da JVM.
        // Fixamos um Locale por omissão sem bundle para o teste ser determinístico.
        Locale originalDefault = Locale.getDefault();
        try {
            Locale.setDefault(Locale.JAPAN);
            assertThrows(MissingResourceException.class,
                    () -> Internacionalizacao.setLanguage("de"),
                    "Error: expected MissingResourceException for language 'de' (no bundle) but none was thrown");
        } finally {
            Locale.setDefault(originalDefault);
        }
    }

    // ---------------------------------------------------------------------
    // calcularNumeros() [private] — CC = 1
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("calcularNumeros() (privado) devolve 10")
    void calcularNumeros() throws Exception {
        // Método privado e não utilizado: testado por reflexão apenas para cobertura.
        // Se for removido na Parte 2 (Safe Delete), este teste deve ser apagado também.
        Method method = Internacionalizacao.class.getDeclaredMethod("calcularNumeros");
        method.setAccessible(true);
        int result = (int) method.invoke(internacionalizacao);

        assertEquals(10, result, "Error: expected 10 but got " + result);
    }

    // ---------------------------------------------------------------------
    // getMessage(String, Object...) — CC = 3
    // if (messages == null || !messages.containsKey(key)) return key;
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("getMessage devolve a própria chave quando messages é null (1ª condição verdadeira)")
    void getMessage1() throws Exception {
        Field messagesField = Internacionalizacao.class.getDeclaredField("messages");
        messagesField.setAccessible(true);
        messagesField.set(null, null);

        String result = Internacionalizacao.getMessage("menu.goodbye");

        assertEquals("menu.goodbye", result,
                "Error: expected the key 'menu.goodbye' to be returned when messages is null but got '" + result + "'");
        // O @AfterEach volta a carregar o bundle com setLanguage("pt")
    }

    @Test
    @DisplayName("getMessage devolve a própria chave quando esta não existe (1ª falsa, 2ª verdadeira)")
    void getMessage2() {
        String result = Internacionalizacao.getMessage("chave.que.nao.existe");

        assertEquals("chave.que.nao.existe", result,
                "Error: expected the missing key 'chave.que.nao.existe' to be returned but got '" + result + "'");
    }

    @Test
    @DisplayName("getMessage devolve a mensagem formatada quando a chave existe (ambas as condições falsas)")
    void getMessage3() {
        String semArgumentos = Internacionalizacao.getMessage("menu.goodbye");
        String umArgumento = Internacionalizacao.getMessage("log.fleet.success", 5);
        String tresArgumentos = Internacionalizacao.getMessage("log.fleet.fail", "Galeao", "A1", "N");

        assertAll("Mensagens existentes e formatadas",
                () -> assertEquals("Bons ventos!", semArgumentos,
                        "Error: expected 'Bons ventos!' but got '" + semArgumentos + "'"),
                () -> assertEquals("5 navios adicionados com sucesso!", umArgumento,
                        "Error: expected '5 navios adicionados com sucesso!' but got '" + umArgumento + "'"),
                () -> assertTrue(tresArgumentos.endsWith("Galeao A1 N"),
                        "Error: expected message ending with 'Galeao A1 N' but got '" + tresArgumentos + "'"),
                () -> assertFalse(tresArgumentos.contains("{0}"),
                        "Error: expected placeholders to be replaced but got '" + tresArgumentos + "'"));
    }

    // ---------------------------------------------------------------------
    // getCurrentLocale() — CC = 1
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("getCurrentLocale devolve o locale definido por setLanguage")
    void getCurrentLocale() {
        Locale locale = Internacionalizacao.getCurrentLocale();

        assertAll("Locale atual",
                () -> assertNotNull(locale, "Error: expected a non-null Locale but got null"),
                () -> assertEquals(Locale.forLanguageTag("pt"), locale,
                        "Error: expected locale 'pt' but got '" + locale + "'"));
    }
}