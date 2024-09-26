package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Formatter;
import java.util.Set;

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
     * Combinations of log messages, other metadata (commit date, author, etc.),
     * a reference to a tree, and references to parent commits.
     * The repository also maintains a mapping from branch heads to references to commits,
     * so that certain important commits have symbolic names.
     */

    /** message & ID */
    protected String message;
    protected String ID;
    protected String parent1ID;
    protected String parent2ID;

    /** metadata */
    private LocalDateTime timeStamp;
    private Tree blobTree = new Tree();
    private String log;
    private String branch;
    protected final File logLocation = Utils.join(Repository.GITLET_DIR, "logs");

    /* CONSTRUCTORS */

    /** the "root" constructor */
    public Commit(String message, LocalDateTime timeStamp,
                  String parent1ID, String parent2ID, String branch) throws IOException {
        this.message = message;
        this.timeStamp = timeStamp;

        this.parent1ID = parent1ID;
        this.parent2ID = parent2ID;

        this.branch = branch;

        this.ID = Utils.sha1(this.message, this.timeStamp.toString(),
                        this.parent1ID, this.parent2ID, this.branch);

        log = generateLog(ID, this.timeStamp, this.message);
    }

    /** the constructor for merge command, as it allows to specify two parents */
    public static Commit newCommit(String message, String parent1, String parent2, String branch)
            throws IOException {
        return new Commit(message, LocalDateTime.now(), parent1, parent2, branch);
    }

    /** the usual constructor which specifies message, single parent and the branch name */
    public static Commit newCommit(String message, String parent1, String branch)
            throws IOException {
        return new Commit(message, LocalDateTime.now(), parent1, "0", branch);
    }

    /** the constructor specialised for init() as it can specify the timestamp */
    public static Commit newCommit(String message, LocalDateTime timeStamp) throws IOException {
        return new Commit(message, timeStamp, "0", "0", "master");
    }

    String getID() {
        return ID;
    }

    String getBlobID(String blobName) {
        if (blobTree.getBlobNames().contains(blobName)) {
            return blobTree.getBlobID(blobName);
        }
        return " ";
    }

    String getParent1ID() {
        return parent1ID;
    }

    String getParent2ID() {
        return parent2ID;
    }

    String getBranch() {
        return branch;
    }

    Set<String> getBlobNames() {
        return blobTree.getBlobNames();
    }

    /** write global logs as the commits go on */
    public void writeGlobalLog() throws IOException {
        if (!logLocation.exists()) {
            logLocation.createNewFile();
        }
        Utils.appendFile(logLocation, log);
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    /** generate the log for the Commit */
    private String generateLog(String id, LocalDateTime time, String commitMessage) {
        // Convert LocalDateTime to Date
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());

        // Create a formatter for formatting the date
        Formatter formatter = new Formatter();

        // Set the desired format for the output
        formatter.format("Date: %ta %tb %td %tT %tY %tz", date, date, date, date, date, date);

        return ("===\ncommit " + id + "\n"
                + formatter
                + "\n" + commitMessage + "\n\n");
    }


    /** return a file which follows such pattern:
     *  - objects
     *      - ID[0]ID[1] (directory)
     *          - ID[rest] (file)
     * @param
     * @return fileLocation;

    protected File makeFileLocation(String id) {
        File dirLocation = Utils.join(Repository.GITLET_OBJ, id.substring(0, 2));
        dirLocation.mkdirs();
        return new File(dirLocation.toString() + "/" +  id.substring(2));
    }
    */

    protected String getLog() {
        return log;
    }

}
