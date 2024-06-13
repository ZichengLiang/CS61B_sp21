/** Class that prints the Collatz sequence starting from a given number.
 *  @author Zicheng Liang
 */
public class Collatz {

    /** if n is even, the next number is n / 2
     *  if n is odd, the next number is 3n + 1
     *  if n is 1, the sequence is over
     */
    public static int nextNumber(int n) {
       if (n % 2 == 0) {
           return n / 2;
       } else if (n % 2 == 1){
           return 3 * n + 1;
       } else {
           return 0;
       }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

