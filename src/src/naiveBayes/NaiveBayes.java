package naiveBayes;

import wordCounter.WordCounter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private final List<String> classificationNames;
    private final HashMap<String, HashMap<String, Double>> wordProbabilites;
    private HashMap<String, Double> classificationProbabilites;
    private final HashMap<String, Double> estimatedClassificationResults;


    public NaiveBayes() {
        this.classificationNames = new ArrayList<String>();
        this.wordProbabilites = new HashMap<>();
        this.estimatedClassificationResults = new HashMap<String, Double>();
    }

    public NaiveBayes(HashMap<String, Double> singleClassProbabilites) {
        this.classificationNames = new ArrayList<String>();
        this.classificationProbabilites = singleClassProbabilites;
        this.wordProbabilites = new HashMap<>();
        this.estimatedClassificationResults = new HashMap<String, Double>();
    }


    public void trainForClass(String className, WordCounter wordCounterForClass) {
        this.addSingleClass(className);
        this.wordProbabilites.put(className, wordCounterForClass.getWordProbabilities());
    }

    public void train(List<WordCounter> wordCounters) {
        for (WordCounter wc: wordCounters) {
            if (wc.isTestData()) {
                this.trainForClass(wc.getClassName(), wc);
            }
        }
    }

    public String predict(WordCounter textToBeClassified) {
        // if automatic wordCounter counting is enabled, classification probs are set automatically
        if (this.classificationProbabilites == null) {
            this.classificationProbabilites = WordCounter.getClassProbabilities();
        }

        this._predict(textToBeClassified);
        System.out.println(this.estimatedClassificationResults);
        return this.getPredictedClassification();
    }

    private void addSingleClass(String className) {
        this.classificationNames.add(className);
    }

    private void _predict(WordCounter textToBeClassified) {
        double classificationProbability;
        HashMap<String, Integer> wordCount = textToBeClassified.getWordCount();

        for (String classificationName: this.classificationNames) {
            classificationProbability = this.calculateClassProbability(this.wordProbabilites, classificationName, wordCount);
            this.estimatedClassificationResults.put(classificationName, classificationProbability);
        }
    }

    private String getPredictedClassification() {
        String classification = null;
        double highestProbability = -1;
        double estimatedProbability;

        for (String classificationName: this.classificationNames) {
            estimatedProbability = this.estimatedClassificationResults.get(classificationName);

            if (estimatedProbability > highestProbability) {
                highestProbability = estimatedProbability;
                classification = classificationName;
            }
        }
        return classification;
    }

    private double calculateClassProbability(HashMap<String, HashMap<String, Double>> wordProbabilityPerClass, String className, HashMap<String, Integer> textToBeClassified) {

        int wordCount;
        double wordProbability;

        // share of class on total training data amount
        double probability = this.classificationProbabilites.get(className);
        // pretrained probabilites per class per word
        HashMap<String, Double> wordProbabilities = wordProbabilityPerClass.get(className);

        // iterate over all words of text to be classified, grab count and multiply with trained probability and overall class probabilty
        for (String word: textToBeClassified.keySet()) {
            wordCount = textToBeClassified.get(word); // total occurence of word within text to be classified
            wordProbability = wordProbabilities.getOrDefault(word, 1.0); // Probability of the word in the class
            probability = probability * Math.pow(wordProbability, wordCount); // Multiply the probability for each word to get total prob
        }

        return probability;
    }

}
