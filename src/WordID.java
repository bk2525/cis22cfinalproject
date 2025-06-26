/**
 * WordID.java
 * @author Naman Kumar 
 * @author Jeses Louis
 * CIS 22C, Final Project
 */
import java.util.Objects;

/**
 * Maps a lowercase word to a unique integer ID for inverted indexing. Equality
 * and hashcode are based only on the word (case-insensitive).
 */
public class WordID {
	/**
	 * The word
	 */
	private final String word;
	/**
	 * The word's unique ID
	 */
	private final int id;

	/**
	 * Constructor that takes in and sets the word and its unique id
	 * 
	 * @param word the word to set the object to
	 * @param id   the id to set the object to
	 */
	public WordID(String word, int id) {
		this.word = word.toLowerCase();
		this.id = id;
	}

	/**
	 * Returns the word
	 * 
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * Returns the id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns whether the two wordID objects are equal.
	 * They are equal if they are the same word.
	 * 
	 * @return whether they are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WordID))
			return false;
		return word.equalsIgnoreCase(((WordID) o).word);
	}

	/**
	 * Returns the hashcode of the word
	 * 
	 * @return the hashcode of the word
	 */
	@Override
	public int hashCode() {
		return Objects.hash(word.toLowerCase());
	}

	/**
	 * Returns the wordID in a string form by appending id to word with a ->
	 * 
	 * @return the wordID in string form
	 */
	@Override
	public String toString() {
		return word + "->" + id;
	}
}
