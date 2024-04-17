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

    public WordCounter(String filePath, String fileTypeToRead) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<String, Integer>();
        this.fileTypeToRead = fileTypeToRead;
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
            if (curVal == 0) {
                curVal = 1;
            }
            double prob = (double)curVal/totalWordCount;
            wordProbabilities.put(word, prob);

        }

        return wordProbabilities;
    }

    public HashMap<String, Integer> mergeOtherWordCountKeysToCurrent(HashMap<String, Integer> wordCountToMerge) {
        HashMap<String, Integer> mergedWordCounter = new HashMap<>(this.wordCounter);
        Set<String> newWords = this.mergeWordCounterWith(wordCountToMerge);
          for (String word: newWords) {
              if (!mergedWordCounter.containsKey(word)) {
                  mergedWordCounter.put(word, 0);
              }
          }
          return mergedWordCounter;
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
        if (this.wordCounter.containsKey(word)) {
            Integer currentValue = this.wordCounter.get(word);
            this.wordCounter.put(word, currentValue + 1);
        } else {
            this.wordCounter.put(word, 1);
        };
    }
}
