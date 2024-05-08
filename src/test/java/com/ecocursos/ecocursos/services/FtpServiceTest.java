package com.ecocursos.ecocursos.services;

import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FtpServiceTest {

    @Autowired
    private FtpService ftpService;

    @Test
    public void testarConexao() {
        System.out.println(ftpService.loginFtp());
    }

    @SneakyThrows
    @Test
    public void testListaDeDiretorios() {
        for(FTPFile file : ftpService.loginFtp().listFiles("arquivos")) {
            System.out.println(file.isFile());
        }
    }

}