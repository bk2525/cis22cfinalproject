
/**
 * CLInterface.java
 * @author Stephen Kyker
 * (suplemental authoring denoted in method javadoc comments)
 * CIS 22C, Group Project
 */

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Controls the command line interface by integrating the Menu class with the
 * various components of the program; this class does not require any arguments
 * to instantiate it.
 */
public class CLInterface {
	/**
	 * If true, ANSI escape-sequence terminal clearing will be disabled. This is
	 * intended only for running tests and should not be enabled by default.
	 */
	boolean debugNoAnsiClear = false;

	/**
	 * Scanner for capturing user input via keyboard. This Scanner is intended to be
	 * shared across all instances, therefore the object and its wrapped stream
	 * should not be closed manually or via AutoCloseable (e.g.,
	 * try-with-resources).
	 */
	private static final Scanner keyboardInput = new Scanner(System.in);

	/**
	 * The SearchEngine object that is used by the members of this class.
	 */
	private SearchEngine amse;

	/**
	 * Default Constructor - does not do anything more than the implicit default
	 * constructor, but is explicitly implemented for visibility.
	 */
	public CLInterface() {
	}

	/**
	 * Initializes the handler loop for a CL session.
	 */
	public void init() {
		while (this.sessionHandler()) {
			System.out.println("Restarting program...\n");
		}
	}

