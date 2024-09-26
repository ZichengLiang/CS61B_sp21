package gitlet;

import java.io.File;
import java.io.Serializable;

/**
 * blobs: The saved contents of files.
 * Since Gitlet saves many versions of files, a single file might correspond to multiple blobs,
 * of which each being tracked in a different commit.
 * @author Zicheng Liang
 */

public class Blob implements Serializable {
    protected String id;
    protected String name;
    protected String contents;
    Blob(String fileName) {
        name = fileName;
        File file = new File(Repository.STAGE_FOR_ADDITION + "/" + fileName);
        contents = Utils.readContentsAsString(file);
        id = Utils.sha1(contents);
    }

    protected String getID() {
        return id;
    }
    protected String getName() {
        return name;
    }
    public boolean checkDiff(Blob otherBlob) {
        return this.id.equals(otherBlob.id);
    }
}
