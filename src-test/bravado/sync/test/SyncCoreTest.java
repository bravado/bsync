package bravado.sync.test;

import bravado.sync.core.FileEntry;
import bravado.sync.core.SyncToken;
import bravado.sync.core.SyncOperation;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.*;

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

    public void testRepo1PutAndGetFile2() {

        MockSyncToken repo1 = new MockSyncToken("repo1", "~/");
        MockSyncToken repo2 = new MockSyncToken("repo2", "~/");

        MockServer server = new MockServer();
        server.addFile(repo2, new FileEntry("file3.txt", "8343"));

        // repo1 add file
        repo1.addFile(new FileEntry("file2.txt", "2343"));

        List<SyncOperation> operationsRepo1 = server.sync(repo1, repo1.repositoryFiles);

        List<SyncOperation> operationsExpected = new ArrayList<SyncOperation>();
        operationsExpected.add(new SyncOperation(new FileEntry("file3.txt", "8343"), GET));
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), PUT));

        Assert.assertTrue(operationsRepo1.containsAll(operationsExpected));
    }

    public void testRepo1PutDeleteAndPut() {

        MockSyncToken repo1 = new MockSyncToken("repo1", "~/");

        MockServer server = new MockServer();

        // repo1 add file
        repo1.addFile(new FileEntry("file2.txt", "2343"));

        List<SyncOperation> operationsRepo1 = server.sync(repo1, repo1.repositoryFiles);

        List<SyncOperation> operationsExpected = new ArrayList<SyncOperation>();
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), PUT));

        Assert.assertTrue(operationsRepo1.containsAll(operationsExpected));

        repo1.repositoryFiles.clear();

        operationsRepo1 = server.sync(repo1, repo1.repositoryFiles);

        operationsExpected = new ArrayList<SyncOperation>();
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), PUT));

    }

    public void testRepo1Delete() {

        MockSyncToken repo1 = new MockSyncToken("repo1", "~/");
        MockSyncToken repo2 = new MockSyncToken("repo2", "~/");

        MockServer server = new MockServer();

        // repo1 add file
        repo1.addFile(new FileEntry("file2.txt", "2343"));

        List<SyncOperation> operations = server.sync(repo1, repo1.repositoryFiles);

        List<SyncOperation> operationsExpected = new ArrayList<SyncOperation>();
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), PUT));

        Assert.assertTrue(operations.containsAll(operationsExpected));

        operations = server.sync(repo2, repo2.repositoryFiles);
        operationsExpected.clear();
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), GET));

        Assert.assertTrue(operations.containsAll(operationsExpected));

        repo1.repositoryFiles.clear();
        operations = server.sync(repo1, repo1.repositoryFiles);

        operations = server.sync(repo2, repo1.repositoryFiles);

        operationsExpected.clear();
        operationsExpected.add(new SyncOperation(new FileEntry("file2.txt", "2343"), DELETE));

        Assert.assertTrue(operations.containsAll(operationsExpected));

    }
    
    @Test
    public void testNotIn() {
        
        List<FileEntry> list1 = new ArrayList<FileEntry>();
        List<FileEntry> list2 = new ArrayList<FileEntry>();
        
        list1.add(new FileEntry("lalala", "123"));
        list1.add(new FileEntry("lalala2", "123"));

        list2.add(new FileEntry("lalala", "123"));
        list2.add(new FileEntry("lalala3", "123"));

        System.out.println("inner join");
        System.out.println(select(list1, org.hamcrest.Matchers.isIn(list2)));
        System.out.println("left join");
        System.out.println(select(list1, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(list2))));
        System.out.println("right join");
        System.out.println(select(list2, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(list1))));

    }

}
