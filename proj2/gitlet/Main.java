package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zicheng Liang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    static Repository repo = new Repository();
    public static void main(String[] args)  {
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
                        String[] fileNames = new String[args.length - 1];
                        for (int i = 0; i < fileNames.length; i++) {
                            File theFile = new File(fileNames[i]);
                            if (theFile.isFile()) {
                                repo.stage(new Tree(fileNames[i], new Blob(theFile)));
                            } else{
                                System.err.println("File does not exist.");
                            }
                        }
                        break;
                    case "commit":
                        if (args.length == 2) {
                            Commit newCommit = new Commit(args[1]);
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "rm":
                        // TODO: handle the `rm [filename]` command
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
                            repo.printLog();
                        } else {
                            System.err.println("Incorrect operands.");
                        }
                        break;
                    case "find":
                        // TODO: handle the `find [commit message]` command
                        break;
                    case "status":
                        if (args.length == 1) {
                            repo = Utils.readObject(Repository.repoFile, repo.getClass());
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
    }

    public static void printStatus() {
        StringBuilder status = new StringBuilder();

        status.append("=== Branches ===\n");
        for(Branch b : repo.branches.values()) {
            if (b.isCurrent(repo.head)) {
                status.append("*").append(b.name).append("\n");
            } else {
                status.append(b.name).append("\n");
            }
        }
        status.append("\n");

        status.append("=== Staged Files ===\n");
        for (Tree t : repo.aStage) {
            System.out.println(t.map.keySet());
        } // TODO: test if this works
        status.append("\n");

        status.append("=== Removed Files ===\n");
        //TODO: complete removed files and finish this
        status.append("\n");

        status.append("=== Modifications Not Staged For Commit ===\n");
        //TODO: complete related functions, sample:
        // junk.txt (deleted)
        // wug3.txt (modified)
        status.append("\n");

        status.append("=== Untracked Files ===\n");
        // TODO: complete this.
        status.append("\n");

       System.out.println(status.toString());
    }
}
