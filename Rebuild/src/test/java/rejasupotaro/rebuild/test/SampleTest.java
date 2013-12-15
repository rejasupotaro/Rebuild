package rejasupotaro.rebuild.test;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
public class SampleTest {

    @Test
    public void sample() {
        assertThat(1, is(1));
    }
}
