package bravado.sync.core;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.select;

/**
 * SyncCore
 * bob
 * 6/2/12 1:24 PM
 */
public class SyncCore {

    public List<FileEntry> getHistoryFor(SyncToken syncToken) {
        HashMap<SyncToken, List<FileEntry>> serverHistory = getServerRepositoryHistory();

        List<FileEntry> userHistory = serverHistory.get(syncToken);

        if (userHistory == null) {
            userHistory = new ArrayList<FileEntry>();
            serverHistory.put(syncToken, userHistory);
        }

        return userHistory;
    }

    protected HashMap<SyncToken, List<FileEntry>> getServerRepositoryHistory() {
        throw new NotImplementedException();
    }

    protected List<FileEntry> getServerRepositoryFiles() {
        throw new NotImplementedException();
    }

    public List<SyncOperation> sync(SyncToken syncToken, List<FileEntry> userFiles) {

        List<SyncOperation> operations = new ArrayList<SyncOperation>();

        List<FileEntry> serverFiles = getServerRepositoryFiles();
        List<FileEntry> userHistory = getHistoryFor(syncToken);

        // Files that were deleted on the client
        List<FileEntry> clientDeletedFiles = select(userHistory, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(userFiles)));
        for (FileEntry fileEntry : clientDeletedFiles) {
            if (!userFiles.contains(fileEntry)) {
                operations.add(new SyncOperation(fileEntry, SyncOperation.DELETE));
            }
        }

        // New files on server
        List<FileEntry> serverNewFiles = select(serverFiles, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(userFiles)));
        for (FileEntry fileEntry : serverNewFiles) {
            operations.add(new SyncOperation(fileEntry, SyncOperation.GET));

        }

        // New files on the client
        List<FileEntry> clientNewFiles = select(userFiles, org.hamcrest.Matchers.not(org.hamcrest.Matchers.isIn(serverFiles)));
        for (FileEntry fileEntry : clientNewFiles) {
            operations.add(new SyncOperation(fileEntry, SyncOperation.PUT));
        }
/*
        // New or conflicting files from client
        for (FileEntry fileEntry : userFiles) {

            // server does not have file (or it has been deleted)
            if (!serverFiles.contains(fileEntry)) {

            }


            String hashServer = files.get(k);
            if (hashServer == null) { // arquivo ainda não existe no server ou foi excluído
                if (historico.get(k) == null) { // nunca baixou esse arquivo do servidor
                    operations.put(k, "put");
                } else {
                    // tinha baixado o arquivo, mas ele não existe mais
                    operations.put(k, "delete-local");
                }
            } else { // servidor já tem o arquivo

                if (hashServer.equals(currentUserFiles.get(k))) {
                    // arquivos são iguais, não fazer nada
                    // Caso extremo - usuário não baixou a última versão, mas tem a versão atual localmente
                    //  (Precisaria marcar que o hash historico igual ao que o usuário enviou)

                } else {
                    // arquivo do server e enviado pelo client são diferentes!
                    String hashAntigo = historico.get(k);

                    if (hashAntigo.equals(hashServer)) { // usuário tinha a última versão do arquivo
                        operations.put(k, "put");
                    } else {
                        // o hashAntigo é diferente do que tinha no server

                        // se o client enviou o mesmo arquivo de antes
                        if (hashAntigo.equals(currentUserFiles.get(k))) {
                            operations.put(k, "get");
                        }
                    }
                }
            }

        }

        // TODO filter only files found on server and syncToken (join ?)
        for (FileEntry fileEntry : repositoryFilesAndServerJoin) {
            operations.add(new SyncOperation(fileEntry, canRepositoryUpdateFile(syncToken, fileEntry) ? SyncOperation.PUT : SyncOperation.GET));
        }
*/
        return operations;
    }
/*
    private boolean canRepositoryUpdateFile(SyncToken syncToken, FileEntry fileEntry) {
        FileEntry historyFileEntry = getServerRepositoryHistory().get(syncToken);

        if (historyFileEntry == null) {
            return false; // TODO check conflict ?
        }

        return getServerRepositoryFiles().indexOf(historyFileEntry) > 0;
    }
*/
}
