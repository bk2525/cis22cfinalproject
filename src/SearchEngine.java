/**
 * SearchEngine builds an inverted index using BSTs and allows keyword searches over Song lyrics.
 *
 * @author Jeses Louis
 */
import java.util.*;

public class SearchEngine {
    // maps each unique word to its WordID (word + assigned ID)
    private HashTable<WordID> wordMap;
    // for each word ID, a BST of Songs containing that word
    private ArrayList<BST<Song>> invertedIndex;
    // set of stopwords to ignore during indexing
    private Set<String> stopwords;

    /**
     * Constructs a SearchEngine with a given list of stopwords.
     * @param removedWords collection of words to exclude from indexing
     */
    public SearchEngine(Collection<String> removedWords) {
        // Convert stopword list into a HashSet for O(1) lookups
        this.stopwords = new HashSet<>(removedWords);
        // Initialize the wordMap with a capacity based on stopwords size
        int initialSize = removedWords.size() * 2 + 1;
        this.wordMap = new HashTable<>(initialSize);
        this.invertedIndex = new ArrayList<>();
    }

    /**
     * Builds the inverted index over an array of Song objects.
     * @param songs array of Song objects with filtered lyrics
     */
    public void buildIndex(Song[] songs) {
        // 1) Assign each unique word a new ID
        int idCounter = 0;
        for (Song s : songs) {
            String[] tokens = s.getLyrics().split("\\s+");
            for (String w : tokens) {
                if (w.isEmpty() || stopwords.contains(w) || wordMap.get(new WordID(w, 0)) != null) {
                    continue;
                }
                wordMap.add(new WordID(w, idCounter));
                idCounter++;
            }
        }
        // 2) Prepare the BST list for each assigned ID
        for (int i = 0; i < idCounter; i++) {
            invertedIndex.add(new BST<Song>());
        }
        // 3) Index each song into all relevant BSTs
        for (Song s : songs) {
            indexSong(s);
        }
    }

    /**
     * Indexes a single Song by inserting it into each BST corresponding to its keywords.
     * @param song the Song to index
     */
    private void indexSong(Song song) {
        String[] tokens = song.getLyrics().split("\\s+");
        Set<String> seen = new HashSet<>();
        for (String w : tokens) {
            if (w.isEmpty() || stopwords.contains(w) || seen.contains(w)) {
                continue;
            }
            WordID lookup = new WordID(w, 0);
            WordID entry = wordMap.get(lookup);
            if (entry == null) {
                continue;
            }
            int id = entry.getId();
            BST<Song> tree = invertedIndex.get(id);
            // Insert Song into BST, using its Comparable implementation
            tree.insert(song, new Comparator<Song>() {
                @Override
                public int compare(Song a, Song b) {
                    return a.compareTo(b);
                }
            });
            seen.add(w);
        }
    }

    /**
     * Searches for songs containing the given keyword and prints results.
     * @param keyword the term to search for
     */
    public void searchKeyword(String keyword) {
        String key = keyword.toLowerCase();
        WordID lookup = new WordID(key, 0);
        WordID entry = wordMap.get(lookup);
        if (entry == null) {
            System.out.println("No songs found for \"" + keyword + "\"");
            return;
        }
        BST<Song> tree = invertedIndex.get(entry.getId());
        System.out.println("Songs containing \"" + keyword + "\":");
        System.out.print(tree.inOrderString());
    }
}
