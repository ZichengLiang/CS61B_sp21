package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Branch implements Serializable {
    String name;
    List<Commit> commits = new LinkedList<>();
    Map<String, Commit> commitMap = new HashMap<>();

    public Branch() throws IOException {
        name = "master";
        commits.add(new Commit());
        commitMap.put(commits.get(0).getID(), commits.get(0));
    }

    public Commit getCommit(String ID) {
        return commitMap.get(ID);
    }

    public boolean isCurrent(Commit head) {
        for(Commit c : commitMap.values()) {
            if (c.equals(head)) {
                return true;
            }
        }
        return false;
    }

}
