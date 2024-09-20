package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  @author Zicheng Liang
 */
public class Repository implements Serializable {
    /**
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /* DIRS AND FILES */
    public static final File CWD = new File(System.getProperty("user.dir"));
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    public static final File GITLET_OBJ = join(GITLET_DIR, "objects");
    public static final File STAGE = join(GITLET_DIR, "stages");
    public static final File STAGE_FOR_ADDITION = join(STAGE, "addition");
    public static final File STAGE_FOR_REMOVAL = join(STAGE, "removal");
    public static final File REPO_STATE = new File(".gitlet/repo");

    /** other information in the repository */
    private Commit head;
    protected String currentBranch;
    protected List<String> branches = new ArrayList<>();
    protected List<Commit> commitTree = new LinkedList<>();
    protected Map<String, String> trackedFiles = new HashMap<>();
    // key: name; value: hashcode
    protected Map<String, String> untrackedFiles = new HashMap<>();

    /* METHODS */
    public void init() throws IOException {
        if (!GITLET_DIR.exists()) {
            // create all subdirectories
            GITLET_OBJ.mkdirs();
            STAGE_FOR_ADDITION.mkdirs();
            STAGE_FOR_REMOVAL.mkdirs();

            currentBranch = "master";
            branches.add(currentBranch);

            commitTree.add(new Commit());
        } else {
            System.err.println(
                    "A Gitlet version-control system already exists in the current directory."
            );
            return;
        }

        REPO_STATE.createNewFile();
    }

    public void makeCommit(String message, String branch) throws IOException {
        Commit newCommit = new Commit(message, head, branch);
        commitTree.add(newCommit);
        head = newCommit;

        for (Blob blob : newCommit.getBlobTree()) {
            trackedFiles.put(blob.name, blob.ID);
        }
    }
    public void makeCommit(String message) throws IOException {
        makeCommit(message, currentBranch);
    }

    public void setCurrentBranch(String name) {
        currentBranch = name;
        branches.add(name);
    }

    public boolean add(File file, String fileName) {
        if (STAGE_FOR_ADDITION.exists()) {
            File cwdFile = new File(Repository.CWD + "/" + fileName);
            File stgFile = new File(Repository.STAGE_FOR_ADDITION + "/" + fileName);
            // below handles the situation when a file is changed, added,
            // and then changed back to its original version
            if (Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION).contains(fileName)
                    // the stage has a file with the same name
                    && Utils.sha1(Utils.readContentsAsString(cwdFile))
                            .equals(Utils.sha1(Utils.readContentsAsString(stgFile)))
                    // the file in stage has the same content with target file
                    && this.trackedFiles.containsKey(fileName)
                    // the target file has already been tracked
            ) {
                Utils.delete(stgFile);
                // remove the file from the stage for addition
                return true;
            }
            // the normal situation: copy the file from CWD to the stage for addition
            byte[] content = Utils.readContents(file);
            File fileToAdd = Utils.join(STAGE_FOR_ADDITION, fileName);
            Utils.writeContents(fileToAdd, content);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(String fileName) {
        if (Utils.join(STAGE_FOR_ADDITION, fileName).exists()) {
            Utils.delete(Utils.join(STAGE_FOR_ADDITION, fileName));
            return true;
            // Unstage the file if it is currently staged for addition.
        } else if (trackedFiles.containsKey(fileName)) {
            File target = Utils.join(CWD, fileName);
            if (target.exists()) {
                String content = Utils.readContentsAsString(target);
                Utils.delete(target);

                Utils.writeContents(Utils.join(STAGE_FOR_REMOVAL, fileName), content);
            }
            return true;
        } else {
            System.err.println("No reason to remove the file.");
            return false;
        }
    }

    public boolean checkout() {
        return false;
    }

    public void printLog() {
        System.out.println(recursiveLog(head, ""));
    }
    private String recursiveLog(Commit current, String log) {
        //TASK: Cannot invoke "Object.equals(Object)" because "current" is null
        if (current.equals(commitTree.get(0))) {
            return log + commitTree.get(0).getLog();
        } else {
            log = log + current.getLog();
            return recursiveLog(current.getParent(), log);
        }
    }
    public void printGlobalLog() {
        System.out.println(Utils.readContentsAsString(Utils.join(".gitlet", "logs")));
    }

    public void printStatus() {
        StringBuilder status = new StringBuilder();

        status.append("=== Branches ===\n");
        for (String branch : branches) {
            if (branch.equals(currentBranch)) {
                status.append("*");
            }
            status.append(branch).append("\n");
        }

        status.append("\n=== Staged Files ===\n");
        for (String fileName : Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION)) {
            status.append(fileName).append("\n");
        }

        status.append("\n=== Removed Files ===\n");
        for (String fileName : Utils.plainFilenamesIn(Repository.STAGE_FOR_REMOVAL)) {
            status.append(fileName).append("\n");
        }

        status.append("\n=== Modifications Not Staged For Commit ===\n");
        /* sample:
         junk.txt (deleted)
         wug3.txt (modified)
         */
        /*
        Set<String> tracked = trackedFiles.keySet();
        List<String> CWDFiles = Utils.plainFilenamesIn(CWD);

        for (String fileName : tracked) {
            if (!CWDFiles.contains(fileName)) {
                status.append(fileName).append(" (deleted)");
            } else if (true){
               //TASK: complete the (modified) list
            }
            status.append("\n");
        }
         */

        status.append("\n=== Untracked Files ===\n");
        /*
        for (String fileName : CWDFiles) {
            if (!tracked.contains(fileName)) {
                status.append(fileName).append("\n");
            }
        }
        */
        System.out.println(status.toString());
    }
}

