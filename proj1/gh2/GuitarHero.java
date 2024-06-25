package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static final double CONCERT_A = 440.0;
    GuitarHero() {
        fillNotes();
    }
    static GuitarString[] notes = new GuitarString[37];
    static double getFrequency(double i) {
        return CONCERT_A * Math.pow(2, i / 12.0);
    }

    static void fillNotes() {
        for (int i = 0; i < notes.length; i++) {
            notes[i] = new GuitarString(getFrequency(i));
        }
    }

    static boolean keyboardContains(char key) {
        char[] keys = KEYBOARD.toCharArray();
        for (int i = 0; i < keys.length; i++) {
            if (key == keys[i]) {
                return true;
            }
        }
        return false;
    }

    static GuitarString getString(char key) {
        if (keyboardContains(key)) {
            return notes[KEYBOARD.indexOf(key)];
        }
        return null;
    }

    public static void main(String[] args) {
        /* create two guitar strings, for concert A and C */
        fillNotes();
        GuitarString theString;
        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                theString = getString(key);
                if (theString != null) {
                    theString.pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = .0;
            for (GuitarString i : notes) {
                sample += i.sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (GuitarString i : notes) {
                i.tic();
            }
        }
    }
}
