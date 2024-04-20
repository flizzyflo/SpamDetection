package naiveBayes;

import wordCounter.WordCounter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NaiveBayes {

    private final List<String> classificationNames;
    private final HashMap<String, HashMap<String, Double>> wordProbabilitiesPerClassification;
    private HashMap<String, Double> classificationProbabilites = new HashMap<>();
    private final HashMap<String, Double> estimatedClassificationResults;
    private final HashMap<String, Integer> classificationAppearences;
    private final int k;

    public NaiveBayes(int k, HashMap<String, Integer> classificationAppearences) {
        this.classificationNames = new ArrayList<String>();
        this.wordProbabilitiesPerClassification = new HashMap<>();
        this.k = k;
        this.classificationAppearences = classificationAppearences;
        this.estimatedClassificationResults = new HashMap<String, Double>();
    }

    public void train(HashMap<String, HashMap<String, Integer>> wordsPerClassCount) {

        this.generateProbabilitesPerClassification(wordsPerClassCount);
        this.generateWordProbabilityPerClass(wordsPerClassCount);

    }

    public String predict(WordCounter textToBeClassified) {
        // if automatic wordCounter counting is enabled, classification probs are set automatically

        this._predict(textToBeClassified);
        return this.getPredictedClassification();
    }

    private void generateProbabilitesPerClassification(HashMap<String, HashMap<String, Integer>> wordsPerClassificationCount) {

        int totalCount = this.classificationAppearences.values().stream().mapToInt(Integer::intValue).sum();
        int singleCount;
        double probabilityOfClassificaiton;


        for (String k: this.classificationAppearences.keySet()) {
            singleCount = this.classificationAppearences.get(k);
            probabilityOfClassificaiton = (double) singleCount / totalCount;
            this.classificationProbabilites.put(k, probabilityOfClassificaiton);
        }
    }

    private void generateWordProbabilityPerClass(HashMap<String, HashMap<String, Integer>> wordsPerClassificationCount) {

        int totalWordCountPerClassification;
        int uniqueWords = WordCounter.getUniqueWords();

        HashMap<String, Double> wordProbabilities = new HashMap<>();
        HashMap<String, Integer> wordCount;

        for (String classficitation: wordsPerClassificationCount.keySet()) {
            wordCount = wordsPerClassificationCount.get(classficitation);
            totalWordCountPerClassification = wordCount.values().stream().mapToInt(Integer::intValue).sum();
            int div = totalWordCountPerClassification + uniqueWords;

            for (String word: wordCount.keySet()) {
                int wordAppearance = wordCount.get(word);
                double wordProb = (double) (wordAppearance + this.k) / totalWordCountPerClassification;
                wordProbabilities.put(word, wordProb);
            }
            this.wordProbabilitiesPerClassification.put(classficitation, wordProbabilities);
            wordProbabilities = new HashMap<>();
        }
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



    private double makePredictionsPerClassification(HashMap<String, HashMap<String, Double>> wordProbabilityPerClass, String className, HashMap<String, Integer> textToBeClassified) {

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
