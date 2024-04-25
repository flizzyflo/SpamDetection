package stringProcessor;

public class TrimWord implements StringProcessor{

    @Override
    public String doProcessing(String s) {
        return s.trim();
    }
}
