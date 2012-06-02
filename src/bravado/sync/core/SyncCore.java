package bravado.sync.core;

import com.twmacinta.util.MD5;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import org.hamcrest.*;

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

    protected HashMap<Repository, FileEntry> getServerRepositoryHistory() {
        throw new NotImplementedException();
    }

    protected List<FileEntry> getServerRepositoryFiles() {
        throw new NotImplementedException();
    }

    public List<SyncOperation> sync(Repository repository, List<FileEntry> repositoryFiles) {

        List<SyncOperation> operations = new ArrayList<SyncOperation>();
        List<FileEntry> repositoryFilesAndServerJoin = select(getServerRepositoryFiles(), org.hamcrest.Matchers.isIn(repositoryFiles));

        // TODO filter only files found on server and repository (join ?)
        for (FileEntry fileEntry : repositoryFilesAndServerJoin) {
            operations.add(new SyncOperation(fileEntry, canRepositoryUpdateFile(repository, fileEntry) ? SyncOperation.PUT : SyncOperation.GET));
        }

        return operations;
    }

    private boolean canRepositoryUpdateFile(Repository repository, FileEntry fileEntry) {
        FileEntry historyFileEntry = getServerRepositoryHistory().get(repository);

        if (historyFileEntry == null) {
            return false; // TODO check conflict ?
        }

        return getServerRepositoryFiles().indexOf(historyFileEntry) > 0;
    }

}
