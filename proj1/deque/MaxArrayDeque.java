package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> c;
    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }


    /** Returns the maximum element in the deque as governed by the previously given Comparator.
     *  If the MaxArrayDeque is empty, simply return null.
     */
    public T max() {
        return max(this.c);
    }

    /** Returns the maximum element in the deque as governed by the parameter Comparator c.
     *  If the MaxArrayDeque is empty, simply return null.
     */
    public T max(Comparator<T> c) {
        if (!this.isEmpty()) {
            T max = null;
            for (T i : this) {
                if (c.compare(max, i) < 0) {
                    max = i;
                }
            }
            return max;
        }
        return null;
    }

}
