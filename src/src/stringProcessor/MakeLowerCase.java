package stringProcessor;

public class MakeLowerCase implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        return s.toLowerCase();
    }
}
