package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.models.dtos.ImportacaoResponse;
import com.ecocursos.ecocursos.repositories.CpfParceiroRepository;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CpfParceiroService {

    @Autowired
    private CpfParceiroRepository repository;

    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private EntityManager em;

    private static final Pattern CPF_PATTERN = Pattern.compile("[^0-9]");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @SneakyThrows
    public ImportacaoResponse salvar(MultipartFile multipartFile, Integer idParceiro) {
        ImportacaoResponse response = new ImportacaoResponse();
        
        try {
            String fileName = multipartFile.getOriginalFilename().toLowerCase();
            
            if (fileName.endsWith(".csv")) {
                return processarCSV(multipartFile, idParceiro);
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                return processarExcel(multipartFile, idParceiro);
            } else {
                response.setSucesso(false);
                response.setMensagem("Formato de arquivo não suportado. Use CSV ou Excel.");
                return response;
            }
        } catch (Exception e) {
            response.setSucesso(false);
            response.setMensagem("Erro durante o processamento do arquivo: " + e.getMessage());
            return response;
        }
    }

    @SneakyThrows
    private ImportacaoResponse processarCSV(MultipartFile multipartFile, Integer idParceiro) {
        ImportacaoResponse response = new ImportacaoResponse();
        List<CpfParceiro> cpfsValidos = new ArrayList<>();
        Parceiro parceiro = parceiroService.listarById(idParceiro);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            int linhaNumero = 0;
            
            while ((line = reader.readLine()) != null) {
                linhaNumero++;
                
                // Pular linha vazia
                if (line.trim().isEmpty()) {
                    adicionarRegistroIgnorado(response, "Linha " + linhaNumero, "Linha vazia");
                    continue;
                }
                
                // Pular cabeçalho
                if (isFirstLine) {
                    isFirstLine = false;
                    adicionarRegistroIgnorado(response, "Linha " + linhaNumero, "Cabeçalho");
                    continue;
                }
                
                ProcessamentoResultado resultado = processarLinhaCSV(line, parceiro, linhaNumero);
                processarResultadoLinha(response, resultado, cpfsValidos);
            }
        }
        
        // Salvar registros válidos
        salvarRegistrosValidos(response, cpfsValidos);
        
        // Finalizar response
        finalizarResponse(response);
        return response;
    }

    @SneakyThrows
    private ImportacaoResponse processarExcel(MultipartFile multipartFile, Integer idParceiro) {
        ImportacaoResponse response = new ImportacaoResponse();
        List<CpfParceiro> cpfsValidos = new ArrayList<>();
        Parceiro parceiro = parceiroService.listarById(idParceiro);

        Workbook workbook;
        
        if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        } else {
            workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook(multipartFile.getInputStream());
        }
        
        Sheet sheet = workbook.getSheetAt(0);
        
        Iterator<Row> rowIterator = sheet.rowIterator();
        int linhaNumero = 0;
        
        while(rowIterator.hasNext()) {
            linhaNumero++;
            Row row = rowIterator.next();
            
            // Pular cabeçalho na primeira linha
            if (linhaNumero == 1) {
                adicionarRegistroIgnorado(response, "Linha " + linhaNumero, "Cabeçalho");
                continue;
            }
            
            // Pular linhas vazias
            if (isRowEmpty(row)) {
                adicionarRegistroIgnorado(response, "Linha " + linhaNumero, "Linha vazia");
                continue;
            }

            ProcessamentoResultado resultado = processarLinhaExcel(row, parceiro, linhaNumero);
            processarResultadoLinha(response, resultado, cpfsValidos);
        }
        
        workbook.close();
        
        // Salvar registros válidos
        salvarRegistrosValidos(response, cpfsValidos);
        
        // Finalizar response
        finalizarResponse(response);
        return response;
    }

    private ProcessamentoResultado processarLinhaCSV(String linha, Parceiro parceiro, int linhaNumero) {
        ProcessamentoResultado resultado = new ProcessamentoResultado();
        resultado.linhaNumero = linhaNumero;
        
        // Dividir a linha por vírgula (formato CSV simples)
        String[] colunas = linha.split(",");
        
        if (colunas.length < 1) {
            resultado.erro = "Linha sem dados válidos";
            return resultado;
        }

        String cpf = colunas[0].trim();
        String email = colunas.length > 1 ? colunas[1].trim() : null;

        // Validar CPF
        if (cpf.isEmpty() || cpf.equalsIgnoreCase("cpf")) {
            resultado.erro = "CPF vazio ou inválido";
            return resultado;
        }

        // Limpar e formatar CPF (remover pontos e traços)
        cpf = limparCpf(cpf);
        
        // Validar se CPF tem 11 dígitos
        if (cpf.length() != 11) {
            resultado.erro = "CPF deve conter 11 dígitos após limpeza";
            return resultado;
        }

        resultado.cpf = cpf;
        resultado.emailOriginal = email;

        // Verificar se CPF já existe
        if (repository.existsByCpf(cpf)) {
            resultado.ignorar = true;
            resultado.mensagem = "CPF já existe na base de dados";
            return resultado;
        }

        // Processar email
        if (email == null || email.isEmpty() || !isEmailValido(email)) {
            // Criar email padrão: cpf@ecocursos.com.br
            resultado.email = cpf + "@ecocursos.com.br";
            resultado.emailGerado = true;
        } else {
            resultado.email = email.toLowerCase();
        }

        resultado.valido = true;
        resultado.parceiro = parceiro;
        return resultado;
    }

    private ProcessamentoResultado processarLinhaExcel(Row row, Parceiro parceiro, int linhaNumero) {
        ProcessamentoResultado resultado = new ProcessamentoResultado();
        resultado.linhaNumero = linhaNumero;
        
        Iterator<Cell> cellIterator = row.cellIterator();
        String cpf = null;
        String email = null;

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            
            switch (cell.getColumnIndex()) {
                case 0: // CPF
                    cpf = getCellValueAsString(cell);
                    break;
                case 1: // Email
                    email = getCellValueAsString(cell);
                    break;
            }
        }

        // Validar CPF
        if (cpf == null || cpf.trim().isEmpty()) {
            resultado.erro = "CPF não informado";
            return resultado;
        }

        // Limpar e formatar CPF (remover pontos e traços)
        cpf = limparCpf(cpf);
        
        // Validar se CPF tem 11 dígitos
        if (cpf.length() != 11) {
            resultado.erro = "CPF deve conter 11 dígitos após limpeza";
            return resultado;
        }

        resultado.cpf = cpf;
        resultado.emailOriginal = email;

        // Verificar se CPF já existe
        if (repository.existsByCpf(cpf)) {
            resultado.ignorar = true;
            resultado.mensagem = "CPF já existe na base de dados";
            return resultado;
        }

        // Processar email
        if (email == null || email.trim().isEmpty() || !isEmailValido(email)) {
            // Criar email padrão: cpf@ecocursos.com.br
            resultado.email = cpf + "@ecocursos.com.br";
            resultado.emailGerado = true;
        } else {
            resultado.email = email.toLowerCase();
        }

        resultado.valido = true;
        resultado.parceiro = parceiro;
        return resultado;
    }

    private void processarResultadoLinha(ImportacaoResponse response, ProcessamentoResultado resultado, List<CpfParceiro> cpfsValidos) {
        response.setTotalRegistrosProcessados(response.getTotalRegistrosProcessados() + 1);
        
        ImportacaoResponse.RegistroImportado registro = new ImportacaoResponse.RegistroImportado();
        registro.setCpf(resultado.cpf);
        
        if (resultado.erro != null) {
            // Registro com erro
            registro.setStatus("ERRO");
            registro.setMensagem(resultado.erro);
            response.getRegistrosImportados().add(registro);
            response.setRegistrosComErro(response.getRegistrosComErro() + 1);
        } else if (resultado.ignorar) {
            // Registro ignorado (duplicado)
            registro.setStatus("IGNORADO");
            registro.setMensagem(resultado.mensagem);
            response.getRegistrosImportados().add(registro);
            response.setRegistrosIgnorados(response.getRegistrosIgnorados() + 1);
        } else if (resultado.valido) {
            // Registro válido
            CpfParceiro cpfParceiro = new CpfParceiro();
            cpfParceiro.setCpf(resultado.cpf);
            cpfParceiro.setEmail(resultado.email);
            cpfParceiro.setParceiro(resultado.parceiro);
            cpfsValidos.add(cpfParceiro);
            
            registro.setEmail(resultado.email);
            registro.setStatus("SUCESSO");
            registro.setMensagem(resultado.emailGerado ? "Email gerado automaticamente" : "Registro processado com sucesso");
            response.getRegistrosImportados().add(registro);
        }
    }

    private void salvarRegistrosValidos(ImportacaoResponse response, List<CpfParceiro> cpfsValidos) {
        for (CpfParceiro cpf : cpfsValidos) {
            try {
                if (!repository.existsByCpf(cpf.getCpf())) {
                    repository.save(cpf);
                    response.setRegistrosInseridos(response.getRegistrosInseridos() + 1);
                }
            } catch (Exception e) {
                // Atualizar status do registro para erro
                for (ImportacaoResponse.RegistroImportado registro : response.getRegistrosImportados()) {
                    if (registro.getCpf().equals(cpf.getCpf()) && "SUCESSO".equals(registro.getStatus())) {
                        registro.setStatus("ERRO");
                        registro.setMensagem("Erro ao salvar no banco: " + e.getMessage());
                        response.setRegistrosComErro(response.getRegistrosComErro() + 1);
                        response.setRegistrosInseridos(response.getRegistrosInseridos() - 1);
                        break;
                    }
                }
            }
        }
    }

    private void finalizarResponse(ImportacaoResponse response) {
        // Lógica melhorada: considera sucesso se inseriu algum registro
        if (response.getRegistrosInseridos() > 0 && response.getRegistrosComErro() == 0) {
            response.setSucesso(true);
            response.setMensagem("Importação concluída com sucesso! " + 
                response.getRegistrosInseridos() + " registros inseridos, " +
                response.getRegistrosIgnorados() + " registros ignorados.");
        } else if (response.getRegistrosInseridos() > 0) {
            response.setSucesso(true); // Ainda considera sucesso se inseriu registros
            response.setMensagem("Importação concluída com sucesso! " +
                response.getRegistrosInseridos() + " registros inseridos, " +
                response.getRegistrosIgnorados() + " registros ignorados, " +
                response.getRegistrosComErro() + " registros com erro.");
        } else {
            response.setSucesso(false);
            response.setMensagem("Importação falhou! " +
                response.getRegistrosComErro() + " registros com erro, " +
                response.getRegistrosIgnorados() + " registros ignorados.");
        }
    }

    private void adicionarRegistroIgnorado(ImportacaoResponse response, String identificacao, String motivo) {
        ImportacaoResponse.RegistroImportado registro = new ImportacaoResponse.RegistroImportado();
        registro.setCpf(identificacao);
        registro.setStatus("IGNORADO");
        registro.setMensagem(motivo);
        response.getRegistrosImportados().add(registro);
        response.setRegistrosIgnorados(response.getRegistrosIgnorados() + 1);
        response.setTotalRegistrosProcessados(response.getTotalRegistrosProcessados() + 1);
    }

    // Classe auxiliar para processamento
    private static class ProcessamentoResultado {
        int linhaNumero;
        String cpf;
        String email;
        String emailOriginal;
        String erro;
        String mensagem;
        boolean valido = false;
        boolean ignorar = false;
        boolean emailGerado = false;
        Parceiro parceiro;
    }

    // Os métodos auxiliares permanecem iguais...
    private boolean isEmailValido(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private String limparCpf(String cpf) {
        return CPF_PATTERN.matcher(cpf).replaceAll("");
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == Math.floor(numericValue)) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    // Os outros métodos do service permanecem iguais...
    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(limparCpf(cpf));
    }

    public CpfParceiro listarByCpf(String cpf) {
        return repository.findAllByCpf(limparCpf(cpf));
    }

    public List<CpfParceiro> listarByParceiro(Integer id) {
        return repository.findAllByParceiro(parceiroService.listarById(id));
    }

    public List<CpfParceiro> listarBySearch(Integer page, Integer size, Integer idParceiro) {
        String query = "SELECT p FROM CpfParceiro p WHERE p.parceiro.id = :parceiro";
        var q = em.createQuery(query, CpfParceiro.class);
        q.setParameter("parceiro", idParceiro);
        q.setFirstResult(page * 25);
        q.setMaxResults(25);
        return q.getResultList();
    }

    public void deletar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public void deletarByCpf(String cpf) {
        CpfParceiro cpfParceiro = listarByCpf(cpf);
        if (cpfParceiro != null) {
            repository.delete(cpfParceiro);
        }
    }

    public void atualizarByAluno(Aluno aluno) {
        CpfParceiro cpfParceiro = listarByCpf(aluno.getCpf());
        if (cpfParceiro != null) {
            cpfParceiro.setEmail(aluno.getEmail());
            cpfParceiro.setNome(aluno.getNome());
            repository.save(cpfParceiro);
        }
    }

    public void save(CpfParceiro cpfParceiro) {
        if (cpfParceiro != null && cpfParceiro.getCpf() != null) {
            cpfParceiro.setCpf(limparCpf(cpfParceiro.getCpf()));
            repository.save(cpfParceiro);
        }
    }
}