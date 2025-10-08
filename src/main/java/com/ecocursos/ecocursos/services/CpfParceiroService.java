package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
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
    public List<CpfParceiro> salvar(MultipartFile multipartFile, Integer idParceiro) {
        String fileName = multipartFile.getOriginalFilename().toLowerCase();
        
        if (fileName.endsWith(".csv")) {
            return processarCSV(multipartFile, idParceiro);
        } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
            return processarExcel(multipartFile, idParceiro);
        } else {
            throw new IllegalArgumentException("Formato de arquivo não suportado. Use CSV ou Excel.");
        }
    }

    @SneakyThrows
    private List<CpfParceiro> processarCSV(MultipartFile multipartFile, Integer idParceiro) {
        List<CpfParceiro> cpfs = new ArrayList<>();
        Parceiro parceiro = parceiroService.listarById(idParceiro);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Pular linha vazia
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Pular cabeçalho
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                CpfParceiro cpfParceiro = processarLinhaCSV(line, parceiro);
                if (cpfParceiro != null) {
                    cpfs.add(cpfParceiro);
                }
            }
        }
        
        return salvarRegistrosValidos(cpfs);
    }

    private CpfParceiro processarLinhaCSV(String linha, Parceiro parceiro) {
        // Dividir a linha por vírgula (formato CSV simples)
        String[] colunas = linha.split(",");
        
        if (colunas.length < 1) {
            return null; // Linha sem dados
        }

        String cpf = colunas[0].trim();
        String email = colunas.length > 1 ? colunas[1].trim() : null;

        // Validar CPF
        if (cpf.isEmpty() || cpf.equalsIgnoreCase("cpf")) {
            return null; // CPF vazio ou é cabeçalho
        }

        // Limpar e formatar CPF (remover pontos e traços)
        cpf = limparCpf(cpf);
        
        // Validar se CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return null; // CPF inválido
        }

        // Verificar se CPF já existe
        if (repository.existsByCpf(cpf)) {
            return null; // CPF já existe, não fazer nada
        }

        // Processar email
        if (email == null || email.isEmpty() || !isEmailValido(email)) {
            // Criar email padrão: cpf@ecocursos.com.br
            email = cpf + "@ecocursos.com.br";
        }

        CpfParceiro cpfParceiro = new CpfParceiro();
        cpfParceiro.setCpf(cpf);
        cpfParceiro.setEmail(email.toLowerCase());
        cpfParceiro.setParceiro(parceiro);

        return cpfParceiro;
    }

    @SneakyThrows
    private List<CpfParceiro> processarExcel(MultipartFile multipartFile, Integer idParceiro) {
        Workbook workbook;
        
        if (multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        } else {
            workbook = new org.apache.poi.hssf.usermodel.HSSFWorkbook(multipartFile.getInputStream());
        }
        
        Sheet sheet = workbook.getSheetAt(0);
        List<CpfParceiro> cpfs = new ArrayList<>();
        Parceiro parceiro = parceiroService.listarById(idParceiro);

        Iterator<Row> rowIterator = sheet.rowIterator();
        
        // Pular cabeçalho se existir
        if (rowIterator.hasNext()) {
            rowIterator.next(); // Pula a primeira linha (cabeçalho)
        }

        while(rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            // Pular linhas vazias
            if (isRowEmpty(row)) {
                continue;
            }

            CpfParceiro cpfParceiro = processarLinhaExcel(row, parceiro);
            if (cpfParceiro != null) {
                cpfs.add(cpfParceiro);
            }
        }
        
        workbook.close();
        return salvarRegistrosValidos(cpfs);
    }

    private CpfParceiro processarLinhaExcel(Row row, Parceiro parceiro) {
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
            return null; // CPF é obrigatório, pular linha
        }

        // Limpar e formatar CPF (remover pontos e traços)
        cpf = limparCpf(cpf);
        
        // Validar se CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return null; // CPF inválido, pular linha
        }

        // Verificar se CPF já existe
        if (repository.existsByCpf(cpf)) {
            return null; // CPF já existe, não fazer nada
        }

        // Processar email
        if (email == null || email.trim().isEmpty() || !isEmailValido(email)) {
            // Criar email padrão: cpf@ecocursos.com.br
            email = cpf + "@ecocursos.com.br";
        }

        CpfParceiro cpfParceiro = new CpfParceiro();
        cpfParceiro.setCpf(cpf);
        cpfParceiro.setEmail(email.toLowerCase());
        cpfParceiro.setParceiro(parceiro);

        return cpfParceiro;
    }

    private boolean isEmailValido(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
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
                    // Remove .0 do final de números inteiros
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

    private List<CpfParceiro> salvarRegistrosValidos(List<CpfParceiro> cpfs) {
        List<CpfParceiro> registrosSalvos = new ArrayList<>();
        
        for (CpfParceiro cpf : cpfs) {
            if (cpf != null && !repository.existsByCpf(cpf.getCpf())) {
                registrosSalvos.add(repository.save(cpf));
            }
        }
        
        return registrosSalvos;
    }

    // Os outros métodos permanecem iguais...
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