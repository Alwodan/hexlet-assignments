package exercise;

// BEGIN
public class ReversedSequence implements CharSequence {
    String str;

    public ReversedSequence(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result = str.charAt(i) + result;
        }
        this.str = result;
    }

    @Override
    public int length() {
        return str.length();
    }

    @Override
    public char charAt(int i) {
        return str.charAt(i);
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return str.substring(i, i1);
    }

    public String toString() {
        return str;
    }
}
// END
