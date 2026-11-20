package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Fleet.
 * Author: 129849
 * Date: 2026-11-20 14:10
 * Cyclomatic Complexity for each method (MetricsTree):
 * - Fleet(): 1
 * - createRandom(): 4
 * - getShips(): 1
 * - addShip(IShip): 4
 * - getShipsLike(String): 3
 * - getFloatingShips(): 3
 * - getSunkShips(): 3
 * - shipAt(IPosition): 3
 * - colisionRisk(IShip) [private]: 3
 * - printShips(List): 2
 * - printStatus(): 1
 * - printShipsByCategory(String): 1
 * - printFloatingShips(): 1
 * - printAllShips(): 1
 */
@DisplayName("Testes unitários da classe Fleet")
public class FleetTest {

	private Fleet fleet;
	private PrintStream originalOut;
	private ByteArrayOutputStream capturedOut;

	@BeforeEach
	void setUp() {
		fleet = new Fleet();
		originalOut = System.out;
		capturedOut = new ByteArrayOutputStream();
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
		fleet = null;
		capturedOut = null;
	}

	/** Redireciona o System.out para podermos verificar o que é impresso. */
	private void captureOutput() {
		System.setOut(new PrintStream(capturedOut, true));
	}

	private String output() {
		return capturedOut.toString();
	}

	// =====================================================================
	// Testes originais (mantidos; acrescentado apenas o @DisplayName)
	// =====================================================================

