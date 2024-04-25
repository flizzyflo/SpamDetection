package stringProcessor;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.EnglishStemmer;
import org.tartarus.snowball.ext.PorterStemmer;

public class WordStemmer implements StringProcessor{

    @Override
    public String doProcessing(String s) {

        SnowballStemmer stemmer = new PorterStemmer();

        stemmer.setCurrent(s);
        System.out.println(s);
        stemmer.stem();
        s = stemmer.getCurrent();
        System.out.println(s);
        return "";
    }
}
