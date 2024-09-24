package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    protected String head;
    protected String currentBranch;
    protected List<String> branches = new ArrayList<>();
    protected List<Commit> commitTree = new LinkedList<>();
    protected Status status = new Status();

    protected class Status implements Serializable {
        protected Set<String> stageForAddition = new HashSet<>();
        protected Set<String> stageForRemoval = new HashSet<>();
        protected Set<String> trackedFiles = new HashSet<>();
        protected Set<String> untrackedFiles = new HashSet<>();

        protected boolean addAllFilesIn(Commit commit) {
            if (commit != null) {
                trackedFiles.addAll(commit.getBlobNames());
                return true;
            }
            return false;
        }
        protected Set<String> getUntrackedFiles() {
            List<String> allCWDFiles = Utils.plainFilenamesIn(CWD);
            for(String file : trackedFiles) {
                allCWDFiles.remove(file);
            }
            untrackedFiles.addAll(allCWDFiles);
            return untrackedFiles;
        }

        protected void clearStages() {
            for (String file : stageForAddition) {
                stageForRemoval.remove(file);
            }
        }
        protected void printStatus() {
            clearStages();
            StringBuilder status = new StringBuilder();

            status.append("=== Branches ===\n");
            for (String branch : branches) {
                if (branch.equals(currentBranch)) {
                    status.append("*");
                }
                status.append(branch).append("\n");
            }

            status.append("\n=== Staged Files ===\n");
            for (String fileName : Utils.plainFilenamesIn(STAGE_FOR_ADDITION)) {
                status.append(fileName).append("\n");
            }

            status.append("\n=== Removed Files ===\n");
            for (String fileName : Utils.plainFilenamesIn(STAGE_FOR_REMOVAL)) {
                status.append(fileName).append("\n");
            }

            for (String fileName : trackedFiles) {
                if (!Utils.plainFilenamesIn(Repository.CWD).contains(fileName)) {
                    status.append(fileName).append("\n");
                }
            }

            status.append("\n=== Modifications Not Staged For Commit ===\n");
        /* sample:
         junk.txt (deleted)
         wug3.txt (modified)
         */

            status.append("\n=== Untracked Files ===\n");
            for (String file: getUntrackedFiles()) {
                status.append(file).append("\n");
            }
            System.out.println(status.toString());
        }
    }

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
        status.addAllFilesIn(newCommit);
        commitTree.add(newCommit);
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
                    && this.status.trackedFiles.contains(fileName)
                    // the target file has already been tracked
            ) {
                status.stageForAddition.remove(stgFile);
                Utils.delete(stgFile);
                return true;
            }
            // the normal situation: copy the file from CWD to the stage for addition
            byte[] content = Utils.readContents(file);
            status.stageForAddition.add(fileName);
            File fileToAdd = Utils.join(STAGE_FOR_ADDITION, fileName);
            Utils.writeContents(fileToAdd, content);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(String fileName) {
        if (Utils.join(STAGE_FOR_ADDITION, fileName).exists()) {
            // Unstage the file if it is currently staged for addition.
            status.stageForAddition.remove(fileName);
            Utils.delete(Utils.join(STAGE_FOR_ADDITION, fileName));
            return true;
        } else if (status.trackedFiles.contains(fileName)) {
            // Delete the file from the working directory
            File target = Utils.join(CWD, fileName);
            if (target.exists()) {
                status.stageForRemoval.add(fileName);

                String content = Utils.readContentsAsString(target);
                Utils.writeContents(Utils.join(STAGE_FOR_REMOVAL, fileName), content);
                Utils.delete(target);
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
    private String recursiveLog(String currentID, String log) {
        return "";
    }
    public void printGlobalLog() {
        System.out.println(Utils.readContentsAsString(Utils.join(".gitlet", "logs")));
    }

    protected void printStatus() {
        status.printStatus();
    }

}

