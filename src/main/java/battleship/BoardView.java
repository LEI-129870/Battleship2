package battleship;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Representação gráfica do Tabuleiro da Batalha Naval utilizando JavaFX.
 */
public class BoardView extends Application {

    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 40;
    private final Rectangle[][] cells = new Rectangle[GRID_SIZE][GRID_SIZE];

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(2);
        grid.setVgap(2);

        // Cabeçalho das Colunas (1 a 10)
        for (int col = 0; col < GRID_SIZE; col++) {
            Label colLabel = new Label(String.valueOf(col + 1));
            colLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            grid.add(colLabel, col + 1, 0);
            GridPane.setHalignment(colLabel, javafx.geometry.HPos.CENTER);
        }

        // Cabeçalho das Linhas (A a J) e Células do Tabuleiro
        for (int row = 0; row < GRID_SIZE; row++) {
            // Letra da Linha (A = 65 em ASCII)
            Label rowLabel = new Label(String.valueOf((char) ('A' + row)));
            rowLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            grid.add(rowLabel, 0, row + 1);
            GridPane.setHalignment(rowLabel, javafx.geometry.HPos.CENTER);

            for (int col = 0; col < GRID_SIZE; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                cell.setFill(Color.LIGHTBLUE); // Cor inicial (Água desconhecida)
                cell.setStroke(Color.DARKBLUE);

                cells[row][col] = cell;

                StackPane cellPane = new StackPane(cell);

                // Exemplo de interação ao clicar na célula
                final int r = row;
                final int c = col;
                cellPane.setOnMouseClicked(e -> handleCellClick(r, c));

                grid.add(cellPane, col + 1, row + 1);
            }
        }

        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setTitle("Batalha Naval - Visualização do Tabuleiro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Trata o clique numa posição do tabuleiro.
     */
    private void handleCellClick(int row, int col) {
        char rowChar = (char) ('A' + row);
        int colNum = col + 1;
        System.out.println("Célula selecionada: " + rowChar + colNum);
    }

    /**
     * Atualiza a cor de uma célula específica com base no estado do tiro.
     * @param row Índice da linha (0 a 9)
     * @param col Índice da coluna (0 a 9)
     * @param status "MISS" (água), "HIT" (tiro), "SUNK" (afundado)
     */
    public void updateCell(int row, int col, String status) {
        if (row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) return;

        switch (status.toUpperCase()) {
            case "MISS":
                cells[row][col].setFill(Color.GRAY); // Água / Tiro falhado
                break;
            case "HIT":
                cells[row][col].setFill(Color.ORANGE); // Tiro certeiro
                break;
            case "SUNK":
                cells[row][col].setFill(Color.RED); // Navio afundado
                break;
            default:
                cells[row][col].setFill(Color.LIGHTBLUE);
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