	/**
	 * Handler for a user session.
	 * 
	 * @return true if session finished unsuccessfully and should be restarted;
	 *         false for any other exit condition
	 */
	private boolean sessionHandler() {
		try {
			this.runSession();
		} catch (Exception e1) {
			// Clear Scanner input if there is left over junk
			if (keyboardInput.hasNextLine()) {
				keyboardInput.nextLine();
			}

			// Print error and prompt for restart
			e1.printStackTrace();
			System.err.print(
					"CLInterface(): The program encountered a fatal " + "error. Would you like to restart (Y/N)? ");
			try {
				if (keyboardInput.nextLine().equalsIgnoreCase("Y")) {
					return true;
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				System.err.println("CLInterface(): Input capture failed. Exiting.");
			}
		}
		return false;
	}

	/**
	 * Logic controller for a user session, which integrates the frontend Menu class
	 * with the backend component classes.
	 * 
	 * @throws Exception up the stack when a general exception is caught
	 */
	private void runSession() throws Exception {
		// Spin up the search engine and import the songs from file
		this.amse = new SearchEngine();
		for (Song song : ImportSongs.fetchSongs()) {
			this.amse.indexSong(song);
		}

		// Clear the screen and begin the program
		this.clearConsole();
		System.out.println("[LAUNCHING THE ADELE MUSIC SEARCH ENGINE...]\n");

		// Set up the main menu variables
		int userSelection;
		String appTitle = "Adele Music Search Engine";
		String mainMenuTitle = "Main Menu";
		String borderPattern = "(*)";
		String[] mainMenuRows = { "Upload a new record", "Delete a record", "Search for a record",
				"Modify or update a record", "Statistics", "Quit and print records to file" };

		// Launch the main menu loop and capture user input
		Menu mainMenu = new Menu(CLInterface.keyboardInput, appTitle, mainMenuTitle, mainMenuRows);
		mainMenu.setBorderPattern(borderPattern);

		boolean exit = false;
		while (!exit) {
			mainMenu.display();
			userSelection = Menu.getSelection(mainMenuRows.length);

			if (this.actionHandler(userSelection, mainMenu)) {
				exit = true;
			}
		}
		return;
	}

	/**
	 * File print for a user session
	 * 
	 * @throws Exception up the stack when a general exception is caught
	 */
	private void printRecordsToFile() throws Exception {
		String dirPath = "./exports/";
		File dir = new File(dirPath);
		if (!dir.exists() && !dir.mkdir()) {
			throw new IOException("printRecordsToFile(): Failed to open export directory at '" + dirPath + "'");
		}

		System.out.print("Please enter a filename to export your search engine records to: ");
		String fileName = keyboardInput.nextLine();
		File file = new File(dirPath + fileName);

		// Don't allow file overwrite to protect previous runs
		while (file.exists()) {
			System.out.printf("%nThe file '%s' already exists.%nPlease choose a unique file name: ", fileName);
			fileName = keyboardInput.nextLine();
			file = new File(dirPath + fileName);
		}

		// Print records to file
		try (PrintWriter pw = new PrintWriter(file)) {
			pw.print(amse.toString());
		} catch (Exception e) {
			throw new Exception("printRecordsToFile(): An error occurred while writing to file.", e);
		}

		System.out.println("Records exported to '" + dirPath + fileName + "'.");
	}

	/**
	 * Action handler for the Main Menu
	 * 
	 * @param userSelection the menu selection made by the user
	 * @param mainMenu      the menu context
	 * @return T if user would like to exit, F if not.
	 * @throws Exception up the stack when a general exception is caught
	 */
	private boolean actionHandler(int userSelection, Menu mainMenu) throws Exception {
		if (userSelection != 3) { // Don't print if entering the search sub menu
			System.out.print("[" + mainMenu.getRows()[userSelection - 1] + "]\n");
		}
		switch (userSelection) {
		case 1:
			// System.out.println("actionHandler() Debug: 'Upload a new record' was
			// selected.");
			this.addRecord();
			break;
		case 2:
			// System.out.println("actionHandler() Debug: 'Delete a record' was selected.");
			this.deleteRecord();
			break;
		case 3:
			// System.out.println("actionHandler() Debug: 'Search for a record' was
			// selected.");
			this.searchMenu(mainMenu);
			return false;
		case 4:
			// System.out.println("actionHandler() Debug: 'Modify or update a record' was
			// selected.");
			this.modifyMenu(mainMenu);
			return false;
		case 5:
			// System.out.println("actionHandler() Debug: 'Statistics' was selected.");
			System.out.println("Statistic 1: Total number of songs: " + amse.getSongCount());
			System.out.println("Statistic 2: Unique words: " + amse.getTotalUniqueWords());
			System.out.println("Statistic 3: Average year of all songs " + amse.getAverageYear());
			break;
		case 6:
			// System.out.println("actionHandler() Debug: 'Quit' was selected.");
			this.printRecordsToFile();
			System.out.print("\nPress \"Enter\" to Exit. ");
			keyboardInput.nextLine();
			return true;
		default:
			// No-op; input validation ensures
			// this default is never used, but
			// including for best practice
			break;
		}

		System.out.print("\nPress \"Enter\" to return to the Main Menu. ");
		keyboardInput.nextLine();
		this.clearConsole();
		return false;
	}

	/**
	 * adds a user-specified record to the search engine
	 */
	private void addRecord() {
		System.out.print("Enter the name of a file containing a song you would like to add: ");
		amse.importSong("./data/" + keyboardInput.nextLine()); // Allow trailing white space
	}

	/**
	 * deletes a user-specified record from the search engine
	 */
	private void deleteRecord() {
		System.out.print("Enter the primary key (a song's exact title) of a song you would like to delete: ");
		this.amse.deleteSong(keyboardInput.nextLine().trim(), false); // Ignore trailing white space
	}

	/**
	 * Searches for a single record using the primary key, or for several records
	 * using keywords.
	 * 
	 * @param menu the parent menu context
	 */
	private void searchMenu(Menu menu) {
		String appTitle = menu.getAppName();
		String menuTitle = "Search Menu";
		String[] menuRows = { "Find and display a record by primary key", "Find and display records using keywords",
				"Return to Main Menu" };

		// Pass null for the Scanner since its static and was set previously
		Menu searchMenu = new Menu(null, appTitle, menuTitle, menuRows);
		searchMenu.setBorderPattern(menu.getBorderPattern());

		boolean returnToParent = false;
		while (!returnToParent) {
			boolean resetMenu = false;
			this.clearConsole();
			searchMenu.display();
			int userSelection = Menu.getSelection(menuRows.length);
			switch (userSelection) {
			case 1:
				// System.out.println("recordSearch() Debug: 'Find and display a record by
				// primary key' was selected.");
				System.out.print("Please enter a primary key (a song's exact title) to search for a record: ");
				this.amse.searchByKey(keyboardInput.nextLine().trim()); // Ignore trailing white space
				break;
			case 2:
				// System.out.println("recordSearch() Debug: 'Find and display records using
				// keywords' was selected.");
				System.out.print("Please enter a keyword to search for one or more records: ");
				String query = keyboardInput.nextLine().trim(); // Ignore trailing white space
				BST<Song> results = this.amse.searchByKeyword(query);
				if (results != null) {
					this.searchResultsMenu(query, results, searchMenu);
					resetMenu = true;
				} else {
					System.out.println("No songs found with lyrics containing the keyword '" + query + "'.");
				}
				break;
			case 3:
				// System.out.println("recordSearch() Debug: 'Return to Main Menu' was
				// selected.");
				returnToParent = true;
				break;
			default:
				// No-op; input validation ensures
				// this default is never used, but
				// including for best practice
				break;
			}
			if (!returnToParent && !resetMenu) {
				System.out.print("\nPress \"Enter\" to return to the search menu. ");
				keyboardInput.nextLine();
			}
		}
		this.clearConsole();
	}

	/**
	 * Displays the results menu from a keyword search
	 * 
	 * @param keyword the search query
	 * @param results the search results
	 * @param menu    the parent menu context
	 */
	private void searchResultsMenu(String keyword, BST<Song> results, Menu menu) {
		String appTitle = menu.getAppName();
		String menuTitle = "SEARCH RESULTS MENU";
		String[] menuRows;

		// Get result rows from results
		int resultCount = results.getSize();

		// Build the menu rows using the BST results
		menuRows = new String[resultCount + 1]; // + 1 for Return option
		int index = -1;
		for (String row : results.inOrderString().split("\n")) {
			if (row.trim().startsWith("Title: ")) {
				menuRows[++index] = row.split(": ")[1].trim();
			}
		}
		menuRows[++index] = "Return to the Search Menu";

		// Generate the results summary
		String plural = "s";
		String article = "a";
		if (resultCount == 1) {
			plural = "";
			article = "the";
		}
		String resultsSummary = String.format(
				"   %d song%s found with lyrics containing the keyword '%s'.%n"
						+ "   Enter %s number from the menu above to see more information about %s%n"
						+ "   result, or you can enter '%d' to return to the Search Menu.",
				resultCount, plural, keyword, article, article, menuRows.length);

		// Pass null for the Scanner since its static and was set previously
		Menu searchResultsMenu = new Menu(null, appTitle, menuTitle, menuRows);
		searchResultsMenu.setBorderPattern(menu.getBorderPattern());

		boolean returnToParent = false;
		while (!returnToParent) {
			this.clearConsole();
			searchResultsMenu.display();
			System.out.println(resultsSummary);
			int userSelection = Menu.getSelection(menuRows.length);
			if (userSelection == resultCount + 1) {
				returnToParent = true;
			} else {
				Song songChoice = new Song(menuRows[userSelection - 1]);
				System.out.println(results.search(songChoice, new SongNameComparator()));
			}

			if (!returnToParent) {
				System.out.print("\nPress \"Enter\" to return to the Search Results menu. ");
				keyboardInput.nextLine();
			}
		}
		this.clearConsole();
	}

	/**
	 * Modifies a record in the search engine.
	 * 
	 * @param menu the parent menu context
	 */
	private void modifyMenu(Menu menu) {
		System.out.print("Enter the primary key (a song's exact title) of a song you would like to modify: ");
		String title = keyboardInput.nextLine().trim();

		Song song = amse.getSong(title);
		if (song == null) {
			System.out.printf("The song titled '%s' could not be found in the search engine.%n", title);
			System.out.print("\nPress \"Enter\" to return to the Main Menu. ");
			keyboardInput.nextLine();
			return;
		}
		amse.deleteSong(title, true); // Remove the original entry /quiet

		// Display modify menu
		String appTitle = menu.getAppName();
		String menuTitle = "MODIFY SONG MENU";
		String[] menuRows = { "Update the Title", "Update the Year", "Update the Album", "Update the Lyrics",
				"Return to Main Menu" };

		// Pass null for the Scanner since its static and was set previously
		Menu modifyMenu = new Menu(null, appTitle, menuTitle, menuRows);
		modifyMenu.setBorderPattern(menu.getBorderPattern());

		boolean returnToParent = false;
		while (!returnToParent) {
			this.clearConsole();
			modifyMenu.display();
			System.out.printf("Modifying record for '%s'.%n", title);
			int userSelection = Menu.getSelection(menuRows.length);
			switch (userSelection) {
			case 1:
				System.out.print("Enter a new Title: ");
				song.setTitle(keyboardInput.nextLine());
				title = song.getTitle(); // Update the localized title
				break;
			case 2:
				System.out.print("Enter a new Year: ");
				song.setYear(keyboardInput.nextInt());
				keyboardInput.nextLine(); // Clear new line out
				break;
			case 3:
				System.out.print("Enter a new name for the Album: ");
				song.setAlbum(keyboardInput.nextLine());
				break;
			case 4:
				System.out.print("Enter new Lyrics: ");
				song.setLyrics(keyboardInput.nextLine());
				break;
			case 5:
				returnToParent = true;
				break;
			default:
				// No-op; input validation ensures
				// this default is never used, but
				// including for best practice
				break;
			}

			if (!returnToParent) {
				System.out.print("\nPress \"Enter\" to return to the Modify Menu. ");
				keyboardInput.nextLine();
			}
		}
		amse.addSong(song);
		this.clearConsole();
	}

	/**
	 * Attempts to clear the console environment using ANSI escape-sequences; In
	 * case the environment cannot process the sequences, a message and 5 new lines
	 * are printed to act as a pseudo-clear. For debugging or testing, set the class
	 * variable this.debugNoAnsiClear to true for an early return.
	 */
	public void clearConsole() {
		// Start with fall back in case the clear fails
		// and evaluate the debug condition
		System.out.print("[SIMULATING SCREEN CLEAR...]");
		System.out.print("\n\n\n\n\n");
		if (this.debugNoAnsiClear) {
			return;
		}

		// Attempt to clear the console using Ansi escape sequences
		System.out.print("\033[1J"); // Clear all from top of screen to cursor
		System.out.print("\033[H"); // Reposition cursor to top left (1,1)
		System.out.flush();
	}
}

/**
 * A utility class to aid in the import and parsing of song data. This class is
 * not intended to be instantiated.
 */
class ImportSongs {
	/**
	 * Imports and parses external song data
	 * 
	 * @return the Song objects in an array
	 * @throws Exception up the stack when a general exception is caught
	 */
	public static Song[] fetchSongs() throws Exception {
		final String DIR_PATH = "./data/";
		final String PREFIX = "song";
		final String SUFFIX = ".txt";
		String rawData = "";

		// Fetch the raw data from the files
		try (FileHandler fh = new FileHandler()) {
			rawData = fh.readDir(DIR_PATH, PREFIX, SUFFIX);
		} catch (Exception e) {
			throw new Exception("parse(): Failed to fetch data.", e);
		}

		// Read the song count and parse the data
		int songCount = 0;
		Song[] songs;
		try (Scanner stringScanner = new Scanner(rawData)) {
			// Get song count by consuming text,
			// up until first int
			while (!stringScanner.hasNextInt()) {
				stringScanner.next();
			}
			songCount = stringScanner.nextInt();
			if (stringScanner.hasNextLine()) {
				stringScanner.nextLine();
			}

			// Parse the data
			songs = new Song[songCount];
			int i = -1;
			while (stringScanner.hasNextLine()) {
				Song song;
				stringScanner.nextLine(); // Skip over file start stamp
				String title = stringScanner.nextLine();
				int year = Integer.parseInt(stringScanner.nextLine());
				String album = stringScanner.nextLine();
				String lyrics = stringScanner.nextLine();
				stringScanner.nextLine(); // Skip over file end stamp

				song = new Song(title, year, album, lyrics);
				songs[++i] = song;
			}
		} catch (Exception e) {
			throw new Exception("parse(): Failed to parse data.", e);
		}

		return songs;
	}

}