package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> c;
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
    public T max(Comparator<T> comp) {
        if (!this.isEmpty() && comp != null) {
            T max = this.get(0);
            for (T i : this) {
                if (comp.compare(max, i) < 0) {
                    max = i;
                }
            }
            return max;
        }
        return null;
    }

}
