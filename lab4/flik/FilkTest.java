package flik;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FilkTest {
    @Test
    public void ObjectToIntTest() {
        int testBt = 128;
        Integer testRt = 128;
        assertTrue(testRt == (Integer) testBt);
    }

    @Test
    public void IntEqualsTest() {
        int testBt = 128;
        Integer testRt = 128;
        assertTrue(testRt.equals(testBt)) ;
    }
}
