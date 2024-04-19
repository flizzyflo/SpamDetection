package wordCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class WordCounter {

    final private static HashMap<String, Double> classProbalities = new HashMap<>();
    final private static List<WordCounter> wordCounters= new ArrayList<>();
    final private String filePath;
    final private HashMap<String, Integer> wordCounter;
    final private String fileTypeToRead;
    private int classCount;
    private String className;
    private boolean isTestData;

    public WordCounter(String filePath, String fileTypeToRead, Boolean trackClassWordCounters) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;
        this.countWords(this.filePath);
        this.isTestData = false;

        // automatically track wordcounter instances for probability calculation and so on.
        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
            WordCounter.mergeWordSets();
            WordCounter.generateOverallClassProbabilities();
        }
    }

    public WordCounter(String filePath, String fileTypeToRead, String classNameToCountFor, Boolean trackClassWordCounters) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;
        this.classCount = 0;
        this.className = classNameToCountFor;
        this.countWords(this.filePath);
        this.isTestData = true;

        // automatically track wordcounter instances for probability calculation and so on.
        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
            WordCounter.mergeWordSets();
            WordCounter.generateOverallClassProbabilities();
        }
    }

    public static List<WordCounter> getWordCounters() {
        return WordCounter.wordCounters;
    }

    public static void clearWordCounters() {
        WordCounter.wordCounters.clear();
    }

    private static void mergeWordSets() {
        for (int i = 0; i < WordCounter.wordCounters.size(); i++) {
            for (int j = 0; j < WordCounter.wordCounters.size(); j++) {
                if (j == i) {
                    continue;
                }
                WordCounter.wordCounters.get(i).mergeOtherWordCountKeysToCurrent(WordCounter.wordCounters.get(j).getWordCount());
            }
        }
    }

    public static void mergeWordSets(List<WordCounter> wordCounters) {
        for (int i = 0; i < wordCounters.size(); i++) {
            for (int j = 0; j < wordCounters.size(); j++) {
                if (j == i) {
                    continue;
                }
                wordCounters.get(i).mergeOtherWordCountKeysToCurrent(wordCounters.get(j).getWordCount());
            }
        }
    }

    public static void generateOverallClassProbabilities() {

        int totalCount = 0;
        for (WordCounter wc: WordCounter.wordCounters) {
            if (wc.className == null) {
                continue;
            }
            totalCount = totalCount + wc.classCount;
        }

        for (WordCounter wc: WordCounter.wordCounters) {
            if (wc.className == null) {
                continue;
            }
            WordCounter.classProbalities.put(wc.className, (double) wc.classCount / totalCount);
        }
    }

    public static HashMap<String, Double> getClassProbabilities() {
        return WordCounter.classProbalities;
    }

    public String getClassName() {
        return this.className;
    }

    public boolean isTestData() {
        return this.isTestData;
    }

    public HashMap<String, Integer> getWordCount() {
        return new HashMap<>(this.wordCounter);
    }

    public HashMap<String, Double> getWordProbabilitiesForWordsIn(HashMap<String, Integer> wordCounter) {
        HashMap<String, Double> wordProbabilities = new HashMap<>();

        int totalWordCount = wordCounter.values().stream().mapToInt(Integer::intValue).sum();
        for (String word: wordCounter.keySet()) {

            // assumption: even if word does not really occur, we assume it occurs at least once in average
            // test. thus, minimum word count is at least 1 (Laplace-Smoothing)
            int curVal = wordCounter.get(word);
            curVal = curVal + 1;
            double prob = (double)curVal/totalWordCount;
            wordProbabilities.put(word, prob);
        }
        return wordProbabilities;
    }

    public HashMap<String, Double> getWordProbabilities() {
        return this.getWordProbabilitiesForWordsIn(this.getWordCount());
    }

    public void mergeOtherWordCountKeysToCurrent(HashMap<String, Integer> wordCountToMerge) {

        Set<String> wordsFromOtherText = this.mergeWordCounterWith(wordCountToMerge);

        for (String word: wordsFromOtherText) {
            if (!this.wordCounter.containsKey(word)) {
                this.wordCounter.put(word, 1);
            }
        }
    }

    public HashMap<String, Double> generateClassProbabilitiesWith(WordCounter otherClassWordCounter) {
        HashMap<String, Double> res = new HashMap<>();
        int totalCount = this.classCount + otherClassWordCounter.classCount;
        res.put(this.className, (double) this.classCount/totalCount);
        res.put(otherClassWordCounter.className, (double) otherClassWordCounter.classCount / totalCount);
        return res;
    }

    private Set<String> mergeWordCounterWith(HashMap<String, Integer> wordCountToMerge) {
        Set<String> newWords = new HashSet<>(wordCountToMerge.keySet());
        Set<String> currentWords = new HashSet<>(this.wordCounter.keySet());
        currentWords.addAll(newWords); // union of both word sets
        return currentWords;
    }

    private void countWords(String filePath){
        File directory = new File(filePath);
        File[] files = directory.listFiles();

        // ensure files is not null
        assert files != null;

        // call the files, if directory recursive step into directory and
        // get the files from there
        for (File currentFile: files) {
            if (currentFile.isDirectory()) {
                this.countWords(currentFile.toString());
            }
            else if (currentFile.getName().endsWith(this.fileTypeToRead)) {
                // class name is given as argument in constructor, thus count class
                // relevant for training and calculation overall class probability
                if (this.className != null) {
                    this.classCount = this.classCount + 1;
                }
                this.readFileContentOf(currentFile);
            }
        }
    }

    private void readFileContentOf(File file) {
        String line;

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader fileContentReader = new BufferedReader(fileReader);
            line = fileContentReader.readLine();

            while (line != null) {
                this.processString(line);
                line = fileContentReader.readLine();
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void processString(String fileLine) {
        // filtering and cleaning process of words
        fileLine = this.removeInterpunctuation(fileLine);
        String[] words = fileLine.split(" ");

        this.turnLowerCase(words);

        for (String word: words) {
            // if valid word, add to counter
            if (!(word.equals(" ") | word.isEmpty())) {
                this.addWordToWordCounter(word);
            }
        }
    }

    private String removeInterpunctuation(String line) {
        for (String interPunctuation: Punctuation.PUNCTUATION) {
            line = line.replace(interPunctuation, " ");
        }
        return line;
    }

    private void turnLowerCase(String[] words) {
        String currentWord;
        for (int i = 0; i < words.length; i++){
            currentWord = words[i].toLowerCase();
            words[i] = currentWord;
        }
    }

    private void addWordToWordCounter(String word) {

        if (StopWords.isStopword(word)) {
            return;
        }
        if (this.wordCounter.containsKey(word)) {
            Integer currentValue = this.wordCounter.get(word);
            this.wordCounter.put(word, currentValue + 1);
        }
        else
        {
            this.wordCounter.put(word, 1);
        }
    }
}
