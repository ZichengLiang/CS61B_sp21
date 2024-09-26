package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;

/**
 * Represents a gitlet repository.
 *
 * @author Zicheng Liang
 */
public class Repository implements Serializable {
    /**
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

    /**
     * other information in the repository
     */
    protected String head;
    protected String currentBranch;
    protected List<String> branches = new ArrayList<>();
    protected Map<String, Commit> commitTree = new TreeMap<>();
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
            assert allCWDFiles != null;
            untrackedFiles.addAll(allCWDFiles);
            for (String file : trackedFiles) {
                if (allCWDFiles.contains(file)) {
                    untrackedFiles.remove(file);
                }
            }
            return untrackedFiles;
        }

        protected void clearStages() {
            for (String file : stageForAddition) {
                stageForRemoval.remove(file);
            }
        }
        protected void clearAfterCommit() {
            stageForAddition.clear();
            stageForRemoval.clear();
        }

        protected void printStatus() {
            clearStages();
            StringBuilder statusReport = new StringBuilder();

            statusReport.append("=== Branches ===\n");
            for (String branch : branches) {
                if (branch.equals(currentBranch)) {
                    statusReport.append("*");
                }
                statusReport.append(branch).append("\n");
            }

            statusReport.append("\n=== Staged Files ===\n");
            for (String fileName : stageForAddition) {
                statusReport.append(fileName).append("\n");
            }

            statusReport.append("\n=== Removed Files ===\n");
            for (String fileName : stageForRemoval) {
                statusReport.append(fileName).append("\n");
            }
            statusReport.append("\n=== Modifications Not Staged For Commit ===\n");
        /* sample:
         junk.txt (deleted)
         wug3.txt (modified)
         */

            statusReport.append("\n=== Untracked Files ===\n");
            for (String file : getUntrackedFiles()) {
                statusReport.append(file).append("\n");
            }

            System.out.println(statusReport);
        }
    }

    /* METHODS */
    Repository() throws IOException {
        init();
        currentBranch = "master";
        branches.add(currentBranch);
        Commit initCommit = new Commit("initial commit",
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0), "0", "0", "master");
        Utils.writeObject(Utils.generateObject(initCommit.ID), initCommit);
        commitTree.put(initCommit.getID(), initCommit);
        head = initCommit.getID();
    }

    public void init() throws IOException {
        if (!GITLET_DIR.exists()) {
            // create all subdirectories
            GITLET_OBJ.mkdirs();
            STAGE_FOR_ADDITION.mkdirs();
            STAGE_FOR_REMOVAL.mkdirs();
        } else {
            System.err.println(
                    "A Gitlet version-control system already exists in the current directory.");
        }
    }

    public void makeCommit(String message, String branch) throws IOException {
        Commit newCommit = Commit.newCommit(message, head, branch);
        status.addAllFilesIn(newCommit);
        status.clearAfterCommit();
        commitTree.put(newCommit.getID(), newCommit);
        head = newCommit.getID();
        Utils.writeObject(Utils.generateObject(newCommit.ID), newCommit);
    }

    public void makeCommit(String message) throws IOException {
        makeCommit(message, currentBranch);
    }

    public void setNewBranch(String name) {
        branches.add(name);
    }

    public boolean add(File file, String fileName) {
        if (STAGE_FOR_ADDITION.exists()) {
            File cwdFile = new File(Repository.CWD + "/" + fileName);
            File stgFile = new File(Repository.STAGE_FOR_ADDITION + "/" + fileName);

            if (status.stageForRemoval.contains(fileName)) {
                status.stageForRemoval.remove(fileName);
            }
            // below handles the situation when a file is changed, added,
            // and then changed back to its original version
            boolean fileAlreadyInSTG = stgFile.exists();
            boolean fileTracked = this.status.trackedFiles.contains(fileName);
            String cwdFileHash = Utils.sha1(Utils.readContentsAsString(cwdFile));
            String stgFileHash = " ";
            if (new File(STAGE_FOR_ADDITION + "/" + fileName).exists()) {
                stgFileHash = Utils.sha1(Utils.readContentsAsString(stgFile));
            }
            String cmtFileHash = " ";
            Set<String> currentCommitFiles = Utils.readCommitFrom(head).getBlobNames();
            if (currentCommitFiles.contains(fileName)) {
                cmtFileHash = Utils.readCommitFrom(head).getBlobID(fileName);
                cmtFileHash = Utils.readContentHashFromBlob(cmtFileHash);
            }
            // above are preparations for if statements

            if (fileTracked && ((fileAlreadyInSTG && (cwdFileHash.equals(stgFileHash))
                    || cmtFileHash.equals(cwdFileHash)))
                    // a file in the stage has the same content with target file
                ) {
                status.stageForAddition.remove(stgFile);
                Utils.delete(stgFile);
                return true;
            }
            // the normal situation: copy the file from CWD to the stage for addition
            byte[] content = Utils.readContents(file);
            status.stageForAddition.add(fileName);
            status.trackedFiles.add(fileName);
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

    public boolean checkout(String commitID, String fileName) throws IOException {
        File cwdFile = new File(CWD + "/" + fileName);
        if (!commitTree.containsKey(commitID)) {
            System.err.println("No commit with that id exists.");
            return false;
        }
        Commit theCommit = Utils.readCommitFrom(commitID);
        if (!theCommit.getBlobNames().contains(fileName)) {
            System.err.println("File does not exist in that commit.");
            return false;
        }
        String blobID = theCommit.getBlobID(fileName);
        String content = Utils.readContentAsStringFromBlob(blobID);

        if (!cwdFile.exists()) {
            cwdFile.createNewFile();
        }

        Utils.writeContents(cwdFile, content);

        return true;
    }

    public boolean checkout(String fileName) throws IOException {
        return checkout(head, fileName);
    }

    public void printLog() {
        System.out.println(recursiveLog(head, ""));
    }

    private String recursiveLog(String currentID, String log) {
        if (commitTree.get(currentID).message.equals("initial commit")) {
            return log + commitTree.get(currentID).getLog();
        }
        log = log + commitTree.get(currentID).getLog();
        String currentParent1ID = commitTree.get(currentID).getParent1ID();
        return recursiveLog(currentParent1ID, log);
    }

    public void printGlobalLog() {
        System.out.println(Utils.readContentsAsString(Utils.join(".gitlet", "logs")));
    }

    protected void printStatus() {
        status.printStatus();
    }
}

