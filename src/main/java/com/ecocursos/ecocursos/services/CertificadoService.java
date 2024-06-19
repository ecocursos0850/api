package com.ecocursos.ecocursos.services;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Certificado;
import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.MatriculaLogs;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.models.enums.StatusDeclaracaoMatricula;
import com.ecocursos.ecocursos.repositories.CertificadoRepository;
import com.ecocursos.ecocursos.repositories.UserRepository;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.SneakyThrows;

@Service
public class CertificadoService {

    private static final String BACKGROUND = "https://srv448021.hstgr.cloud/arquivos/imgs/bg_certificado.png";
    private static final String ASSINATURA = "https://srv448021.hstgr.cloud/arquivos/imgs/assinatura.png";
    private static final String LOGO = "https://srv448021.hstgr.cloud/arquivos/imgs/logo.png";

    @Autowired
    private CertificadoRepository certificadoRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private DeclaracaoMatriculaService declaracaoMatriculaService;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private MatriculaLogsService matriculaLogsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private ParceiroService parceiroService;

    public List<Certificado> listar() {
        return certificadoRepository.findAll();
    }

    public Certificado listarById(Integer id) {
        return certificadoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Certificado gerado com sucesso"));
    }

    public void deletar(Matricula matricula) {
        certificadoRepository.delete(certificadoRepository.findByMatricula(matricula));
    }

    public List<Certificado> listarByAluno(Integer id) {
        return certificadoRepository.findAllByAluno(alunoService.listarById(id));
    }

    public Certificado findByMatricula(Integer id) {
        return certificadoRepository.findByMatricula(matriculaService.listarById(id));
    }

    public void salvarByMatricula(Matricula matricula, Integer idUsuario) {
        Certificado certificado = new Certificado();
        certificado.setMatricula(matricula);
        certificado.setDataCadastro(LocalDateTime.now());
        certificadoRepository.save(certificado);
        criarMatriculaLogs(matricula, idUsuario);
    }
 
    private void criarMatriculaLogs(Matricula matricula, Integer idUsuario) {
        MatriculaLogs logs = new MatriculaLogs();
        logs.setData(LocalDate.now());
        logs.setDescricao("Certificado gerado");
        logs.setMatricula(matricula);
        logs.setUsuario(userRepository.findById(idUsuario).get());
        matriculaLogsService.salvar(logs);
    }

