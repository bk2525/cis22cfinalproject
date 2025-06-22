import java.util.*;

/**
 * Builds an inverted index (ArrayList<BST<Song>>) for keyword searches.
 * Uses WordID for O(1) word-to-ID lookup and filters stopwords.
 */
public class SearchEngine {
    private final HashTable<WordID> wordMap;
    private final HashTable<Song> songsMap;
    private final ArrayList<BST<Song>> invertedIndex;

    public SearchEngine() {
        this.wordMap = new HashTable<>(4096); // FIX ME
        this.songsMap = new HashTable<>(4096); // FIX ME
        this.invertedIndex = new ArrayList<>();
    }

    /**
     * Indexes songs by:
     * 1. Assigning IDs to unique words
     * 2. Building BSTs for each word ID
     */
    public void buildIndex(Song[] songs) {
        // Phase 1: Assign word IDs
        int nextId = 0;
        for (Song song : songs) {
            Scanner stringScanner = new Scanner(song.getLyrics());
            while (stringScanner.hasNext()) {
                String word = stringScanner.next();
                if (!wordMap.contains(new WordID(word, 0))) {
                    wordMap.add(new WordID(word, nextId++));
                    invertedIndex.add(new BST<>());
                }
            }
        }

        // Phase 2: Populate BSTs
        for (Song song : songs) {
            indexSong(song);
        }
    }

    private void indexSong(Song song) {
        ArrayList<String> words = new ArrayList<String>();
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

    public void search(String keyword) {
        WordID wordId = wordMap.get(new WordID(keyword, 0));
        if (wordId == null) {
            System.out.println("No songs found for: " + keyword);
            return;
        }
        BST<Song> resultTree = invertedIndex.get(wordId.getId());
        System.out.println("Songs containing \"" + keyword + "\":\n" + resultTree.inOrderString());
    }

    public void search(String keyQuery) {
        this.invertedIndex
        if (wordId == null) {
            System.out.println("No songs found for: " + keyword);
            return;
        }
        BST<Song> resultTree = invertedIndex.get(wordId.getId());
        System.out.println("Songs containing \"" + keyword + "\":\n" + resultTree.inOrderString());
    }
}