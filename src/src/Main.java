import naiveBayes.NaiveBayes;
import wordCounter.WordCounter;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        final String spam = "Spam";
        final String notSpam = "NotSpam";

        WordCounter spamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/spam", ".txt", spam, true);
        WordCounter nonSpamCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/notSpam", ".txt", notSpam, true);
        WordCounter testCounter = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/SpamDetection/src/learningData/testfile", ".txt", true);

        List<WordCounter> w = WordCounter.getWordCounters();
        NaiveBayes nb = new NaiveBayes();
        nb.train(w);

        String resClass = nb.predict(testCounter);
        System.out.println(resClass);

        }
    }