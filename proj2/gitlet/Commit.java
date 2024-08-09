package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
public class Commit implements Serializable {
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
    public String log;
    public final File dirLocation;
    public final File fileLocation;
    public final File logLocation = Utils.join(Repository.GITLET_DIR, "logs");
    /* TODO: fill in the rest of this class. */

    /** the default commit constructor is set for the initial commit */
    public Commit() throws IOException {
        message = "initial commit";
        timeStamp = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        ID = Utils.sha1(message, timeStamp.toString());
        log = ("===\ncommit " + ID + "\n" + "Date: " + timeStamp.toString() + "\n" + message + "\n");
        dirLocation = Utils.join(Repository.GITLET_OBJ, ID.substring(0, 2));
        fileLocation = Utils.join(dirLocation, ID.substring(2));
        this.makeObject();
        this.writeLog();
    }

    public Commit(String message) throws IOException {
        this.message = message;
        timeStamp = LocalDateTime.now();
        ID = Utils.sha1(message, timeStamp);
        dirLocation = Utils.join(Repository.GITLET_OBJ, ID.substring(0, 2));
        fileLocation = Utils.join(dirLocation, ID.substring(2));
        commitTree = new Tree(message, Utils.readObject(Repository.aStageArea, Tree.class));
        this.makeObject();
        this.writeLog();
        Utils.restrictedDelete(Repository.aStageArea);
    }

    String getID() {
        return ID;
    }

    public void makeObject() throws IOException {
        dirLocation.mkdirs();
        Utils.writeObject(fileLocation, this);
    }

    public void writeLog() throws IOException {
        if (!logLocation.exists()) {
            logLocation.createNewFile();
        }

        Utils.appendFile(logLocation, log);
    }
}
