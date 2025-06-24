import java.util.*;
import java.io.File;

/**
 * SearchEngine builds an inverted index using BSTs and allows keyword searches over Song lyrics.
 * Includes direct song access via HashTable and supports adding/deleting songs.
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
        this.wordMap = new HashTable<>(4096); // TODO sizing should be based on data size, not hardcoded
        this.songsMap = new HashTable<>(4096); // TODO sizing should be based on data size, not hardcoded
        this.invertedIndex = new ArrayList<>();
    }

    // Imports a song from file (Javadoc needed)
    public void importSong(String fileName) {
        Song song;
        try {
            song = createSongFromFile(fileName);
        } catch (Exception e) {
            System.out.printf(
                "The file '%s' could not be found or was locked for reading.%n", fileName);
            return;
        }

        songsMap.add(song);
        indexSong(song);
        System.out.printf(
            "Imported the song titled: %s%n"
            + "There are now %d songs stored in the search engine.%n",
            song.getTitle(), this.getSongCount());
    }

    // Adds a song to the system (Javadoc needed)
    public void addSong(Song song) {
    	songsMap.add(song);
    	indexSong(song);
    }

    // Param: boolean true if deleteting to modify an existing entry; suppresses print messages
    public void deleteSong(String title, Boolean isModify) {
        // Find the song in the database
        Song songToDelete = new Song(title);
        Song existingSong = songsMap.get(songToDelete);

        if (existingSong == null) {
            System.out.printf(
                "The song titled '%s' could not be found in the search engine.%n", title);
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
            System.out.printf(
                "Removed the song titled: %s%n"
                    + "There are now %d songs stored in the search engine.%n",
                existingSong.getTitle(), this.getSongCount());
        }
    }

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

    
    public void buildIndex(Song[] songs) {
        int nextId = 0;
        for (Song song : songs) {
            songsMap.add(song);
            Scanner stringScanner = new Scanner(song.getFilteredLyrics());
            while (stringScanner.hasNext()) {
                String word = stringScanner.next();
                if (!wordMap.contains(new WordID(word, 0))) {
                    wordMap.add(new WordID(word, nextId++));
                    invertedIndex.add(new BST<>());
                }
            }
        }

        for (Song song : songs) {
            indexSong(song);
        }
    }
  
    private void indexSong(Song song) {
        ArrayList<String> words = new ArrayList<>();
        Scanner stringScanner = new Scanner(song.getFilteredLyrics());
        while (stringScanner.hasNext()) {
            String word = stringScanner.next();
            if (!words.contains(word)) {
                words.add(word);
                WordID wordId = wordMap.get(new WordID(word, 0));
                if (wordId == null) {
                    // New word detected, assign with new ID;
                    // update wordMap and invertedIndex
                    wordId = new WordID(word, invertedIndex.size());
                    wordMap.add(wordId);
                    invertedIndex.add(new BST<>());
                	// System.out.println(invertedIndex.get(wordId.getId()).getSize());
                }
                invertedIndex.get(wordId.getId()).insert(song, new SongNameComparator());
            }
        }
    }
    
    public Song getSong(String name) {
    	return songsMap.get(new Song(name, 0, null, null));
    }

    /**
     * Gets the number of songs in the HashTable
     * Used as a statistic
     * @return the number of songs in the HashTable songsMap
     */
    public int getSongCount() {
        return this.songsMap.getNumElements();
    }

    /**
     * Returns the number of unique words
     * Used as a statistic
     * @return the number of unique words in the HashTable wordMap
     */
    public int getTotalUniqueWords() {
        return this.wordMap.getNumElements();
    }

    /**
     * Gets the average Year for all the songs
     * used for the statistic
     * @return a year
     */
     public int getAverageYear() {
        int sumYear = 0;
        ArrayList<Song> songs = songsMap.getAllElements();
        for (Song song: songs) {
            sumYear += song.getYear();
        }
        return sumYear / songs.size();
     }

    public void searchByKey(String key) {
        Song song = new Song(key, 0, null, null);;
        Song useful = songsMap.get(song);
        if (useful == null) {
            System.out.println("There are no songs matching the primary key: " + key);
            return;
        }
        System.out.println(useful);
    }

    public BST<Song> searchByKeyword(String keyword) {
        WordID wordId = wordMap.get(new WordID(keyword, 0));
        if (wordId == null) {
            return null;
        }
        BST<Song> resultTree = invertedIndex.get(wordId.getId());
        return resultTree;
    }
}