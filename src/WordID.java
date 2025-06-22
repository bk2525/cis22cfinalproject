/**
 * Simple pair of a word and its assigned integer ID.
 * @author Jeses Louis
 */
public class WordID {
    private String word;
    private int id;

    /**
     * @param word the keyword
     * @param id   the integer index for that keyword in the inverted index
     */
    public WordID(String word, int id) {
        this.word = word.toLowerCase();
        this.id   = id;
    }

    /** Returns the keyword. */
    public String getWord() {
        return word;
    }

    /** Returns the assigned integer ID. */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WordID)) return false;
        WordID other = (WordID) obj;
        return word.equals(other.word);
    }

    @Override
    public int hashCode() {
        // Hash only by the word, so lookup only requires matching word.
        return word.hashCode();
    }

    @Override
    public String toString() {
        return word + "→" + id;
    }
}
