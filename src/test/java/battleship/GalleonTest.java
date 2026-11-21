package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for the Galleon class.
 */
public class GalleonTest {

	private Galleon galleon;

	@BeforeEach
	void setUp() {
		galleon = new Galleon(Compass.NORTH, new Position(5, 5));
	}

	@AfterEach
	void tearDown() {
		galleon = null;
	}

	@Test
	@DisplayName("Testa o construtor com orientação NORTH e validação das 5 posições")
	void testConstructorNorth() {
		List<IPosition> positions = galleon.getPositions();
		assertNotNull(galleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for NORTH.");
		assertEquals(new Position(5, 6), positions.get(1), "Error: The second position is incorrect for NORTH.");
		assertEquals(new Position(5, 7), positions.get(2), "Error: The third position is incorrect for NORTH.");
		assertEquals(new Position(6, 6), positions.get(3), "Error: The fourth position is incorrect for NORTH.");
		assertEquals(new Position(7, 6), positions.get(4), "Error: The fifth position is incorrect for NORTH.");
	}

	@Test
	@DisplayName("Testa o construtor com orientação SOUTH e validação das posições")
	void testConstructorSouth() {
		Galleon southGalleon = new Galleon(Compass.SOUTH, new Position(5, 5));
		List<IPosition> positions = southGalleon.getPositions();
		assertNotNull(southGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for SOUTH.");
		assertEquals(new Position(6, 5), positions.get(1), "Error: The second position is incorrect for SOUTH.");
		assertEquals(new Position(7, 4), positions.get(2), "Error: The third position is incorrect for SOUTH.");
		assertEquals(new Position(7, 5), positions.get(3), "Error: The fourth position is incorrect for SOUTH.");
		assertEquals(new Position(7, 6), positions.get(4), "Error: The fifth position is incorrect for SOUTH.");
	}

	@Test
	@DisplayName("Testa o construtor com orientação EAST e validação das posições")
	void testConstructorEast() {
		Galleon eastGalleon = new Galleon(Compass.EAST, new Position(5, 5));
		List<IPosition> positions = eastGalleon.getPositions();
		assertNotNull(eastGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for EAST.");
		assertEquals(new Position(6, 3), positions.get(1), "Error: The second position is incorrect for EAST.");
		assertEquals(new Position(6, 4), positions.get(2), "Error: The third position is incorrect for EAST.");
		assertEquals(new Position(6, 5), positions.get(3), "Error: The fourth position is incorrect for EAST.");
		assertEquals(new Position(7, 5), positions.get(4), "Error: The fifth position is incorrect for EAST.");
	}

	@Test
	@DisplayName("Testa o construtor com orientação WEST e validação das posições")
	void testConstructorWest() {
		Galleon westGalleon = new Galleon(Compass.WEST, new Position(5, 5));
		List<IPosition> positions = westGalleon.getPositions();
		assertNotNull(westGalleon, "Error: Galleon instance should not be null.");
		assertEquals(5, positions.size(), "Error: Galleon should have exactly 5 positions.");
		assertEquals(new Position(5, 5), positions.get(0), "Error: The first position is incorrect for WEST.");
		assertEquals(new Position(6, 5), positions.get(1), "Error: The second position is incorrect for WEST.");
		assertEquals(new Position(6, 6), positions.get(2), "Error: The third position is incorrect for WEST.");
		assertEquals(new Position(6, 7), positions.get(3), "Error: The fourth position is incorrect for WEST.");
		assertEquals(new Position(7, 5), positions.get(4), "Error: The fifth position is incorrect for WEST.");
	}

	@Test
	@DisplayName("Testa se o navio continua a flutuar quando está intacto")
	void testStillFloating_Intact() {
		assertTrue(galleon.stillFloating(), "Error: Galleon should initially be floating.");
	}

	@Test
	@DisplayName("Testa se o navio deixa de flutuar quando todas as posições são atingidas")
	void testStillFloating_AllHit() {
		galleon.getPositions().forEach(pos -> pos.shoot());
		assertFalse(galleon.stillFloating(), "Error: Galleon should not float when all positions are hit.");
	}

	@Test
	@DisplayName("Testa se o navio continua a flutuar com danos parciais (algumas posições intactas)")
	void testStillFloating_PartialHit() {
		List<IPosition> positions = galleon.getPositions();
		// Atinge apenas algumas posições, deixando a última intacta
		for (int i = 0; i < positions.size() - 1; i++) {
			positions.get(i).shoot();
		}
		assertTrue(galleon.stillFloating(), "Error: Galleon should still be floating if at least one position is not hit.");
	}

	@Test
	@DisplayName("Testa os limites do navio (top, bottom, left, right)")
	void testGetLimits() {
		assertEquals(5, galleon.getTopMostPos(), "Error: The topmost position should be 5.");
		assertEquals(7, galleon.getBottomMostPos(), "Error: The bottommost position should be 7.");
		assertEquals(5, galleon.getLeftMostPos(), "Error: The leftmost position should be 5.");
		assertEquals(7, galleon.getRightMostPos(), "Error: The rightmost position should be 7.");
	}

	@Test
	@DisplayName("Testa exceções do construtor com inputs nulos (NullPointerException)")
	void testConstructorWithInvalidInput() {
		assertThrows(NullPointerException.class, () -> new Galleon(null, null),
				"Error: NullPointerException should have been thrown for null inputs.");
		assertThrows(NullPointerException.class, () -> new Galleon(Compass.NORTH, null),
				"Error: NullPointerException should have been thrown when position is null.");
		assertThrows(NullPointerException.class, () -> new Galleon(null, new Position(5, 5)),
				"Error: NullPointerException should have been thrown when compass is null.");
	}
}