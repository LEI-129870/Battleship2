package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for Ship.
 * Author: ${user.name}
 * Date: ${current_date}
 * Time: ${current_time}
 * Cyclomatic Complexity for each method:
 * - Constructor: 1
 * - getCategory: 1
 * - getSize: 1
 * - getBearing: 1
 * - getPositions: 1
 * - stillFloating: 2
 * - shoot: 2
 * - occupies: 2
 * - tooCloseTo (IShip): 2
 * - tooCloseTo (IPosition): 2
 * - getTopMostPos: 2
 * - getBottomMostPos: 2
 * - getLeftMostPos: 2
 * - getRightMostPos: 2
 */
public class ShipTest {

    private Ship ship, shipBorder;

    @BeforeEach
    void setUp() {
        // Since Ship is abstract, instantiate it with a concrete subclass (e.g., Barge)
        ship = new Barge(Compass.NORTH, new Position(5, 5));
        shipBorder = new Barge(Compass.NORTH, new Position(0, 0));
    }

    @AfterEach
    void tearDown() {
        ship = null;
    }

    /**
     * Test for the constructor.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testConstructor() {
        assertNotNull(ship, "Error: Ship instance should not be null.");
        assertEquals("Barca", ship.getCategory(), "Error: Ship category is incorrect.");
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing is incorrect.");
        assertEquals(1, ship.getSize(), "Error: Ship size is incorrect.");
        assertFalse(ship.getPositions().isEmpty(), "Error: Ship positions should not be empty.");
    }

    /**
     * Test for the getCategory method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetCategory() {
        assertEquals("Barca", ship.getCategory(), "Error: Ship category should be 'Barca'.");
    }

    /**
     * Test for the getSize method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetSize() {
        assertEquals(1, ship.getSize(), "Error: Ship size should be 1.");
    }

    /**
     * Test for the getBearing method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetBearing() {
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing should be NORTH.");
    }

    /**
     * Test for the getPositions method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetPositions() {
        List<IPosition> positions = ship.getPositions();
        assertNotNull(positions, "Error: Ship positions should not be null.");
        assertEquals(1, positions.size(), "Error: Ship should have exactly one position.");
        assertEquals(5, positions.get(0).getRow(), "Error: Position's row should be 5.");
        assertEquals(5, positions.get(0).getColumn(), "Error: Position's column should be 5.");
    }

    /**
     * Test for the stillFloating method (all positions intact).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testStillFloating1() {
        assertTrue(ship.stillFloating(), "Error: Ship should still be floating.");
    }

    /**
     * Test for the stillFloating method (all positions hit).
     */
    @Test
    void testStillFloating2() {
        ship.getPositions().get(0).shoot();
        assertFalse(ship.stillFloating(), "Error: Ship should no longer be floating after being hit.");
    }

    /**
     * Test for the shoot method (valid position).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testShoot1() {
        Position target = new Position(5, 5);
        ship.shoot(target);
        assertTrue(ship.getPositions().get(0).isHit(), "Error: Position should be marked as hit.");
    }

    /**
     * Test for the shoot method (invalid position).
     */
    @Test
    void testShoot2() {
        Position target = new Position(0, 0);
        ship.shoot(target); // No exception expected
        assertFalse(ship.getPositions().get(0).isHit(), "Error: Position should not be marked as hit for an invalid target.");
    }

    /**
     * Test for the occupies method (position occupied).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testOccupies1() {
        Position pos = new Position(5, 5);
        assertTrue(ship.occupies(pos), "Error: Ship should occupy position (5, 5).");
    }

    /**
     * Test for the occupies method (position not occupied).
     */
    @Test
    void testOccupies2() {
        Position pos = new Position(1, 1);
        assertFalse(ship.occupies(pos), "Error: Ship should not occupy position (1, 1).");
    }

    /**
     * Test for the tooCloseTo method with another IShip (ships too close).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testTooCloseToShip1() {
        Ship nearbyShip = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(ship.tooCloseTo(nearbyShip), "Error: Ships should be too close.");
    }

    /**
     * Test for the tooCloseTo method with another IShip (ships not close).
     */
    @Test
    void testTooCloseToShip2() {
        Ship farShip = new Barge(Compass.NORTH, new Position(10, 10));
        assertFalse(ship.tooCloseTo(farShip), "Error: Ships should not be too close.");
    }

