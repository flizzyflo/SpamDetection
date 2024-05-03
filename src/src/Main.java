import naiveBayes.NaiveBayes;
import wordCounter.WordCounter;
import java.util.HashMap;


public class Main {
    public static void main(String[] args) {

        new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/spam", ".txt", "spam", true);
        new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/notSpam", ".txt", "not spam", true);

        WordCounter testCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/testfile", ".txt", true);

        HashMap<String, HashMap<String, Integer>> w = WordCounter.getWordCountPerClass();
        NaiveBayes nb = new NaiveBayes(1, WordCounter.getClassificationAppearances());
        nb.train(w);
        System.out.println(nb.predict(testCounter));

    }
}