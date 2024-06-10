package com.ecocursos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class EmailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sender(String assunto ,String mensagem, String destinatario, String enviadoPor) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(enviadoPor);
        message.setTo(destinatario);
        message.setSubject(assunto);
        message.setText(mensagem);
        javaMailSender.send(message);
    }

    public String resetPassword(String assunto, String novaSenha, String nome, String destinatario) {
        try {
            // Construa a URL do endpoint
            URL url = new URL("https://srv448021.hstgr.cloud/php_api/email/resetPassword");

            // Abra uma conexão HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Defina o método HTTP como POST
            conn.setRequestMethod("POST");
            
            // Defina os cabeçalhos HTTP
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Habilitar envio de dados
            conn.setDoOutput(true);

            // Construa o corpo da requisição em JSON
            String jsonInputString = "{\"assunto\": \"" + assunto + "\", \"senha\": \"" + novaSenha + "\", \"nome\": \"" + nome + "\", \"destinatario\": \"" + destinatario + "\"}";

            // Obtenha o OutputStream da conexão para escrever os dados da requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leia a resposta do servidor
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

            // Encerre a conexão
            conn.disconnect();
            return "Email enviado com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }

    public String happyBirthday(String assunto, String presente, String nome, String destinatario) {
        try {
            // Construa a URL do endpoint
            URL url = new URL("https://srv448021.hstgr.cloud/php_api/email/happyBirthday");

            // Abra uma conexão HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Defina o método HTTP como POST
            conn.setRequestMethod("POST");
            
            // Defina os cabeçalhos HTTP
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Habilitar envio de dados
            conn.setDoOutput(true);

            // Construa o corpo da requisição em JSON
            String jsonInputString = "{\"assunto\": \"" + assunto + "\", \"presente\": \"" + presente + "\", \"nome\": \"" + nome + "\", \"destinatario\": \"" + destinatario + "\"}";

            // Obtenha o OutputStream da conexão para escrever os dados da requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leia a resposta do servidor
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

            // Encerre a conexão
            conn.disconnect();
            return "Email enviado com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }

    public String requestPostGraduate(String assunto, String login, String senha, String destinatario) {
        try {
            // Construa a URL do endpoint
            URL url = new URL("https://srv448021.hstgr.cloud/php_api/email/requestPostGraduate");

            // Abra uma conexão HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Defina o método HTTP como POST
            conn.setRequestMethod("POST");
            
            // Defina os cabeçalhos HTTP
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Habilitar envio de dados
            conn.setDoOutput(true);

            // Construa o corpo da requisição em JSON
            String jsonInputString = "{\"assunto\": \"" + assunto + "\", \"login\": \"" + login + "\", \"senha\": \"" + senha + "\", \"destinatario\": \"" + destinatario + "\"}";

            // Obtenha o OutputStream da conexão para escrever os dados da requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leia a resposta do servidor
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

            // Encerre a conexão
            conn.disconnect();
            return "Email enviado com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }

    public String requestPostRegister(String assunto, String nome, String destinatario) {
        try {
            // Construa a URL do endpoint
            URL url = new URL("https://srv448021.hstgr.cloud/php_api/email/requestPostRegister");

            // Abra uma conexão HttpURLConnection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Defina o método HTTP como POST
            conn.setRequestMethod("POST");
            
            // Defina os cabeçalhos HTTP
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // Habilitar envio de dados
            conn.setDoOutput(true);

            // Construa o corpo da requisição em JSON
            String jsonInputString = "{\"assunto\": \"" + assunto + "\", \"nome\": \"" + nome + "\", \"destinatario\": \"" + destinatario + "\"}";

            // Obtenha o OutputStream da conexão para escrever os dados da requisição
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Leia a resposta do servidor
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }

            // Encerre a conexão
            conn.disconnect();
            return "Email enviado com sucesso.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }
    }
}