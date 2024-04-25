package stringProcessor;

public class RemoveNumbers implements StringProcessor{

    @Override
    public String doProcessing(String s) {

        for (int number = 0; number < 10; number ++) {
            s = s.replace(String.valueOf(number), "");
        }

        return s;
    }
}
