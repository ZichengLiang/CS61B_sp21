package gitlet;

// TODO: any imports you need here

import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

/** Represents a gitlet commit object.
 * Combinations of:
 * 1) log messages,
 * 2) other metadata (commit date, author, etc.),
 * 3) a reference to a tree, and references to parent commits.
 *
 * The repository also maintains a mapping from branch heads to references to commits,
 * so that certain important commits have symbolic names
 *
 *  @author Zicheng Liang
 */
public class Commit {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    // metadata
    private String message;
    private String ID;
    private List<String> parentID;
    private LocalDateTime timeStamp;
    private Tree commitTree;
    /* TODO: fill in the rest of this class. */

    /** the default commit constructor is set for the initial commit */
    public Commit() {
        message = "initial commit";
        timeStamp = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        ID = Utils.sha1(message, timeStamp);
    }

    public Commit(String message) {
        this.message = message;
    }

    String getID() {
        return ID;
    }

}
