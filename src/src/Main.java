import wordCounter.WordCounter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        WordCounter wc = new WordCounter("/Users/florianluebke/Desktop/SpamDetection/src/learningData/spam/", ".txt");
        wc.countWords();
        System.out.println(wc.getWordCount());
        }
    }