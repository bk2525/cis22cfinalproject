/**
 * CLInterface.java
 * @author Stephen Kyker
 * (suplemental authoring denoted in method javadoc comments)
 * CIS 22C, Group Project
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Controls the command line interface by integrating the Menu class
 * with the various components of the program; this class does not
 * require any arguments to instantiate it.
 */
public class CLInterface {
    /**
     * If true, ANSI escape-sequence terminal clearing will be disabled.
     * This is intended only for running tests and should not be enabled by default.
     */
    boolean debugNoAnsiClear = false;

    /**
     * Scanner for capturing user input via keyboard.
     * This Scanner is intended to be shared across all instances,
     * therefore the object and its wrapped stream should not be
     * closed manually or via AutoCloseable (e.g., try-with-resources).
     */
    private static final Scanner keyboardInput = new Scanner(System.in);

    /**
     * The SearchEngine object that is used by the members of this class.
     */
    private SearchEngine amse;

    /**
     * Default Constructor - does not do anything more than the implicit default constructor,
     * but is explicitly implemented for visibility.
     */
    public CLInterface() {}

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
     * @return true if session finished unsuccessfully and should be
     *         restarted; false for any other exit condition
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
                "CLInterface(): The program encountered a fatal "
                + "error. Would you like to restart (Y/N)? ");
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
     * Logic controller for a user session, which integrates the
     * frontend Menu class with the backend component classes.
     * @throws Exception up the stack when a general exception is caught
     */
    private void runSession() throws Exception {
        // Spin up the search engine and import the songs from file
        this.amse = new SearchEngine();
        this.amse.buildIndex(ImportSongs.fetchSongs());

        // Clear the screen and begin the program
        this.clearConsole();
        System.out.println("[LAUNCHING THE ADELE MUSIC SEARCH ENGINE...]\n");

        // Set up the main menu variables
        int userSelection;
        String appTitle = "Adele Music Search Engine";
        String mainMenuTitle = "Main Menu";
        String borderPattern = "(*)";
        String[] mainMenuRows = {
            "Upload a new record",
            "Delete a record",
            "Search for a record",
            "Modify or update a record",
            "Statistics",
            "Quit"
        };

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

        // TODO: Write all records to a file of user choice and exit
        return;
    }

    /**
     * Action handler for the Main Menu
     * @param userSelection the menu selection made by the user
     * @param mainMenu the menu context
     * @return T if user would like to exit, F if not.
     * @throws NullPointerException when this.amse == null
     */
    private boolean actionHandler(int userSelection, Menu mainMenu) {
        if (userSelection != 3) { // Don't print if entering the search sub menu
            System.out.print("[" + mainMenu.getRows()[userSelection - 1] + "]\n");
        }
        switch (userSelection) {
            case 1:
                // System.out.println("actionHandler() Debug: 'Upload a new record' was selected.");
                this.addRecord();
                break;
            case 2:
                // System.out.println("actionHandler() Debug: 'Delete a record' was selected.");
                this.deleteRecord();
                break;
            case 3:
                // System.out.println("actionHandler() Debug: 'Search for a record' was selected.");
                this.recordSearch(mainMenu);
                return false;
            case 4:
                // System.out.println("actionHandler() Debug: 'Modify or update a record' was selected.");
                this.modifyRecord();
                break;
            case 5:
                // System.out.println("actionHandler() Debug: 'Statistics' was selected.");
                System.out.println("Statistic 1: Total number of songs: " + amse.getSongCount());
                System.out.println("Statistic 2: Unique words: " + amse.getTotalUniqueWords());
                System.out.println("Statistic 3: Average year of all songs " + amse.getAverageYear());
                break;
            case 6:
                // System.out.println("actionHandler() Debug: 'Quit' was selected.");
                System.out.println("\n[Exiting program...]\n");
                return true;
            default:
                // No-op; input validation ensures
                // this default is never used, but
                // including for best practice
                break;
        }

        System.out.print("\nPress \"Enter\" to return to the main menu. ");
        keyboardInput.nextLine();
        this.clearConsole();
        return false;
    }

    // Adds a record (Javadoc needed)
    private void addRecord() {
        System.out.print("Enter the name of a file containing a song you would like to add: ");
        amse.importSong("./data/" + keyboardInput.nextLine()); // Allow trailing white space
    }

    // Deletes a record (Javadoc needed)
    private void deleteRecord() {
        System.out.print("Enter the primary key (a song's exact title) of a song you would like to delete: ");
        this.amse.deleteSong(keyboardInput.nextLine().trim()); // Ignore trailing white space
    }

    /**
     * Searches for a single record using the primary key,
     * or for several records using keywords.
     * @param menu the parent menu context
     */
    private void recordSearch(Menu menu) {
        String appTitle = menu.getAppName();
        String menuTitle = "Search Menu";
        String[] menuRows = {
            "Find and display a record by primary key",
            "Find and display records using keywords",
            "Return to Main Menu"
        };

        // Pass null for the Scanner since its static and was set previously
        Menu searchMenu = new Menu(null, appTitle, menuTitle, menuRows);
        searchMenu.setBorderPattern(menu.getBorderPattern());

        boolean returnToParent = false;
        while (!returnToParent) {
            this.clearConsole();
            searchMenu.display();
            int userSelection = Menu.getSelection(menuRows.length);
            switch (userSelection) {
                case 1:
                    // System.out.println("recordSearch() Debug: 'Find and display a record by primary key' was selected.");
                    System.out.print("Please enter a primary key (a song's exact title) to search for a record: ");
                    this.amse.searchByKey(keyboardInput.nextLine().trim()); // Ignore trailing white space
                    break;
                case 2:
                    //System.out.println("recordSearch() Debug: 'Find and display records using keywords' was selected.");
                    System.out.print("Please enter a keyword to search for one or more records: ");
                    this.amse.searchByKeyword(keyboardInput.nextLine().trim()); // Ignore trailing white space
                    break;
                case 3:
                    // System.out.println("recordSearch() Debug: 'Return to Main Menu' was selected.");
                    returnToParent = true;
                    break;
                default:
                    // No-op; input validation ensures
                    // this default is never used, but
                    // including for best practice
                    break;
            }
            if (!returnToParent) {
                System.out.print("\nPress \"Enter\" to return to the search menu. ");
                keyboardInput.nextLine();
            }
        }
        this.clearConsole();
    }

    // Modifies a record (Javadoc needed)
    private void modifyRecord() {
        System.out.print("Enter the exact name of a song you'd like to modify: ");
        String name = keyboardInput.nextLine();
        
        Song song = amse.getSong(name);
        if (song == null) {
            System.out.printf(
                "The song titled '%s' could not be found in the search engine.%n", name);
        	return;
        }
        amse.deleteSong(name);
        
        int year = song.getYear();
        String album = song.getAlbum();
        String lyrics = song.getLyrics();
        
        System.out.print("enter 1,2,3,4: ");
        int choice = keyboardInput.nextInt();
        keyboardInput.nextLine();
        
        switch (choice) {
        	case 1: //if 1, modify name:
        		System.out.print("Enter a new song name: ");
                name = keyboardInput.nextLine();
                break;
        	case 2: //if 2, modify year:
        		System.out.print("Enter a new year: ");
        		year = keyboardInput.nextInt();
                keyboardInput.nextLine();
                break;
        	case 3: //if 3, modify album
        		System.out.print("Enter a new song album: ");
                album = keyboardInput.nextLine();
                break;
        	case 4: //if 4, modify lyrics
        		System.out.print("Enter new song lyrics: ");
        		lyrics = keyboardInput.nextLine();
        		System.out.println(lyrics);
        		break;
        }
        amse.addSong(new Song(name, year, album, lyrics));
    }



    /**
     * Attempts to clear the console environment
     * using ANSI escape-sequences; In case the environment
     * cannot process the sequences, a message and 5 new
     * lines are printed to act as a pseudo-clear.
     * For debugging or testing, set the class variable
     * this.debugNoAnsiClear to true for an early return.
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
        System.out.print("\033[H");  // Reposition cursor to top left (1,1)
        System.out.flush();
    }
}

/**
 * A utility class to aid in the import and parsing of song data.
 * This class is not intended to be instantiated.
 */
class ImportSongs {
    /**
     * Imports and parses external song data
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


        // Mem cache the unfiltered lyrics for testing purposes
        // and Filter the stop words out
//        int i = -1;
//        String[] unfilteredLyrics = new String[songs.length];
//        for (Song song : songs) {
//            unfilteredLyrics[++i] = song.getLyrics();
//            song.setLyrics(song.getLyrics());
//        }

        // Print imported songs for debugging
        // Commented out for now; remove before prod
        /*
        int count = 0;
        for (Song song : songs) {
            System.out.println(String.format(
                "Data for Adele Song #%d:%n"
                + "            Title: %s%n"
                + "             Year: %d%n"
                + "            Album: %s%n"
                + "Unfiltered Lyrics: %s...%n"
                + "  Filtered Lyrics: %s...%n",
                ++count,
                song.getTitle(),
                song.getYear(),
                song.getAlbum(),
                unfilteredLyrics[count - 1]
                    .substring(0, 32).trim(),
                song.getLyrics()
                    .substring(0, 32).trim()));
        }
        */
        return songs;
    }

    
}