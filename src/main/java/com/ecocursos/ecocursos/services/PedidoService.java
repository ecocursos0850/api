package com.ecocursos.ecocursos.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.EntradaCaixa;
import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.Pedido;
import com.ecocursos.ecocursos.models.PedidoPosGraduacaoPortal;
import com.ecocursos.ecocursos.models.dtos.FaturamentoTotal;
import com.ecocursos.ecocursos.models.dtos.PedidosPorEstado;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.models.enums.StatusAvaliacaoMatricula;
import com.ecocursos.ecocursos.models.enums.StatusMatricula;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.models.enums.TipoEntradaCaixa;
import com.ecocursos.ecocursos.models.enums.TipoPagamento;
import com.ecocursos.ecocursos.repositories.DeclaracaoMatriculaRepository;
import com.ecocursos.ecocursos.repositories.PedidoPosGraduacaoPortalRepository;
import com.ecocursos.ecocursos.repositories.PedidoRepository;
import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private VariavelGlobalService variavelGlobalService;

    @Autowired
    private EntradaCaixaService entradaCaixaService;

    @Autowired
    private DeclaracaoMatriculaRepository declaracaoMatriculaRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AsaasService asaasService;

    @Autowired
    private PedidoPosGraduacaoPortalRepository pedidoPosGraduacaoPortalRepository;

    private void gerarPedidoPosGraduacaoPortal(Pedido pedido) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal = new PedidoPosGraduacaoPortal();
            pedidoPosGraduacaoPortal.setUrlPortal("https://unica.portalprominas.com.br/tportal");
            pedidoPosGraduacaoPortal.setLogin(pedido.getAluno().getCpf());
            pedidoPosGraduacaoPortal.setSenha(dateTimeFormatter.format(pedido.getAluno().getDataNascimento()));
            pedidoPosGraduacaoPortal.setStatus(Status.INATIVO);
            pedidoPosGraduacaoPortal.setPedido(pedido);
            pedidoPosGraduacaoPortalRepository.save(pedidoPosGraduacaoPortal);
        } catch (Exception e) {
            log.error("Erro ao gerar login de pós-graduação");
        }
    }

    private void cancelarCobranchaAsaas(String referencia) {
        asaasService.desativar("payments/" + referencia);
    }

    public List<Pedido> listar() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "dataPedido"));
    }

    public List<Pedido> listar25Ultimos() {
        return repository.listar25Ultimos();
    }

    public List<Pedido> filtro(
            Integer status,
            Integer tipoCurso,
            String aluno,
            LocalDateTime periodoInicial,
            LocalDateTime periodoFinal,
            Integer page
    ) {
        String jpql = "select p from Pedido p ";
        String condicao = "where";
        if (status != null) {
            jpql += condicao + " p.status = :status";
            condicao = " and ";
        }

        if (tipoCurso != null) {
            jpql += condicao + " p.tipoCurso = :tipoCurso";
            condicao = " and ";
        }

        if (aluno != null) {
            jpql += condicao + " p.aluno.nome LIKE CONCAT(:nome, '%')";
            condicao = " and ";
        }

        if (periodoInicial != null && periodoFinal != null) {
            jpql += condicao + " p.dataPedido BETWEEN :periodoInicial AND :periodoFinal";
        } else if (periodoInicial != null) {
            jpql += condicao + " p.dataPedido >= :periodoInicial";
        } else if (periodoFinal != null) {
            jpql += condicao + " p.dataPedido <= :periodoFinal";
        }

        jpql += " ORDER BY p.dataPedido DESC ";

        var query = entityManager.createQuery(jpql, Pedido.class);

        if (status != null) {
            query.setParameter("status", StatusPedido.toEnum(status));
        }

        if (tipoCurso != null) {
            query.setParameter("tipoCurso", TipoCurso.toEnum(tipoCurso));
        }

        if (aluno != null) {
            query.setParameter("nome", aluno);
        }

        if (periodoInicial != null) {
            query.setParameter("periodoInicial", periodoInicial);
        }

        if (periodoFinal != null) {
            query.setParameter("periodoFinal", periodoFinal);
        }
        query.setFirstResult(page * 25);
        query.setMaxResults(25);
        return query.getResultList();
    }

    public List<Pedido> listarByRelatorio(
            Integer aluno,
            Integer tipoPagamento,
            Integer tipoCheckout,
            LocalDateTime periodoInicial,
            LocalDateTime periodoFinal,
            Integer mes,
            Integer ano,
            Integer status
    ) {
        String query = "SELECT  p FROM Pedido p";
        String condicao = " WHERE ";

        if (aluno != null) {
            query += condicao + " p.aluno = :aluno";
            condicao = " AND ";
        }
        if (tipoPagamento != null) {
            query += condicao + " p.tipoPagamentos IN :tipoPagamento";
            condicao = " AND ";
        }
        if (tipoCheckout != null) {
            query += condicao + " p.tipoCurso = :tipoCheckout";
            condicao = " AND ";
        }
        if (periodoInicial != null && periodoFinal != null) {
            query += condicao + " p.dataPedido BETWEEN :periodoInicial AND :periodoFinal";
            condicao = " AND ";
        } else if (periodoInicial != null) {
            query += condicao + " p.dataPedido >= :periodoInicial";
            condicao = " AND ";
        } else if (periodoFinal != null) {
            query += condicao + " p.dataPedido <= :periodoFinal";
            condicao = " AND ";
        }
        if (mes != null) {
            query += condicao + " MONTH(p.dataPedido) = :mes";
            condicao = " AND ";
        }
        if (ano != null) {
            query += condicao + " YEAR(p.dataPedido) = :ano";
            condicao = " AND ";
        }
        if (status != null) {
            query += condicao + " p.status = :status";
            condicao = " AND ";
        }
        var q = entityManager.createQuery(query, Pedido.class);
        if (aluno != null) {
            q.setParameter("aluno", alunoService.listarById(aluno));
        }
        if (tipoPagamento != null) {

            q.setParameter("tipoPagamento", List.of(tipoPagamento));
        }
        if (tipoCheckout != null) {
            q.setParameter("tipoCheckout", TipoCurso.toEnum(tipoCheckout));
        }
        if (periodoInicial != null) {
            q.setParameter("periodoInicial", periodoInicial);
        }
        if (periodoFinal != null) {
            q.setParameter("periodoFinal", periodoFinal);
        }
        if (status != null) {
            q.setParameter("status", StatusPedido.toEnum(status));
        }
        return q.getResultList();
    }

    public List<FaturamentoTotal> faturamentoTotal() {
        List<FaturamentoTotal> faturamentosTotais = new ArrayList<>();
        List<Integer> mesesNumericos = List.of(01, 02, 03, 04, 05, 06, 07, 8, 9, 10, 11, 12);
        for (Integer mes : mesesNumericos) {
            FaturamentoTotal faturamentoTotal = new FaturamentoTotal();
            faturamentoTotal.setMes(mes);
            Double valorDeclaracao = declaracaoMatriculaRepository.valorTotalMesDeclaracao(mes, LocalDateTime.now().getYear());
            Double valorTotalMes;
            if (valorDeclaracao != null) {
                if (repository.pegarValorTotalMes(mes, LocalDateTime.now().getYear()) != null) {
                    Double valor = repository.pegarValorTotalMes(mes, LocalDateTime.now().getYear());
                    valorTotalMes = valor + valorDeclaracao;
                } else {
                    valorTotalMes = valorDeclaracao;
                }   
                faturamentoTotal.setTotalDeclaracao(valorDeclaracao);
            } else {
                valorTotalMes = repository.pegarValorTotalMes(mes, LocalDateTime.now().getYear());
            }
            faturamentoTotal.setValorTotal(valorTotalMes);
            faturamentoTotal.setTotalSite(repository.pegarValorTotalMesSite(mes, LocalDateTime.now().getYear()));
            faturamentoTotal.setQuantidade(repository.pegarQuantidadeMes(mes, LocalDateTime.now().getYear()));
            faturamentosTotais.add(faturamentoTotal);
        }
        return faturamentosTotais;
    }

    public List<PedidosPorEstado> pedidosPorEstados() {
        List<PedidosPorEstado> pedidossPorEstads = new ArrayList<>();
        List<String> siglasEstados = new ArrayList<>(Arrays.asList(
                "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS",
                "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC",
                "SP", "SE", "TO"
        ));
        for (String estado : siglasEstados) {
            PedidosPorEstado pedidosPorEstado = new PedidosPorEstado();
            pedidosPorEstado.setSigla(estado);
            pedidosPorEstado.setQuantidade(repository.pegarQuantidadePorEstado(estado));
            pedidossPorEstads.add(pedidosPorEstado);
        }
        return pedidossPorEstads;
    }

    private void verificarCadastroAlunoAsaas(Aluno aluno) {
        try {
            if (aluno.getReferencia() == null || !aluno.getReferencia().contains("cus")) {
                alunoService.criarAlunoAsaas(aluno);
            }
        } catch (Exception e) {
            throw new ErrorException("Erro ao criar aluno no ASAAS atráves de pedido");
        }
    }

    public void cancelarPedido(Integer id) {
        try {
            Pedido pedido = listarById(id);
            pedido.setStatus(StatusPedido.CANCELADO);
            if (pedido.getReferencia() != null) {
                cancelarCobranchaAsaas(pedido.getReferencia());
            }
            repository.save(pedido);
        } catch(Exception e) {
            throw new ErrorException("Erro no método de cancelar pedido");
        }
    }

    public List<Pedido> listarByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataPedido"));
        return repository.findAll(pageable).toList();
    }

    public Pedido listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Pedido salvar(Pedido pedido) {
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setAluno(alunoService.listarById(pedido.getAluno().getId()));
       if (repository.findAllByAluno(pedido.getAluno()).stream().filter(x -> (x.getStatus().equals(StatusPedido.PENDENTE) || x.getStatus().equals(StatusPedido.EM_ANALISE)) && x.getDataPedido().isAfter(pedido.getDataPedido().minusDays(7))).count() >= 1) {
            throw new ErrorException("Você possui pedidos pendentes no sistema realizados há menos de 2 dias. Entre em contato conosco.");
        }
        buscarCursos(pedido.getCursos());
        atualizarValorTotal(pedido);
        totalCargaHorariaTotal(pedido);
        pedido.setQtdItens(pedido.getCursos().size());
        if (pedido.getDescontos() == null) {
            pedido.setDescontos(0D);
        }
        Pedido result = repository.save(pedido);
        List<TipoPagamento> gratuito = result.getTipoPagamentos().stream().filter(x -> x.getCodigo() == 0).toList();
        if (gratuito.isEmpty()) {
            criarPedidoAsaas(result);
        } else {
            result.setStatus(StatusPedido.PAGO);
            result.setTotal(0D);
            result.getCursos().forEach(x -> {
                if (x.getCategoria().getTitulo().equals("DIREITO ONLINE")) {
                    Matricula matricula = new Matricula();
                    matricula.setPedido(repository.findById(result.getId()).get());
                    matricula.setCurso(x);
                    matricula.setAluno(result.getAluno());
                    matricula.setStatus(StatusAvaliacaoMatricula.LIBERADO_PARCEIRO);
                    matricula.setDataMatricula(LocalDateTime.now());
                    matricula.setDataLiberacao(LocalDateTime.now());
                    alunoService.atualizarHorasDisponiveis(result);
                    matriculaService.salvar(matricula);
                }
            });
        }
        result.getCursos().forEach(curso -> {
            if (curso.getCategoria().getTitulo().equals("PÓS-GRADUAÇÃO / MBA") && curso.getTipoCurso().equals(TipoCurso.TAXA_MATRICULA)) {
                if (curso.getTipoCurso().equals(TipoCurso.TAXA_MATRICULA)) {
                    gerarPedidoPosGraduacaoPortal(result);
                }
            }
        });
        return repository.save(result);
    }

    private void criarPedidoAsaas(Pedido result) {
        try {
            verificarCadastroAlunoAsaas(alunoService.findByEmail(result.getAluno().getEmail()));
            JsonObject object = asaasService.save("payments", Pedido.criarPedidoAsaas(result));
            if (object != null) {
                result.setLinkPagamento(object.get("invoiceUrl").getAsString());
                result.setReferencia(object.get("id").getAsString());
                result.setStatus(converterStatus(result, object));
            }
        } catch(Exception e) {
            throw new ErrorException("Erro ao criar pedido no ASAAS");
        }
    }

    public Pedido  salvarPedidoPorMatricula(Pedido pedido) {
        Pedido result = repository.save(pedido);
        if (pedido.getStatus() != StatusPedido.PAGO) {
            if (result != null) {
                criarPedidoAsaas(result);
            }
        }
        return result;
    }

    private void atualizarValorTotal(Pedido pedido) {
        if (pedido.getDescontos() != null) {
            pedido.setTotal(pedido.getSubtotal() - pedido.getDescontos());
        } else {
            pedido.setTotal(pedido.getSubtotal());
        }
        if (pedido.getIsento() == StatusMatricula.NAO_ISENTO) {
            pedido.setTotal(pedido.getTotal() + pedido.getTaxaMatricula());
        }
    }

    private void totalCargaHorariaTotal(Pedido pedido) {
        pedido.setCargaHorariaTotal(pedido.getCursos().stream().mapToInt(Curso::getCargaHoraria).sum());
    }

    private void buscarCursos(List<Curso> cursos) {
        cursos.stream().map(x -> {
            Curso curso = cursoService.listarById(x.getId());
            x.setId(x.getId());
            x.setTitulo(curso.getTitulo());
            x.setCategoria(curso.getCategoria());
            x.setCargaHoraria(curso.getCargaHoraria());
            x.setPreco(curso.getPreco());
            x.setTipoCurso(curso.getTipoCurso());
            return x;
        }).collect(Collectors.toList());
    }

    public Pedido alterar(Integer id, Pedido pedido) {
        Pedido pedidoExistente = listarById(id);
        if (pedidoExistente != null) {
            pedido.setId(pedidoExistente.getId());
            return repository.save(pedido);
        }
        return null;
    }

    private StatusPedido converterStatus(Pedido pedido, JsonObject object) {
        try {
            StatusPedido statusPedido = StatusPedido.PENDENTE;
            if (object.get("status").getAsString().equals("CONFIRMED") || object.get("status").getAsString().equals("RECEIVED")) {
                statusPedido = StatusPedido.PAGO;
            }
            if (object.get("status").getAsString().equals("PENDING")) {
                statusPedido = StatusPedido.PENDENTE;
            }
            if (object.get("status").getAsString().equals("canceled")) {
                statusPedido = StatusPedido.CANCELADO;
            }
            if (object.get("status").getAsString().equals("REFUNDED")) {
                statusPedido = StatusPedido.REEMBOLSADO;
            }
            if (object.get("status").getAsString().equals("OVERDUE")) {
                statusPedido = StatusPedido.EXPIRADO;
            }
            return statusPedido;
        } catch (Exception e) {
            log.error("Erro na atualização de status no método 'converterStatus' Pedido " + pedido.getId());
        }
        return null;
    }

    public void deletar(Integer id) {
        Pedido pedido = listarById(id);
        if (pedido != null) {
            repository.delete(pedido);
        }
    }

    public void atualizarPagamento(String referencia) {
        Pedido pedido = repository.findByReferencia(referencia);
        if (pedido != null) {
            log.info("Pedido: " + pedido.getId() + " sendo capturado");
            try {
                atualizarStatusCobrancaAsaas(referencia, pedido);
                repository.save(pedido);
            } catch (Exception e) {
                log.error("Erro ao atualizar status de pagamento de pedido");
            }
        }
    }

    public void atualizatStatusPagamentoWebhook(String referencia, String status) {
        try {
            Pedido pedido = repository.findByReferencia(referencia);
            StatusPedido statusPedido = StatusPedido.PENDENTE;
            if (status.equals("CONFIRMED") || status.equals("RECEIVED")) {
                statusPedido = StatusPedido.PAGO;
            }
            if (status.equals("PENDING")) {
                statusPedido = StatusPedido.PENDENTE;
            }
            if (status.equals("canceled")) {
                statusPedido = StatusPedido.CANCELADO;
            }
            if (status.equals("REFUNDED")) {
                statusPedido = StatusPedido.REEMBOLSADO;
            }
            if (status.equals("OVERDUE")) {
                statusPedido = StatusPedido.EXPIRADO;
            }
            pedido.setStatus(statusPedido);
            repository.save(pedido);
        } catch(Exception e) {
            throw new ErrorException("Erro ao atualizar status de cobrança do ASAAS");
        }
    }

    private void atualizarStatusCobrancaAsaas(String referencia, Pedido pedido) {
        JsonObject object = asaasService.getBy(String.format("payments/%s/status", referencia));
        pedido.setStatus(converterStatus(pedido, object));
        if (pedido.getStatus() == StatusPedido.PAGO) {
            if (!matriculaService.existsByPedido(pedido)) {
                criarMatriculaByPedido(pedido.getId());
                EntradaCaixa entradaCaixa = new EntradaCaixa();
                entradaCaixa.setDataPagamento(LocalDateTime.now());
                entradaCaixa.setTipoPagamento(pedido.getTipoPagamentos().get(0));
                entradaCaixa.setTipoEntradaCaixa(TipoEntradaCaixa.SITE);
                entradaCaixa.setCategoria(pedido.getCursos().get(0).getCategoria());
                entradaCaixa.setValor(pedido.getTotal());
                entradaCaixaService.salvar(entradaCaixa);
            }
        }
    }

    public void atualizarFaturaEmAnalise() {
        List<Pedido> pedidos = listar25Ultimos();
        pedidos.forEach(pedido -> {
            if (pedido != null && pedido.getLinkPagamento() != null && pedido.getReferencia() != null) {
                log.info("Pedido: " + pedido.getId() + " sendo capturado");
                try {
                    capturarCobrancaAsaas(pedido);
                    repository.save(pedido);
                } catch (Exception e) {
                    log.error("Erro ao atualizar status de pedido com cartão de crédito");
                }
                return ;
            }
        });
    }

    private void capturarCobrancaAsaas(Pedido pedido) {
        JsonObject object = asaasService.save(String.format("payments/%s/captureAuthorized", pedido.getReferencia()), new Object());
        pedido.setStatus(converterStatus(pedido, object));
        if (pedido.getStatus() == StatusPedido.PAGO) {
            if (!matriculaService.existsByPedido(pedido)) {
                criarMatriculaByPedido(pedido.getId());
                EntradaCaixa entradaCaixa = new EntradaCaixa();
                entradaCaixa.setDataPagamento(LocalDateTime.now());
                entradaCaixa.setTipoPagamento(pedido.getTipoPagamentos().get(0));
                entradaCaixa.setTipoEntradaCaixa(TipoEntradaCaixa.SITE);
                entradaCaixa.setCategoria(pedido.getCursos().get(0).getCategoria());
                entradaCaixa.setValor(pedido.getTotal());
                entradaCaixaService.salvar(entradaCaixa);
            }
        }
    }

    public void atualizarStatusPagamentos() {
        List<Pedido> pedidos = repository.listar25UltimosSemFiltro();
        pedidos.stream().forEach(x -> {
            try {
                atualizarPagamento(x.getReferencia());
                matriculaService.listarByPedido(x.getId()).stream()
                        .forEach(matricula -> {
                            if (x.getStatus() == StatusPedido.PAGO && x.getReferencia() != null) {
                                matricula.setStatus(StatusAvaliacaoMatricula.LIBERADO_PAGO);
                                matriculaService.alterar(matricula.getId(), matricula);
                                if (matricula.getCurso().getCategoria().getTitulo().equals("PÓS-GRADUAÇÃO / MBA")) {
                                    EntradaCaixa entradaCaixa = new EntradaCaixa();
                                    entradaCaixa.setDataPagamento(LocalDateTime.now());
                                    entradaCaixa.setTipoPagamento(x.getTipoPagamentos().get(0));
                                    entradaCaixa.setTipoEntradaCaixa(TipoEntradaCaixa.SITE);
                                    entradaCaixa.setCategoria(matricula.getCurso().getCategoria());
                                    entradaCaixa.setValor(x.getTotal() - Double.parseDouble(variavelGlobalService.listarByChave("Repasse Pós-Graduação").getValor()));
                                    entradaCaixaService.salvar(entradaCaixa);
                                } else {
                                    EntradaCaixa entradaCaixa = new EntradaCaixa();
                                    entradaCaixa.setDataPagamento(LocalDateTime.now());
                                    entradaCaixa.setTipoPagamento(x.getTipoPagamentos().get(0));
                                    entradaCaixa.setTipoEntradaCaixa(TipoEntradaCaixa.SITE);
                                    entradaCaixa.setCategoria(matricula.getCurso().getCategoria());
                                    entradaCaixa.setValor(x.getTotal());
                                    entradaCaixaService.salvar(entradaCaixa);
                                }
                            }
                        });

            } catch (Exception e) {
                log.error("Erro ao atualizar status de pagamento, PEDIDO " + x.getId());
            }
        });
    }

    public List<Pedido> listarBySearch(StatusPedido status, TipoCurso tipoCurso) {
        Set<Pedido> pedidos = new HashSet<>();
        if (status != null) {
            pedidos.addAll(repository.findAllByStatus(status));
        }
        if (tipoCurso != null) {
            pedidos.addAll(repository.findAllByTipoCurso(tipoCurso));
        }
        return pedidos.stream().toList();
    }

    public void criarMatriculaByPedido(Integer id) {
        Pedido pedido = listarById(id);
        if (pedido != null) {
            pedido.getCursos().stream().forEach(x -> {
                Matricula matricula = new Matricula();
                matricula.setAluno(pedido.getAluno());
                matricula.setValorMatricula(pedido.getTaxaMatricula());
                matricula.setDataMatricula(LocalDateTime.now());
                matricula.setDataLiberacao(LocalDateTime.now());
                matricula.setCurso(x);
                matricula.setValorCurso(pedido.getTotal());
                matricula.setObservacao("Gerado pelo pedido: " + pedido.getId());
                matricula.setPedido(pedido);
                matriculaService.salvar(matricula);
            });
        }
    }


    public List<Pedido> listarByAluno(Integer id) {
        return repository.findAllByAluno(alunoService.listarById(id));
    }

    @SneakyThrows
    public Map<String, String> generateDirectBilling(Map<String, Object> map) {
        try {
            Map<String, Object> body = new HashMap<String, Object>();
            Map<String, String> pdf = new HashMap<>();
            if (map.get("method").equals("bank_slip")) {
                JsonObject object = asaasService.getBy(String.format("payments/%s", map.get("invoice_id").toString()));
                pdf.put("pdf", object.get("invoiceUrl").getAsString());
                return pdf;
            }
            body.put("creditCard", criarCartaoCredito((Map<String, Object>) map.get("infoCard")));
            body.put("creditCardHolderInfo", criarDadosTitularCartao(repository.findByReferencia(map.get("invoice_id").toString())));
            asaasService.save("/payments/" + map.get("invoice_id") + "/payWithCreditCard", body);
            atualizarPagamento(map.get("invoice_id").toString());
            JsonObject object = asaasService.getBy(String.format("payments/%s", map.get("invoice_id").toString()));
            pdf.put("pdf", object.get("invoiceUrl").toString());
            return pdf;
        } catch (Exception e) {
            throw new ErrorException("Erro ao gerar cobrança no cartão de crédito");
        }
    }

    private Object criarDadosTitularCartao(Pedido pedido) {
        Map<String, Object> map = new HashMap<>();
        map.put("custumerId", pedido.getAluno().getReferencia());
        map.put("name", pedido.getAluno().getNome());
        map.put("email", pedido.getAluno().getEmail());
        map.put("cpfCnpj", pedido.getAluno().getCpf());
        map.put("postalCode", pedido.getAluno().getCep());
        map.put("addressNumber", pedido.getAluno().getNumero());
        map.put("phone", pedido.getAluno().getCelular());
        return map;
    }

    private Map<String, Object> criarCartaoCredito(Map<String, Object> cartaoCredito) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", cartaoCredito.get("number"));
        map.put("expiryMonth", cartaoCredito.get("month"));
        map.put("expiryYear", cartaoCredito.get("year"));
        map.put("ccv", cartaoCredito.get("verification_value"));
        map.put("holderName", cartaoCredito.get("first_name"));
        return map;
    }

}
