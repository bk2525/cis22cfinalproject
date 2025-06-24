import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Represents a single Adele song, storing its title, album, year, and lyrics.
 * Implements Comparable based on the title (case-insensitive).
 * @author Kaylee Bui
 * @author Stephen Lin
 * @author Stephen Kyker
 * CIS 22C, Group Project
 */
public class Song implements Comparable<Song> {
    private String title;  // Unique key
    private int year;
    private String album;
    private String unfilteredLyrics;
    private String filteredLyrics;


    /** CONSTRUCTORS **/

    /**
     * Constructs a new Song object for search purposes;
     * songCount and songNumber do not get updated as this
     * creates a dummy object only.
     * @param title the unique title of the song
     */
    public Song(String title) {
        this.title = title;

        // Dummy values:
        this.year = 0;
        this.album = "";
        this.unfilteredLyrics = "";
        this.filteredLyrics = "";
    }

    /**
     * Constructs a new Song object with all fields initialized.
     * @param title the unique title of the song
     * @param year the release year of the song
     * @param album the album name
     * @param lyrics the full lyrics of the song
     */
    public Song(String title, int year, String album, String lyrics) {
        this.title = title;
        this.year = year;
        this.album = album;
        this.unfilteredLyrics = lyrics;
        this.filteredLyrics = removeWords(lyrics);
    }

    /** ACCESSORS **/

    /**
     * Returns the title of the song.
     * @return The song's title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the album the song belongs to.
     * @return The song's album.
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Returns the release year of the song.
     * @return The song's release year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the unfiltered lyrics of the song.
     * @return The song's unfiltered lyrics.
     */
    public String getUnfilteredLyrics() {
        return unfilteredLyrics;
    }

    /**
     * Returns the filtered lyrics of the song.
     * @return The song's filtered lyrics.
     */
    public String getFilteredLyrics() {
        return filteredLyrics;
    }

    /** MUTATORS **/

    /**
     * Sets a new title for the song.
     * @param title The new album title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets a new album for the song.
     * @param album The new album title.
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    /**
     * Sets a new release year for the song.
     * @param year The new release year.
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * Sets new lyrics for the song.
     * Note: sets both unfiltered and filtered variables at the same time
     * @param lyrics The new lyrics.
     */
    public void setLyrics(String lyrics) {
        this.unfilteredLyrics = lyrics;
        this.filteredLyrics = removeWords(lyrics);
    }

    /** ADDITIONAL OPERATIONS **/

    /**
     * Compares this song to another based on their titles, ignoring case.
     * @param other the other Song to compare to
     * @return a negative, zero, or positive number
     */
    @Override
    public int compareTo(Song other) {
        return title.compareToIgnoreCase(other.title);
    }

    /**
     * Returns the hash code of the song, based on its lowercase title.
     * @return the hash code
     */
   @Override
   public int hashCode() {
       return title.toLowerCase().hashCode();
   }

    /**
     * Compare this title to another object for equality
     * @param obj The reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Song)) {
            return false;
        }
        Song song = (Song) obj;
        return title.equalsIgnoreCase(song.title);
    }

    /**
     * Returns a string representation of the Song object.
     * @return A string containing the song's title, album, and year.
     */
    @Override
    public String toString() {
        final int MAX_WIDTH = 72;

        StringBuilder wrappedLyrics = new StringBuilder();
        String[] words = this.unfilteredLyrics.split("\\s+");
        String attributeName = "Lyrics: ";
        int currentLineLength = attributeName.length(); // Have to account for the attribute name that's returned later
        int indentWidth = currentLineLength; // Indent each line to format the block respective to the attribute name

        // Iterate each word, formatting in accordance with the block formatting
        for (String word : words) {
            // Add a new line and indent if running up to max width, otherwise just add a space
            if (currentLineLength + word.length() + 1 > MAX_WIDTH) {
                wrappedLyrics.append('\n');
                wrappedLyrics.append(" ".repeat(indentWidth));
                currentLineLength = indentWidth;
            } else {
                wrappedLyrics.append(' ');
                currentLineLength++; // Accounting for the added space
            }

            // Add the new word to the string
            wrappedLyrics.append(word);
            currentLineLength += word.length();
        }

        return String.format(
            " Title: %s%n"
            + "  Year: %d%n"
            + " Album: %s%n"
            + "Lyrics: %s%n",
            this.title,
            this.year,
            this.album,
            wrappedLyrics.toString().trim());
    }
    
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
     * Takes a string of input and removes the words matching
     * ImportSongs.REMOVED_WORDS. Created by Stephen Lin; contact for
     * questions.
     * @param input the String to modify
     * @return the modified String
     */
    public static String removeWords(String input) {
    	if (input == null) {
    		return null;
    	}
    	
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

/**
 * A comparator that compares Song objects based on their titles.
 */
class SongNameComparator implements Comparator<Song>{

    /**
     * Compares two Song objects by their titles using case-sensitive comparison.
     *
     * @param o1 the first Song to be compared
     * @param o2 the second Song to be compared
     * @return a negative integer if o1's title comes before o2's title,
     *         zero if the titles are equal, or a positive integer if o1's title comes
     *         after o2's title
     */
	@Override
	public int compare(Song o1, Song o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}
	
}
