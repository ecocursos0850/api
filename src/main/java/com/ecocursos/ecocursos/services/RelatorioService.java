package com.ecocursos.ecocursos.services;

import java.sql.Connection;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
@Slf4j
public class RelatorioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public byte[] gerarPdfRelatorio(Map<String, Object> params, String jasperPath) {
        log.info("Gerando relatório: {}", jasperPath);
        log.info("Parâmetros: {}", params);
        
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String caminhoJasper = jasperPath;
            JasperReport jasperReport = JasperCompileManager.compileReport(caminhoJasper);

            // Configuração de locale para português
            params.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
            
            // Adiciona parâmetros padrão
            params.put("LOGO_PATH", "src/main/resources/static/logo.png"); // Ajuste o caminho do logo
            
            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, connection);
            byte[] pdf = JasperExportManager.exportReportToPdf(print);
            
            log.info("Relatório gerado com sucesso. Tamanho: {} bytes", pdf.length);
            return pdf;

        } catch (Exception e) {
            log.error("Erro ao gerar relatório: {}", e.getMessage(), e);
            throw new ErrorException("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}