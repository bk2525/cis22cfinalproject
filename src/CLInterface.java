/**
 * CLInterface.java
 * @author Stephen Kyker
 * (suplemental authoring denoted in method javadoc comments)
 * CIS 22C, Group Project
 */
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
     * Scanner for capturing user input via keyboard.
     * This Scanner is intended to be shared across all instances,
     * therefore the object and its wrapped stream should not be
     * closed manually or via AutoCloseable (e.g., try-with-resources).
     */
    private static final Scanner keyboardInput = new Scanner(System.in);
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
            System.err.println(e1.getMessage());
            System.err.print(
                "CLInterface(): The program encountered a fatal "
                + "error. Would you like to restart (Y/N)? ");
            try {
                if (keyboardInput.nextLine().equalsIgnoreCase("Y")) {
                    return true;
                }
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
                System.err.println(
                    "CLInterface(): Input capture failed. Exiting.");
            }
        }
        return false;
    }

    /**
     * Logic controller for a user session, which integrates the
     * frontend Menu class with the backend component classes.
     */
    private void runSession() {
        try {
            this.amse = new SearchEngine();
            this.amse.buildIndex(ImportSongs.fetchSongs());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        int userSelection;
        String appTitle = "Adele Music Search Engine";
        String mainMenuTitle = "Main Menu";
        String[] mainMenuRows = {
            "Upload a new record",
            "Delete a record",
            "Search for a record",
            "Modify or update a record",
            "Statistics",
            "Quit"
        };

        // Launch the main menu loop and capture user input
        System.out.println("[LAUNCHING THE ADELE MUSIC SEARCH ENGINE...]\n\n");
        Menu mainMenu = new Menu(CLInterface.keyboardInput, appTitle, mainMenuTitle, mainMenuRows);
        boolean exit = false;
        while (!exit) {
            mainMenu.display();
            userSelection = Menu.getSelection(mainMenuRows.length);

            if (this.actionHandler(userSelection, mainMenu)) {
                exit = true;
            }
        }

        // Write all records to a file of user choice and exit
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
        switch (userSelection) {
            case 1:
                System.out.println("actionHandler() Debug: 'Upload a new record' was selected.");
                this.addRecord();
                break;
            case 2:
                System.out.println("actionHandler() Debug: 'Delete a record' was selected.");
                this.deleteRecord();
                break;
            case 3:
                System.out.println("actionHandler() Debug: 'Search for a record' was selected.");
                if (this.amse == null) {
                    throw new NullPointerException("actionHandler(): Class member 'amse' cannot be null.");
                }
                this.recordSearch(mainMenu);
                break;
            case 4:
                System.out.println("actionHandler() Debug: 'Modify or update a record' was selected.");
                //TODO
                break;
            case 5:
                System.out.println("actionHandler() Debug: 'Statistics' was selected.");
                //TODO
                // Display 3 different statistics about the data
                break;
            case 6:
                System.out.println("actionHandler() Debug: 'Quit' was selected.");
                System.out.println("\nExiting program...\n");
                return true;
            default:
                // No-op; input validation ensures
                // this default is never used, but
                // including for best practice
                break;
        }

        System.out.println("\nReturning to main menu...\n");
        return false;
    }
    
    private void addRecord() {
        System.out.print("Enter the name of a file containing a song you'd like to add: ");
        amse.addSong(keyboardInput.nextLine());
    }
    
    private void deleteRecord() {
        System.out.print("Enter the exact name of a song you'd like to delete: ");
        amse.deleteSong(keyboardInput.nextLine());
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

        // We can pass null for the Scanner parameter since its static and was already set earlier
        Menu searchMenu = new Menu(null, appTitle, menuTitle, menuRows);
        boolean returnToParent = false;
        while (!returnToParent) {
            searchMenu.display();
            int userSelection = Menu.getSelection(menuRows.length);
            switch (userSelection) {
                case 1:
                    System.out.println("recordSearch() Debug: 'Find and display a record by primary key' was selected.");
                    System.out.print("Please enter the exact title of a song to search for: ");
                    this.amse.nameSearch(keyboardInput.nextLine());
                    System.out.println();
                    break;
                case 2:
                    System.out.println("recordSearch() Debug: 'Find and display records using keywords' was selected.");
                    System.out.print("Please enter the keyword to search for: ");
                    this.amse.keyWordSearch(keyboardInput.nextLine());
                    System.out.println(); // line break
                    break;
                case 3:
                    System.out.println("recordSearch() Debug: 'Return to Main Menu' was selected.");
                    returnToParent = true;
                default:
                    // No-op; input validation ensures
                    // this default is never used, but
                    // including for best practice
                    break;
            }
        }
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
     * @throws Exception if a general exception is caught when fetching
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
            throw new Exception(String.format(
                "parse(): Failed to fetch data.%n"
                + "  Reason: %s%n",
                e.getMessage()));
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
            throw new Exception(String.format(
                "parse(): Failed to parse data.%n"
                + "  Reason: %s%n",
                e.getMessage()));
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