import naiveBayes.NaiveBayes;
import wordCounter.WordCounter;

import java.util.HashMap;


public class Main {
    public static void main(String[] args) {

/*
        final String notSpam = "notSpam";
        final String spam = "spam";
        final String uncl = "unclear";

        WordCounter nonSpamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/notSpam", ".txt", notSpam, true);
        WordCounter spamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/spam", ".txt",   spam, true);
        WordCounter unclear = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/unclear", ".txt",   uncl, true);
        WordCounter testCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/testfile", ".txt", true);

*/
        WordCounter automobile = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/automobile", ".txt", "automobile", true);
        WordCounter entertainment = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/entertainment", ".txt", "entertainment", true);
        WordCounter politics = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/politics", ".txt", "politics", true);
        WordCounter science = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/science", ".txt", "science", true);
        WordCounter sports = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/sports", ".txt", "sports", true);
        WordCounter technology = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/technology", ".txt", "technology", true);
        WordCounter world = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/world", ".txt", "world", true);

        WordCounter testCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/testfile", ".txt", true);

        HashMap<String, HashMap<String, Integer>> w = WordCounter.getWordCountPerClass();
        NaiveBayes nb = new NaiveBayes(1, WordCounter.getClassificationAppearances());
        nb.train(w);
        System.out.println(nb.predict(testCounter));

    }
}