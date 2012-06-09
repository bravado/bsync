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

    private static String SERVER_REPOSITORY = "server";

    public List<FileEntry> serverFiles = new ArrayList<FileEntry>();
    // user, FileEntry
    public HashMap<SyncToken, List<FileEntry>> history = new HashMap<SyncToken, List<FileEntry>>();

    public void addFile(SyncToken syncToken, FileEntry fileEntry) {
        serverFiles.add(fileEntry);
        getServerRepositoryHistory(syncToken).add(fileEntry);
    }

    public void addFile(FileEntry fileEntry) {
        serverFiles.add(fileEntry);
    }

    @Override
    protected List<FileEntry> getServerRepositoryHistory(SyncToken syncToken) {
        List<FileEntry> listFileEntry = history.get(syncToken);
        if (listFileEntry == null) {
            listFileEntry = new ArrayList<FileEntry>();
            history.put(syncToken, listFileEntry);
        }
        return listFileEntry;
    }

    @Override
    protected List<FileEntry> getServerRepositoryFiles() {
        return serverFiles;
    }
}
