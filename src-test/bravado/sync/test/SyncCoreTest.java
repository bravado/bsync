package bravado.sync.test;

import bravado.sync.core.FileEntry;
import bravado.sync.core.SyncToken;
import bravado.sync.core.SyncOperation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static bravado.sync.core.SyncOperation.*;

/**
 * SyncCoreTest
 * bob
 * 6/2/12 1:39 PM
 */

public class SyncCoreTest {

    @Test
    public void testRepo1PutAllFiles() {

        MockSyncToken repo1 = new MockSyncToken("repo1", "~/");
        repo1.addFile(new FileEntry("file2.txt", "2343"));

        MockServer server = new MockServer();
        List<SyncOperation> operations = server.sync(repo1, repo1.repositoryFiles);

        Assert.assertFalse(operations.size() == 0);
        Assert.assertEquals(operations.get(0).getOperation(), PUT);
    }

}
