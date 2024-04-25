package stringProcessor;

import wordCounter.StopWords;

public class RemoveStopWord implements StringProcessor{

    @Override
    public String doProcessing(String s) {

        if (StopWords.isStopword(s)) {
            s = s.replace(s,"");
        }
        return s;
    }
}
