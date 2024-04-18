package wordCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordCounter {

    private String filePath;
    private HashMap<String, Integer> wordCounter;
    private String fileTypeToRead;
    private int classCount;
    private String className;

    public WordCounter(String filePath, String fileTypeToRead) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<String, Integer>();
        this.fileTypeToRead = fileTypeToRead;
    }

    public WordCounter(String filePath, String fileTypeToRead, String className) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<String, Integer>();
        this.fileTypeToRead = fileTypeToRead;
        this.classCount = 0;
        this.className = className;
    }

    public int getClassCount() {
        return this.classCount;
    }

    public String getClassName() {
        return this.className;
    }

    public void countWords() {
        this.countWords(this.filePath);
    }

    public HashMap<String, Integer> getWordCount() {
        return new HashMap<String, Integer>(this.wordCounter);
    };

    public void setNewFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCurrentFilePath() {
        return this.filePath;
    }

    public void clearCurrentWordCount() {
        this.wordCounter.clear();
    }

    public HashMap<String, Double> getWordProbabilitiesForWordsIn(HashMap<String, Integer> wordCounter) {
        HashMap<String, Double> wordProbabilities = new HashMap<>();

        int totalWordCount = wordCounter.values().stream().mapToInt(Integer::intValue).sum();
        for (String word: wordCounter.keySet()) {

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
      //  HashMap<String, Integer> mergedWordCounter = new HashMap<>(this.wordCounter);

        Set<String> newWords = this.mergeWordCounterWith(wordCountToMerge);

        for (String word: newWords) {
            if (!this.wordCounter.containsKey(word)) {
                this.wordCounter.put(word, 0);
            }
        }
    };


    public HashMap<String, Double> generateClassProbabilitiesWith(WordCounter otherClassWordCounter) {
        HashMap<String, Double> res = new HashMap<>();
        int totalCount = this.classCount + otherClassWordCounter.classCount;
        res.put(this.className, (double) this.classCount/totalCount);
        res.put(otherClassWordCounter.className, (double) otherClassWordCounter.classCount / totalCount);
        return res;
    };

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
                // class name is set
                if (this.className != null) {
                    this.classCount = this.classCount + 1;
                };
                this.readFileContentOf(currentFile);
            }
            else {
                continue;
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
            };

        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void processString(String fileLine) {
        fileLine = this.removeInterpunctuation(fileLine);
        String[] words = fileLine.split(" ");
        this.turnAllToLowerCase(words);

        for (String word: words ) {
            if (!(word.equals(" ") | word.isEmpty())) {
                this.addWordToWordCounter(word);
            };
        };

    }

    private String removeInterpunctuation(String line) {

        char[] interPuntucations = new char[] {
                '!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@',
                '[', '\\', ']', '^', '_', '`', '{', '|', '}', '~'
        };

        for (char interPunctuation: interPuntucations) {
            line = line.replace(interPunctuation, ' ');
        };
        return line;
    };

    private void turnAllToLowerCase(String[] words) {
        String currentWord;
        for (int i = 0; i < words.length; i++){
            currentWord = words[i].toLowerCase();
            words[i] = currentWord;
        }
    };


    private void addWordToWordCounter(String word) {

        if (StopWords.isStopword(word)) {
            return;
        }

        if (this.wordCounter.containsKey(word)) {
            Integer currentValue = this.wordCounter.get(word);
            this.wordCounter.put(word, currentValue + 1);
        } else {
            this.wordCounter.put(word, 1);
        };
    }
}
