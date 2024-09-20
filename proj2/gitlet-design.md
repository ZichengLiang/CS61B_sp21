# Gitlet Design Document

Zicheng Liang:

----
## Classes and Data Structures

----
#### Main: a driver class which calls others to function

##### Fields

---
#### Blob: the basic class for files
In gitlet, every file is represented by a blob

##### Fields:
- `public Blob last`
- `public File file`
- `public String id`

##### Methods:
- Constructors:
    - `Blob (File file)`
    - `Blob (Blob last, File file)`
- `public boolean isRoot()`
- `public boolean isSuc()`
- `public boolean checkDiff(Blob otherBlob)`
----
#### Tree: a structure of blobs and other trees
In gitlet, a tree is a representaion of a series of files (or a single one)

##### Fields:
`public Map<String, Tree> map`: a map to save name-tree pairs
`public Blob blob`: a blob's slot to contain a blob

##### Methods:
- Constructors:
    - `public Tree (String name, Blob blob)` this constructor is used for wrapping a blob into the tree
    - `public Tree (String name, Tree tree)` this constructor is used for wrapping a tree into the tree
    - `public Tree (String name, List<Tree> trees)` this constructor is used for wrapping multiple tree into this tree
- `public boolean hasBlob()` this is used for checking if the tree contains a sigle blob
- `public boolean hasBlob(String ID)` this is used for checking if the specific blob exists in this tree.

----
#### Commit
Combinations of log messages, other metadata (commit date, author, etc.), a reference to a tree, and references to parent commits.

##### Fields:
/- ID -/
`private String message`
`private String ID`
`private List<String> parentID`

/- Metadata -/
`private LocalDateTime timeStamp`
`private Tree commitTree`
`public String log`

/- FilePath -/
`public final File dirLocation`
`public final File fileLocation`
`public final File logLocation`

##### Methods:
/- Constructor -/
`public Commit()` : the constructor used for gitlet initialisation

`public Commit(String message)` : the constructor used for regular commits which allows the user's input message



----
#### Branch:

----
#### Repository: a gitlet repository
The repository is divided into several areas:
- The commits "warehouses"
- The stage area
- The "headquarter" which has the information of:
    - tracked files
    - untracked files
    - current working branch
    - the head commit

##### Fields:
- File paths:
    - `public static final file CWD`
    - `public static final file GITLET_DIR`
    - `public static final file GITLET_OBJ`
    - `public static final file repoFile`
- `public Map<String, Branch> branches`
- `public Commit head`
- `public List<Tree> aStage = new LinkedList<>()`
##### Methods:
- `public void init()`

- `public void setHead()`

- `public List<String> allFilesInCWD()`

- `public void stage(Tree tree)`
- `public List<Tree> loadStage()`

- `public void printLog()`

> In real Git's design, there is a file HEAD which points to refs/heads/[theHead] in which [theHead] contains a hash value which can be directed in /objects.

----

## Algorithms

---
## Persistence

----

