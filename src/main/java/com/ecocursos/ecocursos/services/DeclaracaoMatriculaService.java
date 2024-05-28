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
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.ecocursos.ecocursos.models.enums.StatusDeclaracaoMatricula;
import com.ecocursos.ecocursos.repositories.DeclaracaoMatriculaRepository;
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

    private static final String BACKGROUND = "https://srv448021.hstgr.cloud/arquivos/imgs/bg_certificado.png";
    private static final String ASSINATURA = "https://srv448021.hstgr.cloud/arquivos/imgs/assinatura.png";
    private static final String LOGO = "https://srv448021.hstgr.cloud/arquivos/imgs/logo.png";

    private void buscarExternos(DeclaracaoMatricula declaracaoMatricula) {
        declaracaoMatricula.setMatricula(matriculaService.listarById(declaracaoMatricula.getMatricula().getId()));
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

    public DeclaracaoMatricula salvar(DeclaracaoMatricula declaracaoMatricula) {
        try {
            if(declaracaoMatricula.getStatus() == StatusDeclaracaoMatricula.AGUARDANDO) {
                declaracaoMatricula.setAprovado(false);
            } else {
                declaracaoMatricula.setAprovado(true);
            }
            buscarExternos(declaracaoMatricula);
            declaracaoMatricula.setDataCadastro(LocalDate.now());
            return repository.save(declaracaoMatricula);
        } catch(Exception e) {
            throw new ErrorException(e.getMessage());
        }
    }

    public DeclaracaoMatricula alterar(Integer id, DeclaracaoMatricula declaracaoMatricula) {
        try {
            DeclaracaoMatricula declaracaoMatriculaExistente = listarById(id);
            declaracaoMatricula.setId(id);
            declaracaoMatricula.setDataCadastro(declaracaoMatriculaExistente.getDataCadastro());
            declaracaoMatricula.setMatricula(declaracaoMatriculaExistente.getMatricula());
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
        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA, 14);
        Font fontAlun = FontFactory.getFont(FontFactory.HELVETICA, 24);
        document.open();
        document.setMarginMirroring(true);
        Paragraph paragraph = new Paragraph();
        Image imagemLogo = Image.getInstance(LOGO);
        imagemLogo.scalePercent(5);
        imagemLogo.setAlignment(Element.ALIGN_CENTER);
        Paragraph titulo = new Paragraph("DECLARAÇÃO", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
        Paragraph introducao = new Paragraph("O ECOCURSOS - Educação a distância, inscrito no CNPJ sob n. 10.930.297/0001-48, DECLARA que " + declaracaoMatricula.getAluno().getNome() + " participará/participou do curso de " + declaracaoMatricula.getCurso().getTitulo() + " no período de " +  declaracaoMatricula.getInicioPeriodo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + declaracaoMatricula.getFinalPeriodo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ", com carga horaria de " + declaracaoMatricula.getCurso().getCargaHoraria() + " horas e contém o seguinte conteúdo programático: ");
        titulo.setAlignment(Element.ALIGN_CENTER);
        Paragraph conteudoProgramaticoTitulo = new Paragraph("Conteúdo programático".toUpperCase(), FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD));
        String text = StringEscapeUtils.unescapeHtml4(declaracaoMatricula.getCurso().getConteudo());
        Paragraph conteudoProgramatico = new Paragraph(new Paragraph(Jsoup.parse(text).text(), FontFactory.getFont(FontFactory.HELVETICA, 14)));
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToElements(conteudoProgramatico.toString(), converterProperties    );
        introducao.setSpacingAfter(20f);
        Paragraph ambienteVirtual = new Paragraph(StringEscapeUtils.unescapeHtml4("Curso realizado em ambiente virtual, com tutoria durante todo o período de realização da capacitação e eventos síncronos ao longo do curso, além da atividade avaliativa de 10 questões, que somadas correspondem a 10 pontos, sendo necessário obter o mínimo de 6 pontos para aprovação."), FontFactory.getFont(FontFactory.HELVETICA, 14));
        Paragraph objetivoGeral = new Paragraph(StringEscapeUtils.unescapeHtml4("O objetivo geral é fornecer uma visão mais aprofundada sobre o tema que dá nome a este curso tanto nos seus aspectos teóricos, como práticos e observacionais.\n" +
                "Objetivo especifico é discorrer sobre os temas citados no conteúdo programático.\n"), FontFactory.getFont(FontFactory.HELVETICA, 14));
        Paragraph cursos = new Paragraph(StringEscapeUtils.unescapeHtml4("Os cursos livres de atualização e capacitação profissional são oferecidos na conformidade do que dispõe o Decreto Presidencial No. 5.154/2004, que regulamenta o § 2º do art. 36 e os arts. 39 a 41 da Lei No 9.394, de 20 de dezembro de 1996, que estabelece as diretrizes e bases da educação nacional, e dá outras providências. Lei n. 11.416/06 artigo 15, V para Adicional de Qualificação – Treinamentos dos Servidores Públicos Federais."), FontFactory.getFont(FontFactory.HELVETICA, 14));
        Paragraph dataAtual = new Paragraph(StringEscapeUtils.unescapeHtml4("Votuporanga/SP, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()), FontFactory.getFont(FontFactory.HELVETICA, 14));
        Paragraph ecoCursos = new Paragraph("ECOCURSOS – Educação a Distância", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
        Paragraph cnpjEcocursos = new Paragraph("CNPJ 10.930.297/0001-48", FontFactory.getFont(FontFactory.HELVETICA,  10));
        conteudoProgramatico.setSpacingAfter(20f);
        Image imagemAssinatura = Image.getInstance(ASSINATURA);
        imagemAssinatura.scalePercent(50);
        imagemAssinatura.setAlignment(Element.ALIGN_CENTER);
        cnpjEcocursos.setSpacingAfter(60f);
        ambienteVirtual.setSpacingAfter(20f);
        objetivoGeral.setSpacingAfter(20f);
        cursos.setSpacingAfter(20f);
        dataAtual.setSpacingAfter(20f);
        titulo.setSpacingBefore(30f);
        paragraph.add(imagemLogo);
        LineSeparator separator = new LineSeparator(); // Você pode modificar o estilo da linha aqui
        separator.setLineWidth(UnitValue.createPercentValue(100).getValue()); // Definindo a largura da linha como 100% da página
        separator.setLineWidth(0.1f);
        paragraph.add((Element) separator);
        paragraph.add(titulo);
        paragraph.add(introducao);
        paragraph.add(conteudoProgramaticoTitulo);
        paragraph.add(conteudoProgramatico);
        paragraph.add(ambienteVirtual);
        paragraph.add(objetivoGeral);
        paragraph.add(cursos);
        paragraph.add(dataAtual);
        paragraph.add(ecoCursos);
        paragraph.add(cnpjEcocursos);
        paragraph.add(imagemAssinatura);
        Paragraph rua = new Paragraph("Rua  Ponta Porã n. 3011-sala 2- CEP 15500-090 – Votuporanga/SP.", FontFactory.getFont(FontFactory.HELVETICA,  10, Font.BOLD));
        rua.setAlignment(Element.ALIGN_CENTER);
        Paragraph email = new Paragraph("E-mail: contato@ecocursos.com.br – web: www.ecocursos.com.br", FontFactory.getFont(FontFactory.HELVETICA,  10, Font.BOLD));
        email.setAlignment(Element.ALIGN_CENTER);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(rua);
        paragraph.add(email);
        document.add(paragraph);
        document.close();
        byte[] pdfBytes = baos.toByteArray();
        return pdfBytes;
    }
}
