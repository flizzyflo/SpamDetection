import naiveBayes.NaiveBayes;
import wordCounter.WordCounter;

import java.util.HashMap;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        final String notSpam = "notSpam";
        final String spam = "spam";

        WordCounter nonSpamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/notSpam", ".txt", notSpam, true);
        WordCounter spamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/spam", ".txt",   spam, true);
        WordCounter testCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/testfile", ".txt", true);

        HashMap<String, HashMap<String, Integer>> w = WordCounter.getWordCountPerClass();

        NaiveBayes nb = new NaiveBayes(1, WordCounter.getClassificationAppearences());
        nb.train(w);
        System.out.println(nb.predict(testCounter));

    }
}