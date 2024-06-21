package deque;

import java.util.LinkedList;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;
import static org.junit.Assert.*;
public class ArrayDequeTest {
    @Test
    /* this tests the functionality of addFirst and addLast, resizing not included */
    public void addTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ad1.addLast("Last");
        ad1.addFirst("Middle");
        ad1.addFirst("First");

        System.out.printf("Printing out deque: \n");
        ad1.printDeque();
    }
    @Test
    public void addResizeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 64; i++) {
            ad1.addLast(i);
        }

        System.out.printf("Printing out deque: \n");
        ad1.printDeque();
    }
}
