package gitlet;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;

import jh61b.junit.In;

/**
 * blobs: The saved contents of files.
 * Since Gitlet saves many versions of files, a single file might correspond to multiple blobs,
 * of which each being tracked in a different commit.
 * @author Zicheng Liang
 */

public class Blob implements Serializable {
    // For a blob:
    // if there's change in the file, save its contents
    // otherwise, save a reference to the last version of the file

    public Blob last;
    public File file;
    public String id;

    /** the "root" blob points to itself
     *  isRoot method can return a boolean whether it is a root or not */
    Blob (File file) {
        this.last = this;
        this.file = file;
        this.id = Utils.sha1(file);
    }
    public boolean isRoot() {
        return this.last.equals(this);
    }

    /** the "successor" blob successes another root/successor blob.
     *  isSuc method can return a boolean whether it is a successor or not */
    Blob (Blob last, File file) {
        this.last = last;
        this.file = file;
    }
    public boolean isSuc() {
        return (!this.last.equals(this)) && (this.file != null);
    }

    public boolean checkDiff(Blob otherBlob) {
        return this.id.equals(otherBlob.id);
    }
}
