package bravado.sync.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * SyncCoreTest
 * bob
 * 6/2/12 1:39 PM
 */

public class SyncCoreTest {

    @Test
    public void testReposirotyGetAllFiles() {

        Assert.assertEquals(1, 1);

    }



//    public void testScript() {
//
//        // repo1 inicia vazio
//        SyncCore conn1 = new SyncCore("repo1", "user1", "pass1");
//        SyncCore conn2 = new SyncCore("repo1", "user2", "pass2");
//
//        List<String> user1_files = new ArrayList<String>();
//
//        user1_files.add("file1.txt:md5file1user1");
//        user1_files.add("file2.txt:md5file2user1");
//        List commands = conn1.sync(user1_files);
//
//        // commands deve retornar put e put
//        conn1.put("file1.txt");
//        conn1.put("file2.txt");
//
//        // server agora tem os arquivos file1.txt, file2.txt
//        // server sabe que user1 tem os arquivos file1.txt e file2.txt com hashes correspondentes
//
//        List<String> user2_files = new ArrayList<String>();
//
//        user2_files.add("file3.txt:md5file3user2");
//        user2_files.add("file4.txt:md5file4user2");
//        commands = conn2.sync(user2_files);
//
//        // commands deve retornar comandos para enviar file3, file4 e baixar file1, file2
//        conn2.get("file1.txt");
//        conn2.get("file2.txt");
//
//        conn2.put("file3.txt");
//        conn2.put("file4.txt"); // cada vez que adiciona um arquivo, marcar na lista de arquivos do user
//
//
//        // usuario1 atualiza arquivo
//        user1_files.remove("file1.txt:md5file1user1");
//        user1_files.add("file1.txt:md5file1v1user1");
//
//        conn1.sync(user1_files);
//
//        // deve fazer upload de file1 novamente
//        conn1.put("file1.txt");
//
//        // usuario2 faz sync sem adicionar nada
//
//        conn2.sync(user2_files);
//
//        // usuario2 deve baixar o file1 atualizado
//        conn2.get("file1.txt");
//
//        // agora usuario1 e usuario2 tem os mesmos arquivos
//
//        // TODO talvez saber quando cada usuário fez o sync, pra ter noção de quem está desatualizado
//
//
//        // teste para conflitos
//        user1_files.add("file5.txt:md5file5user1");
//
//        // antes do user1 fazer sync, user2 cria o mesmo arquivo
//
//        user2_files.add("file5.txt:md5file5user2");
//
//        conn1.sync(user1_files);
//
//        // user1 deve enviar file5.txt
//        conn1.put("file5.txt");
//
//        // user2 faz sync
//        conn2.sync(user2_files);
//
//        // Erro - conflito em file5.txt !
//
//    }
}
