package naiveBayes;

import wordCounter.WordCounter;

//TODO klassen flexibler ermöglichen

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private final List<String> differentClassNames;
    private final HashMap<String, HashMap<String, Double>> wordProbabilitiesPerEverySingleClass;
    private final HashMap<String, Double> classProbability;
    private final HashMap<String, Double> probabilityEstimationPerClass;

    public NaiveBayes(HashMap<String, Double> singleClassProbabilites) {
        this.differentClassNames = new ArrayList<String>();
        this.wordProbabilitiesPerEverySingleClass = new HashMap<>();
        this.classProbability = singleClassProbabilites;
        this.probabilityEstimationPerClass = new HashMap<String, Double>();
    }

    public HashMap<String, Double> getProbabilityEstimationPerClass() {
        return this.probabilityEstimationPerClass;
    }

    public void addSingleClass(String className) {
        this.differentClassNames.add(className);
    }

    public void calculateProbabilityPerClass(String className, WordCounter wordCounterForClass) {
        this.wordProbabilitiesPerEverySingleClass.put(className, wordCounterForClass.getWordProbabilities());

    };

    public String predict(WordCounter wordCounterForTextToBePredicted) {
        HashMap<String, Integer> wordCount = wordCounterForTextToBePredicted.getWordCount();

        double spamProbability = this.calculateProbabiltyForAllWords(this.wordProbabilitiesPerEverySingleClass, "spam", wordCount);
        double nonSpamProbabilty = this.calculateProbabiltyForAllWords(this.wordProbabilitiesPerEverySingleClass, "kein_spam", wordCount);

        return spamProbability > nonSpamProbabilty ? "Spam" : "kein Spam";

    };

    private double calculateProbabiltyForAllWords(HashMap<String, HashMap<String, Double>> wordProbabilityPerClass, String className, HashMap<String, Integer> wordCount) {

        double probability = this.classProbability.get(className);

        HashMap<String, Double> wordProbabilities = wordProbabilityPerClass.get(className);

        for (String word: wordCount.keySet()) {
            int count = wordCount.get(word);
            double wordProbability = wordProbabilities.get(word); // Probability of the word in the class
            probability *= Math.pow(wordProbability, count); // Multiply the probability for each word
        }

        return probability;
    }

}