    /**
     * Test for the tooCloseTo method with an IPosition (positions adjacent).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testTooCloseToPosition1() {
        Position pos = new Position(5, 6); // Adjacent position
        assertTrue(ship.tooCloseTo(pos), "Error: Ship should be too close to the given position.");
    }

    /**
     * Test for the tooCloseTo method with an IPosition (positions not adjacent).
     */
    @Test
    void testTooCloseToPosition2() {
        Position pos = new Position(7, 7); // Non-adjacent position
        assertFalse(ship.tooCloseTo(pos), "Error: Ship should not be too close to the given position.");
    }

    /**
     * Test for the getTopMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetTopMostPos() {
        assertEquals(5, ship.getTopMostPos(), "Error: The topmost position should be 5.");
    }

    /**
     * Test for the getBottomMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetBottomMostPos() {
        assertEquals(5, ship.getBottomMostPos(), "Error: The bottommost position should be 5.");
    }

    /**
     * Test for the getLeftMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetLeftMostPos() {
        assertEquals(5, ship.getLeftMostPos(), "Error: The leftmost position should be 5.");
    }

    /**
     * Test for the getRightMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetRightMostPos() {
        assertEquals(5, ship.getRightMostPos(), "Error: The rightmost position should be 5.");
    }

    /**
     * Test for the getRightMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetAdjacentPositions() {
        assertEquals(8, ship.getAdjacentPositions().size(), "Error: The number of adjacent positions of a barge in the middle of the tray should be 8.");
        assertEquals(3, shipBorder.getAdjacentPositions().size(), "Error: The number of adjacent positions of a barge in the middle of the tray should be 8.");
    }


    @Test
    @DisplayName("buildShip1 - Criar uma Barca")
    void buildShip1() {
        Ship result = Ship.buildShip("barca", Compass.NORTH,
                new Position(5, 5));

        assertInstanceOf(Barge.class, result,
                "Error: Expected Barge for ship kind 'barca'.");
    }

    @Test
    @DisplayName("buildShip2 - Criar uma Caravela")
    void buildShip2() {
        Ship result = Ship.buildShip("caravela", Compass.NORTH,
                new Position(5, 5));

        assertInstanceOf(Caravel.class, result,
                "Error: Expected Caravel for ship kind 'caravela'.");
    }

    @Test
    @DisplayName("buildShip3 - Criar uma Nau")
    void buildShip3() {
        Ship result = Ship.buildShip("nau", Compass.NORTH,
                new Position(5, 5));

        assertInstanceOf(Carrack.class, result,
                "Error: Expected Carrack for ship kind 'nau'.");
    }

    @Test
    @DisplayName("buildShip4 - Criar uma Fragata")
    void buildShip4() {
        Ship result = Ship.buildShip("fragata", Compass.NORTH,
                new Position(5, 5));

        assertInstanceOf(Frigate.class, result,
                "Error: Expected Frigate for ship kind 'fragata'.");
    }

    @Test
    @DisplayName("buildShip5 - Criar um Galeão")
    void buildShip5() {
        Ship result = Ship.buildShip("galeao", Compass.NORTH,
                new Position(5, 5));

        assertInstanceOf(Galleon.class, result,
                "Error: Expected Galleon for ship kind 'galeao'.");
    }

    @Test
    @DisplayName("buildShip6 - Tipo de navio desconhecido")
    void buildShip6() {
        Ship result = Ship.buildShip("desconhecido", Compass.NORTH,
                new Position(5, 5));

        assertNull(result,
                "Error: Expected null for an unknown ship kind.");
    }


    @Test
    @DisplayName("isInsideBoard1 - Navio dentro do tabuleiro")
    void isInsideBoard1() {
        Ship s = new Barge(Compass.NORTH, new Position(5, 5));

        assertTrue(s.isInsideBoard(),
                "Error: Expected ship to be inside the board.");
    }

    @Test
    @DisplayName("isInsideBoard2 - Navio fora do limite esquerdo")
    void isInsideBoard2() {
        Ship s = new Barge(Compass.NORTH, new Position(5, -1));

        assertFalse(s.isInsideBoard(),
                "Error: Expected ship to be outside the left boundary.");
    }

    @Test
    @DisplayName("isInsideBoard3 - Navio fora do limite direito")
    void isInsideBoard3() {
        Ship s = new Barge(Compass.NORTH,
                new Position(5, Game.BOARD_SIZE));

        assertFalse(s.isInsideBoard(),
                "Error: Expected ship to be outside the right boundary.");
    }

    @Test
    @DisplayName("isInsideBoard4 - Navio fora do limite superior")
    void isInsideBoard4() {
        Ship s = new Barge(Compass.NORTH, new Position(-1, 5));

        assertFalse(s.isInsideBoard(),
                "Error: Expected ship to be outside the top boundary.");
    }

    @Test
    @DisplayName("isInsideBoard5 - Navio fora do limite inferior")
    void isInsideBoard5() {
        Ship s = new Barge(Compass.NORTH,
                new Position(Game.BOARD_SIZE, 5));

        assertFalse(s.isInsideBoard(),
                "Error: Expected ship to be outside the bottom boundary.");
    }


    @Test
    @DisplayName("getPosition - Verificar a posição inicial do navio")
    void testGetPosition() {
        IPosition position = ship.getPosition();

        assertAll(
                () -> assertNotNull(position,
                        "Error: Expected a non-null position."),
                () -> assertEquals(5, position.getRow(),
                        "Error: Expected row 5."),
                () -> assertEquals(5, position.getColumn(),
                        "Error: Expected column 5.")
        );
    }


    @Test
    @DisplayName("getTopMostPos - Atualizar menor linha")
    void testGetTopMostPos3() {
        Ship multiShip = new Caravel(
                Compass.NORTH, new Position(5, 5));

        multiShip.getPositions().set(1, new Position(3, 5));

        assertEquals(3, multiShip.getTopMostPos(),
                "Error: Expected topmost row 3.");
    }

    @Test
    @DisplayName("getLeftMostPos - Atualizar menor coluna")
    void testGetLeftMostPos3() {
        Ship multiShip = new Caravel(
                Compass.WEST, new Position(5, 5));

        multiShip.getPositions().set(1, new Position(5, 3));

        assertEquals(3, multiShip.getLeftMostPos(),
                "Error: Expected leftmost column 3.");
    }


    @Test
    @DisplayName("occupies - Argumento null")
    void testOccupiesNull() {
        assertThrows(AssertionError.class,
                () -> ship.occupies(null),
                "Error: Expected AssertionError for null position.");
    }

    @Test
    @DisplayName("shoot - Argumento null")
    void testShootNull() {
        assertThrows(AssertionError.class,
                () -> ship.shoot(null),
                "Error: Expected AssertionError for null position.");
    }

    @Test
    @DisplayName("shoot - Posição fora do tabuleiro")
    void testShootOutsideBoard() {
        Position pos = new Position(-1, 5);

        assertThrows(AssertionError.class,
                () -> ship.shoot(pos),
                "Error: Expected AssertionError for position outside board.");
    }

    @Test
    @DisplayName("tooCloseTo(IPosition) - Argumento null")
    void testTooCloseToPositionNull() {
        assertThrows(AssertionError.class,
                () -> ship.tooCloseTo((IPosition) null),
                "Error: Expected AssertionError for null position.");
    }

    @Test
    @DisplayName("tooCloseTo(IShip) - Argumento null")
    void testTooCloseToShipNull() {
        assertThrows(AssertionError.class,
                () -> ship.tooCloseTo((IShip) null),
                "Error: Expected AssertionError for null ship.");
    }


    @Test
    @DisplayName("buildShip7 - Tipo de navio nulo")
    void buildShip7() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip(null, Compass.NORTH,
                        new Position(5, 5)),
                "Error: Expected AssertionError for null ship kind.");
    }

    @Test
    @DisplayName("buildShip8 - Orientação nula")
    void buildShip8() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip("barca", null,
                        new Position(5, 5)),
                "Error: Expected AssertionError for null bearing.");
    }

    @Test
    @DisplayName("buildShip9 - Posição nula")
    void buildShip9() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip("barca", Compass.NORTH, null),
                "Error: Expected AssertionError for null position.");
    }

    @Test
    @DisplayName("getAdjacentPositions - Excluir posições ocupadas e repetidas")
    void testGetAdjacentPositionsCaravel() {
        Ship caravel = new Caravel(
                Compass.NORTH, new Position(5, 5));

        List<IPosition> adjacent = caravel.getAdjacentPositions();

        assertAll(
                () -> assertEquals(10, adjacent.size(),
                        "Error: Expected 10 unique adjacent positions."),
                () -> assertFalse(adjacent.contains(new Position(5, 5)),
                        "Error: Ship's own position must not be adjacent."),
                () -> assertFalse(adjacent.contains(new Position(6, 5)),
                        "Error: Ship's second position must not be adjacent.")
        );
    }

}