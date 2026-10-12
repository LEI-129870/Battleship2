package battleship;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Scoreboard {

    private static final String FILE_PATH = "data/scoreboard.csv";

    // Adiciona uma nova pontuação ao ficheiro CSV
    public static void saveScore(String playerName, int score, int shots) {
        File file = new File(FILE_PATH);

        // Cria a pasta 'data' se ainda não existir
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        boolean fileExists = file.exists() && file.length() > 0;

        try (FileWriter writer = new FileWriter(file, true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Escreve o cabeçalho se o ficheiro for novo
            if (!fileExists) {
                csvPrinter.printRecord("Player", "Score", "Shots");
            }

            // Regista os dados da partida
            csvPrinter.printRecord(playerName, score, shots);
            csvPrinter.flush();
            System.out.println("Pontuação guardada com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao guardar no scoreboard: " + e.getMessage());
        }
    }

    // Le o ficheiro CSV e imprime a tabela de resultados na consola
    public static void showScoreboard() {
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            System.out.println("\nAinda não existem pontuações registadas.");
            return;
        }

        try (FileReader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .build())) {

            System.out.println("\n=== BATTLESHIP SCOREBOARD ===");
            System.out.printf("%-15s %-10s %-10s%n", "JOGADOR", "PONTOS", "TIROS");
            System.out.println("------------------------------------");

            for (CSVRecord record : csvParser) {
                String player = record.get("Player");
                String score = record.get("Score");
                String shots = record.get("Shots");

                System.out.printf("%-15s %-10s %-10s%n", player, score, shots);
            }
            System.out.println("------------------------------------\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler o scoreboard: " + e.getMessage());
        }
    }
}
