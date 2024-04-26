package stringProcessor;

public class TrimWord implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        StringBuilder processed = new StringBuilder();

        for (Character c: s.toCharArray()) {
            if(!Character.isWhitespace(c)) {
                processed.append(c);
            }
        }

        return processed.toString();
    }
}
