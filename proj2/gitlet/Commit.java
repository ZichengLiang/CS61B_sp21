package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Objects;

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
     * Combinations of log messages, other metadata (commit date, author, etc.), a reference to a tree, and references to parent commits.
     * The repository also maintains a mapping from branch heads to references to commits, so that certain important commits have symbolic names.
     *
     */

    /** message & ID */
    private String message;
    private String ID;
    private Commit parent;

    /** metadata */
    private LocalDateTime timeStamp;
    private Tree blobTree = new Tree();
    private String log;
    private String branch;
    public File fileLocation;
    public final File logLocation = Utils.join(Repository.GITLET_DIR, "logs");

    public Commit(String message, LocalDateTime timeStamp, Commit parent, String branch) throws IOException {
        this.message = message;
        this.timeStamp = timeStamp;

        if (parent != null) {
            this.parent = parent;
        } else {
            this.parent = this;
        }

        this.branch = branch;

        ID = Utils.sha1(this.message, this.timeStamp.toString(),
                        this.parent.toString(),
                        blobTree.toString());

        fileLocation = makeFileLocation(ID);

        saveBlobs();
        this.makeObject(fileLocation);

        log = generateLog(ID, this.timeStamp, this.message);
        this.writeGlobalLog();
    }

    public Commit(String message, Commit parent, String branch) throws IOException {
        new Commit(message, LocalDateTime.now(), parent, branch);
    }

    public Commit (String message, Commit parent) throws IOException {
        new Commit(message, LocalDateTime.now(), parent, parent.branch);
    }
    public Commit (String message, LocalDateTime timeStamp) throws IOException {
        new Commit(message, timeStamp, null, "master");
    }

    /** the default commit constructor is set for the initial commit */
    public Commit() throws IOException {
        new Commit("initial commit",
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
    }

    String getID() {
        return ID;
    }

    Commit getParent() {
        return parent;
    }

    List<Blob> getBlobTree() {
        return blobTree.blobs;
    }

    void saveBlobs() throws IOException {
        for (Blob blob : getBlobTree()) {
            makeObject(makeFileLocation(blob.ID));
        }
    }

    public void makeObject(File fileLocation) throws IOException {
        Utils.writeObject(fileLocation, this);
    }

    /** write global logs as the commits go on */
    public void writeGlobalLog() throws IOException {
        if (!logLocation.exists()) {
            logLocation.createNewFile();
        }
        Utils.appendFile(logLocation, log);
    }

    /** generate the log for the Commit */
    private String generateLog(String ID, LocalDateTime timeStamp, String message) {
        return ("===\ncommit " + ID + "\n" +
                "Date: " + timeStamp.toString() + "\n" + message + "\n");
    }

    /** return a file which follows such pattern:
     *  - objects
     *      - ID[0]ID[1] (directory)
     *          - ID[rest] (file)
     * @param ID
     * @return fileLocation;
     */
    private File makeFileLocation(String ID) {
        File dirLocation = Utils.join(Repository.GITLET_OBJ, ID.substring(0, 2));
        dirLocation.mkdirs();
        return new File(dirLocation.toString() + "/" +  ID.substring(2));
    }

    protected String getLog() {
        return log;
    }

}
