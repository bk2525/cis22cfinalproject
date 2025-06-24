import java.util.Objects;

/**
 * Purpose: a wordID class to hold the IDs of unique words for inverted indexing.
 * @author Naman Kumar 
 * @author Jeses Louis
 * CIS 22C, Final Project
 */

/**
 * Maps a lowercase word to a unique integer ID for inverted indexing. Equality
 * and hashcode are based only on the word (case-insensitive).
 */
public class WordID {
	private final String word;
	private final int id;

	/**
	 * constructor that takes in and sets the word and its unique id
	 * 
	 * @param word the word to set the object to
	 * @param id   the id to set the object to
	 */
	public WordID(String word, int id) {
		this.word = word.toLowerCase();
		this.id = id;
	}

	/**
	 * returns the word
	 * 
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * returns the id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * returns whether or not two wordID objects are equal. They are equal if they
	 * are the same word.
	 * 
	 * @return whether or not they are equal
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof WordID))
			return false;
		WordID other = (WordID) o;
		return word.equalsIgnoreCase(other.word);
	}

	/**
	 * returns the hashcode of the word
	 * 
	 * @return the hashcode of the word
	 */
	@Override
	public int hashCode() {
		return Objects.hash(word.toLowerCase());
	}

	/**
	 * returns the wordID in a string form by appending id to word with a ->
	 * 
	 * @return the wordID in string form
	 */
	@Override
	public String toString() {
		return word + "->" + id;
	}
}
