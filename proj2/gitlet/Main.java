package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zicheng Liang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    static Repository repo;
    public static void main(String[] args)  {
        if (!Repository.REPO_STATE.exists()) {
            repo = new Repository();
        } else {
            repo = Utils.readObject(Repository.REPO_STATE, Repository.class);
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
                            if(Objects.requireNonNull(
                                    Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION)).isEmpty()
                                    && Objects.requireNonNull(
                                            Utils.plainFilenamesIn(Repository.STAGE_FOR_REMOVAL)).isEmpty()
                            ) {
                                System.err.println("No changes added to the commit.");
                                break;
                            }
                            if(args[1].isEmpty()) {
                                System.err.println("Please enter a commit message.");
                            }
                            // if there's no error:
                            repo.makeCommit(args[1]);
                        } else {
                            System.err.println("Please enter a commit message.");
                        }
                        break;
                    case "rm":
                        if (args.length == 2) {
                            repo.remove(args[1]);
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "log":
                        if (args.length == 1) {
                            repo.printLog();
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
                            repo = Utils.readObject(Repository.REPO_STATE, repo.getClass());
                            repo.printStatus();
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "branch" :
                        if (args.length == 2) {
                            repo.setCurrentBranch(args[1]);
                        } else {
                            System.err.println("Incorrect operands.");
                        }
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
        Utils.writeObject(Repository.REPO_STATE, repo);
    }
}
