import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by Alvin Natawiguna on 10/27/2015.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReplSetTestString {

    final String clusterName = "testing_set_string";
    final String testString = "hello, world!";

    ReplSet<String> testSet;

    @Before
    public void setUp() {
        try {
            testSet = new ReplSet<>(clusterName);
        } catch (Exception e) {
            // ignore
        }
    }

    @Test
    public void testAdd() throws Exception {
        assertTrue(testSet.add(testString));
    }

    @Test
    public void testContains() throws Exception {
        testSet.add(testString);

        Thread.sleep(200);
        assertTrue(testSet.contains(testString));
    }

    @Test
    public void testNotContains() throws Exception {
        assertFalse(testSet.contains(testString));
    }

    @Test
    public void testRemove() throws Exception {
        testSet.add(testString);

        Thread.sleep(200);
        assertTrue(testSet.remove(testString));
    }

    @Test
    public void testNotRemove() throws Exception {
        assertFalse(testSet.remove(testString));
    }

    @After
    public void tearDown() {
        try {
            testSet.close();
        } catch (IOException e) {
            // ignore
        }
    }
}