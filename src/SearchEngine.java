import java.util.*;
import java.io.File;

public class SearchEngine {
    private final HashTable<WordID> wordMap;
    private final HashTable<Song> songsMap;
    private final ArrayList<BST<Song>> invertedIndex;

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

    public boolean deleteSong(String title) {
        Song songToDelete = new Song(title, 0, null, null);
        Song existingSong = songsMap.get(songToDelete);
        if (existingSong == null) {
            return false;
        }
        
        // Remove from songsMap
        songsMap.delete(existingSong);
        
        // Remove from inverted index
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

    private Song createSongFromFile(String fileName) {
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