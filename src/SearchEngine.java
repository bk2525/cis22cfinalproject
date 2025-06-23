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
        this.wordMap = new HashTable<>(4096);
        this.songsMap = new HashTable<>(4096);
        this.invertedIndex = new ArrayList<>();
    }

    
    public void addSong(String fileName) {
        Song song = createSongFromFile(fileName);
        songsMap.add(song);
        indexSong(song);
    }

    public void addSong(Song song) {
    	songsMap.add(song);
    	indexSong(song);
    }
    public boolean deleteSong(String title) {
        Song songToDelete = new Song(title, 0, null, null);
        Song existingSong = songsMap.get(songToDelete);
        if (existingSong == null) {
            return false;
        }
        
        // Remove from primary storage
        songsMap.delete(existingSong);
        
        // Remove from all inverted index entries
        ArrayList<String> words = new ArrayList<>();
        Scanner stringScanner = new Scanner(existingSong.getLyrics());
        while (stringScanner.hasNext()) {
            String word = stringScanner.next();
            if (!words.contains(word)) {
                words.add(word);
                WordID wordId = wordMap.get(new WordID(word, 0));
                if (wordId != null) {
                    BST<Song> songTree = invertedIndex.get(wordId.getId());
                    songTree.remove(existingSong, new SongNameComparator());
                }
            }
        }
        return true;
    }

    Song createSongFromFile(String fileName) {
        try {
            Scanner fileScanner = new Scanner(new File(fileName));
            String title = fileScanner.nextLine();
            String album = fileScanner.nextLine();
            int year = Integer.parseInt(fileScanner.nextLine());
            StringBuilder lyrics = new StringBuilder();
            while (fileScanner.hasNextLine()) {
                lyrics.append(fileScanner.nextLine()).append(" ");
            }
            return new Song(title, year, album, lyrics.toString().trim());
        } catch (Exception e) {
            throw new RuntimeException("Error reading song file: " + fileName, e);
        }
    }

    
    public void buildIndex(Song[] songs) {
        int nextId = 0;
        for (Song song : songs) {
            songsMap.add(song);
            Scanner stringScanner = new Scanner(song.getLyrics());
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
        Scanner stringScanner = new Scanner(song.getLyrics());
        while (stringScanner.hasNext()) {
            String word = stringScanner.next();
            if (!words.contains(word)) {
                words.add(word);
                WordID wordId = wordMap.get(new WordID(word, 0));
                if (wordId != null) {
                    invertedIndex.get(wordId.getId()).insert(song, new SongNameComparator());
                }
            }
        }
    }
    

    
    public Song getSong(String name) {
    	return songsMap.get(new Song(name, 0, null, null));
    }
    
    public void keyWordSearch(String keyword) {
        WordID wordId = wordMap.get(new WordID(keyword, 0));
        if (wordId == null) {
            System.out.println("No songs found for: " + keyword);
            return;
        }
        BST<Song> resultTree = invertedIndex.get(wordId.getId());
        System.out.println("Songs containing \"" + keyword + "\":\n" + resultTree.inOrderString());
    }
    
    public void nameSearch(String name) {
    	Song song = new Song(name, 0, null, null);;
    	Song useful = songsMap.get(song);
    	if (useful == null) {
    		System.out.println("There are no songs with that exact name. ");
    		return;
    	}
    	System.out.println(useful);
    }
}