	/**
	 * Test for the Fleet constructor.
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("Construtor cria uma frota vazia")
	void testConstructor() {
		assertNotNull(fleet, "Error: Instance of Fleet should not be null.");
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet should be initialized with empty ships list.");
	}

	@Test
	@DisplayName("createRandom cria uma frota completa com FLEET_SIZE navios")
	void testCreateRandom(){
		IFleet r = Fleet.createRandom();
		assertNotNull(r, "Error: createRandom should return a non-null fleet instance.");
		assertEquals(Fleet.FLEET_SIZE, r.getShips().size(), "Error: createRandom should create a fleet with the correct number of ships.");
	}

	/**
	 * Test for the addShip method (all conditions true).
	 */
	@Test
	@DisplayName("addShip aceita um navio válido")
	void testAddShip1() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		assertTrue(fleet.addShip(ship), "Error: Valid ship should be added successfully.");
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should contain one ship after addition.");
	}

	/**
	 * Test for the addShip method (ships placed side by side in column 0).
	 */
	@Test
	@DisplayName("addShip recusa um navio quando a frota já tem navios encostados")
	void testAddShip2() {
		for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
			fleet.addShip(new Barge(Compass.NORTH, new Position(i, 0)));
		}
		IShip anotherShip = new Barge(Compass.NORTH, new Position(10, 10));
		assertFalse(fleet.addShip(anotherShip), "Error: Should not add ship when fleet size limit is reached.");
	}

	/**
	 * Test for the addShip method (ship outside the board).
	 */
	@Test
	@DisplayName("addShip recusa um navio fora do tabuleiro")
	void testAddShip3() {
		IShip shipOutside = new Barge(Compass.NORTH, new Position(99, 99));
		assertFalse(fleet.addShip(shipOutside), "Error: Should not add ship outside the board.");
	}

	/**
	 * Test for the addShip method (collision risk).
	 */
	@Test
	@DisplayName("addShip recusa um navio com risco de colisão")
	void testAddShip4() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));  // Overlapping position
		fleet.addShip(ship1);
		assertFalse(fleet.addShip(ship2), "Error: Should not add ship with a collision risk.");
	}

	/**
	 * Test for the getShips method.
	 * Cyclomatic Complexity: 1
	 */
	@Test
	@DisplayName("getShips devolve a lista de navios adicionados")
	void testGetShips() {
		assertTrue(fleet.getShips().isEmpty(), "Error: Fleet's ships list should initially be empty.");
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		assertEquals(1, fleet.getShips().size(), "Error: Fleet should have size 1 after adding a ship.");
		assertEquals(ship, fleet.getShips().get(0), "Error: Fleet's first ship should match the added ship.");
	}

	/**
	 * Test for the getShipsLike method (ships of specific category).
	 */
	@Test
	@DisplayName("getShipsLike devolve apenas os navios da categoria pedida")
	void testGetShipsLike() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> barges = fleet.getShipsLike("Barca");
		assertEquals(1, barges.size(), "Error: There should be exactly one ship of category 'Barca'.");
		assertEquals(ship1, barges.get(0), "Error: The ship of category 'Barca' does not match.");
	}

	/**
	 * Test for the getFloatingShips method.
	 */
	@Test
	@DisplayName("getFloatingShips exclui os navios afundados")
	void testGetFloatingShips() {
		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(ship1);
		fleet.addShip(ship2);

		List<IShip> floatingShips = fleet.getFloatingShips();
		assertEquals(2, floatingShips.size(), "Error: All ships should be floating initially.");

		ship1.getPositions().get(0).shoot();  // Sink ship1
		floatingShips = fleet.getFloatingShips();
		assertEquals(1, floatingShips.size(), "Error: Only one ship should be floating after sinking one.");
		assertEquals(ship2, floatingShips.get(0), "Error: The floating ship should match the expected result.");
	}

	/**
	 * Test for the shipAt method.
	 */
	@Test
	@DisplayName("shipAt encontra o navio numa posição ocupada e devolve null numa livre")
	void testShipAt() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);

		assertEquals(ship, fleet.shipAt(new Position(1, 1)), "Error: Should return the correct ship at the position.");
		assertNull(fleet.shipAt(new Position(5, 5)), "Error: Should return null for empty positions in the fleet.");
	}

	/**
	 * Test for private method colisionRisk.
	 */
	@Test
	@DisplayName("colisionRisk deteta navios sobrepostos e aceita navios afastados")
	void testColisionRisk() throws Exception {
		var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);

		IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));  // Overlapping position
		fleet.addShip(ship1);

		assertTrue((Boolean) method.invoke(fleet, ship2), "Error: Overlapping ships should be at collision risk.");
		assertFalse((Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(5, 5))),
				"Error: Ships at non-overlapping positions should not have a collision risk.");
	}

	/**
	 * Test for the printStatus method.
	 */
	@Test
	@DisplayName("printStatus não lança exceções")
	void testPrintStatus() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		assertDoesNotThrow(fleet::printStatus, "Error: printStatus should not throw any exceptions.");
	}

	// =====================================================================
	// Novos testes (129849) para atingir 100% de cobertura de linhas e ramos
	// =====================================================================

	// ---------------- addShip(IShip) — CC = 4 ----------------

	@Test
	@DisplayName("addShip recusa um navio quando a frota já ultrapassou FLEET_SIZE")
	void addShip5() {
		// Barcas em linhas/colunas pares nunca são adjacentes: há 25 posições livres
		int added = 0;
		for (int r = 0; r < Game.BOARD_SIZE && added < Fleet.FLEET_SIZE + 1; r += 2)
			for (int c = 0; c < Game.BOARD_SIZE && added < Fleet.FLEET_SIZE + 1; c += 2)
				if (fleet.addShip(new Barge(Compass.NORTH, new Position(r, c))))
					added++;

		// Nota: a condição é "size() <= FLEET_SIZE", por isso a frota aceita FLEET_SIZE + 1 navios
		int sizeBefore = fleet.getShips().size();
		boolean result = fleet.addShip(new Barge(Compass.NORTH, new Position(8, 8)));

		assertAll("Limite de tamanho da frota",
				() -> assertEquals(Fleet.FLEET_SIZE + 1, sizeBefore,
						"Error: expected the fleet to hold " + (Fleet.FLEET_SIZE + 1) + " ships but got " + sizeBefore),
				() -> assertFalse(result,
						"Error: expected addShip to return false when the fleet is full but got true"),
				() -> assertEquals(sizeBefore, fleet.getShips().size(),
						"Error: expected fleet size to stay " + sizeBefore + " but got " + fleet.getShips().size()));
	}

	@Test
	@DisplayName("addShip(null) falha a asserção e lança AssertionError")
	void addShipNullThrowsAssertionError() {
		assertThrows(AssertionError.class, () -> fleet.addShip(null),
				"Error: expected AssertionError when adding a null ship but none was thrown");
	}

	// ---------------- getShipsLike(String) — CC = 3 ----------------

	@Test
	@DisplayName("getShipsLike devolve lista vazia quando a frota está vazia")
	void getShipsLike1() {
		List<IShip> result = fleet.getShipsLike("Barca");
		assertTrue(result.isEmpty(), "Error: expected an empty list for an empty fleet but got " + result);
	}

	@Test
	@DisplayName("getShipsLike devolve lista vazia quando nenhum navio é da categoria pedida")
	void getShipsLike2() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		List<IShip> result = fleet.getShipsLike("Galeao");
		assertTrue(result.isEmpty(), "Error: expected no ships of category 'Galeao' but got " + result);
	}

	@Test
	@DisplayName("getShipsLike devolve todos os navios da categoria pedida")
	void getShipsLike3() {
		IShip b1 = new Barge(Compass.NORTH, new Position(0, 0));
		IShip b2 = new Barge(Compass.NORTH, new Position(5, 5));
		fleet.addShip(b1);
		fleet.addShip(new Caravel(Compass.NORTH, new Position(2, 8)));
		fleet.addShip(b2);

		List<IShip> result = fleet.getShipsLike("Barca");
		assertAll("Navios da categoria Barca",
				() -> assertEquals(2, result.size(), "Error: expected 2 ships of category 'Barca' but got " + result.size()),
				() -> assertTrue(result.contains(b1) && result.contains(b2),
						"Error: expected both barges in the result but got " + result));
	}

	@Test
	@DisplayName("getShipsLike(null) falha a asserção e lança AssertionError")
	void getShipsLikeNullThrowsAssertionError() {
		assertThrows(AssertionError.class, () -> fleet.getShipsLike(null),
				"Error: expected AssertionError for a null category but none was thrown");
	}

	// ---------------- getFloatingShips() — CC = 3 ----------------

	@Test
	@DisplayName("getFloatingShips devolve lista vazia quando a frota está vazia")
	void getFloatingShips1() {
		assertTrue(fleet.getFloatingShips().isEmpty(),
				"Error: expected no floating ships in an empty fleet but got " + fleet.getFloatingShips());
	}

	@Test
	@DisplayName("getFloatingShips devolve lista vazia quando todos os navios foram afundados")
	void getFloatingShips2() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		ship.getPositions().get(0).shoot();

		assertTrue(fleet.getFloatingShips().isEmpty(),
				"Error: expected no floating ships after sinking all but got " + fleet.getFloatingShips());
	}

	@Test
	@DisplayName("getFloatingShips devolve todos os navios quando nenhum foi atingido")
	void getFloatingShips3() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		fleet.addShip(new Barge(Compass.NORTH, new Position(5, 5)));

		assertEquals(2, fleet.getFloatingShips().size(),
				"Error: expected 2 floating ships but got " + fleet.getFloatingShips().size());
	}

	// ---------------- getSunkShips() — CC = 3 ----------------

	@Test
	@DisplayName("getSunkShips devolve lista vazia quando a frota está vazia")
	void getSunkShips1() {
		assertTrue(fleet.getSunkShips().isEmpty(),
				"Error: expected no sunk ships in an empty fleet but got " + fleet.getSunkShips());
	}

	@Test
	@DisplayName("getSunkShips devolve lista vazia quando todos os navios flutuam")
	void getSunkShips2() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		fleet.addShip(new Caravel(Compass.NORTH, new Position(4, 4)));

		assertTrue(fleet.getSunkShips().isEmpty(),
				"Error: expected no sunk ships but got " + fleet.getSunkShips());
	}

	@Test
	@DisplayName("getSunkShips devolve apenas os navios afundados")
	void getSunkShips3() {
		IShip sunk = new Barge(Compass.NORTH, new Position(1, 1));
		IShip floating = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(sunk);
		fleet.addShip(floating);
		sunk.getPositions().get(0).shoot();

		List<IShip> result = fleet.getSunkShips();
		assertAll("Navios afundados",
				() -> assertEquals(1, result.size(), "Error: expected 1 sunk ship but got " + result.size()),
				() -> assertEquals(sunk, result.get(0), "Error: expected the sunk barge but got " + result.get(0)));
	}

	// ---------------- shipAt(IPosition) — CC = 3 ----------------

	@Test
	@DisplayName("shipAt devolve null quando a frota está vazia")
	void shipAt1() {
		assertNull(fleet.shipAt(new Position(1, 1)),
				"Error: expected null for an empty fleet but got " + fleet.shipAt(new Position(1, 1)));
	}

	@Test
	@DisplayName("shipAt devolve null quando nenhum navio ocupa a posição")
	void shipAt2() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertNull(fleet.shipAt(new Position(7, 7)),
				"Error: expected null for a free position but got " + fleet.shipAt(new Position(7, 7)));
	}

	@Test
	@DisplayName("shipAt devolve o navio que ocupa a posição (não o primeiro da lista)")
	void shipAt3() {
		IShip first = new Barge(Compass.NORTH, new Position(1, 1));
		IShip second = new Caravel(Compass.SOUTH, new Position(5, 5));
		fleet.addShip(first);
		fleet.addShip(second);

		assertEquals(second, fleet.shipAt(new Position(6, 5)),
				"Error: expected the caravel at (6,5) but got " + fleet.shipAt(new Position(6, 5)));
	}

	@Test
	@DisplayName("shipAt(null) falha a asserção e lança AssertionError")
	void shipAtNullThrowsAssertionError() {
		assertThrows(AssertionError.class, () -> fleet.shipAt(null),
				"Error: expected AssertionError for a null position but none was thrown");
	}

	// ---------------- colisionRisk(IShip) [private] — CC = 3 ----------------

	@Test
	@DisplayName("colisionRisk devolve false quando a frota está vazia")
	void colisionRisk1() throws Exception {
		Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);

		boolean result = (Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(1, 1)));
		assertFalse(result, "Error: expected no collision risk with an empty fleet but got true");
	}

	@Test
	@DisplayName("colisionRisk deteta um navio adjacente (não sobreposto)")
	void colisionRisk2() throws Exception {
		Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));

		boolean result = (Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(2, 2)));
		assertTrue(result, "Error: expected collision risk for a diagonally adjacent ship but got false");
	}

	@Test
	@DisplayName("colisionRisk(null) falha a asserção e lança AssertionError")
	void colisionRiskNullThrowsAssertionError() throws Exception {
		Method method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);

		InvocationTargetException ex = assertThrows(InvocationTargetException.class,
				() -> method.invoke(fleet, (Object) null),
				"Error: expected the reflective call to fail for a null ship but it did not");
		assertInstanceOf(AssertionError.class, ex.getCause(),
				"Error: expected AssertionError as cause but got " + ex.getCause());
	}

	// ---------------- printShips(List) — CC = 2 ----------------

	@Test
	@DisplayName("printShips não imprime nada para uma lista vazia")
	void printShips1() {
		captureOutput();
		fleet.printShips(new ArrayList<>());
		assertEquals("", output(), "Error: expected no output for an empty list but got '" + output() + "'");
	}

	@Test
	@DisplayName("printShips imprime cada navio da lista numa linha")
	void printShips2() {
		IShip b = new Barge(Compass.NORTH, new Position(1, 1));
		IShip c = new Caravel(Compass.NORTH, new Position(4, 4));
		captureOutput();
		fleet.printShips(List.of(b, c));

		String out = output();
		assertAll("Impressão dos navios",
				() -> assertTrue(out.contains(b.toString()), "Error: expected output to contain " + b + " but got '" + out + "'"),
				() -> assertTrue(out.contains(c.toString()), "Error: expected output to contain " + c + " but got '" + out + "'"),
				() -> assertEquals(2, out.lines().count(), "Error: expected 2 lines but got " + out.lines().count()));
	}

	@Test
	@DisplayName("printShips(null) falha a asserção e lança AssertionError")
	void printShipsNullThrowsAssertionError() {
		assertThrows(AssertionError.class, () -> fleet.printShips(null),
				"Error: expected AssertionError for a null list but none was thrown");
	}

	// ---------------- printStatus() — CC = 1 ----------------

	@Test
	@DisplayName("printStatus imprime o número de navios a flutuar e afundados")
	void printStatus() {
		IShip sunk = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(sunk);
		fleet.addShip(new Caravel(Compass.NORTH, new Position(4, 4)));
		sunk.getPositions().get(0).shoot();

		captureOutput();
		fleet.printStatus();
		assertTrue(output().contains("1 a flutuar, 1 afundados!"),
				"Error: expected '1 a flutuar, 1 afundados!' but got '" + output().trim() + "'");
	}

	// ---------------- printShipsByCategory(String) — CC = 1 ----------------

	@Test
	@DisplayName("printShipsByCategory imprime apenas os navios da categoria pedida")
	void printShipsByCategory() {
		IShip barge = new Barge(Compass.NORTH, new Position(1, 1));
		IShip caravel = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(barge);
		fleet.addShip(caravel);

		captureOutput();
		fleet.printShipsByCategory("Barca");
		String out = output();
		assertAll("Impressão por categoria",
				() -> assertTrue(out.contains(barge.toString()), "Error: expected the barge in the output but got '" + out + "'"),
				() -> assertFalse(out.contains(caravel.toString()), "Error: expected no caravel in the output but got '" + out + "'"));
	}

	@Test
	@DisplayName("printShipsByCategory(null) falha a asserção e lança AssertionError")
	void printShipsByCategoryNullThrowsAssertionError() {
		assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null),
				"Error: expected AssertionError for a null category but none was thrown");
	}

	// ---------------- printFloatingShips() — CC = 1 ----------------

	@Test
	@DisplayName("printFloatingShips imprime apenas os navios a flutuar")
	void printFloatingShips() {
		IShip sunk = new Barge(Compass.NORTH, new Position(1, 1));
		IShip floating = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(sunk);
		fleet.addShip(floating);
		sunk.getPositions().get(0).shoot();

		captureOutput();
		fleet.printFloatingShips();
		String out = output();
		assertAll("Impressão dos navios a flutuar",
				() -> assertTrue(out.contains(floating.toString()), "Error: expected the floating caravel in the output but got '" + out + "'"),
				() -> assertFalse(out.contains(sunk.toString()), "Error: expected no sunk barge in the output but got '" + out + "'"));
	}

	// ---------------- printAllShips() — CC = 1 ----------------

	@Test
	@DisplayName("printAllShips imprime todos os navios da frota")
	void printAllShips() {
		IShip barge = new Barge(Compass.NORTH, new Position(1, 1));
		IShip caravel = new Caravel(Compass.NORTH, new Position(4, 4));
		fleet.addShip(barge);
		fleet.addShip(caravel);

		captureOutput();
		fleet.printAllShips();
		String out = output();
		assertAll("Impressão de todos os navios",
				() -> assertTrue(out.contains(barge.toString()), "Error: expected the barge in the output but got '" + out + "'"),
				() -> assertTrue(out.contains(caravel.toString()), "Error: expected the caravel in the output but got '" + out + "'"));
	}
}