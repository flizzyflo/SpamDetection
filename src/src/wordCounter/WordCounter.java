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
    final private static HashMap<String, Integer> classificationAppearences = new HashMap<>();
    final private String filePath;
    final private HashMap<String, Integer> wordCounter;
    final private String fileTypeToRead;
    private int classCount;
    private String className;
    private boolean isTrainingData;

    public WordCounter(String filePath, String fileTypeToRead, Boolean trackClassWordCounters) {
        this.isTrainingData = false;
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;

        this.countWords();
        WordCounter.addToUniqueWordList(this.wordCounter);
        this.makeWordMatricesContainSameWords();

        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
        }
    }

    public WordCounter(String filePath, String fileTypeToRead, String classNameToCountFor, Boolean trackClassWordCounters) {
        this.isTrainingData = true;
        this.filePath = filePath;
        this.wordCounter = new HashMap<>();
        this.fileTypeToRead = fileTypeToRead;
        this.classCount = 0;
        this.className = classNameToCountFor;
        this.countWords();
        WordCounter.addToUniqueWordList(this.wordCounter);
        this.makeWordMatricesContainSameWords();
        if (trackClassWordCounters) {
            WordCounter.wordCounters.add(this);
        }
    }

    public static List<WordCounter> getWordCounters() {
        return WordCounter.wordCounters;
    }

    public static void clearWordCounters() {
        WordCounter.wordCounters.clear();
    }

    public static List<WordCounter> getTrainingData() {
        return WordCounter.wordCounters.stream().filter(wordCounter -> wordCounter.isTrainingData).toList();
    }

    public static HashMap<String, HashMap<String, Integer>> getWordCountPerClass() {
        return WordCounter.wordCountPerClass;
    }

    public static HashMap<String, Integer> getClassificationAppearences() {
        WordCounter.generateClassifcationAppearences();
        return WordCounter.classificationAppearences;
    }

    private static void generateClassifcationAppearences() {
        for (WordCounter wc: WordCounter.wordCounters) {

            if (wc.isTrainingData()) {
                WordCounter.classificationAppearences.put(wc.className, wc.classCount);
            }
        }
    }


    public static int getUniqueWords() {
        return uniqueWordsInAllDocuments.size();
    }

    public boolean isTrainingData() {
        return this.isTrainingData;
    }

    public HashMap<String, Integer> getWordCount() {
        return new HashMap<>(this.wordCounter);
    }

    private static void addToUniqueWordList(HashMap<String, Integer> wordCounter) {
        WordCounter.uniqueWordsInAllDocuments.addAll(wordCounter.keySet());
    }

    private void makeWordMatricesContainSameWords() {

        for(String word: WordCounter.uniqueWordsInAllDocuments) {
            for (WordCounter wc: WordCounter.wordCounters) {
                if (!wc.isTrainingData()) {
                    continue;
                }
                else if (!wc.wordCounter.containsKey(word)) {
                    wc.wordCounter.put(word, 0);
                }
            }
        }
    }

    private void countWords() {
        switch (this.fileTypeToRead) {
            case ".txt" : {
                this.countWordsInTxt(this.filePath);
                break;
            }
            case ".csv": {
                System .out.println("csv");
                break;
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

        if (!WordCounter.wordCountPerClass.containsKey(this.className) && this.isTrainingData()) {
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
