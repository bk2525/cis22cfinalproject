import java.util.*;
import java.io.File;

/**
 * Purpose: create a search engine to perform operations like adding, deleting, modifying, and looking up songs.
 * @author Naman Kumar & Jeses Louis
 * CIS 22C, Final Project
 */

/**
 * SearchEngine builds an inverted index using BSTs and allows keyword searches
 * over Song lyrics. Includes direct song access via HashTable and supports
 * adding/deleting songs.
 * 
 */
public class SearchEngine {
	// maps each unique word to its WordID (word + assigned ID)
	private final HashTable<WordID> wordMap;
	// stores all songs by title for direct O(1) access
	private final HashTable<Song> songsMap;
	// for each word ID, a BST of Songs containing that word (inverted index)
	private final ArrayList<BST<Song>> invertedIndex;

	/**
	 * Constructs a SearchEngine with default capacity
	 */
	public SearchEngine() {
		this.wordMap = new HashTable<>(4096);
		this.songsMap = new HashTable<>(4096);
		this.invertedIndex = new ArrayList<>();
	}

	/**
	 * creates a new Song object from information from the given file. If the file
	 * does not exist, it prints an error message and does nothing. returns true if the operation was successful
	 * 
	 * @param fileName the file containing song-information
	 * @return true if operation was successful, false if not
	 */
	public boolean importSong(String fileName) {
		Song song;
		try {
			song = createSongFromFile(fileName);
		} catch (Exception e) {
			System.out.printf("The file '%s' could not be found or was locked for reading.%n", fileName);
			return false;
		}
		if (songsMap.contains(song)) {
			System.out.println("There already exists a song in the engine with the title " + song.getTitle()
					+ ". Please choose another file. ");
			return false;
		}
		indexSong(song);
		System.out.printf("Imported the song titled: %s%n" + "There are now %d songs stored in the search engine.%n",
				song.getTitle(), this.getSongCount());
		return true;
	}

	/**
	 * adds a song to the engine from a passed in Song object
	 * 
	 * @param song the song object to add
	 */
	public void addSong(Song song) {
		indexSong(song);
	}

	/**
	 * Deletes a specified song from the database
	 * 
	 * @param title    the title of the song to delete
	 * @param isModify true if the purpose of deleting is to modify an existing
	 *                 entry, i.e the song will be readded later; suppresses print
	 *                 messages
	 */
	public void deleteSong(String title, Boolean isModify) {
		// Find the song in the database
		Song songToDelete = new Song(title);
		Song existingSong = songsMap.get(songToDelete);

		if (existingSong == null) {
			System.out.printf("The song titled '%s' could not be found in the search engine.%n", title);
			return;
		}

		// Remove the song from primary storage
		songsMap.delete(existingSong);

		// Remove from all inverted index entries
		ArrayList<String> words = new ArrayList<>();
		Scanner stringScanner = new Scanner(existingSong.getFilteredLyrics());
		while (stringScanner.hasNext()) {
			String word = stringScanner.next();
			if (!words.contains(word)) {
				words.add(word);
				WordID wordId = wordMap.get(new WordID(word, 0));
				if (wordId != null) {
					BST<Song> songTree = invertedIndex.get(wordId.getId());
					songTree.remove(existingSong, new SongNameComparator());

					// Remove words from wordMap that are unique to this song
					if (songTree.isEmpty()) {
						wordMap.delete(wordId);
						invertedIndex.set(wordId.getId(), null);
					}
				}
			}
		}

		// Print a summary message if removing permanently,
		// but print nothing if this is to modify an existing record
		if (!isModify) {
			System.out.printf("Removed the song titled: %s%n" + "There are now %d songs stored in the search engine.%n",
					existingSong.getTitle(), this.getSongCount());
		}
	}

	/**
	 * Creates a Song object from information from a given file
	 * 
	 * @param fileName the name of the file to read from containing the song
	 *                 information
	 * @return the created song object
	 * @throws Exception if the file name is not a valid file, or the file does not
	 *                   adhere to Song file rules.
	 */
	private Song createSongFromFile(String fileName) throws Exception {
		try (Scanner fileScanner = new Scanner(new File(fileName))) {
			String title = fileScanner.nextLine();
			int year = Integer.parseInt(fileScanner.nextLine());
			String album = fileScanner.nextLine();
			StringBuilder lyrics = new StringBuilder();
			while (fileScanner.hasNextLine()) {
				lyrics.append(fileScanner.nextLine()).append(" ");
			}
			return new Song(title, year, album, lyrics.toString().trim());
		} catch (Exception e) {
			throw new Exception("createSongFromFile(): Error reading song file: " + fileName, e);
		}
	}

	/**
	 * Populates the data structures with the given song
	 * 
	 * @param songs the songs whose information will be used to populate the data
	 *              structure
	 */
	public void indexSong(Song song) {
		songsMap.add(song);
		Scanner stringScanner = new Scanner(song.getFilteredLyrics());
		while (stringScanner.hasNext()) {
			String word = stringScanner.next();
			WordID wordId = wordMap.get(new WordID(word, 0));
			if (wordId == null) {
				// New word detected, assign with new ID;
				// update wordMap and invertedIndex
				wordId = new WordID(word, invertedIndex.size());
				wordMap.add(wordId);
				invertedIndex.add(new BST<>());
			}
			invertedIndex.get(wordId.getId()).insert(song, new SongNameComparator());
		}
	}

	/**
	 * returns the song with the given name, null if it does not exist
	 * 
	 * @param name the name of the song to get
	 * @return the song if it exists, null if not
	 */
	public Song getSong(String name) {
		return songsMap.get(new Song(name, 0, null, null));
	}

	/**
	 * Gets the number of songs in the HashTable Used as a statistic
	 * 
	 * @return the number of songs in the HashTable songsMap
	 */
	public int getSongCount() {
		return this.songsMap.getNumElements();
	}

	/**
	 * Returns the number of unique words Used as a statistic
	 * 
	 * @return the number of unique words in the HashTable wordMap
	 */
	public int getTotalUniqueWords() {
		return this.wordMap.getNumElements();
	}

	/**
	 * Gets the average Year for all the songs used for the statistic
	 * 
	 * @return a year
	 */
	public int getAverageYear() {
		int sumYear = 0;
		ArrayList<Song> songs = songsMap.getAllElements();
		for (Song song : songs) {
			sumYear += song.getYear();
		}
		return sumYear / songs.size();
	}

	/**
	 * searches for a song by the primary key (title)
	 * 
	 * @param key the title of the song to search for
	 */
	public void searchByKey(String key) {
		Song song = new Song(key, 0, null, null);
		Song validSong = songsMap.get(song);
		if (validSong == null) {
			System.out.println("There are no songs matching the primary key: " + key);
			return;
		}
		System.out.println(validSong);
	}

	/**
	 * returns a BST of songs whose lyrics contain the word passed in
	 * 
	 * @param keyword the word to search for
	 * @return a BST of songs with the given word
	 */
	public BST<Song> searchByKeyword(String keyword) {
		WordID wordId = wordMap.get(new WordID(keyword, 0));
		if (wordId == null) {
			return null;
		}
		BST<Song> resultTree = invertedIndex.get(wordId.getId());
		return resultTree;
	}

	/**
	 * Returns a string representation of the Songs in the search engine.
	 * 
	 * @return A formatted string containing all the records.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		ArrayList<Song> songs = this.songsMap.getAllElements();
		int count = 0;
		for (Song song : songs) {
			result.append(String.format("[SONG RECORD #%d]%n%s%n", ++count, song.toString()));
		}

		return result.toString();
	}
}
