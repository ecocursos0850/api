package com.ecocursos.util;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import org.apache.commons.compress.utils.FileNameUtils;

import com.ctc.wstx.shaded.msv_core.util.Uri;
import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FtpClient {
    
    public static void sendFile(File file) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("root", "195.35.40.252", 22);
            var config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword("F@rinha0meupirao1");
            session.connect();
            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            File a = new File("/home/erick/projects/api/src/main/resources/text.txt");
            channel.put("src/main/resources/text.txt", "root/home/rdpuser/projects/ssl_teste/ " + "test.txt");
            channel.exit();
        } catch (Exception e) {
            throw new ErrorException("Erro ao logar no FTP");
        }
    }

}
