package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * trees: directory structures mapping names to references
 * to blobs and other trees(subdirectories).
 * @author Zicheng Liang
 */
public class Tree implements Serializable {
    /** Every commit contains a tree whose nodes are blobs,
     * the tree is constructed when a new commit is being made,
     * it collects all files in the stage for addition. */
    protected Map<String, String> blobs = new HashMap<>();
    // <fileName, hashCode>

    Tree() throws IOException {
        List<String> stageForAddition = Utils.plainFilenamesIn(Repository.STAGE_FOR_ADDITION);
        if (stageForAddition != null) {
            for (String fileName : stageForAddition) {
                Blob currentBlob = new Blob(fileName);
                saveBlob(Utils.generateObject(currentBlob.getID()), currentBlob);
                blobs.put(currentBlob.getName(), currentBlob.getID());
                Utils.delete(Repository.STAGE_FOR_ADDITION + "/" + fileName);
            }
        }
    }

    protected Set<String> getBlobNames() {
        return blobs.keySet();
    }
    protected String getBlobID(String name) {
        return blobs.get(name);
    }
    public void saveBlob(File location, Blob theBlob) throws IOException {
        Utils.writeObject(location, theBlob);
    }

}
