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

    @SneakyThrows
    public List<CpfParceiro> salvar(MultipartFile multipartFile, Integer idParceiro) {
        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
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

            CpfParceiro cpfParceiro = processarLinha(row, parceiro);
            if (cpfParceiro != null) {
                cpfs.add(cpfParceiro);
            }
        }
        
        workbook.close();
        
        // Salvar apenas os registros válidos que não existem
        return salvarRegistrosValidos(cpfs);
    }

    private CpfParceiro processarLinha(Row row, Parceiro parceiro) {
        Iterator<Cell> cellIterator = row.cellIterator();
        CpfParceiro cpfParceiro = new CpfParceiro();
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
        if (email == null || email.trim().isEmpty()) {
            // Criar email padrão: cpf@ecocursos.com.br
            email = cpf + "@ecocursos.com.br";
        }

        cpfParceiro.setCpf(cpf);
        cpfParceiro.setEmail(email.toLowerCase()); // Normalizar para minúsculo
        cpfParceiro.setParceiro(parceiro);

        return cpfParceiro;
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
        repository.delete(repository.findById(id).get());
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
