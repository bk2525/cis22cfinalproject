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

    /**
     * Default Constructor - does not do anything, but explicitly
     * implemented for visibility.
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
            ImportSongs.fetchSongs();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

/**
 * A utility class to aid in the import and parsing of song data.
 * This class is not intended to be instantiated.
 */
class ImportSongs {
    /**
     * The words that should not be considered keywords when performing
     * a search. Created by Stephen Lin; contact for questions.
     */
    private static final ArrayList<String> REMOVED_WORDS =
        new ArrayList<>(Arrays.asList(
            "a", "am", "and", "as", "at", "by", "but", "for", "i",
            "id", "if", "ill", "im", "in", "is", "it", "its", "ive",
            "me", "my", "ooh", "of", "oh", "on", "or", "so", "the",
            "that", "they've", "to", "too", "us", "we", "we're", "ya",
            "yeah", "you", "you'll", "you're", "you've", "your", "was",
            "were", "they", "you'd"));

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

        // Filter the stop words out
        for (Song song : songs) {
            song.setLyrics(removeWords(song.getLyrics()));
        }

        // Print imported songs for debugging
        // This will be removed before prod
        int count = 0;
        for (Song song : songs) {
            System.out.println(String.format(
                "Song #%d%n"
                + " Title: %s%n"
                + "  Year: %d%n"
                + " Album: %s%n"
                + "Lyrics: %s...%n",
                ++count,
                song.getTitle(),
                song.getYear(),
                song.getAlbum(),
                song.getLyrics()
                    .substring(0, 32).trim()));
        }

        return songs;
    }

    /**
     * Takes a string of input and removes the words matching
     * ImportSongs.REMOVED_WORDS. Created by Stephen Lin; contact for
     * questions.
     * @return the sanitized String
     */
    public static String removeWords(String input) {
        input = input.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();  //remove all punctuation, case insensitive
        ArrayList<String> words = new ArrayList<>(Arrays.asList(input.split("\\s+"))); //split the input into individual pieces into an arraylist

        ArrayList<String> filteredWords = new ArrayList<>(); //arraylist to hold the valid words

        for (String word : words) {
            boolean shouldRemove = false;
            for (String removed : REMOVED_WORDS) {
                if (word.equals(removed)) {
                    shouldRemove = true;
                    break;
                }
            }
            if (!shouldRemove) {
                filteredWords.add(word);
            }
        }
        return String.join(" ", filteredWords);
    }
}