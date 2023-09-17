package exercise;

// BEGIN
public class MaxThread extends Thread {
    int[] arr;
    int result;

    @Override
    public void run() {
        int max = -99999;
        for (int num : arr) {
            if (num > max) {
                max = num;
            }
        }
        result = max;
    }

    public int getResult() {
        return result;
    }

    public MaxThread(int[] arr) {
        this.arr = arr;
    }
}
// END
