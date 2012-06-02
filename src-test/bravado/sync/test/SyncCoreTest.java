package bravado.sync.test;

import bravado.sync.core.FileEntry;
import bravado.sync.core.Repository;
import bravado.sync.core.SyncOperation;
import org.junit.Assert;
import org.junit.Test;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SyncCoreTest
 * bob
 * 6/2/12 1:39 PM
 */

public class SyncCoreTest {

    @Test
    public void testReposirotyGetAllFiles() {
        Repository repo1 = new MockRepository();

        MockServer server = new MockServer();
        server.addFile(new FileEntry("file1.txt", "1234"));
        server.addFile(new FileEntry("src/file1.txt", "1256"));

        List<SyncOperation> operations = server.sync(repo1, new ArrayList<FileEntry>());

        System.out.println(operations);
        
        Assert.assertTrue(operations.contains(new SyncOperation(new FileEntry("file1.txt", "1234"), SyncOperation.PUT)));
        Assert.assertTrue(operations.contains(new SyncOperation(new FileEntry("src/file1.txt", "1256"), SyncOperation.PUT)));
    }

}
