package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
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
    private String message;
    private String ID;
    private String parent1ID;
    private String parent2ID;

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

        ID = Utils.sha1(this.message,
                        this.timeStamp.toString(),
                        parent1ID, parent2ID,
                        blobTree.toString());
        Utils.generateObject(this.ID);

        log = generateLog(ID, this.timeStamp, this.message);
        this.writeGlobalLog();
    }

    /** the constructor for merge command, as it allows to specify two parents */
    public Commit (String message, String parent1, String parent2, String branch) throws IOException {
        new Commit(message, LocalDateTime.now(), parent1, parent2, branch);
    }

    /** the usual constructor which specifies message, single parent and the branch name */
    public Commit(String message, String parent1, String branch) throws IOException {
        new Commit(message, LocalDateTime.now(), parent1, "", branch);
    }

    /** the constructor specialised for init() as it can specify the timestamp */
    public Commit(String message, LocalDateTime timeStamp) throws IOException {
        new Commit(message, timeStamp, "", "", "master");
    }
    /** the constructor only for the initial commit */
    public Commit() throws IOException {
        new Commit("initial commit",
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
    }

    String getID() {
        return ID;
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

    /** generate the log for the Commit */
    private String generateLog(String id, LocalDateTime time, String commitMessage) {
        return ("===\ncommit " + id + "\n"
                + "Date: " + time.toString()
                + "\n" + commitMessage + "\n");
    }


    /** return a file which follows such pattern:
     *  - objects
     *      - ID[0]ID[1] (directory)
     *          - ID[rest] (file)
     * @param id
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
