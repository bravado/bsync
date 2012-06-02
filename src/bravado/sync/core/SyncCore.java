package bravado.sync.core;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/**
 * SyncCore
 * bob
 * 6/2/12 1:24 PM
 */
public class SyncCore {

    protected HashMap<SyncToken, FileEntry> getServerRepositoryHistory() {
        throw new NotImplementedException();
    }

    protected List<FileEntry> getServerRepositoryFiles() {
        throw new NotImplementedException();
    }

    public List<SyncOperation> sync(SyncToken syncToken, List<FileEntry> repositoryFiles) {

        List<SyncOperation> operations = new ArrayList<SyncOperation>();
        List<FileEntry> repositoryFilesAndServerJoin = select(getServerRepositoryFiles(), org.hamcrest.Matchers.isIn(repositoryFiles));



        // TODO filter only files found on server and syncToken (join ?)
        for (FileEntry fileEntry : repositoryFilesAndServerJoin) {
            operations.add(new SyncOperation(fileEntry, canRepositoryUpdateFile(syncToken, fileEntry) ? SyncOperation.PUT : SyncOperation.GET));
        }

        return operations;
    }

    private boolean canRepositoryUpdateFile(SyncToken syncToken, FileEntry fileEntry) {
        FileEntry historyFileEntry = getServerRepositoryHistory().get(syncToken);

        if (historyFileEntry == null) {
            return false; // TODO check conflict ?
        }

        return getServerRepositoryFiles().indexOf(historyFileEntry) > 0;
    }

}
