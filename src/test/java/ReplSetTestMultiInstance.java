import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alvin Natawiguna on 10/27/2015.
 */
public class ReplSetTestMultiInstance {
    final String clusterName = "testing_multi_set_string";
    final String testString = "hello, world!";

    ReplSet<String> testInstance1, testInstance2;

    @Before
    public void setUp() {
        try {
            testInstance1 = new ReplSet<>(clusterName);
            testInstance2 = new ReplSet<>(clusterName);
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(testInstance1.add(testString));

        Thread.sleep(500);
        assertTrue(testInstance2.contains(testString));
    }

    @Test
    public void testContains() throws Exception {
        testInstance1.add(testString);

        Thread.sleep(500);
        assertTrue(testInstance1.contains(testString));
        assertTrue(testInstance2.contains(testString));
    }

    @Test
    public void testNotContains() throws Exception {
        assertFalse(testInstance1.contains(testString));
        assertFalse(testInstance2.contains(testString));
    }

    @Test
    public void testRemove() throws Exception {
        testInstance1.add(testString);

        Thread.sleep(500);
        assertTrue(testInstance1.remove(testString));

        Thread.sleep(500);
        assertFalse(testInstance2.contains(testString));
    }

    @Test
    public void testNotRemove() throws Exception {
        assertFalse(testInstance1.remove(testString));
        assertFalse(testInstance2.remove(testString));
    }

    @After
    public void tearDown() {
        try {
            testInstance1.close();
            testInstance2.close();
        } catch (IOException e) {
            // ignore
        }
    }
}
