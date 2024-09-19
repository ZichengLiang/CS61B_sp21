package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zicheng Liang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    static Repository repo;
    public static void main(String[] args)  {
        if (!Repository.repoState.exists()) {
            repo = new Repository();
        } else {
            repo = Utils.readObject(Repository.repoState, Repository.class);
        }

        try {
            if (args.length > 0) {
                String firstArg = args[0];
                switch(firstArg) {
                    case "init":
                        if (args.length == 1) {
                            repo.init();
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "add":
                        /** in gitlet, only one file may be added at a time */
                        if (args.length == 2) {
                            String fileName = args[1];
                            if (Utils.plainFilenamesIn(Repository.CWD)
                                     .contains(fileName)) {
                                File target = Utils.join(Repository.CWD, fileName);
                                repo.add(target, fileName);
                            } else {
                                System.err.println("File does not exist.");
                            }
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "commit":
                        if (args.length == 2) {
                            repo.makeCommit(args[1]);
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "rm":
                        if (args.length == 2) {
                            // TODO: handle the `rm [filename]` command
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "log":
                        if (args.length == 1) {
                            // TODO: this should print the commmits made in the current branch
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "global-log":
                        if (args.length == 1) {
                            repo.printGlobalLog();
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "find":
                        // TODO: handle the `find [commit message]` command
                        break;
                    case "status":
                        if (args.length == 1) {
                            repo = Utils.readObject(Repository.repoState, repo.getClass());
                            printStatus();
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    // TODO: ... more commands from `checkout`
                    default:
                        System.err.println("No command with that name exists.");
                        break;
                }
            } else {
                System.err.println("Please enter a command.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(Repository.repoState, repo);
    }

    public static void printStatus() {
        StringBuilder status = new StringBuilder();

        status.append("=== Branches ===\n");
        for (String branch : repo.branches) {
            if (branch.equals(repo.currentBranch)) {
                status.append("*");
            }
            status.append(branch).append("\n");
        }
        status.append("\n");

        status.append("=== Staged Files ===\n");
        status.append(Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION));
        status.append("\n\n");

        status.append("=== Removed Files ===\n");
        status.append(Utils.plainFilenamesIn(Repository.STAGE_FOR_REMOVAL));
        status.append("\n\n");

        status.append("=== Modifications Not Staged For Commit ===\n");
        //TODO: complete related functions, sample:
        // junk.txt (deleted)
        // wug3.txt (modified)
        status.append("\n\n");

        status.append("=== Untracked Files ===\n");
        // TODO: complete this.
        status.append("\n\n");

       System.out.println(status.toString());
    }
}
