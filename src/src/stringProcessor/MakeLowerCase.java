package stringProcessor;

public class MakeLowerCase implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        StringBuilder processed = new StringBuilder();

        for (Character c: s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                processed.append(Character.toLowerCase(c));
            }
            else {
                processed.append(c);
            }
        }
        return processed.toString();
    }
}
