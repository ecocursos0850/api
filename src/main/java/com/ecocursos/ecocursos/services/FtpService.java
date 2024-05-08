package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.AnexoMaterialCurso;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Arrays;

@Service
public class FtpService {


    @SneakyThrows
    public FTPClient loginFtp() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("codetide.com.br", 21);
        ftpClient.login("codet5185", "3w8.73IxOKcd#F");
        return ftpClient;
    }

    @SneakyThrows
    public void subirArquivo(String path ,MultipartFile file) {
        FTPClient ftpClient = loginFtp();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        System.out.println(file.getName());
        ftpClient.storeFile(path, file.getInputStream());
    }

}
