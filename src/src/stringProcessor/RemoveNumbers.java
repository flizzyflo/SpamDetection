package stringProcessor;

public class RemoveNumbers implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        StringBuilder processed = new StringBuilder();
        
        for (Character c: s.toCharArray()) {
            if (Character.isLetter(c)){
                processed.append(c);
            }
        }
        return processed.toString();
  
    }
}
