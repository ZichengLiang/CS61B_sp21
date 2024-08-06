package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Zicheng Liang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    static Repository repo = new Repository();
    public static void main(String[] args) {
        if (args.length > 0) {
            String firstArg = args[0];
            switch(firstArg) {
                case "init":
                    if (args.length - 1 == 1) {
                        repo.init();
                    } else {
                        System.err.println("Incorrect operands.");
                    }
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    break;
                case "commit":
                    // TODO: handle the `commit [message]' command
                    break;
                case "rm":
                    // TODO: handle the `rm [filename]` command
                    break;
                case "log":
                    // TODO: handle the `log` command
                    break;
                case "global-log":
                    // TODO: handle the `global-log` command
                    break;
                case "find":
                    // TODO: handle the `find [commit message]` command
                    break;
                case "status":
                    // TODO: handle the `status` command
                    break;

                    // TODO: ... more commands from `checkout`
                default:
                    System.err.println("No command with that name exists.");
                    break;
            }
        } else {
            System.err.println("Please enter a command.");
        }
    }
}
