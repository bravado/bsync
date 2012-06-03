package bravado.sync.core;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.*;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;

/**
 * SyncCore
 * bob
 * 6/2/12 1:24 PM
 */
public class SyncCore {

    protected List<FileEntry> getServerRepositoryHistory(SyncToken syncToken) {
        throw new NotImplementedException();
    }

    protected List<FileEntry> getServerRepositoryFiles() {
        throw new NotImplementedException();
    }

    protected void doServerOperation(SyncOperation operation) {
        throw new NotImplementedException();
    }
    
    
    public List<SyncOperation> sync(SyncToken syncToken, List<FileEntry> syncTokenFileEntries) {

        List<SyncOperation> operations = new ArrayList<SyncOperation>();

        List<FileEntry> historyFileEntries = getServerRepositoryHistory(syncToken);
        List<FileEntry> serverFileEntries = getServerRepositoryFiles();

        List<FileEntry> put = select(syncTokenFileEntries, not(isIn(historyFileEntries)));
        
        List<FileEntry> deletedButUpdatedOnServer = select(select(
                historyFileEntries,
                having(on(FileEntry.class).getFilename(),
                        not(isIn(extract(syncTokenFileEntries,
                                on(FileEntry.class).getFilename()))))), not(isIn(serverFileEntries)));

        List<FileEntry> deletedOnClient = select(select(
                historyFileEntries,
                having(on(FileEntry.class).getFilename(),
                        not(isIn(extract(syncTokenFileEntries,
                                on(FileEntry.class).getFilename()))))), having(on(FileEntry.class).getFilename(),
                not(isIn(extract(deletedButUpdatedOnServer,
                        on(FileEntry.class).getFilename())))));

        List<FileEntry> get = select(serverFileEntries, not(isIn(historyFileEntries)));

        List<FileEntry> conflict = select(
                get,
                having(on(FileEntry.class).getFilename(),
                        isIn(extract(put,
                                on(FileEntry.class).getFilename()))));

        List<FileEntry> get2 = select(select(serverFileEntries, not(isIn(historyFileEntries))),
                having(on(FileEntry.class).getFilename(),
                        not(isIn(extract(deletedOnClient, on(FileEntry.class).getFilename())))));


        List<FileEntry> get3 = select(get2, having(on(FileEntry.class).getFilename(),
                not(isIn(extract(conflict, on(FileEntry.class).getFilename())))));

        List<FileEntry> delete_on_client = select(select(
                historyFileEntries,
                        having(on(FileEntry.class).getFilename(),
                                not(isIn(extract(serverFileEntries,
                                        on(FileEntry.class).getFilename()))))),
                        having(on(FileEntry.class).getFilename(),
                                not(isIn(extract(put,
                                        on(FileEntry.class).getFilename())))));

        for(FileEntry fileEntry: put) {
            operations.add(new SyncOperation(fileEntry, SyncOperation.PUT));
        }
        
        for(FileEntry fileEntry: get3) {
            operations.add(new SyncOperation(fileEntry, SyncOperation.GET));
        }
        
        for(FileEntry fileEntry: delete_on_client) {
            operations.add(new SyncOperation(fileEntry, SyncOperation.DELETE));
        }
        
        for(FileEntry fileEntry: deletedOnClient) {
            doServerOperation(new SyncOperation(fileEntry, SyncOperation.DELETE));
        }

        return operations;
    }

}
