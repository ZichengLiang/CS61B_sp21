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
                System.exit(1);
            }

            switch (firstArg) {
                case "init":
                    checkArgc(argc, 1);
                    repo = new Repository();
                    break;
                case "add":
                    /** in gitlet, only one file may be added at a time */
                    checkArgc(argc, 2);
                    String fileName = args[1];

                    check(checkFileIn(fileName, Repository.CWD), FILE_NOT_EXIST);
                    File target = Utils.join(Repository.CWD, fileName);
                    repo.add(target, fileName);
                    break;
                case "commit":
                    checkArgc(argc, 2);
                    if (Objects.requireNonNull(
                            Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION)).isEmpty()
                            && Objects.requireNonNull(
                            Utils.plainFilenamesIn(Repository.STAGE_FOR_REMOVAL)).isEmpty()
                    ) {
                        System.err.println("No changes added to the commit.");
                        System.exit(1);
                    }
                    if (args[1].isEmpty()) {
                        System.err.println("Please enter a commit message.");
                        System.exit(1);
                    }
                    // if there's no error:
                    repo.makeCommit(args[1]);
                    break;
                case "rm":
                    checkArgc(argc, 2);
                    repo.remove(args[1]);
                    break;
                case "log":
                    checkArgc(argc, 1);
                    repo.printLog();
                    break;
                case "global-log":
                    checkArgc(argc, 1);
                    repo.printGlobalLog();
                    break;
                case "find":
                    // TODO: handle the `find [commit message]` command
                    break;
                case "status":
                    checkArgc(argc, 1);
                    repo = Utils.readObject(Repository.REPO_STATE, repo.getClass());
                    repo.printStatus();
                    break;
                case "branch":
                    checkArgc(argc, 2);
                    repo.setNewBranch(args[1]);
                case "checkout":
                    if (args.length == 2) {
                        // checkout [branch name]
                        // Takes all files in the commit at the head of the given branch, and puts them in the working directory, overwriting the versions of the files that are already there if they exist.
                        // Also, at the end of this command, the given branch will now be considered the current branch (HEAD).
                        // Any files that are tracked in the current branch but are not present in the checked-out branch are deleted.
                        // The staging area is cleared, unless the checked-out branch is the current branch (see Failure cases below).
                        if (!repo.branches.contains(args[1])) {
                            System.err.println("No such branch exists.");
                            System.exit(1);
                        } else if (repo.currentBranch.equals(args[1])) {
                            System.err.println("No need to checkout the current branch.");
                            System.exit(1);
                        }
                    } else if (args.length == 3 && args[1].equals("--")) {
                        //java gitlet.Main checkout -- [file name]
                        repo.checkout(args[2]);
                        // The new version of the file is NOT staged.
                    } else if (args.length == 4 && args[2].equals("--")) {
                        // checkout [commit id] -- [file name]
                        repo.checkout(args[1], args[3]);
                    } else {
                        System.err.println(INCORRECT_OPERANDS);
                    }
                    // TODO: ... more commands from `checkout`
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

    private static void check(boolean condition, String message) {
        if (!condition) {
            System.err.println(message);
            System.exit(1);
        }
    }

    private static void checkArgc(int actual, int expected) {
        check(checkNumber(actual, expected), INCORRECT_OPERANDS);
    }
    private static void checkCommandEntry(int argc) {
        check(argc > 0, NO_COMMAND_ENTRY);
    }
}
