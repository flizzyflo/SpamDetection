package stringProcessor;

public class RemovePunctuation implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        StringBuilder processed = new StringBuilder();

        for (Character c: s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                processed.append(c);
            }
        }
        return processed.toString();

    }
}
