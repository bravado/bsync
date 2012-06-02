package bravado.sync.test;

import bravado.sync.core.FileEntry;
import bravado.sync.core.SyncToken;

import java.util.ArrayList;
import java.util.List;

/**
 * MockSyncToken
 * bob
 * 6/2/12 5:10 PM
 */
public class MockSyncToken extends SyncToken {
    public List<FileEntry> repositoryFiles = new ArrayList<FileEntry>();

    public MockSyncToken(String location, String owner) {
        super(location, owner);
    }

    public void addFile(FileEntry fileEntry) {
        repositoryFiles.add(fileEntry);
    }


}
