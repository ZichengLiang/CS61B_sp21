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
    private File file;
    protected String ID;
    protected String name;

    Blob(String fileName) {
        name = fileName;
        file = new File(Repository.STAGE_FOR_ADDITION + "/" + fileName);
        ID = Utils.sha1(Utils.readContentsAsString(file));
    }
    public boolean checkDiff(Blob otherBlob) {
        return this.ID.equals(otherBlob.ID);
    }
}
