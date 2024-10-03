package com.ecocursos.ecocursos.services;

import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class RelatorioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public byte[] gerarPdfRelatorio(Map<String, Object> params, String jasperPath) {
        try(Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            String caminhoJasper = "src/main/resources/relatorios/matricula.jrxml";
            JasperReport jasperReport =  JasperCompileManager.compileReport(caminhoJasper);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, params, connection);
            connection.close();    
            return JasperExportManager.exportReportToPdf(print);

        } catch (Exception e) {
            throw new ErrorException("Erro ao gerar relatorio");
        }
    } 

}
