package test;

import org.junit.Test;
import wordCounter.WordCounter;

import java.util.HashMap;

import static org.junit.Assert.*;

public class WordCounterTest {

    @Test
    public void clearCurrentCountNotEmpty() {
        WordCounter wc = new WordCounter("pfad/zur/Datei", ".txt");
        wc.countWords();
        wc.clearCurrentWordCount();
        assertTrue(wc.getWordCount().isEmpty()); // Der wordCounter sollte leer sein, nachdem clearCurrentCount aufgerufen wurde
    }

    @Test
    public void clearCurrentCountEmpty() {
        WordCounter wc = new WordCounter("pfad/zur/Datei", ".txt");
        wc.clearCurrentWordCount();
        assertTrue(wc.getWordCount().isEmpty()); // Der wordCounter sollte bereits leer sein und unverändert bleiben
    }

    @Test
    public void setNewPathValid() {
        WordCounter wc = new WordCounter("alter/pfad", ".txt");
        wc.setNewFilePath("neuer/pfad");
        assertEquals("neuer/pfad", wc.getCurrentFilePath()); // Der Dateipfad sollte erfolgreich aktualisiert werden
    }

    @Test
    public void setNewPathInvalid() {
        WordCounter wc = new WordCounter("alter/pfad", ".txt");
        wc.setNewFilePath(null);
        assertEquals("alter/pfad", wc.getCurrentFilePath()); // Der Dateipfad sollte unverändert bleiben, wenn ein ungültiger Pfad übergeben wird
    }

    @Test
    public void getWordCounterAfterCount() {
        WordCounter wc = new WordCounter("pfad/zur/Datei", ".txt");
        wc.countWords();
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        assertFalse(wordCounter.isEmpty()); // Das HashMap-Objekt sollte nicht leer sein, nachdem countSeveralStrings aufgerufen wurde
    }

    @Test
    public void getWordCounterBeforeCount() {
        WordCounter wc = new WordCounter("pfad/zur/Datei", ".txt");
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        assertTrue(wordCounter.isEmpty()); // Das HashMap-Objekt sollte leer sein, wenn countSeveralStrings nicht aufgerufen wurde
    }

    @Test
    public void getWordCounterImmutable() {
        WordCounter wc = new WordCounter("pfad/zur/Datei", ".txt");
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        wordCounter.put("test", 5);
        assertTrue(wc.getWordCount().isEmpty()); // Das interne HashMap-Objekt sollte unverändert bleiben
    }

    @Test
    public void countSeveralStringsSingleFile() {
        WordCounter wc = new WordCounter("pfad/zur/einzelnen/Datei", ".txt");
        wc.countWords();
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        assertEquals(3, wordCounter.size()); // Erwartete Anzahl der Wörter in der Datei
    }

    @Test
    public void countSeveralStringsMultipleFiles() {
        WordCounter wc = new WordCounter("pfad/zum/Verzeichnis", ".txt");
        wc.countWords();
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        assertEquals(10, wordCounter.size()); // Erwartete Gesamtzahl der Wörter in allen Dateien im Verzeichnis
    }

    @Test
    public void countSeveralStringsRecursive() {
        WordCounter wc = new WordCounter("pfad/zum/Hauptverzeichnis", ".txt");
        wc.countWords();
        HashMap<String, Integer> wordCounter = wc.getWordCount();
        assertEquals(15, wordCounter.size()); // Erwartete Gesamtzahl der Wörter in allen Dateien im Hauptverzeichnis und seinen Unterordnern
    }


}
