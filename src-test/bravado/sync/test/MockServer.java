package bravado.sync.test;

import bravado.sync.core.FileEntry;
import bravado.sync.core.SyncToken;
import bravado.sync.core.SyncCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MockServer
 * bob
 * 6/2/12 5:11 PM
 */
public class MockServer extends SyncCore {
    
    public List<FileEntry> serverFiles = new ArrayList<FileEntry>();
    public HashMap<SyncToken, List<FileEntry>> history = new HashMap<SyncToken, List<FileEntry>>();

    public void addFile(SyncToken repo, FileEntry fileEntry) {
        serverFiles.add(fileEntry);


        history.put(repo, fileEntry);
    }

    public void addFile(FileEntry fileEntry) {
        serverFiles.add(fileEntry);
    }

    public void deleteFile(SyncToken repo, FileEntry fileEntry) {
        serverFiles.remove(fileEntry);
        history.get(repo);
    }

    @Override
    protected HashMap<SyncToken, FileEntry> getServerRepositoryHistory() {
        return history;
    }

    @Override
    protected List<FileEntry> getServerRepositoryFiles() {
        return serverFiles;
    }
}
