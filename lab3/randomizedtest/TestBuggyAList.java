package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> test = new BuggyAList<>();

        int addTimes = 3;

        for (int i = 0; i < addTimes; i++) {
            correct.addLast(i);
            test.addLast(i);
            assertEquals(correct.size(), test.size());
        }

        for (int i = 0; i < addTimes; i++) {
            assertTrue(correct.removeLast().equals(test.removeLast()));
        }
    }

    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 500000;
        for (int i = 0; i < N; i++) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                B.size();
                L.size();
            } else if (operationNumber == 2 && L.size() > 0) {
                L.getLast();
                B.getLast();
            } else if (operationNumber == 3 && L.size() > 0) {
                L.removeLast();
                B.removeLast();
            }
        }
    }
}


