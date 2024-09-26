package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author Zicheng Liang
 */
public class Main {
    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    static Repository repo;

    /* Errors */
    private static final String NOT_IN_INIT_REPO = "Not in an initialized Gitlet directory.";
    private static final String INCORRECT_OPERANDS = "Incorrect operands.";
    private static final String FILE_NOT_EXIST = "File does not exist.";
    private static final String NO_COMMAND_NAME = "No command with that name exists.";
    private static final String NO_COMMAND_ENTRY = "Please enter a command.";
    private static final String NOT_IN_REPOSITORY = "Not in an initialized Gitlet directory.";
    private static final String NO_CHANGE_TO_COMMIT = "No changes added to the commit.";

    public static void main(String[] args) {
        int argc = args.length;
        if (!repoInitialised()) {
            repo = null;
        } else {
            repo = Utils.readObject(Repository.REPO_STATE, Repository.class);
        }

        try {
            checkCommandEntry(argc);
            String firstArg = args[0];
            if(!firstArg.equals("init") && !repoInitialised()) {
                System.err.println("Not in an initialized Gitlet directory.");
                throw new GitletException();
            }

            switch (firstArg) {
                case "init":
                    if (checkArgc(argc, 1)) {
                        break;
                    }
                    repo = new Repository();
                    break;
                case "add":
                    /** in gitlet, only one file may be added at a time */
                    if (checkArgc(argc, 2)) {
                        break;
                    }
                    String fileName = args[1];

                    if (check(checkFileIn(fileName, Repository.CWD), FILE_NOT_EXIST)) {
                        break;
                    }
                    File target = Utils.join(Repository.CWD, fileName);
                    repo.add(target, fileName);
                    break;
                case "commit":
                    if (checkArgc(argc, 2)) {
                        break;
                    }
                    if (Objects.requireNonNull(
                            Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION)).isEmpty()
                            && Objects.requireNonNull(
                            Utils.plainFilenamesIn(Repository.STAGE_FOR_REMOVAL)).isEmpty()
                    ) {
                        System.err.println("No changes added to the commit.");
                        break;
                    }
                    if (args[1].isEmpty()) {
                        System.err.println("Please enter a commit message.");
                        break;
                    }
                    // if there's no error:
                    repo.makeCommit(args[1]);
                    break;
                case "rm":
                    if (checkArgc(argc, 2)) {
                        break;
                    }
                    repo.remove(args[1]);
                    break;
                case "log":
                    if (checkArgc(argc, 1)) {
                        break;
                    }
                    repo.printLog();
                    break;
                case "global-log":
                    if (checkArgc(argc, 1)) {
                        break;
                    }
                    repo.printGlobalLog();
                    break;
                case "find":
                    if (checkArgc(argc, 2)) {
                        break;
                    }
                    repo.find(args[1]);
                    break;
                case "status":
                    if (checkArgc(argc, 1)) {
                        break;
                    }
                    repo = Utils.readObject(Repository.REPO_STATE, repo.getClass());
                    repo.printStatus();
                    break;
                case "branch":
                    if (checkArgc(argc, 2)) {
                        break;
                    }
                    repo.setNewBranch(args[1]);
                    break;
                case "checkout":
                    if (args.length == 2) {
                        if (!repo.branches.contains(args[1])) {
                            System.err.println("No such branch exists.");
                            break;
                        } else if (repo.currentBranch.equals(args[1])) {
                            System.err.println("No need to checkout the current branch.");
                            break;
                        }
                    } else if (args.length == 3 && args[1].equals("--")) {
                        repo.checkout(args[2]);
                        break;
                    } else if (args.length == 4 && args[2].equals("--")) {
                        repo.checkout(args[1], args[3]);
                        break;
                    } else {
                        System.err.println(INCORRECT_OPERANDS);
                    }
                    break;
                default:
                    System.err.println(NO_COMMAND_NAME);
                    break;
            }
            if (!repoInitialised()) Repository.REPO_STATE.createNewFile();
        } catch (IOException e) { e.printStackTrace(); }
        Utils.writeObject(Repository.REPO_STATE, repo);
    }

    private static boolean checkFileIn(String file, File dir) {
        return Utils.plainFilenamesIn(dir).contains(file);
    }

    private static boolean checkNumber(int actual, int expected) {
        return actual == expected;
    }

    private static boolean repoInitialised() {
        return Repository.REPO_STATE.exists();
    }

    private static boolean check(boolean condition, String message) {
        if (!condition) {
            System.err.println(message);
            return true;
        }
        return false;
    }

    private static boolean checkArgc(int actual, int expected) {
        return check(checkNumber(actual, expected), INCORRECT_OPERANDS);
    }
    private static boolean checkCommandEntry(int argc) {
        return check(argc > 0, NO_COMMAND_ENTRY);
    }
}
