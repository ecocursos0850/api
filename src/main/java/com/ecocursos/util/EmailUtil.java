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

    public String sendEmailByApi(String assunto ,String mensagem, String destinatario) {
        try {
            // Construa a URL do endpoint
            URL url = new URL("https://srv448021.hstgr.cloud/enviar_email/email/send");

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
            String jsonInputString = "{\"assunto\": \"" + assunto + "\", \"mensagem\": \"" + mensagem + "\", \"destinatario\": \"" + destinatario + "\"}";

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