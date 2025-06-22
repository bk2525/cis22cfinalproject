import java.util.*;

/**
 * Builds an inverted index (ArrayList<BST<Song>>) for keyword searches.
 * Uses WordID for O(1) word-to-ID lookup and filters stopwords.
 */
public class SearchEngine {
    private final HashTable<WordID> wordMap;
    private final ArrayList<BST<Song>> invertedIndex;
    private final Set<String> stopwords;

    public SearchEngine(Collection<String> stopwords) {
        this.stopwords = new HashSet<>(stopwords);
        this.wordMap = new HashTable<>(stopwords.size() * 2);  
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
            for (String word : tokenizeLyrics(song.getLyrics())) {
                if (isValidWord(word) && !wordMap.contains(new WordID(word, 0))) {
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
        Set<String> processedWords = new HashSet<>();  
        for (String word : tokenizeLyrics(song.getLyrics())) {
            if (isValidWord(word) && processedWords.add(word)) {
                WordID wordId = wordMap.get(new WordID(word, 0));
                if (wordId != null) {
                    invertedIndex.get(wordId.getId()).insert(song, new SongNameComparator());
                }
            }
        }
    }

    private String[] tokenizeLyrics(String lyrics) {
        return lyrics.toLowerCase()
                   .replaceAll("[^a-z\\s]", "")  
                   .split("\\s+");              
    }

    private boolean isValidWord(String word) {
        return !word.isEmpty() && !stopwords.contains(word);
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
}