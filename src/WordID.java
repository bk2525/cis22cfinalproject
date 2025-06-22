import java.util.Objects;

/**
 * Maps a lowercase word to a unique integer ID for inverted indexing.
 * Equality and hashcode are based only on the word (case-insensitive).
 */
public class WordID {
    private final String word;  
    private final int id;

    public WordID(String word, int id) {
        this.word = word.toLowerCase();
        this.id = id;
    }

    public String getWord() { return word; }
    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordID)) return false;
        WordID other = (WordID) o;
        return word.equalsIgnoreCase(other.word);  
    }

    @Override
    public int hashCode() {
        return Objects.hash(word.toLowerCase());  
    }

    @Override
    public String toString() {
        return word + "→" + id;
    }
}