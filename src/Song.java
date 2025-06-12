/**
 * Represents a single Adele song, storing its title, album, year, and lyrics.
 *  UNFINISHED, STILL NEEDS CONSTRUCTORS ADDITIONAL CHANGES
 * @author Kaylee Bui
 */
public class Song {
    private String title;  // Unique key
    private int year;
    private String album;
    private String lyrics;

    /** CONSTRUCTORS **/

    public Song(String title, int year, String album, String lyrics) {
        this.title = title;
        this.year = year;
        this.album = album;
        this.lyrics = lyrics;
    }

    /** ACCESSRORS **/

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
     * Returns the lyrics of the song.
     * @return The song's lyrics.
     */
    public String getLyrics() {
        return lyrics;
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
     * @param lyrics The new lyrics.
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /** ADDITIONAL OPPERATIONS **/

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
        return title.equals(song.title);
    }

    /**
     * Returns a string representation of the Song object.
     * @return A string containing the song's title, album, and year.
     */
    @Override
    public String toString() {
        String albumText;
        if (album != null) {
            albumText = album;
        } else {
            albumText = "N/A";
        }

        return "Title: " + title +
                ", Album: " + albumText +
                ", Year: " + year;
    }
}
