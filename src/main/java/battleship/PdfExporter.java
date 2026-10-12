
package battleship;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.util.List;

public class PdfExporter {

    public static void exportarJogadas(
            List<IMove> jogadas,
            String nomeFicheiro) throws IOException {

        try (PDDocument documento = new PDDocument()) {

            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            PDPageContentStream conteudo =
                    new PDPageContentStream(documento, pagina);

            float y = 750;

            try {
                conteudo.beginText();
                conteudo.setFont(
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                        12
                );
                conteudo.newLineAtOffset(50, y);

                // Titulo do documento
                conteudo.showText("Historico de Jogadas - Battleship");
                conteudo.newLineAtOffset(0, -30);
                y -= 30;

                // Cada jogada vai ter um nº de disparos
                for (IMove jogada : jogadas) {

                    // Ve se a pagina ja ta a acabar e cria outra
                    // o documento é o mm muda se é a pagina e o conteudo
                    if (y < 50) {
                        conteudo.endText();
                        conteudo.close();

                        pagina = new PDPage();
                        documento.addPage(pagina);

                        conteudo = new PDPageContentStream(documento, pagina);
                        conteudo.beginText();
                        conteudo.setFont(
                                new PDType1Font(
                                        Standard14Fonts.FontName.HELVETICA
                                ),
                                12
                        );
                        conteudo.newLineAtOffset(50, 750);
                        y = 750;
                    }

                    conteudo.showText("Jogada " + jogada.getNumber());
                    conteudo.newLineAtOffset(0, -20);
                    y -= 20;

                    // agora vê se os disparos da jogada que está a ser verificada
                    for (int i = 0; i < jogada.getShots().size(); i++) {

                        // mm coisa que no ciclo exterior
                        if (y < 50) {
                            conteudo.endText();
                            conteudo.close();

                            pagina = new PDPage();
                            documento.addPage(pagina);

                            conteudo = new PDPageContentStream(
                                    documento, pagina
                            );

                            conteudo.beginText();
                            conteudo.setFont(
                                    new PDType1Font(
                                            Standard14Fonts.FontName.HELVETICA
                                    ),
                                    12
                            );
                            conteudo.newLineAtOffset(50, 750);
                            y = 750;
                        }

                        IPosition disparo = jogada.getShots().get(i);

                        // Converter a posicao para coordenadas
                        String coordenada =
                                "" + disparo.getClassicRow()
                                        + disparo.getClassicColumn();

                        // Obter o resultado do disparo
                        String resultado = "Sem resultado";

                        if (i < jogada.getShotResults().size()) {
                            resultado = obterResultado(
                                    jogada.getShotResults().get(i)
                            );
                        }


                        conteudo.showText(
                                "Disparo: " + coordenada + " - " + resultado
                        );

                        conteudo.newLineAtOffset(0, -20);
                        y -= 20;
                    }

                    conteudo.newLineAtOffset(0, -20);
                    y -= 20;
                }

                conteudo.endText();

            } finally {
                conteudo.close();
            }

            // Guardar o ficheiro com o nome dado
            documento.save(nomeFicheiro);
        }
    }

    // Converter o resultado do disparo para texto
    private static String obterResultado(IGame.ShotResult resultado) {

        if (!resultado.valid()) {
            return "Disparo invalido";
        }

        if (resultado.repeated()) {
            return "Disparo repetido";
        }

        if (resultado.ship() == null) {
            return "Agua";
        }

        if (resultado.sunk()) {
            return "Navio afundado";
        }

        return "Acertou";
    }
}
