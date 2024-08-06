package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  @author Zicheng Liang
 */
public class Repository {
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
    private Map<String, Branch> branches = new TreeMap<>();
    private Commit head;

    public void init() {
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdirs();
            branches.put("master", new Branch());
            head = branches.get("master").commits.getFirst();
        } else {
            System.err.println("A Gitlet version-control system already exists in the current directory.");
        }
    }

    public void setHead(String ID) {
        this.head = branches.get("master").getCommit(ID);
    }

    /** returns a list of files that have not been added to the repository.
     *  returns null if there's no such file. **/
    /*
    public List<String> checkDiffFiles() {
        List<String> diffFiles = new ArrayList<>();
        for(String s : actualFiles) {
            if (!currentFiles.contains(s)) {
                diffFiles.add(s);
            }
        }
        return diffFiles;
    }
     */

    /*
    public void printDiffFiles() {
        for(String s : checkDiffFiles()) {
            System.out.println(s);
        }
    }
     */

    /** returns a list of the names of all plain files in the current working dir,
     *  in lexicographical order as Java strings. Return null if CWD is empty. */
    public List<String> allFilesInCWD() {
        String[] files = CWD.list();
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }
}
