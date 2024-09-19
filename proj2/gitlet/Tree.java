package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hamcrest.core.Every;

/**
 * trees: directory structures mapping names to references to blobs and other trees (subdirectories).
 * @author Zicheng Liang
 */
public class Tree implements Serializable {
    /** Every commit contains a tree whose nodes are blobs,
     * the tree is constructed when a new commit is being made,
     * it collects all files in the stage for addition. */
    protected List<Blob> blobs = new ArrayList<>();

    Tree() {
        for (String fileName : Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION)) {
            System.out.println("trying to wrap: " + fileName + " into the tree");
            blobs.add(new Blob(fileName));
            Utils.delete(Repository.STAGE_FOR_ADDITION + "/" + fileName);
        }
    }

    Tree(Commit parent) {
        // a naive implementation:
        // not considering the case when tracked files are modified
        this.blobs = parent.getBlobTree();
        new Tree();
    }

}
