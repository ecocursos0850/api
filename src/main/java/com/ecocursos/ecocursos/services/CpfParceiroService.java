package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.repositories.CpfParceiroRepository;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CpfParceiroService {

    @Autowired
    private CpfParceiroRepository repository;

    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private EntityManager em;

    @SneakyThrows
    public List<CpfParceiro> salvar(MultipartFile multipartFile, Integer idParceiro) {
        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<CpfParceiro> cpfs = new ArrayList<>();

        Iterator rowIterator = sheet.rowIterator();
        while(rowIterator.hasNext()) {
            Row row = (Row) rowIterator.next();

            Iterator cellIterator = row.cellIterator();
            CpfParceiro cpf = new CpfParceiro();

            cpf.setParceiro(parceiroService.listarById(idParceiro));
            while (cellIterator.hasNext()) {
                Cell cell = (Cell) cellIterator.next();
                switch (cell.getColumnIndex()) {
                    case 0:
                        cpf.setCpf(cell.getStringCellValue());
                        break;
                    case 1:
                        cpf.setEmail((String) cell.getStringCellValue());
                        break;
                }
            }
            cpfs.add(cpf);
        }
        workbook.close();
        return repository.saveAll(cpfs);
    }

    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    public CpfParceiro listarByCpf(String cpf) {
        return repository.findAllByCpf(cpf);
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
        repository.delete(listarByCpf(cpf));
    }

    public void atualizarByAluno(Aluno aluno) {
        CpfParceiro cpfParceiro = listarByCpf(aluno.getCpf());
        cpfParceiro.setEmail(aluno.getEmail());
        cpfParceiro.setNome(aluno.getNome());
        repository.save(cpfParceiro);
    }

    public void save(CpfParceiro cpfParceiro) {
        repository.save(cpfParceiro);
    }
}
