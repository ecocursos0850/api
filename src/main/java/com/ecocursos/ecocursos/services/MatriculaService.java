package com.ecocursos.ecocursos.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.AlunoAvaliacao;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.EntradaCaixa;
import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.MatriculaLogs;
import com.ecocursos.ecocursos.models.Pedido;
import com.ecocursos.ecocursos.models.User;
import com.ecocursos.ecocursos.models.dtos.MatriculaMes;
import com.ecocursos.ecocursos.models.enums.StatusAvaliacaoMatricula;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.models.enums.TipoEntradaCaixa;
import com.ecocursos.ecocursos.models.enums.TipoPagamento;
import com.ecocursos.ecocursos.repositories.AfiliadoRepository;
import com.ecocursos.ecocursos.repositories.AlunoAvaliacaoRepository;
import com.ecocursos.ecocursos.repositories.MatriculaRepository;
import com.ecocursos.ecocursos.repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class MatriculaService {

    @Autowired
    private MatriculaRepository repository;
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private AfiliadoRepository afiliadoRepository;
    @Autowired
    private AfiliadoService afiliadoService;
    @Autowired
    private CursoService cursoService;
    @Autowired
    private AlunoService alunoService;
    @Autowired
    private AlunoAvaliacaoRepository alunoAvaliacaoRepository;
    @Autowired
    private AvaliacaoService avaliacaoService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntradaCaixaService entradaCaixaService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CertificadoService certificadoService;
    @Autowired
    private ParceiroService parceiroService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MatriculaLogsService matriculaLogsService;

        private void completarCampos(Matricula matricula) {
            if (matricula.getPedido() != null) {
                matricula.setObservacao(matricula.getObservacao());
                matricula.setValorCurso(matricula.getPedido().getTotal());
                matricula.setValorMatricula(matricula.getPedido().getTaxaMatricula());
                if(matricula.getStatus() != StatusAvaliacaoMatricula.LIBERADO_PARCEIRO) {
                    matricula.setStatus(StatusAvaliacaoMatricula.LIBERADO_PAGO);
                }
                matricula.setCurso(cursoService.listarById(matricula.getCurso().getId()));
            }
            matricula.setAluno(alunoService.listarById(matricula.getAluno().getId()));
            matricula.setCurso(cursoService.listarById(matricula.getCurso().getId()));
            Optional<Afiliado> afiliado = Optional.ofNullable(matricula.getAfiliado());
            if(afiliado.isPresent() && afiliado.get().getId() != null) {
            Pedido pedido = gerarPedido(matricula);
                if(matricula.getAfiliado() != null) {
                    if (matricula.getStatus() == StatusAvaliacaoMatricula.LIBERADO_PARCEIRO) {
                        alunoService.atualizarHorasDisponiveis(pedido);
                    }
                    matricula.setPedido(pedido);
                    EntradaCaixa entradaCaixa = new EntradaCaixa();
                    entradaCaixa.setDataPagamento(LocalDateTime.now());
                    entradaCaixa.setTipoPagamento(pedido.getTipoPagamentos().get(0));
                    entradaCaixa.setTipoEntradaCaixa(TipoEntradaCaixa.VENDEDOR);
                    entradaCaixa.setValor(pedido.getTotal());
                    entradaCaixa.setCategoria(matricula.getCurso().getCategoria());
                    entradaCaixa.setAfiliado(afiliadoService.listarById(matricula.getAfiliado().getId()));
                    entradaCaixaService.salvar(entradaCaixa);
                }
                matricula.setAfiliado(afiliadoService.listarById(matricula.getAfiliado().getId()));
            }
        }

    private Pedido gerarPedido(Matricula matricula) {
        Pedido pedido = new Pedido();
        pedido.setAluno(matricula.getAluno());
        pedido.setCargaHorariaTotal(matricula.getCurso().getCargaHoraria());
        List<Curso> cursos = new ArrayList<>();
        cursos.add(matricula.getCurso());
        pedido.setCursos(cursos);
        pedido.setDataPedido(LocalDateTime.now());
        if (matricula.getAfiliado() != null) {
            matricula.getAfiliado().getComissoes().forEach(x -> {
                pedido.getCursos().forEach(y -> {
                    if(x.getCategoria() == y.getCategoria()) {
                        pedido.setDescontos(x.getDesconto());
                    } else {
                        pedido.setDescontos(0D);
                    }
                });
            });
        }
        pedido.setTipoCurso(matricula.getCurso().getTipoCurso());
        pedido.setTipoPagamentos(List.of(TipoPagamento.PIX,TipoPagamento.BOLETO, TipoPagamento.CARTAO_CREDITO));
        pedido.setQtdItens(1);
        if (matricula.getCurso().getCategoria().getTitulo().equals("PÓS-GRADUAÇÃO / MBA")) {
            pedido.setTotal(matricula.getValorCurso() + matricula.getValorMatricula() - 720D);
        } else {
            pedido.setTotal(matricula.getValorCurso() + matricula.getValorMatricula());
        }
        pedido.setTipoCurso(matricula.getCurso().getTipoCurso());
        if(matricula.getCurso().getTaxaMatricula() != null) {
            pedido.setTaxaMatricula(matricula.getCurso().getTaxaMatricula().getValor());
        } else {
            pedido.setTaxaMatricula(0D);
        }
        pedido.setStatus(StatusPedido.PAGO);
        pedido.setSubtotal(matricula.getValorCurso() + matricula.getValorMatricula());
        return pedidoService.salvarPedidoPorMatricula(pedido);
    }

    private void setParameterIfNotNull(Query q, String paramName, Object paramValue) {
        if (paramValue != null) {
            q.setParameter(paramName, paramValue);
        }
    }

    private void buscarExternos(Matricula matricula) {
        if (matricula.getPedido() != null) {
            matricula.setPedido(pedidoService.listarById(matricula.getPedido().getId()));
        }
        if (matricula.getAfiliado() != null) {
            matricula.setAfiliado(afiliadoService.listarById(matricula.getAfiliado().getId()));
        }
    }

    private TipoCurso convertTipoCurso(Integer tipoCurso) {
        // Lógica para converter Integer para TipoCurso usando o serviço ou lógica apropriada
        return TipoCurso.toEnum(tipoCurso);
    }

    private List<StatusAvaliacaoMatricula> convertStatus(Integer status) {
        // Lógica para converter Integer para StatusAvaliacaoMatricula usando o serviço ou lógica apropriada
        if (status != null) {
            return List.of(StatusAvaliacaoMatricula.toEnum(status));
        }
        return List.of();
    }

    public List<Matricula> listarByFiltro(
        Integer status,
        String nome, 
        String curso,
        LocalDateTime periodoInicial,
        LocalDateTime periodoFinal,
        String afiliado,
        Integer page
                ) {
        String query = "SELECT m FROM Matricula m ";
        String condicao = "WHERE";

        if(status != null) {
            query += condicao + " m.status = :status";
            condicao = " AND ";
        }
        if (nome != null) {
            query += condicao + " m.aluno.nome LIKE CONCAT(:nome, '%')";
            condicao = " AND ";
        }
        if (curso != null) {
            query += condicao + " m.curso.titulo LIKE CONCAT(:curso, '%')";
            condicao = " AND ";
        }
        if (periodoInicial != null && periodoFinal != null) {
            query += condicao + " m.dataMatricula BETWEEN :periodoInicial AND :periodoFinal";
        } else if (periodoInicial != null) {
            query += condicao + " m.dataMatricula >= :periodoInicial";
        } else if (periodoFinal != null) {
            query += condicao + " m.dataMatricula <= :periodoFinal";
        }
        if (afiliado != null) {
            query += condicao + " m.afiliado.nome LIKE CONCAT(:afiliado, '%')";
            condicao = " AND ";
        }

        query += " ORDER BY m.dataMatricula DESC";
        condicao = " AND ";

        var q = entityManager.createQuery(query, Matricula.class);

        if (status != null) {
            q.setParameter("status", StatusAvaliacaoMatricula.toEnum(status));
        }
        if (nome != null) {
            q.setParameter("nome", nome);
        }
        if (curso != null) {
            q.setParameter("curso", curso);
        }
        if (afiliado != null) {
            q.setParameter("afiliado", afiliado);
        }
        if (periodoInicial != null) {
            q.setParameter("periodoInicial", periodoInicial);
        }
    
        if (periodoFinal != null) {
            q.setParameter("periodoFinal", periodoFinal);
        }

        q.setFirstResult(page * 25);
        q.setMaxResults(25);

        return q.getResultList();
    }

    public List<Matricula> listarByRelatorio(
            Integer tipoCurso,
            Integer categoria,
            Integer curso,
            Integer parceiro,
            LocalDateTime dataCadastroInicial,
            LocalDateTime dataCadastroFinal,
            LocalDateTime dataLiberacaoInicial,
            LocalDateTime dataLiberacaoFinal,
            LocalDateTime dataCertificadoInicial,
            LocalDateTime dataCertificadoFinal,
            String estado,
            Integer status
    ) {
        String query = "SELECT m FROM Matricula m";
        String condicao = " WHERE ";

        if (tipoCurso != null) {
            query += condicao + " m.curso.tipoCurso = :tipoCurso";
            condicao = " AND ";
        }
        if (categoria != null) {
            query += condicao + " m.curso.categoria = :categoria";
            condicao = " AND ";
        }
        if (curso != null) {
            query += condicao + " m.curso = :curso";
            condicao = " AND ";
        }
        if (parceiro != null) {
            query += condicao + " m.aluno.parceiro = :parceiro";
            condicao = " AND ";
        }
        if (status != null) {
            query += condicao + " m.status IN :status";
            condicao = " AND ";
        }
        if (dataCadastroInicial != null && dataCadastroFinal != null) {
            query += condicao + " m.dataMatricula BETWEEN :dataCadastroInicial AND :dataCadastroFinal";
            condicao = " AND ";
        } else if (dataCadastroInicial != null) {
            query += condicao + " m.dataMatricula >= :dataCadastroInicial";
            condicao = " AND ";
        } else if (dataCadastroFinal != null) {
            query += condicao + " m.dataMatricula <= :dataCadastroFinal";
            condicao = " AND ";
        }
        if (dataCertificadoInicial != null && dataCertificadoFinal != null) {
            query += condicao + " m.certificado.dataCadastro BETWEEN :dataCertificadoInicial AND :dataCertificadoFinal";
            condicao = " AND ";
        } else if (dataCertificadoInicial != null) {
            query += condicao + " m.certificado.dataCadastro >= :dataCertificadoInicial";
            condicao = " AND ";
        } else if (dataCertificadoFinal != null) {
            query += condicao + " m.certificado.dataCadastro <= :dataCertificadoFinal";
            condicao = " AND ";
        }
        if (dataLiberacaoInicial != null && dataLiberacaoFinal != null) {
            query += condicao + " m.dataLiberacao BETWEEN :dataLiberacaoInicial AND :dataLiberacaoFinal";
            condicao = " AND ";
        } else if (dataLiberacaoInicial != null) {
            query += condicao + " m.dataLiberacao >= :dataLiberacaoInicial";
            condicao = " AND ";
        } else if (dataLiberacaoFinal != null) {
            query += condicao + " m.dataLiberacao <= :dataLiberacaoFinal";
        }
        if (estado != null) {
            query += condicao + " m.aluno.estado = :estado";
            condicao = " AND ";
        }
        if (status != null ){
            query += condicao + " m.status = :status";
            condicao = " AND ";
        }
        var q = entityManager.createQuery(query, Matricula.class);
        setParameterIfNotNull(q, "tipoCurso", convertTipoCurso(tipoCurso));
        if (categoria != null) {
            setParameterIfNotNull(q, "categoria", categoriaService.listarById(categoria));
        }
        if (curso != null) {
            setParameterIfNotNull(q, "curso", cursoService.listarById(curso));
        }
        if (parceiro != null) {
            setParameterIfNotNull(q, "parceiro", parceiroService.listarById(parceiro));
        }
        setParameterIfNotNull(q, "dataCadastroInicial", dataCadastroInicial);
        setParameterIfNotNull(q, "dataCadastroFinal", dataCadastroFinal);
        setParameterIfNotNull(q, "dataLiberacaoInicial", dataLiberacaoInicial);
        setParameterIfNotNull(q, "dataLiberacaoFinal", dataLiberacaoFinal);
        setParameterIfNotNull(q, "dataCertificadoInicial", dataCertificadoInicial);
        setParameterIfNotNull(q, "dataCertificadoFinal", dataCertificadoFinal);
        setParameterIfNotNull(q, "estado", estado);
        if (status != null) {
            q.setParameter("status", StatusAvaliacaoMatricula.toEnum(status));
        }
        return q.getResultList();
    }

    public List<Matricula> findAllByAluno(Aluno aluno) {
            return repository.findAllByAluno(aluno);
    }

    public List<Matricula> listar() {
        return repository.findAll();
    }

    public Boolean existsByPedido(Pedido pedido) {
            return repository.existsByPedido(pedido);
    }

    public List<Matricula> listarBySearch(Integer page, Integer size, Integer idAfiliado) {
        String query = "SELECT m FROM Matricula m ";
        String condicao = "WHERE";
        if (idAfiliado != null) {
            query += condicao + " m.afiliado.id = :idAfiliado";
            condicao = " and ";
        }
        query += " ORDER BY m.dataMatricula DESC";
        var q = entityManager.createQuery(query, Matricula.class);
        if (idAfiliado != null) {
            q.setParameter("idAfiliado", idAfiliado);
        };
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        // Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataMatricula"));
        // if (idAfiliado != null) {
        //     return repository.findAllByAfiliado(afiliadoService.listarById(idAfiliado), LocalDateTime.now().getMonth().getValue() , pageable);
        // }
        return q.getResultList();
    }

    public List<Matricula> vendasByAfiliado(Integer page, Integer size, Integer ano, Integer mes, Integer idAfiliado) {
        String query = "SELECT m FROM Matricula m ";
        String condicao = " WHERE ";

        if (idAfiliado != null) {
            query += condicao + " m.afiliado.id = :idAfiliado";
            condicao = " AND ";
        }
        if (ano != null) {
            query += condicao + " YEAR(m.pedido.dataPedido) = :ano";
            condicao = " AND ";
        }
        if (mes != null) {
            query += condicao + " MONTH(m.pedido.dataPedido) = :mes";
            condicao = " AND ";
        }
        if (mes == null && ano == null) {
            query += " AND MONTH(m.pedido.dataPedido) = :mes AND YEAR(m.pedido.dataPedido) = :year";
        }
        System.out.println(query);
        var q = entityManager.createQuery(query, Matricula.class);
        
        if (idAfiliado != null) {
            q.setParameter("idAfiliado", idAfiliado);
        }
        if (ano != null) {
            q.setParameter("ano", ano);
        } else {
            q.setParameter("year", LocalDateTime.now().getYear());
        }
        if (mes != null) {
            q.setParameter("mes", mes);
        } else {
            q.setParameter("mes", LocalDateTime.now().getMonth().getValue());
        }
        q.setFirstResult(page * size);
        q.setMaxResults(size);
        return q.getResultList();
    }

    public List<MatriculaMes> listarByMesCurso(Integer afiliado) {
        List<MatriculaMes> matriculasMes = new ArrayList<MatriculaMes>();

        List<Integer> mesesNumericos = List.of(01, 02, 03, 04, 05, 06, 07, 8 , 9, 10, 11, 12);
        for (Integer mes : mesesNumericos ) {
            MatriculaMes matriculaMes = new MatriculaMes();
            matriculaMes.setMes(mes);
            matriculaMes.setTotal(repository.matriculaMesAfiliadoCursoLivre(mes, LocalDate.now().getYear(), afiliadoService.listarById(afiliado)));
            matriculaMes.setTotalCursoLivre(repository.matriculaMesAfiliadoPosGraduacao(mes, LocalDate.now().getYear(), afiliadoService.listarById(afiliado)));
            matriculasMes.add(matriculaMes);
        }
        return matriculasMes;
    }

    public Matricula listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ErrorException("Não foi possivel encontrar a matrícula"));
    }

    public List<Matricula> listarByAluno(Integer id, Integer page) {
        Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "dataMatricula"));
        return repository.findAllByAluno(alunoService.listarById(id), pageable);
    }


    public List<MatriculaMes> listarMatriculaMes() {
        List<MatriculaMes> matriculasMes = new ArrayList<MatriculaMes>();
        List<Integer> mesesNumericos = List.of(01, 02, 03, 04, 05, 06, 07, 8 , 9, 10, 11, 12);
        for (Integer mes : mesesNumericos ) {
            MatriculaMes matriculaMes = new MatriculaMes();
            matriculaMes.setMes(mes);
            matriculaMes.setTotal(repository.matriculaMes(mes, LocalDateTime.now().getYear()));
            matriculasMes.add(matriculaMes);
        }
        return matriculasMes;
    }

    public List<MatriculaMes> listarMatriculaMesAfiliado(Integer afiliado) {
        List<MatriculaMes> matriculasMes = new ArrayList<MatriculaMes>();
        List<Integer> mesesNumericos = List.of(01, 02, 03, 04, 05, 06, 07, 8 , 9, 10, 11, 12);
        for (Integer mes : mesesNumericos ) {
            MatriculaMes matriculaMes = new MatriculaMes();
            matriculaMes.setMes(mes);
            matriculaMes.setTotal(repository.matriculaMesAfiliado(mes, LocalDate.now().getYear(), afiliadoService.listarById(afiliado)));
            matriculasMes.add(matriculaMes);
        }
        return matriculasMes;
    }

    public List<Curso> cursosMaisVendidos() {
        System.out.println(repository.findTop10CursosMaisVendidos());
        return repository.findTop10CursosMaisVendidos();
    }

    public Matricula salvar(Matricula matricula) {
        if(matricula.getDataLiberacao() != null) {
            matricula.setDataLiberacao(LocalDateTime.now());
        }
        buscarExternos(matricula);
        completarCampos(matricula);
        return repository.save(matricula);
    }

    public Matricula alterar(Integer id, Matricula matricula) {
        Matricula matriculaExistente = listarById(id);
        if (matriculaExistente != null) {
            matricula.setId(id);
            buscarExternos(matriculaExistente);
            completarCampos(matriculaExistente);
            return repository.save(matricula);
        }
        return null;
    }

    public void deletar(Integer id) {
        Matricula matricula = listarById(id);
        if (matricula != null) { 
            repository.delete(matricula);
        }
    }

    public List<Matricula> listarByAfiliado(Integer id) {
        return repository.findAllByAfiliado(afiliadoRepository.findById(id).get());
    }

    public void create(Matricula matricula) {
        repository.save(matricula);
    }

    public List<Matricula> listarByPedido(Integer id) {
        return this.repository.findAllByPedido(pedidoService.listarById(id));
    }

    public byte[] gerarCertificadoByMatricula(Integer id) {
        return certificadoService.gerar(listarById(id));
    }

    public Matricula atualizarStatus(Integer id, StatusAvaliacaoMatricula statusAvaliacaoMatricula) {
        Matricula matricula = listarById(id);
        if (certificadoService.findByMatricula(id) != null) {
            certificadoService.deletar(matricula);
        }
        matricula.setStatus(statusAvaliacaoMatricula);
        if (matricula.getStatus().equals(StatusAvaliacaoMatricula.CANCELADO)) {
            Aluno aluno = alunoService.listarById(matricula.getAluno().getId());
            Integer horas = aluno.getHorasDisponiveis() + matricula.getCurso().getCargaHoraria();
            if (horas > 360) {
                aluno.setHorasDisponiveis(361);
            } else {
                aluno.setHorasDisponiveis(horas);
            }
            alunoService.save(aluno);
        }
        return repository.save(matricula);
    }

    public Matricula atualizarDataLiberacao(Integer id, LocalDateTime dataLiberacao) {
        Matricula matricula = listarById(id);
        LocalDateTime dataLiberacaoAtual = matricula.getDataMatricula();
        matricula.setDataLiberacao(dataLiberacao);
        Aluno aluno = alunoService.listarById(matricula.getAluno().getId());
        aluno.setObservacao(aluno.getObservacao() + "\n Data de liberação " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataLiberacaoAtual) + " alterada para " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(matricula.getDataLiberacao()) + "<br>");
        alunoService.alterar(aluno.getId(), aluno);
        return repository.save(matricula);
    }

    public Matricula atualizarDataMatricula(Integer id, LocalDateTime dataMatricula) {
        Matricula matricula = listarById(id);
        LocalDateTime dataMatriculaAtual = matricula.getDataMatricula();
        matricula.setDataMatricula(dataMatricula);
        Aluno aluno = alunoService.listarById(matricula.getAluno().getId());
        aluno.setObservacao(aluno.getObservacao() + "\n Data de matricula " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataMatriculaAtual) + " alterada para " + DateTimeFormatter.ofPattern("dd/MM/yyyy").format(matricula.getDataMatricula()) + "<br>");
        alunoService.alterar(aluno.getId(), aluno);
        return repository.save(matricula);
    }


    public List<Matricula> listarByMes(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataMatricula"));
        return repository.listarByMes(LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear(), pageable);
    }

    public void finalizarAvaliacao(AlunoAvaliacao alunoAvaliacao, Integer matricula, Integer idUsuario) {
        Matricula matriculaExistente = listarById(matricula);
        matriculaExistente.setAcertos(alunoAvaliacao.getAcertos());
        if (alunoAvaliacao.isAprovado()) {
            certificadoService.salvarByMatricula(matriculaExistente, idUsuario);
            matriculaExistente.setStatus(StatusAvaliacaoMatricula.APROVADO);
        } else {
            matriculaExistente.setStatus(StatusAvaliacaoMatricula.REPROVADO);
        }
        matriculaExistente.setNota(alunoAvaliacao.getNota());
        Matricula result = repository.save(matriculaExistente);
    }

    public void alterarCurso(Integer id, Integer idCurso, Integer idUsuario) {
        try {
            Matricula matricula = listarById(id);
            Curso curso = cursoService.listarById(idCurso);
            matricula.setCurso(curso);
            Matricula result = repository.save(matricula);
            criarMatriculaLogs(idUsuario, result);
        } catch(Exception e) {
            throw new ErrorException("Erro ao alterar curso de matrícula");
        }
    }

    private void criarMatriculaLogs(Integer id, Matricula result) {
        if (result != null) {
            User user = userRepository.findById(id).get();
            MatriculaLogs log = new MatriculaLogs();
            log.setData(LocalDate.now());
            log.setDescricao("Curso alterado pelo usuário: " + user.getFirstname());
            log.setMatricula(result);
            log.setUsuario(user);
            matriculaLogsService.salvar(log);
        }
    }
}