    @SneakyThrows
    public byte[] gerar(Matricula matricula) {
        DeclaracaoMatricula declaracaoMatricula = new DeclaracaoMatricula();
        if(declaracaoMatriculaService.listarByMatricula(matricula.getId()) != null) {
            if (declaracaoMatriculaService.listarByMatricula(matricula.getId()).getStatus() == StatusDeclaracaoMatricula.APROVADA)
            declaracaoMatricula = declaracaoMatriculaService.listarByMatricula(matricula.getId());
        }
        Certificado certificado = certificadoRepository.findByMatricula(matricula);
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14);
        Font fontAlun = FontFactory.getFont(FontFactory.TIMES_ROMAN, 24);
        document.open();
        document.setMarginMirroring(true);
        Paragraph title = new Paragraph(new Phrase(10f, "CERTIFICADO", FontFactory.getFont(FontFactory.TIMES_ROMAN, 32f, Font.BOLD )));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(new Chunk().setLineHeight(100f));
        title.setSpacingBefore(100f);
        Paragraph paragraphPrincipal = new Paragraph();
        Paragraph content = new Paragraph("Certificamos que:");
        Paragraph aluno = new Paragraph(matricula.getAluno().getNome().toUpperCase(), FontFactory.getFont(FontFactory.TIMES_ROMAN, 24, Font.BOLD));
        content.setAlignment(Element.ALIGN_CENTER);
        document.add(new Chunk().setLineHeight(100f));
        aluno.setAlignment(Element.ALIGN_CENTER);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph portador = new Paragraph("Portador(a) do RG nº " + matricula.getAluno().getRg() + ", concluiu com aproveitamento", fontParagraph);
        Paragraph datas = new Paragraph();
        if (matricula.getNota() != null && matricula.getNota() > 0) {
            datas = new Paragraph("no período de " + formatter.format(declaracaoMatricula.getInicioPeriodo() != null ? declaracaoMatricula.getInicioPeriodo() : matricula.getDataMatricula()) + " a " + formatter.format(declaracaoMatricula.getFinalPeriodo() != null ? declaracaoMatricula.getFinalPeriodo() : certificado.getDataCadastro()) + " o curso de atualização/capacitação profissional com nota: " + matricula.getNota() + " em:", fontParagraph);
        } else {
            datas = new Paragraph("no período de " + formatter.format(declaracaoMatricula.getInicioPeriodo() != null ? declaracaoMatricula.getInicioPeriodo() : matricula.getDataMatricula()) + " a " + formatter.format(declaracaoMatricula.getFinalPeriodo() != null ? declaracaoMatricula.getFinalPeriodo() : certificado.getDataCadastro()) + " o curso de atualização/capacitação profissional em:", fontParagraph);
        }
        Paragraph cursoNome = new Paragraph(matricula.getCurso().getTitulo(), fontAlun);
        Paragraph cargaHoraria = new Paragraph("com carga horária total de " + matricula.getCurso().getCargaHoraria() + " horas", fontParagraph);
        Paragraph cidade = new Paragraph("Votuporanga/SP, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fontParagraph);
        Image assinatura = Image.getInstance(ASSINATURA);
        assinatura.scaleToFit(300, 550);
        assinatura.setAlignment(Element.ALIGN_CENTER);
        portador.setAlignment(Element.ALIGN_CENTER);
        datas.setAlignment(Element.ALIGN_CENTER);
        cursoNome.setAlignment(Element.ALIGN_CENTER);
        cargaHoraria.setAlignment(Element.ALIGN_CENTER);
        cidade.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        Aluno alunoExistente = alunoService.listarById(matricula.getAluno().getId());
        if (alunoExistente != null) {
            if (alunoExistente.getParceiro() != null) {
                Parceiro parceiro = parceiroService.listarById(alunoExistente.getParceiro().getId());
                Image logoParceiro = Image.getInstance(parceiro.getLogo(), false);
                logoParceiro.scaleToFit(100, 350);
                logoParceiro.setAlignment(Element.ALIGN_RIGHT);
                paragraphPrincipal.add(logoParceiro);
            }
        }
        paragraphPrincipal.add(title);
        paragraphPrincipal.add(content);
        paragraphPrincipal.add(aluno);
        paragraphPrincipal.add(portador);
        paragraphPrincipal.add(datas);
        paragraphPrincipal.add(cursoNome);
        paragraphPrincipal.add(cargaHoraria);
        paragraphPrincipal.add(cidade);
        
        document.add(new Chunk().setLineHeight(150f));
        cidade.setSpacingAfter(2f);
        paragraphPrincipal.add(assinatura);
//        document.add(content);
//        document.add(aluno);
//        document.add(portador);
//        document.add(datas);
//        document.add(cursoNome);
//        document.add(cargaHoraria);
//        document.add(cidade);
//        document.add(assinatura);
        document.add(paragraphPrincipal);
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image image = Image.getInstance(BACKGROUND);
        image.scaleAbsolute(PageSize.A4.rotate());
        image.setAbsolutePosition(0, 0);
        canvas.addImage(image);
        document.newPage();
        Paragraph realizado = new Paragraph("Curso realizado em ambiente virtual, com tutoria durante todo o período de realização da capacitação e possui eventos síncronos ao longo do curso e atividade " +
                "avaliativa de 10 questões, que somadas correspondem a 10 pontos, sendo necessário obter o mínimo de 6 pontos para aprovação", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLDITALIC));
        Paragraph tipoCurso = new Paragraph("Tipo de curso: " +  matricula.getCurso().getCategoria().getTitulo() + " - Modalidade: A Distância (E-LEARNING)\n", FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLDITALIC));
        document.add(realizado);
        document.add(new Paragraph("Conteúdo Programático", FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD)));
        String utf8String = new String(matricula.getCurso().getConteudo().getBytes(), StandardCharsets.UTF_8);
        String text = StringEscapeUtils.unescapeHtml4(matricula.getCurso().getConteudo());
        document.add(new Paragraph(Jsoup.parse(text).text(), fontParagraph));
        Paragraph cargaHorariaTotal = new Paragraph("CARGA HORÁRIA TOTAL: " + matricula.getCurso().getCargaHoraria() + " HORAS - Certificado registrado no ECOCURSOS sob nº " + certificado.getId(),  FontFactory.getFont(FontFactory.TIMES_ROMAN, 12));
        cargaHorariaTotal.setSpacingBefore(30f);
        document.add(cargaHorariaTotal);
        document.add(new Paragraph("Expedido na conformidade do art. 3º., inciso I do decreto federal 2208/97. ECOCURSOS registrado no CNPJ sob nº 10.930.297/0001-48",  FontFactory.getFont(FontFactory.TIMES_ROMAN, 12)));
        document.close();
        byte[] pdfBytes = baos.toByteArray();
        return pdfBytes;
    }

}
