package exercise;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

class AppTest {

    @Test
    void testTake() {
        // BEGIN
        List<Integer> testList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));

        assertThat(App.take(testList, 1).size()).isEqualTo(1);
        assertThat(App.take(testList, 3).size()).isEqualTo(3);
        assertThat(App.take(testList, 5).size()).isEqualTo(5);
        assertThat(App.take(testList, 6).size()).isEqualTo(5);
        assertThat(App.take(testList, 0).size()).isEqualTo(0);
        // END
    }
}
