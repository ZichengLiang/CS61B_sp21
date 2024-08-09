package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * trees: directory structures mapping names to references to blobs and other trees (subdirectories).
 * @author Zicheng Liang
 */
public class Tree implements Serializable {
    // TODO: read the specs and maybe watch the video to construct this class
    // Every commit contains a tree whose nodes are blobs and subtrees

    public Map<String, Tree> map = new HashMap<>();
    public Blob blob;

    public Tree (String name, Blob blob) {
        this.put(name, blob);
        this.blob = blob;
    }

    public Tree (String name, Tree tree) {
        map.put(name, tree);
    }

    public Tree (String name, List<Tree> trees) {
        for(Tree t : trees) {
            map.put(t.blob.id, t);
        }
    }

    public void put (String name, Blob blob) {
        Tree newTree = new Tree(name, blob);
        map.put(name, newTree);
    }

    public boolean hasBlob() {
        return !(blob == null);
    }

    public boolean hasBlob(String ID) {
        return blob.id.equals(ID);
    }
}
