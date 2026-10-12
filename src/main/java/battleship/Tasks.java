package battleship;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

/**
 * The type Tasks.
 */
public class Tasks {
	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The constant GOODBYE_MESSAGE.
	 */
	private static final String GOODBYE_MESSAGE = Internacionalizacao.getMessage("menu.pdf.success") ;

	/**
	 * Strings to be used by the user
	 */
	private static final String AJUDA = "ajuda";
	private static final String GERAFROTA = "gerafrota";
	private static final String LEFROTA = "lefrota";
	private static final String DESISTIR = "desisto";
	private static final String RAJADA = "rajada";
	private static final String TIROS = "tiros";
	private static final String MAPA = "mapa";
	private static final String STATUS = "estado";
	private static final String SIMULA = "simula";
	private static final String EXPORTARPDF = "exportarpdf";
	private static final String LANG = "lang";


	/**
	 * This task also tests the fighting element of a round of three shots
	 */
	public static void menu() {

		IFleet myFleet = null;
		IGame game = null;
		menuHelp();

		System.out.print("> ");
		Scanner in = new Scanner(System.in);
		String command = in.next();
		while (!command.equals(DESISTIR)) {

			switch (command) {
				case GERAFROTA:
					myFleet = Fleet.createRandom();
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					break;
				case LEFROTA:
					myFleet = buildFleet(in);
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					break;
				case STATUS:
					if (myFleet != null)
						myFleet.printStatus();
					break;
				case MAPA:
					if (myFleet != null)
						game.printMyBoard(false, true);
					break;
				case RAJADA:
					if (game != null) {
						game.readEnemyFire(in);
						myFleet.printStatus();
						game.printMyBoard(true, false);

						if (game.getRemainingShips() == 0) {
							game.over();
							System.exit(0);
						}
					}
					break;
				case SIMULA:
					if (game != null) {
						while (game.getRemainingShips() > 0){
							game.randomEnemyFire();
							myFleet.printStatus();
							game.printMyBoard(true, false);
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt(); // Best practice: restore interrupt status
							}
						}

						if (game.getRemainingShips() == 0) {
							game.over();
							System.exit(0);
						}
					}
					break;
				case TIROS:
					if (game != null)
						game.printMyBoard(true, true);
					break;
                case AJUDA:
                    menuHelp();
                    break;
				case LANG:
					if (in.hasNext()) {
						String langChoice = in.next();
						Internacionalizacao.setLanguage(langChoice);
						menuHelp();
					}
					break;
				case EXPORTARPDF:
					if (game != null) {
						try {
							PdfExporter.exportarJogadas(
									game.getAlienMoves(),
									"historico_jogadas.pdf"
							);
							System.out.println(Internacionalizacao.getMessage("menu.pdf.success"));
						} catch (IOException e) {
							System.out.println(Internacionalizacao.getMessage("menu.pdf.error"));
						}
					} else {
						System.out.println(Internacionalizacao.getMessage("menu.pdf.nofleet"));
					}
					break;
				default:
					System.out.println(Internacionalizacao.getMessage("menu.unknown"));
			}
			System.out.print("> ");
			command = in.next();
		}
		System.out.println(Internacionalizacao.getMessage("menu.goodbye"));
	}

	/**
	 * This function provides help information about the menu commands.
	 */
	public static void menuHelp() {
		System.out.println(Internacionalizacao.getMessage("menu.header"));
		System.out.println(Internacionalizacao.getMessage("menu.info"));
		System.out.println(Internacionalizacao.getMessage("menu.gerafrota"));
		System.out.println(Internacionalizacao.getMessage("menu.lefrota"));
		System.out.println(Internacionalizacao.getMessage("menu.estado"));
		System.out.println(Internacionalizacao.getMessage("menu.mapa"));
		System.out.println(Internacionalizacao.getMessage("menu.rajada"));
		System.out.println(Internacionalizacao.getMessage("menu.simula"));
		System.out.println(Internacionalizacao.getMessage("menu.tiros"));
		System.out.println(Internacionalizacao.getMessage("menu.desisto"));
		System.out.println(Internacionalizacao.getMessage("menu.exportarpdf"));
		System.out.println(Internacionalizacao.getMessage("menu.lang"));
		System.out.println(Internacionalizacao.getMessage("menu.footer"));
	}
	/**
	 * This operation allows the build up of a fleet, given user data
	 *
	 * @param in The scanner to read from
	 * @return The fleet that has been built
	 */
	public static Fleet buildFleet(Scanner in) {

		assert in != null;

		Fleet fleet = new Fleet();
		int i = 0; // i represents the total of successfully created ships
		while (i < Fleet.FLEET_SIZE) {
			IShip s = readShip(in);
			if (s != null) {
				boolean success = fleet.addShip(s);
				if (success)
					i++;
				else
					LOGGER.info(Internacionalizacao.getMessage("log.fleet.fail", s.getCategory(), s.getBearing(), s.getPosition()));
			} else {
				LOGGER.info(Internacionalizacao.getMessage("log.fleet.unknown"));
			}
		}
		LOGGER.info(Internacionalizacao.getMessage("log.fleet.success", i));
		return fleet;
	}

	/**
	 * This operation reads data about a ship, build it and returns it
	 *
	 * @param in The scanner to read from
	 * @return The created ship based on the data that has been read
	 */
	public static Ship readShip(Scanner in) {

		assert in != null;

		String shipKind = in.next();
		Position pos = readPosition(in);
		char c = in.next().charAt(0);
		Compass bearing = Compass.charToCompass(c);
		return Ship.buildShip(shipKind, bearing, pos);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The position that has been read
	 */
	public static Position readPosition(Scanner in) {

		assert in != null;

		int row = in.nextInt();
		int column = in.nextInt();
		return new Position(row, column);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The classic position that has been read
	 */
	public static IPosition readClassicPosition(@NotNull Scanner in) {
		// Verifica se ainda há tokens disponíveis
		if (!in.hasNext()) {
			throw new IllegalArgumentException(Internacionalizacao.getMessage("err.pos.notfound"));
		}

		String part1 = in.next(); // Primeiro token
		String part2 = null;

		if (in.hasNextInt()) {
			part2 = in.next(); // Segundo token, se disponível
		}

		String input = (part2 != null) ? part1 + part2 : part1;

		// Normalizar o input para tratar letras maiúsculas e minúsculas
		input = input.toUpperCase();

		// Verificar os dois formatos possíveis: compactos e com espaço
		if (input.matches("[A-Z]\\d+")) {
			char column = input.charAt(0); // Extrair a coluna
			int row = Integer.parseInt(input.substring(1)); // Extrair a linha
			return new Position(column, row);
		} else if (part2 != null && part1.matches("[A-Z]") && part2.matches("\\d+")) {
			char column = part1.charAt(0); // Extrair a coluna
			int row = Integer.parseInt(part2); // Extrair a linha
			return new Position(column, row);
		} else {
			throw new IllegalArgumentException(Internacionalizacao.getMessage("err.pos.invalid"));
		}
	}

}