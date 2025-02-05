package com.ecocursos.ecocursos.services;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.enums.Status;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.ecocursos.ecocursos.models.MatriculaLogs;
import com.ecocursos.ecocursos.models.enums.StatusDeclaracaoMatricula;
import com.ecocursos.ecocursos.repositories.DeclaracaoMatriculaRepository;
import com.ecocursos.ecocursos.repositories.UserRepository;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;


@Service
@RequiredArgsConstructor
public class DeclaracaoMatriculaService {
    
    private final DeclaracaoMatriculaRepository repository;
    private final AlunoService alunoService;
    private final MatriculaService matriculaService;
    private final EntityManager entityManager;
    private final CursoService cursoService;
    private final MatriculaLogsService matriculaLogsService;
    private final UserRepository userRepository;

    private static final String BACKGROUND = "https://srv448021.hstgr.cloud/arquivos/imgs/bg_certificado.png";
    private static final String ASSINATURA = "https://srv448021.hstgr.cloud/arquivos/imgs/assinatura.png";
    private static final String LOGO = "https://srv448021.hstgr.cloud/arquivos/imgs/logo.png";

    private void buscarExternos(DeclaracaoMatricula declaracaoMatricula) {
        try {
            declaracaoMatricula.setMatricula(matriculaService.listarById(declaracaoMatricula.getMatricula().getId()));
        } catch (Exception e) {
            throw new ErrorException("Erro ao buscar externos de declaração");
        }
    }

    public List<DeclaracaoMatricula> listar() {
        return repository.findAll();
    }

    public List<DeclaracaoMatricula> listarByFiltro(Integer status, String nome, Integer page) {
        String query = "SELECT d FROM DeclaracaoMatricula d ";
        String condicao = "WHERE";

        if (nome != null) {
            query += condicao + " d.aluno.nome LIKE CONCAT(:nome, '%')";
            condicao = " AND ";
        }
        if (status != null) {
            query += condicao + " d.status = :status";
        }

        var q = entityManager.createQuery(query, DeclaracaoMatricula.class);

        if (status != null) {
            q.setParameter("status", StatusDeclaracaoMatricula.toEnum(status));
        }
        if (nome != null) {
            q.setParameter("nome", nome);
        }

        q.setFirstResult(page * 25);
        q.setMaxResults(25);

        return q.getResultList();
    }

    public List<DeclaracaoMatricula> listarByRelatorio(
            Integer curso,
            LocalDate dataCadastroInicial,
            LocalDate dataCadastroFinal,
            Integer status
    ) {
        String query = "SELECT d FROM DeclaracaoMatricula d ";
        String condicao = " WHERE ";

        if (curso != null) {
            query += condicao + " d.curso = :curso";
            condicao = " AND ";
        }
        if (status != null) {
            query += condicao + " d.status = :status";
            condicao = " AND ";
        }
        if (dataCadastroInicial != null && dataCadastroFinal != null) {
            query += condicao + " d.dataCadastro BETWEEN :dataCadastroInicial AND :dataCadastroFinal";
        } else if (dataCadastroInicial != null) {
            query += condicao + " d.dataCadastro >= :dataCadastroInicial";
        } else if (dataCadastroFinal != null) {
            query += condicao + " d.dataCadastro <= :dataCadastroFinal";
        }

        var q = entityManager.createQuery(query, DeclaracaoMatricula.class);
        if (curso != null) {
            q.setParameter("curso", cursoService.listarById(curso));
        }
        if (status != null) {
            q.setParameter("status", StatusDeclaracaoMatricula.toEnum(status));
        }
        if (dataCadastroInicial != null) {
            q.setParameter("dataCadastroInicial", dataCadastroInicial);
        }
        if (dataCadastroFinal != null) {
            q.setParameter("dataCadastroFinal", dataCadastroFinal);
        }
        return q.getResultList();
    }

