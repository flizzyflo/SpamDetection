package wordCounter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.stream.Stream;

public class WordCounter {

    private String filePath;
    private HashMap<String, Integer> wordCounter;
    private String fileTypeToRead;

    public WordCounter(String filePath, String fileTypeToRead) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<String, Integer>();
        this.fileTypeToRead = fileTypeToRead;
    }

    public void countSeveralStrings () {
        this.countSeveralStrings(this.filePath);
    }
    private void countSeveralStrings(String filePath){
        File directory = new File(filePath);
        File[] files = directory.listFiles();

        // ensure files is not null
        assert files != null;

        // call the files, if directory recursive step into directory and
        // get the files from there
        for (File currentFile: files) {
            if (currentFile.isDirectory()) {
                this.countSeveralStrings(currentFile.toString());
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
        System.out.println(this.wordCounter);
    }

    private void processString(String fileLine) {
        fileLine = this.removeInterpunctuation(fileLine);
        String[] words = fileLine.split(" ");
        this.turnAllToLowerCase(words);

        for (String word: words ) {
            this.addWordToWordCounter(word);
        };

    }

    private String removeInterpunctuation(String line) {

        String[] interPuntucations = new String[] {"!", "?", ".", ";", ",", ".", "-","=","(",")","[","]","{","}","<",">","|" };

        for (String interPunctuation: interPuntucations) {
            line = line.replace(interPunctuation, " ");
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

    public HashMap<String, Integer> getWordCounter() {
        return this.wordCounter;
    };

    public static void main(String[] args) {
        WordCounter wc = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/src/src/", ".java");
        wc.countSeveralStrings();
    }

}
