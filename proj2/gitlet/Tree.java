package gitlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * trees: directory structures mapping names to references to blobs and other trees (subdirectories).
 * @author Zicheng Liang
 */
public class Tree {
    // TODO: read the specs and maybe watch the video to construct this class
    // Every commit contains a tree whose nodes are blobs and subtrees

    Map<String, Tree> map = new HashMap<>();
    Blob blob;

    public Tree (String name, Blob blob) {
        this.put(name, blob);
        this.blob = blob;
    }

    public Tree (String name, Tree tree) {
        map.put(name, tree);
    }

    public void put (String name, Blob blob) {
        Tree newTree = new Tree(name, blob);
        map.put(name, newTree);
    }

    public boolean hasBlob() {
        return !(blob == null);
    }

}
