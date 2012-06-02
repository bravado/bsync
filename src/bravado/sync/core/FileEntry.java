package bravado.sync.core;

/**
 * FileEntry
 * bob
 * 6/2/12 5:15 PM
 */
public class FileEntry {
    private String filename;
    private String hash;

    public FileEntry(String filename, String hash) {
        this.filename = filename;
        this.hash = hash;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof FileEntry) {
            return ((FileEntry) o).getFilename().equals(getFilename()) && ((FileEntry) o).getHash().equals(getHash());
        }
        return false;
    }
}
