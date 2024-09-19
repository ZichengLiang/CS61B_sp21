package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The .gitlet/objects directory where we store the file backups. */
    public static final File GITLET_OBJ = join(GITLET_DIR, "objects");
    public static final File STAGE = join(GITLET_DIR, "stages");
    public static final File STAGE_FOR_ADDITION = join(STAGE, "addition");
    public static final File STAGE_FOR_REMOVAL = join(STAGE, "removal");
    public static final File repoState = new File(".gitlet/repo");

    /** other information in the repository */
    private Commit head;
    protected String currentBranch;
    protected List<String> branches = new ArrayList<>();
    public List<Commit> commitTree = new LinkedList<>();
    public Map<String, String> trackedFiles = new HashMap<>();
    public Map<String, String> untrackedFiles = new HashMap<>();
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
            System.err.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }

        repoState.createNewFile();
    }

    public void makeCommit(String message, String branch) throws IOException {
        Commit newCommit = new Commit(message, head, branch);
        commitTree.add(newCommit);
        head = commitTree.getLast();

        for (Blob blob : newCommit.getBlobTree()) {
            trackedFiles.put(blob.name, blob.ID);
        }
    }
    public void makeCommit(String message) throws IOException {
        makeCommit(message, currentBranch);
    }

    public boolean add(File file, String fileName) {
        if(STAGE_FOR_ADDITION.exists()) {
            // below handles the situation when a file is changed, added, and then changed back to its original version
            if (Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION).contains(fileName)
                    // the stage has a file with the same name
                    && Utils.sha1(file)
                            .equals(Utils.sha1(Utils.join(Repository.STAGE_FOR_ADDITION, fileName)))
                    // the file in stage has the same content with target file
                    && this.trackedFiles.containsKey(fileName)
                    // the target file has already been tracked
            ) {
                Utils.restrictedDelete(Utils.join(Repository.STAGE_FOR_ADDITION, fileName));
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

    public void printGlobalLog() {
        System.out.println(Utils.readContentsAsString(Utils.join(".gitlet", "logs")));
    }
}

