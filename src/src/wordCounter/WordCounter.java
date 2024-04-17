package wordCounter;

import java.io.File;
import java.util.HashMap;

public class WordCounter {

    private String filePath;
    private HashMap<String, Integer> wordCounter;

    public WordCounter(String filePath) {
        this.filePath = filePath;
        this.wordCounter = new HashMap<String, Integer>();
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
        for (File file: files) {
            if (file.isDirectory()) {
                this.countSeveralStrings(file.toString());
            }else {
               // logic here, read file and counting
                // System.out.println(file.getName());
            }
        }
    }

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
        WordCounter wc = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/src/src/");
        wc.countSeveralStrings();
    }

}
