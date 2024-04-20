package wordCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class WordCounter {

    final private static Set<String> uniqueWordsInAllDocuments = new HashSet<>();
    final private static List<WordCounter> wordCounters= new ArrayList<>();
    final private static HashMap<String, Double> classProbalities = new HashMap<>();
    final private static HashMap<String, HashMap<String, Integer>> wordCountPerClass = new HashMap<>();

    final private String filePath;
    final private HashMap<String, Integer> wordCounter;
    final private String fileTypeToRead;
    private int classCount;
    private String className;
    private boolean isTestData;
    private int k;

    public WordCounter(String filePath, String fileTypeToRead, int k, Boolean trackClassWordCounters) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;
        this.countWords();
        this.k = k;
        WordCounter.addToUniqueWordList(this.wordCounter);
        this.isTestData = false;

        // automatically track wordcounter instances for probability calculation and so on.
        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
            WordCounter.generateClassificationProbabilites();
        }
    }

    public WordCounter(String filePath, String fileTypeToRead, String classNameToCountFor, int k, Boolean trackClassWordCounters) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;
        this.classCount = 0;
        this.className = classNameToCountFor;
        this.countWords();
        this.k = k;
        this.isTestData = true;

        // automatically track wordcounter instances for probability calculation and so on.
        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
            WordCounter.generateClassificationProbabilites();
        }
    }

    public static List<WordCounter> getWordCounters() {
        return WordCounter.wordCounters;
    }

    public static void clearWordCounters() {
        WordCounter.wordCounters.clear();
    }

    public static List<WordCounter> getTrainingData() {
        return WordCounter.wordCounters.stream().filter(wordCounter -> wordCounter.isTestData).toList();
    }

    public static HashMap<String, HashMap<String, Integer>> getWordCountPerClass() {
        return WordCounter.wordCountPerClass;
    }

    public static void generateClassificationProbabilites() {

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

    public static HashMap<String, Double> getClassisficationProbabilities() {
        return WordCounter.classProbalities;
    }

    public String getClassificationName() {
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
        int totalWordCount =  wordCounter.values().stream().mapToInt(Integer::intValue).sum();
        for (String word: wordCounter.keySet()) {

            // assumption: even if word does not really occur, we assume it occurs at least once in average
            // test. thus, minimum word count is at least 1 (Laplace-Smoothing)
            int curVal = wordCounter.get(word);


            int d = totalWordCount + WordCounter.uniqueWordsInAllDocuments.size();
            double prob = (double) (curVal + this.k) / (d);
            wordProbabilities.put(word, prob);
        }
        return wordProbabilities;
    }

    public HashMap<String, Double> getWordProbabilities() {
        return this.getWordProbabilitiesForWordsIn(this.getWordCount());
    }

    public HashMap<String, Double> generateClassProbabilitiesWith(WordCounter otherClassWordCounter) {
        HashMap<String, Double> res = new HashMap<>();
        int totalCount = this.classCount + otherClassWordCounter.classCount;
        res.put(this.className, (double) this.classCount/totalCount);
        res.put(otherClassWordCounter.className, (double) otherClassWordCounter.classCount / totalCount);
        return res;
    }

    private static void addToUniqueWordList(HashMap<String, Integer> wordCounter) {
        WordCounter.uniqueWordsInAllDocuments.addAll(wordCounter.keySet());
    }

    private void countWords() {
        switch (this.fileTypeToRead) {
            case ".txt" : {
                this.countWordsInTxt(this.filePath);
            }
            case ".csv": {
                System.out.println("csv");
            }
        }
    }

    private void countWordsInTxt(String filePath){

        File directory = new File(filePath);
        File[] files = directory.listFiles();

        // ensure files is not null
        assert files != null;

        // call the files, if directory recursive step into directory and
        // get the files from there
        for (File currentFile: files) {
            if (currentFile.isDirectory()) {
                this.countWordsInTxt(currentFile.toString());
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

        if (!WordCounter.wordCountPerClass.containsKey(this.className) && this.className != null) {
            WordCounter.wordCountPerClass.put(this.className, this.wordCounter);
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

        if (StopWords.isStopword(word)| word.matches("[0-9]*")) {
            return;
        }

        if (word.length() <= 1) {
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
