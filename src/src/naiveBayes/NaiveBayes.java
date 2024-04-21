package naiveBayes;

import wordCounter.WordCounter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private final List<String> classificationNames;
    private final HashMap<String, HashMap<String, Double>> wordProbabilitiesPerClassification;
    private final HashMap<String, Double> classificationProbabilities = new HashMap<>();
    private final HashMap<String, Double> estimatedClassificationResults;
    private final HashMap<String, Integer> classificationAppearances;
    private final int k;

    public NaiveBayes(int k, HashMap<String, Integer> classificationAppearances) {
        this.k = k;
        this.classificationNames = new ArrayList<>(classificationAppearances.keySet());
        this.wordProbabilitiesPerClassification = new HashMap<>();
        this.classificationAppearances = classificationAppearances;
        this.estimatedClassificationResults = new HashMap<>();
    }

    public void train(HashMap<String, HashMap<String, Integer>> wordsPerClassCount) {
        this.generateProbabilitiesPerClassification();
        this.generateWordProbabilityPerClass(wordsPerClassCount);
    }

    public String predict(WordCounter textToBeClassified) {
        this._predict(textToBeClassified);
        return this.getPredictedClassification();
    }

    private void _predict(WordCounter textToBeClassified) {
        double classificationProbability;
        HashMap<String, Integer> wordCount = textToBeClassified.getWordCount();

        for (String classificationName: this.classificationNames) {
            classificationProbability = this.makePredictionsPerClassification(this.wordProbabilitiesPerClassification, classificationName, wordCount);
            this.estimatedClassificationResults.put(classificationName, classificationProbability);
        }
    }

    private String getPredictedClassification() {
        double highestProbability = -1;
        double estimatedProbability;
        String classification = null;

        for (String classificationName: this.classificationNames) {
            estimatedProbability = this.estimatedClassificationResults.get(classificationName);

            if (estimatedProbability > highestProbability) {
                highestProbability = estimatedProbability;
                classification = classificationName;
            }
        }
        return classification;
    }

    private void generateProbabilitiesPerClassification() {
        int totalCount = this.classificationAppearances.values().stream().mapToInt(Integer::intValue).sum();
        int singleCount;
        double probabilityOfClassificaiton;

        for (String k: this.classificationAppearances.keySet()) {
            singleCount = this.classificationAppearances.get(k);
            probabilityOfClassificaiton = (double) singleCount / totalCount;
            this.classificationProbabilities.put(k, probabilityOfClassificaiton);
        }
    }

    private void generateWordProbabilityPerClass(HashMap<String, HashMap<String, Integer>> wordCountPerClassification) {
        int totalWordCountPerClassification;
        int uniqueWords = WordCounter.getUniqueWordCount();
        int divisor;
        int wordAppearance;
        double wordProb;
        HashMap<String, Double> wordProbabilities = new HashMap<>();
        HashMap<String, Integer> words;

        for (String classification: wordCountPerClassification.keySet()) {
            words = wordCountPerClassification.get(classification);
            totalWordCountPerClassification = words.values().stream().mapToInt(Integer::intValue).sum();

            divisor = totalWordCountPerClassification + uniqueWords;

            for (String word: words.keySet()) {
                wordAppearance = words.get(word);
                wordProb = (double) (wordAppearance + this.k) / divisor;
                wordProbabilities.put(word, wordProb);
            }
            this.wordProbabilitiesPerClassification.put(classification, wordProbabilities);

            wordProbabilities = new HashMap<>();
        }
    }

    private double makePredictionsPerClassification(HashMap<String, HashMap<String, Double>> wordProbabilityPerClass, String className, HashMap<String, Integer> textToBeClassified) {

        int wordCount;
        double wordProbability;
        double probability;

        // share of class on total training data amount
        probability = this.classificationProbabilities.get(className);
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
