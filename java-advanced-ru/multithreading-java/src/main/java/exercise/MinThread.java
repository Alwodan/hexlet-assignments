package exercise;

// BEGIN
public class MinThread extends Thread {
    int[] arr;
    int result;

    @Override
    public void run() {
        int min = 99999;
        for (int num : arr) {
            if (num < min) {
                min = num;
            }
        }
        result = min;
    }

    public int getResult() {
        return result;
    }

    public MinThread(int[] arr) {
        this.arr = arr;
    }
}
// END
