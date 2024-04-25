package stringProcessor;

import wordCounter.Punctuation;

public class RemovePunctuation implements StringProcessor{

    @Override
    public String doProcessing(String s) {

        for (String interPunctuation: Punctuation.PUNCTUATION) {
            s = s.replace(interPunctuation, "");
        }
        return s;
    }
}
