package battleship;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Scoreboard {

    private static final String FILE_PATH = "data/scoreboard.csv";

    public static void saveScore(String nameOfPlayer, int score, int shots) {
        File file = new File(FILE_PATH);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        boolean fileExists = file.exists() && file.length() > 0;

        try (FileWriter writer = new FileWriter(file, true);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            if (!fileExists) {
                csvPrinter.printRecord("Player", "Score", "Shots");
            }

            csvPrinter.printRecord(nameOfPlayer, score, shots);
            csvPrinter.flush();
            System.out.println("Pontuação guardada com sucesso!");

        } catch (IOException e) {
            System.err.println("Erro ao guardar no scoreboard: " + e.getMessage());
        }
    }

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

            System.out.println(SCOREBOARD_TITLE());
            System.out.printf("%-15s %-10s %-10s%n", "JOGADOR", "PONTOS", "TIROS");
            System.out.println("------------------------------------");

            printScoreboardRecords(csvParser);
            System.out.println("------------------------------------\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler o scoreboard: " + e.getMessage());
        }
    }

    private static @NotNull String SCOREBOARD_TITLE() {
        return "\n=== BATTLESHIP SCOREBOARD ===";
    }

    private static void printScoreboardRecords(CSVParser csvParser) {
        for (CSVRecord record : csvParser) {

            System.out.printf("%-15s %-10s %-10s%n", record.get("Player"), record.get("Score"), record.get("Shots"));
        }
    }

}
