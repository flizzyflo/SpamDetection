package stringProcessor;

import org.tartarus.snowball.ext.PorterStemmer;

public class WordStemmer implements StringProcessor{

    @Override
    public String doProcessing(String s) {

        PorterStemmer stemmer = new PorterStemmer();
        stemmer.setCurrent(s);
        stemmer.stem();
        return stemmer.getCurrent();

    }
}
