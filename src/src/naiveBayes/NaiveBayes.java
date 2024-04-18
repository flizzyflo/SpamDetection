package naiveBayes;

import wordCounter.WordCounter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private final List<String> differentClasses;
    private final HashMap<String, HashMap<String, Double>> wordProbabilitiesPerClass;
    private final HashMap<String, Double> singleClassProbabilites;


    public NaiveBayes(HashMap<String, Double> singleClassProbabilites) {
        this.differentClasses = new ArrayList<String>();
        this.wordProbabilitiesPerClass = new HashMap<>();
        this.singleClassProbabilites = singleClassProbabilites;
    }

    public void addDifferentClasses(List<String> classNames) {
        for (String className: classNames) {
            this.addSingleClass(className);
        }
    }

    private void countClassOccurence() {

    }

    public void addSingleClass(String className) {
        this.differentClasses.add(className);
    }

    public void calculateProbabilityPerClass(String className, WordCounter wordCounterForClass) {
        this.wordProbabilitiesPerClass.put(className, wordCounterForClass.getWordProbabilities());

    };

    public String predict(WordCounter wordCounterForTextToBePredicted) {
        HashMap<String, Integer> wordCount = wordCounterForTextToBePredicted.getWordCount();

        double spamProbability = this.calculateProbabiltyForAllWords(this.wordProbabilitiesPerClass, "spam", wordCount);
        double nonSpamProbabilty = this.calculateProbabiltyForAllWords(this.wordProbabilitiesPerClass, "kein_spam", wordCount);

        return spamProbability > nonSpamProbabilty ? "Spam" : "kein Spam";

    };

    private double calculateProbabiltyForAllWords(HashMap<String, HashMap<String, Double>> classProbabilities, String className, HashMap<String, Integer> wordCount) {

        double probability = this.singleClassProbabilites.get(className);

        HashMap<String, Double> classProbability = classProbabilities.get(className);

        for (String word: wordCount.keySet()) {
            int count = wordCount.get(word);
            double wordProbability = classProbability.getOrDefault(word, 0.0); // Probability of the word in the class
            probability *= Math.pow(wordProbability, count); // Multiply the probability for each word
        }

        return probability;
    }

}
