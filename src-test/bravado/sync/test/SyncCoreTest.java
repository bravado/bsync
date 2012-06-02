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
    public void testAlgoritmo() {

        List<FileEntry> historico = new ArrayList<FileEntry>();
        List<FileEntry> user = new ArrayList<FileEntry>();
        List<FileEntry> server = new ArrayList<FileEntry>();

        historico.add(new FileEntry("lalala", "123"));
        historico.add(new FileEntry("lalala2", "123"));
        historico.add(new FileEntry("lalala3", "123"));
        historico.add(new FileEntry("lalala4", "123"));
        System.out.println("Historico \n" + historico);

        user.add(new FileEntry("lalala", "123"));
        user.add(new FileEntry("lalala3", "123"));
        user.add(new FileEntry("lalala4", "124"));
        user.add(new FileEntry("lalala6", "123"));
        System.out.println("Client \n" + user);

        server.add(new FileEntry("lalala", "123"));
        server.add(new FileEntry("lalala2", "123"));
        server.add(new FileEntry("lalala3", "123"));
        server.add(new FileEntry("lalala4", "123"));
        server.add(new FileEntry("lalala5", "321"));
        System.out.println("Server \n" + server);

        System.out.println("\n\ndeleted on client");
        System.out.println(select(
                historico,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(extract(user,
                                on(FileEntry.class).getFilename()))))));

        System.out.println("\nupdated/created on client");
        System.out.println(select(user, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(historico))));

        System.out.println("\nnew on client");
        System.out.println(select(
                user,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(extract(server,
                                on(FileEntry.class).getFilename()))))));

        System.out.println("\nnew on server");
        System.out.println(select(
                server,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(extract(user,
                                on(FileEntry.class).getFilename()))))));
    }

    @Test
    public void testNotIn() {

        List<FileEntry> list1 = new ArrayList<FileEntry>();
        List<FileEntry> list2 = new ArrayList<FileEntry>();

        list1.add(new FileEntry("lalala", "123"));
        list1.add(new FileEntry("lalala2", "123"));
        list1.add(new FileEntry("lalala3", "123"));
        list1.add(new FileEntry("lalala4", "123"));

        list2.add(new FileEntry("lalala", "123"));
        list2.add(new FileEntry("lalala3", "123"));
        list2.add(new FileEntry("lalala4", "124"));


        System.out.println("inner join");
        System.out.println(select(
                list1,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.isIn(extract(list2,
                                on(FileEntry.class).getFilename())))));

        System.out.println("left join");
        System.out.println(select(
                list1,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(extract(list2,
                                on(FileEntry.class).getFilename()))))));

        System.out.println("right join");
        System.out.println(select(
                list2,
                having(on(FileEntry.class).getFilename(),
                        org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(extract(list1,
                                on(FileEntry.class).getFilename()))))));


    }

}
