package bravado.sync.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: guigouz
 * Date: 02/06/12
 * Time: 16:25
 * To change this template use File | Settings | File Templates.
 */
public class SyncMock1 {

    public static void main(String args[]) {
        SyncRepo repo = new SyncRepo("/home/projects/project1");

        HashMap<String,String> arquivosgui = new HashMap<String, String>();
        HashMap<String,String> arquivosbob = new HashMap<String, String>();
        HashMap<String, String> operations;

        try {
            arquivosbob.put("file3", "v2");
            System.out.println("Arquivos bob local " + arquivosbob);

            // bob envia file3, v2
            operations = repo.sync("bob", arquivosbob);
            System.out.println(operations);
            repo.exec("bob", operations, arquivosbob);
            System.out.print(repo.listFiles());

            arquivosgui.put("file1", "v1");
            arquivosgui.put("file2", "v1");

            System.out.println("Arquivos gui local " + arquivosgui);
            operations = repo.sync("gui", arquivosgui);
            System.out.println(operations);
            repo.exec("gui", operations, arquivosgui);
            System.out.print(repo.listFiles());

            operations = repo.sync("bob", arquivosbob);
            System.out.println(operations);
            repo.exec("bob", operations, arquivosbob);
            System.out.print(repo.listFiles());


            System.out.println("Arquivos bob local " + arquivosbob);
            arquivosgui.put("file1", "v2");
            System.out.println("Arquivos gui local " + arquivosgui);
            operations = repo.sync("gui", arquivosgui);
            System.out.println(operations);
            repo.exec("gui", operations, arquivosgui);
            System.out.print(repo.listFiles());

            arquivosbob.remove("file3");
            System.out.println("Arquivos bob local " + arquivosbob);
            operations = repo.sync("bob", arquivosbob);
            System.out.println(operations);
            repo.exec("bob", operations, arquivosbob);
            System.out.print(repo.listFiles());

//            arquivosbob.put("file3", "v1");
//            arquivosbob.put("file4", "v2");
        }
        catch(Exception ex) {
            System.out.println("exception");
        }
//        try {
//
//            System.out.println("sync gui");
//            System.out.println(repo.sync("gui", arquivosgui).toString());
//
//            System.out.println("sync bob");
//            System.out.println(repo.sync("bob", arquivosbob).toString());
//
//            arquivosgui.put("file3", "v2");
//
//            System.out.println(repo.sync("gui", arquivosgui).toString());
//
//            System.out.println("sync bob");
//            System.out.println(repo.sync("bob", arquivosbob).toString());
//
//        }
//        catch(Exception ex) {
//            System.out.println(ex.getMessage());
//
//            System.out.print(repo.listFiles());
//        }

    }


    private static class SyncRepo {
        private String root;
        private HashMap<String,String> files;

        // cache de arquivos dos usuários
        private HashMap<String, HashMap<String,String>> userFiles = new HashMap<String, HashMap<String, String>>();

        private SyncRepo(String root) {
            this.root = root;

            // TODO carregar arquivos do server em files;
            this.files = new HashMap<String,String>();

        }

        public void exec(String user, HashMap<String, String> operations, HashMap<String, String> filelist) throws Exception {
            for(String k: operations.keySet()) {
                String cmd = operations.get(k);

                if(cmd.equals("get")) {
                    this.get(user, k);
                }
                else if(cmd.equals("put")) {
                    this.put(user, k, filelist.get(k));
                }
                else if(cmd.equals("delete-server")) {
                    files.remove(k);
                    getUserFiles(user).remove(k);
                }
                else if(cmd.equals("delete-local")) {
                    filelist.remove(k);
                }
            }
        }


        private HashMap<String,String> getUserFiles(String user) {
            HashMap<String,String> currentUserFiles = userFiles.get(user);

            if(currentUserFiles == null) {
                HashMap<String, String> u = new HashMap<String, String>();
                userFiles.put(user, u);
                return u;
            }
            else {
                return currentUserFiles;
            }
        }

        private String get(String user, String file) throws Exception {
            String hash = files.get(file);

            if(hash == null) {
                throw new Exception("File not found!");
            }

            // marca que usuário baixou o arquivo
            getUserFiles(user).put(file, hash);

            return hash;
        }

        private void put(String user, String file, String hash) throws Exception {
            if(canPut(user, file)) {
                // atualiza lista do server
                files.put(file, hash);
                // atualiza lista deste user
                getUserFiles(user).put(file, hash);
            }
            else {
                throw new Exception("Conflict");
            }
        }

        private boolean canPut(String user, String file) {
            String serverFile = files.get(file);

            if(serverFile == null) {
                // server não tem o arquivo, usuário pode enviar
                return true;
            }

            String lastUserFile = getUserFiles(user).get(file);

            return (lastUserFile.equals(serverFile));

        }

        public HashMap<String, String> sync(String user, HashMap<String, String> currentUserFiles) throws Exception {

            ArrayList<String> ret = new ArrayList<String>();

            HashMap<String, String> operations = new HashMap<String, String>();
            HashMap<String, String> historico = getUserFiles(user);

            // arquivos que foram excluídos do client
            for(String k: historico.keySet()) {
                if(currentUserFiles.get(k) == null) {
                    operations.put(k, "delete-server");
                }
            }

            // arquivos novos do server devem ser baixados
            for(String k: files.keySet()) {
                if(currentUserFiles.get(k) == null) {
                    operations.put(k, "get");
                }
            }

            // arquivos novos/conflitantes que foram enviados
            for(String k: currentUserFiles.keySet()) {

                String hashServer = files.get(k);
                if(hashServer == null) { // arquivo ainda não existe no server ou foi excluído
                    if(historico.get(k) == null) { // nunca baixou esse arquivo do servidor
                        operations.put(k, "put");
                    }
                    else {
                        // tinha baixado o arquivo, mas ele não existe mais
                        operations.put(k, "delete-local");
                    }
                }
                else { // servidor já tem o arquivo

                    if(hashServer.equals(currentUserFiles.get(k))) {
                        // arquivos são iguais, não fazer nada
                        // Caso extremo - usuário não baixou a última versão, mas tem a versão atual localmente
                        //  (Precisaria marcar que o hash historico igual ao que o usuário enviou)

                    }
                    else {
                        // arquivo do server e enviado pelo client são diferentes!
                        String hashAntigo = historico.get(k);

                        if(hashAntigo.equals(hashServer)) { // usuário tinha a última versão do arquivo
                            operations.put(k, "put");
                        }
                        else {
                            // o hashAntigo é diferente do que tinha no server

                            // se o client enviou o mesmo arquivo de antes
                            if(hashAntigo.equals(currentUserFiles.get(k))) {
                                operations.put(k, "get");
                            }
                        }
                    }
                }

            }



            return operations;

        }

        public String listFiles() {
            StringBuffer b = new StringBuffer();

            b.append("Server files\n");
            for(String key: files.keySet()) {
                b.append(key + files.get(key) + "\n");
            }

            for(String key: userFiles.keySet()) {

                b.append(key + " files\n");
                HashMap<String,String> uf = userFiles.get(key);
                for(String filename: uf.keySet()) {
                    b.append(filename + uf.get(filename) + "\n");
                }
            }

            return b.toString();



        }

    }



}
