
package battleship;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PdfExporter.
 *
 * Cyclomatic complexity:
 * exportarJogadas: loops and pagination conditions
 * obterResultado: 5 possible outcomes
 */
@DisplayName("Tests for the PdfExporter class")
class PdfExporterTest {

    private File criarFicheiro() throws IOException {
        return File.createTempFile("battleship-test-", ".pdf");
    }

    private String lerTexto(File ficheiro) throws IOException {
        try (PDDocument documento = Loader.loadPDF(ficheiro)) {
            return new PDFTextStripper().getText(documento);
        }
    }

    private IMove criarJogada(
            int numero,
            List<IPosition> disparos,
            List<IGame.ShotResult> resultados) {

        return new Move(numero, disparos, resultados);
    }

    @Test
    @DisplayName("Export an empty list of moves")
    void exportarJogadas1() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            PdfExporter.exportarJogadas(
                    Collections.emptyList(),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(ficheiro.exists());
            assertTrue(ficheiro.length() > 0);

            try (PDDocument documento = Loader.loadPDF(ficheiro)) {
                assertEquals(1, documento.getNumberOfPages());
            }

            assertTrue(lerTexto(ficheiro)
                    .contains("Historico de Jogadas - Battleship"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a move without shots")
    void exportarJogadas2() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IMove jogada = criarJogada(
                    1,
                    Collections.emptyList(),
                    Collections.emptyList()
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            String texto = lerTexto(ficheiro);

            assertTrue(texto.contains("Jogada 1"));
            assertFalse(texto.contains("Disparo:"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a shot without result")
    void exportarJogadas3() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IMove jogada = criarJogada(
                    2,
                    List.of(new Position(5, 5)),
                    Collections.emptyList()
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            String texto = lerTexto(ficheiro);

            assertTrue(texto.contains("Jogada 2"));
            assertTrue(texto.contains("Sem resultado"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export an invalid shot")
    void exportarJogadas4() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IGame.ShotResult resultado =
                    new IGame.ShotResult(false, false, null, false);

            IMove jogada = criarJogada(
                    3,
                    List.of(new Position(5, 5)),
                    List.of(resultado)
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(lerTexto(ficheiro)
                    .contains("Disparo invalido"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a repeated shot")
    void exportarJogadas5() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IGame.ShotResult resultado =
                    new IGame.ShotResult(true, true, null, false);

            IMove jogada = criarJogada(
                    4,
                    List.of(new Position(5, 5)),
                    List.of(resultado)
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(lerTexto(ficheiro)
                    .contains("Disparo repetido"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a shot that hits water")
    void exportarJogadas6() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IGame.ShotResult resultado =
                    new IGame.ShotResult(true, false, null, false);

            IMove jogada = criarJogada(
                    5,
                    List.of(new Position(5, 5)),
                    List.of(resultado)
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(lerTexto(ficheiro).contains("Agua"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a shot that sinks a ship")
    void exportarJogadas7() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IShip navio =
                    new Caravel(Compass.NORTH, new Position(5, 5));

            IGame.ShotResult resultado =
                    new IGame.ShotResult(true, false, navio, true);

            IMove jogada = criarJogada(
                    6,
                    List.of(new Position(5, 5)),
                    List.of(resultado)
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(lerTexto(ficheiro)
                    .contains("Navio afundado"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export a shot that hits a ship")
    void exportarJogadas8() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            IShip navio =
                    new Caravel(Compass.NORTH, new Position(5, 5));

            IGame.ShotResult resultado =
                    new IGame.ShotResult(true, false, navio, false);

            IMove jogada = criarJogada(
                    7,
                    List.of(new Position(5, 5)),
                    List.of(resultado)
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            assertTrue(lerTexto(ficheiro).contains("Acertou"));

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export many shots across multiple pages")
    void exportarJogadas9() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            List<IPosition> disparos = new ArrayList<>();

            for (int i = 0; i < 80; i++) {
                disparos.add(new Position(5, 5));
            }

            IMove jogada = criarJogada(
                    8,
                    disparos,
                    Collections.emptyList()
            );

            PdfExporter.exportarJogadas(
                    List.of(jogada),
                    ficheiro.getAbsolutePath()
            );

            try (PDDocument documento = Loader.loadPDF(ficheiro)) {
                assertTrue(documento.getNumberOfPages() > 1);
            }

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export many moves across multiple pages")
    void exportarJogadas10() throws IOException {
        File ficheiro = criarFicheiro();

        try {
            List<IMove> jogadas = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                jogadas.add(criarJogada(
                        i + 1,
                        Collections.emptyList(),
                        Collections.emptyList()
                ));
            }

            PdfExporter.exportarJogadas(
                    jogadas,
                    ficheiro.getAbsolutePath()
            );

            try (PDDocument documento = Loader.loadPDF(ficheiro)) {
                assertTrue(documento.getNumberOfPages() > 1);
            }

        } finally {
            Files.deleteIfExists(ficheiro.toPath());
        }
    }

    @Test
    @DisplayName("Export to an invalid file path")
    void exportarJogadas11() {
        File diretorio = new File(
                "diretorio-inexistente-" + System.nanoTime()
        );

        assertThrows(IOException.class, () ->
                PdfExporter.exportarJogadas(
                        Collections.emptyList(),
                        new File(diretorio, "teste.pdf").getPath()
                )
        );
    }

    @Test
    @DisplayName("Test PdfExporter constructor")
    void constructorTest() {
        PdfExporter exporter = new PdfExporter();
        assertNotNull(exporter);
    }
}
