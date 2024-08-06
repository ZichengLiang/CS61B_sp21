package gitlet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Branch {
    String name;
    List<Commit> commits = new LinkedList<>();
    Map<String, Commit> commitMap = new HashMap<>();

    public Branch() {
        name = "master";
        commits.add(new Commit());
        commitMap.put(commits.getFirst().getID(), commits.getFirst());
    }

    public Commit getCommit(String ID) {
        return commitMap.get(ID);
    }
}