    public List<DeclaracaoMatricula> listarByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataCadastro"));
        return repository.findAll(pageable).toList();
     }

    public DeclaracaoMatricula listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Declação não encontrada"));
    }

    public List<DeclaracaoMatricula> listarByAluno(Integer id) {
        return repository.findAllByAluno(alunoService.listarById(id));
    }

    public DeclaracaoMatricula salvar(DeclaracaoMatricula declaracaoMatricula, Integer idUsuario) {
        try {
            if(declaracaoMatricula.getStatus() == StatusDeclaracaoMatricula.AGUARDANDO) {
                declaracaoMatricula.setAprovado(false);
            } else {
                declaracaoMatricula.setAprovado(true);
            }
            buscarExternos(declaracaoMatricula);
            declaracaoMatricula.setDataCadastro(LocalDate.now());
            criarMatriculaLogs(declaracaoMatricula, idUsuario);
            return repository.save(declaracaoMatricula);
        } catch(Exception e) {
            throw new ErrorException(e.getMessage());
        }
    }

    private void criarMatriculaLogs(DeclaracaoMatricula declaracaoMatricula, Integer idUsuario) {
        try {
            MatriculaLogs logs = new MatriculaLogs();
            logs.setData(LocalDate.now());
            logs.setDescricao("Usuário solicitou declaração");
            logs.setMatricula(declaracaoMatricula.getMatricula());
            logs.setUsuario(userRepository.findById(idUsuario).get());
            matriculaLogsService.salvar(logs);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao salvar declaração");
        }
    }

    public DeclaracaoMatricula alterar(Integer id, DeclaracaoMatricula declaracaoMatricula) {
        try {
            DeclaracaoMatricula declaracaoMatriculaExistente = listarById(id);
            declaracaoMatricula.setId(id);
            declaracaoMatricula.setDataCadastro(declaracaoMatriculaExistente.getDataCadastro());
            declaracaoMatricula.setMatricula(declaracaoMatriculaExistente.getMatricula());
            if (declaracaoMatricula.getInicioPeriodo() == null) declaracaoMatricula.setInicioPeriodo(declaracaoMatriculaExistente.getInicioPeriodo());
            if (declaracaoMatricula.getFinalPeriodo() == null) declaracaoMatricula.setFinalPeriodo(declaracaoMatriculaExistente.getFinalPeriodo());
            return repository.save(declaracaoMatricula);
        } catch (Exception e) {
            throw new ErrorException("Erro ao alterar declaração de matrícula");
        }
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public DeclaracaoMatricula aprovar(Integer id) {
        DeclaracaoMatricula declaracaoMatricula = listarById(id);
        declaracaoMatricula.setStatus(StatusDeclaracaoMatricula.APROVADA);
        declaracaoMatricula.setAprovado(true);
        return repository.save(declaracaoMatricula);
    }

    public DeclaracaoMatricula listarByMatricula(Integer id) {
        return repository.findByMatricula(matriculaService.listarById(id));
    }

    public void reprovar(Integer id) {
        try {
            DeclaracaoMatricula declaracaoMatricula = listarById(id);
            declaracaoMatricula.setStatus(StatusDeclaracaoMatricula.REPROVADO);
            repository.save(declaracaoMatricula);
        } catch (Exception e) {
            throw new ErrorException("Erro ao reprovar declaração");
        }
    }

    @SneakyThrows
    public byte[] gerarCertificadoDeclaracao(Integer id) {
        DeclaracaoMatricula declaracaoMatricula = listarById(id);
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_7);
    
        // Fonte única padronizada para todo o documento
        Font fontePadrao = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fonteNegrito = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
    
        document.open();
        document.setMarginMirroring(true);
        
        // Logo
        Image imagemLogo = Image.getInstance(LOGO);
        imagemLogo.scalePercent(5);
        imagemLogo.setAlignment(Element.ALIGN_CENTER);
        
        // Título
        Paragraph titulo = new Paragraph("DECLARAÇÃO", fonteNegrito);
        titulo.setAlignment(Element.ALIGN_CENTER);
        
        // Introdução
        Paragraph introducao = new Paragraph(
            "O ECOCURSOS - Educação a distância, inscrito no CNPJ sob n. 10.930.297/0001-48, DECLARA que " +
            declaracaoMatricula.getAluno().getNome() +
            " participará/participou do curso de " +
            declaracaoMatricula.getCurso().getTitulo() +
            " no período de " +
            declaracaoMatricula.getInicioPeriodo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
            " a " +
            declaracaoMatricula.getFinalPeriodo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
            ", com carga horária de " +
            declaracaoMatricula.getCurso().getCargaHoraria() +
            " horas e contém o seguinte conteúdo programático: ",
            fontePadrao
        );
        introducao.setSpacingAfter(20f);
    
        // Conteúdo programático
        Paragraph conteudoProgramaticoTitulo = new Paragraph("CONTEÚDO PROGRAMÁTICO", fonteNegrito);
        conteudoProgramaticoTitulo.setSpacingAfter(10f);
    
        String text = StringEscapeUtils.unescapeHtml4(declaracaoMatricula.getCurso().getConteudo());
        Paragraph conteudoProgramatico = new Paragraph(Jsoup.parse(text).text(), fontePadrao);
        conteudoProgramatico.setSpacingAfter(20f);
        conteudoProgramatico.setAlignment(Element.ALIGN_JUSTIFIED);
    
        // Outros parágrafos
        Paragraph ambienteVirtual = new Paragraph(
            "Curso realizado em ambiente virtual, com tutoria durante todo o período de realização da capacitação...",
            fontePadrao
        );
        ambienteVirtual.setSpacingAfter(20f);
        ambienteVirtual.setAlignment(Element.ALIGN_JUSTIFIED);
    
        Paragraph objetivoGeral = new Paragraph(
            "O objetivo geral é fornecer uma visão mais aprofundada sobre o tema...",
            fontePadrao
        );
        objetivoGeral.setSpacingAfter(20f);
        objetivoGeral.setAlignment(Element.ALIGN_JUSTIFIED);
    
        Paragraph cursos = new Paragraph(
            "Os cursos livres de atualização e capacitação profissional são oferecidos na conformidade...",
            fontePadrao
        );
        cursos.setSpacingAfter(20f);
        cursos.setAlignment(Element.ALIGN_JUSTIFIED);
    
        Paragraph dataAtual = new Paragraph(
            "Votuporanga/SP, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            fontePadrao
        );
        dataAtual.setSpacingAfter(20f);
    
        // Rodapé
        Paragraph ecoCursos = new Paragraph("ECOCURSOS – Educação a Distância", fonteNegrito);
        Paragraph cnpjEcocursos = new Paragraph("CNPJ 10.930.297/0001-48", fontePadrao);
        Paragraph rua = new Paragraph("Rua Ponta Porã n. 3011-sala 2- CEP 15500-090 – Votuporanga/SP.", fonteNegrito);
        Paragraph email = new Paragraph("E-mail: contato@ecocursos.com.br – web: www.ecocursos.com.br", fonteNegrito);
    
        // Assinatura
        Image imagemAssinatura = Image.getInstance(ASSINATURA);
        imagemAssinatura.scalePercent(50);
        imagemAssinatura.setAlignment(Element.ALIGN_CENTER);
    
        // Adicionando elementos ao documento
        document.add(imagemLogo);
        document.add(titulo);
        document.add(introducao);
        document.add(conteudoProgramaticoTitulo);
        document.add(conteudoProgramatico);
        document.add(ambienteVirtual);
        document.add(objetivoGeral);
        document.add(cursos);
        document.add(dataAtual);
        document.add(ecoCursos);
        document.add(cnpjEcocursos);
        document.add(imagemAssinatura);
        document.add(rua);
        document.add(email);
    
        document.close();
        return baos.toByteArray();
    }
}
