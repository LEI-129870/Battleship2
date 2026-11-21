package battleship;

import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Game.
 */
public class GameTest {

	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(new Fleet());
	}

	@AfterEach
	void tearDown() {
		game = null;
	}

	@Test
	@DisplayName("Testa a inicialização correta do jogo e contadores a zero")
	void constructor() {
		assertNotNull(game, "Game instance should not be null after construction.");
		assertNotNull(game.getAlienMoves(), "Shots list should not be null after initialization.");
		assertNotNull(game.getMyMoves(), "My moves list should not be null after initialization.");
		assertNotNull(game.getAlienFleet(), "Alien fleet should not be null after initialization.");
		assertTrue(game.getAlienMoves().isEmpty(), "Shots list should be empty upon initialization.");
		assertTrue(game.getMyMoves().isEmpty(), "My moves list should be empty upon initialization.");
		assertEquals(0, game.getInvalidShots(), "Invalid shots count should be zero upon initialization.");
		assertEquals(0, game.getRepeatedShots(), "Repeated shots count should be zero upon initialization.");
		assertEquals(0, game.getHits(), "Hits count should be zero upon initialization.");
		assertEquals(0, game.getSunkShips(), "Sunk ships count should be zero upon initialization.");
	}

	@Test
	@DisplayName("Testa o incremento do contador de tiros inválidos (fora do tabuleiro)")
	void fire2() {
		Position invalidPosition = new Position(-1, 5);
		game.fireSingleShot(invalidPosition, false);
		assertEquals(1, game.getInvalidShots(), "Invalid shots counter should increase for an invalid shot.");
	}

	@Test
	@DisplayName("Testa o incremento do contador de tiros repetidos")
	void fire3() {
		Position position = new Position(2, 3);
		game.fireSingleShot(position, false);
		game.fireSingleShot(position, true);
		assertEquals(1, game.getRepeatedShots(), "Repeated shots counter should increase for a repeated shot.");
	}

	@Test
	@DisplayName("Testa o método repeatedShot quando a posição já foi atingida")
	void repeatedShot1() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		Position position = new Position(2, 3);
		assertTrue(game.repeatedShot(position), "Position (2,3) should be marked as repeated after firing.");
	}

	@Test
	@DisplayName("Testa o método repeatedShot quando a posição ainda não foi atingida")
	void repeatedShot2() {
		Position position = new Position(2, 3);
		assertFalse(game.repeatedShot(position), "Position (2,3) should not be marked as repeated before firing.");
	}

	@Test
	@DisplayName("Testa a listagem de movimentos alienígenas após disparar")
	void getAlienMoves() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		assertEquals(1, game.getAlienMoves().size(), "Shots list should contain one shot after firing once.");
	}

	@Test
	@DisplayName("Testa o cálculo do número de navios restantes na frota")
	void getRemainingShips() {
		IFleet fleet = game.getMyFleet();
		Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		Ship ship2 = new Frigate(Compass.EAST, new Position(5, 5));

		fleet.addShip(ship1);
		assertEquals(1, game.getRemainingShips(), "Just one ship was created!");
		fleet.addShip(ship2);
		assertEquals(2, game.getRemainingShips(), "Two ships were created!");
		ship2.sink();
		assertEquals(1, game.getRemainingShips(), "Remaining ships count should be 1 after sinking one of two ships.");
	}

	@Test
	@DisplayName("Testa um tiro bem-sucedido que atinge e afunda um navio (Hit & Sink)")
	void testFireSingleShot_HitAndSink() {
		IFleet fleet = game.getMyFleet();
		Ship ship = new Barge(Compass.NORTH, new Position(0, 0));
		fleet.addShip(ship);

		game.fireSingleShot(new Position(0, 0), false);

		assertEquals(1, game.getHits(), "O contador de acertos deve ser 1.");
		assertEquals(1, game.getSunkShips(), "O contador de navios afundados deve ser 1.");
	}

	@Test
	@DisplayName("Testa a exceção ao disparar um número incorreto de tiros com fireShots")
	void testFireShots_InvalidSize() {
		List<IPosition> wrongSizeShots = List.of(new Position(0, 0));

		assertThrows(IllegalArgumentException.class, () -> {
			game.fireShots(wrongSizeShots);
		}, "Deve lançar IllegalArgumentException se o número de tiros for diferente de NUMBER_SHOTS.");
	}

	@Test
	@DisplayName("Testa a leitura bem-sucedida de tiros do inimigo através do Scanner")
	void testReadEnemyFire_Valid() {
		Scanner scanner = new Scanner("A1 B2 C3");
		String jsonResult = game.readEnemyFire(scanner);

		assertNotNull(jsonResult, "O resultado da serialização JSON não deve ser nulo.");
		assertTrue(jsonResult.contains("\"row\""), "O JSON gerado deve conter a chave row.");
		assertTrue(jsonResult.contains("\"column\""), "O JSON gerado deve conter a chave column.");
	}

	@Test
	@DisplayName("Testa exceção ao ler tiros do inimigo com formato inválido")
	void testReadEnemyFire_InvalidFormat() {
		Scanner scanner = new Scanner("A X");

		assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(scanner);
		}, "Deve lançar IllegalArgumentException se o formato do input do inimigo estiver incorreto.");
	}

	@Test
	@DisplayName("Testa disparo aleatório do inimigo (randomEnemyFire)")
	void testRandomEnemyFire() {
		String jsonResult = game.randomEnemyFire();

		assertNotNull(jsonResult, "O JSON retornado pelo disparo aleatório não deve ser nulo.");
		assertEquals(1, game.getAlienMoves().size(), "Deve ter sido registado 1 movimento alienígena.");
	}

	@Test
	@DisplayName("Testa disparo aleatório do inimigo quando restam menos de 3 posições livres no tabuleiro")
	void testRandomEnemyFire_AlmostFullBoard() {
		// Preenche 98 das 100 posições do tabuleiro para sobrarem apenas 2 posições livres (< NUMBER_SHOTS)
		for (int r = 0; r < Game.BOARD_SIZE; r++) {
			for (int c = 0; c < Game.BOARD_SIZE; c++) {
				if (r == 9 && c >= 8) {
					continue; // Deixa apenas (9,8) e (9,9) livres
				}
				game.fireShots(List.of(new Position(r, c), new Position(r, c), new Position(r, c)));
			}
		}

		String jsonResult = game.randomEnemyFire();
		assertNotNull(jsonResult, "O JSON retornado não deve ser nulo mesmo com poucas posições livres.");
	}

	@Test
	@DisplayName("Testa a impressão dos tabuleiros e mensagem de fim de jogo")
	void testPrintBoardsAndOver() {
		assertDoesNotThrow(() -> {
			game.printMyBoard(true, true);
			game.printAlienBoard(false, false);
			game.over();
		}, "Os métodos de impressão não devem lançar exceções.");
	}

	@Test
	@DisplayName("Testa leitura de tiros do inimigo com formato separado (letra e número em tokens distintos)")
	void testReadEnemyFire_SeparateTokens() {
		Scanner scanner = new Scanner("A 1 B 2 C 3");
		String jsonResult = game.readEnemyFire(scanner);
		assertNotNull(jsonResult, "O resultado não deve ser nulo para tokens separados.");
	}

	@Test
	@DisplayName("Testa exceção quando a coluna do inimigo não é seguida por uma linha")
	void testReadEnemyFire_IncompletePosition() {
		Scanner scanner = new Scanner("A X");
		assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(scanner);
		}, "Deve lançar exceção se faltar o número da linha.");
	}

	@Test
	@DisplayName("Testa um tiro num navio grande que sofre dano mas não afunda (Hit sem Sink)")
	void testFireSingleShot_HitWithoutSinking() {
		IFleet fleet = game.getMyFleet();
		Ship ship = new Frigate(Compass.NORTH, new Position(0, 0));
		fleet.addShip(ship);

		game.fireSingleShot(new Position(0, 0), false);

		assertEquals(1, game.getHits(), "O contador de acertos deve ser 1.");
		assertEquals(0, game.getSunkShips(), "O navio ainda não deve estar afundado.");
	}

	@Test
	@DisplayName("Testa a impressão detalhada do tabuleiro com navios afundados, acertos, água e tiros fora")
	void testPrintBoardDetailedWithSunkShipsAndHits() {
		IFleet fleet = game.getMyFleet();
		Ship barge = new Barge(Compass.NORTH, new Position(2, 2)); // Vai ser afundado
		Ship frigate = new Frigate(Compass.EAST, new Position(6, 2)); // Vai ser atingido parcialmente
		fleet.addShip(barge);
		fleet.addShip(frigate);

		// Dispara no Barge (afunda), no Frigate (acerto) e numa posição fora do tabuleiro (-1, -1)
		game.fireShots(List.of(new Position(2, 2), new Position(6, 2), new Position(-1, -1)));
		// Dispara na água e numa posição adjacente ao Barge afundado (2, 3)
		game.fireShots(List.of(new Position(0, 0), new Position(2, 3), new Position(8, 8)));

		assertDoesNotThrow(() -> {
			Game.printBoard(game.getMyFleet(), game.getAlienMoves(), true, true);
		}, "A impressão do tabuleiro com navios afundados, adjacentes, acertos e tiros fora não deve lançar exceções.");
	}

	@Test
	@DisplayName("Testa se o método repeatedShot deteta corretamente um tiro inserido numa jogada anterior")
	void testRepeatedShotFromAlienMoves() {
		List<IPosition> positions = List.of(new Position(5, 5), new Position(5, 6), new Position(5, 7));
		game.fireShots(positions);

		assertTrue(game.repeatedShot(new Position(5, 5)), "A posição (5,5) já foi alvo de disparo e deve ser repetida.");
	}

	@Test
	@DisplayName("Testa exceção quando o input do inimigo no scanner é demasiado curto ou vazio")
	void testReadEnemyFire_EmptyOrShortInput() {
		Scanner scanner = new Scanner("A1");

		assertThrows(IllegalArgumentException.class, () -> {
			game.readEnemyFire(scanner);
		}, "Deve lançar exceção se o número de posições fornecidas for inferior a NUMBER_SHOTS.");
	}

	@Test
	@DisplayName("Testa fireSingleShot explicitamente com tiro repetido (isRepeated = true)")
	void testFireSingleShot_ExplicitlyRepeated() {
		Position pos = new Position(3, 3);
		game.fireSingleShot(pos, false);
		var result = game.fireSingleShot(pos, true);

		assertNotNull(result, "O resultado do tiro não deve ser nulo.");
		assertEquals(1, game.getRepeatedShots(), "O contador de tiros repetidos deve ser 1.");
	}
}