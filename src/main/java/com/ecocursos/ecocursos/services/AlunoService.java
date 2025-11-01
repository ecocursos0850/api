package com.ecocursos.ecocursos.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ecocursos.ecocursos.models.*;
import com.ecocursos.ecocursos.models.dtos.AlunoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.repositories.AlunoRepository;
import com.ecocursos.ecocursos.repositories.UserRepository;
import com.ecocursos.util.PasswordGenerator; // Importe a classe PasswordGenerator
import com.ecocursos.util.EmailUtil;
import com.google.gson.JsonObject;

import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AlunoService {

    private final AlunoRepository alunoRepository;

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private CpfParceiroService cpfParceiroService;

    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private AfiliadoService afiliadoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CupomDescontoService cupomDescontoService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private EntityManager em;

    @Autowired
    private AsaasService asaasService;

    @Autowired
    public AlunoService(AlunoRepository alunoRepository, UserRepository userRepository) {
        this.alunoRepository = alunoRepository;
        this.userRepository = userRepository;
    }

    public Aluno listarByEmail(String email) {
        return alunoRepository.findByEmail(email);
    }

    public List<Aluno> listar() {
        return repository.findAll();
    }

    public List<AlunoDTO> listarDTO() {
        return repository.listarDTO();
    }

    public List<Aluno> listarAniversariantes(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.listarAniversariantesMes(pageable);
    }

    public List<Aluno> listarByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataCadastro"));
        return repository.findAll(pageable).toList();
    }

    public Aluno salvar(Aluno aluno) {
        if (repository.existsByCpf(aluno.getCpf())) {
            throw new ErrorException("Já tem um aluno com o CPF cadastrado");
        }
        String senhaLimpa = aluno.getSenha();
        aluno.setDataCadastro(LocalDateTime.now());
        aluno.setHorasDisponiveis(361);
        verificarIdade(aluno);
        // verificarAlunoParceiro(aluno);
        aluno = verificarAlunoParceiro(aluno);
        aluno.setSenha(DigestUtils.md5DigestAsHex(aluno.getSenha().getBytes()));
        Aluno result = repository.save(aluno);
        // registrarAlunoIugu(aluno, result);
        registrarUsuarioLogin(senhaLimpa, result);
        criarAlunoAsaas(result);
        User user = userRepository.findByEmail(result.getEmail())
                .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
        result.setUsuario(user);
        repository.save(result);
        return result;
    }

    private void verificarIdade(Aluno aluno) {
        aluno.setIdade(String.valueOf(LocalDateTime.now().getYear() - aluno.getDataNascimento().getYear()));
    }

    private void registrarUsuarioLogin(String senhaLimpa, Aluno result) {
        if (result.getSenha() != null) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setId(result.getId());
            registerRequest.setPassword(senhaLimpa);
            registerRequest.setNome(result.getNome().split(" ")[0]);
            registerRequest.setRole(Role.USER);
            registerRequest.setEmail(result.getEmail());
            authenticationService.register(registerRequest);
        }
    }

    public void criarAlunoAsaas(Aluno aluno) {
        JsonObject object = asaasService.save("/customers", Aluno.convertToAsaas(aluno));
        aluno.setReferencia(object.get("id").getAsString());
        repository.saveReferencia(aluno.getReferencia(), aluno.getId());
    }

    private void verificarAlunoParceiro(Aluno aluno) {
        if (cpfParceiroService.existsByCpf(aluno.getCpf())) {
            Integer idParceiro = cpfParceiroService.listarByCpf(aluno.getCpf()).getParceiro().getId();
            aluno.setParceiro(parceiroService.listarById(idParceiro));
        } else if (aluno.getParceiro() != null) {
            aluno.setParceiro(parceiroService.listarById(aluno.getParceiro().getId()));
        }
    }

    /*private Aluno verificarAlunoParceiro(Aluno aluno) {
         if (cpfParceiroService.existsByCpf(aluno.getCpf())) {
             Integer idParceiro = cpfParceiroService.listarByCpf(aluno.getCpf()).getParceiro().getId();
             aluno.setParceiro(parceiroService.listarById(idParceiro));
             cpfParceiroService.deletarByCpf(aluno.getCpf().trim().replaceAll("[^0-9]", ""));
         } else if (aluno.getParceiro() != null) {
             aluno.setParceiro(parceiroService.listarById(aluno.getParceiro().getId()));
         }
         return aluno;
     }*/    

    public Aluno listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public Aluno findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Boolean existsByReferencia(String ref) {
        return repository.existsByReferencia(ref);
    }

    public List<Aluno> listarByFilter(
            String idParceiro,
            String sexo,
            String estado,
            String cpf,
            String nomeAluno,
            String email,
            String celular,
            Integer page
    ) {
        String query = "select a from Aluno a ";
        String condicao = "where";
        if (idParceiro != null) {
            query += condicao + " a.parceiro.id = :parceiro";
            condicao = " and ";
        }
        if (sexo != null) {
            query += condicao + " a.sexo = :sexo";
            condicao = " and ";
        }
        if (estado != null) {
            query += condicao + " a.estado = :estado";
            condicao = " and ";
        }
        if (cpf != null) {
            query += condicao + " a.cpf = :cpf";
            condicao = " and ";
        }
        if (nomeAluno != null) {
            query += condicao + " a.nome like CONCAT(:nomeAluno ,'%')";
            condicao = " and ";
        }
        if (celular != null) {
            query += condicao + " a.celular like CONCAT(:celular ,'%')";
            condicao = " and ";
        }
        if (email != null) {
            query += condicao + " a.email = :email";
            condicao = " and ";
        }

        var q = em.createQuery(query, Aluno.class);

        if (idParceiro != null) {
            q.setParameter("parceiro", idParceiro);
        }
        if (sexo != null) {
            q.setParameter("sexo", sexo);
        }
        if (estado != null) {
            q.setParameter("estado", estado);
        }
        if (cpf != null) {
            q.setParameter("cpf", cpf);
        }
        if (nomeAluno != null) {
            q.setParameter("nomeAluno", nomeAluno);
        }
        if (celular != null) {
            q.setParameter("celular", celular);
        }
        if (email != null) {
            q.setParameter("email", email);
        }
        q.setFirstResult(page * 25);
        q.setMaxResults(25);
        return q.getResultList();
    }

    public List<Aluno> listarByRelatorio(
            Integer status,
            String idParceiro,
            String sexo,
            String estado,
            LocalDateTime dataNascimento,
            LocalDateTime periodoInicial,
            LocalDateTime periodoFinal
    ) {
        String query = "select a from Aluno a ";
        String condicao = "where";
        
        if (status != null) {
            query += condicao + " a.status = :status";
            condicao = " and ";
        }
        if (idParceiro != null) {
            query += condicao + " a.parceiro.id = :parceiro";
            condicao = " and ";
        }
        if (dataNascimento != null) {
            query += condicao + " a.dataNascimento = :dataNascimento";
            condicao = " and "; // ADICIONAR ESTA LINHA
        }
        if (sexo != null) {
            query += condicao + " a.sexo = :sexo";
            condicao = " and ";
        }
        if (estado != null) {
            query += condicao + " a.estado = :estado";
            condicao = " and ";
        }
        if (periodoInicial != null && periodoFinal != null) {
            query += condicao + " a.dataCadastro BETWEEN :periodoInicial AND :periodoFinal";
        } else if (periodoInicial != null) {
            query += condicao + " a.dataCadastro >= :periodoInicial";
        } else if (periodoFinal != null) {
            query += condicao + " a.dataCadastro <= :periodoFinal";
        }

        var q = em.createQuery(query, Aluno.class);

        if (status != null) {
            q.setParameter("status", Status.toEnum(status));
        }
        if (idParceiro != null) {
            q.setParameter("parceiro", idParceiro);
        }
        if (dataNascimento != null) {
            q.setParameter("dataNascimento", dataNascimento);
        }
        if (sexo != null) {
            q.setParameter("sexo", sexo);
        }
        if (estado != null) {
            q.setParameter("estado", estado);
        }
        if (periodoInicial != null) {
            q.setParameter("periodoInicial", periodoInicial);
        }
        if (periodoFinal != null) {
            q.setParameter("periodoFinal", periodoFinal);
        }
        
        return q.getResultList();
    }    

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public Aluno alterarStatus(Integer id) {
        Aluno aluno = listarById(id);
        if (aluno.getStatus() == Status.ATIVO) {
            aluno.setStatus(Status.INATIVO);
        } else {
            aluno.setStatus(Status.ATIVO);
        }
        return repository.save(aluno);
    }

    public Aluno alterar(Integer id, Aluno aluno) {
        String senhaLimpa = aluno.getSenha();
        Aluno alunoExistente = listarById(id);
        String emailAntigo = alunoExistente.getEmail();
        aluno.setDataCadastro(alunoExistente.getDataCadastro());
        aluno.setHorasDisponiveis(alunoExistente.getHorasDisponiveis());
        if (senhaLimpa == "" || senhaLimpa == null) {
            aluno.setSenha(alunoExistente.getSenha());
        } else {
            aluno.setSenha(DigestUtils.md5DigestAsHex(aluno.getSenha().getBytes()).toString());
        }
        if (userRepository.existsByEmail(emailAntigo)) {
            User user = userRepository.findByEmail(emailAntigo).get();
            aluno.setUsuario(user);
            user.setEmail(aluno.getEmail());
            user.setPassword(aluno.getSenha());
            userRepository.save(user);
        }
        if (alunoExistente != null) {
            aluno.setId(alunoExistente.getId());
            if (aluno.getSenha() == null) {
                aluno.setSenha(alunoExistente.getSenha());
            }
            verificarAtualizarCpfParceiro(aluno);
            return repository.save(aluno);
        }
        verificarAtualizarCpfParceiro(aluno);
        return aluno;
    }

    private void verificarAtualizarCpfParceiro(Aluno aluno) {
        if (cpfParceiroService.existsByCpf(aluno.getCpf())) {
            CpfParceiro cpfParceiro = cpfParceiroService.listarByCpf(aluno.getCpf());
            cpfParceiro.setEmail(aluno.getEmail());
            cpfParceiro.setNome(aluno.getNome());
            cpfParceiro.setParceiro(aluno.getParceiro());
            aluno.setCpf(aluno.getCpf());
            cpfParceiroService.save(cpfParceiro);
        }
    }

    public void deletar(Integer id) {
        Aluno aluno = listarById(id);
        if (aluno != null && aluno.getPedidos().isEmpty()) {
            cpfParceiroService.deletarByCpf(aluno.getCpf());
            repository.delete(aluno);
        } else {
            throw new ErrorException("O aluno possui pedidos");
        }
    }

    public List<Aluno> listarBySearch(String nome, String email) {
        Set<Aluno> alunos = new HashSet<>();
        if (nome != null) {
            repository.findAllByNomeContainingIgnoreCase(nome).stream().forEach(x -> {
                alunos.add(x);
            });
        }
        if (email != null) {
            repository.findAllByEmailContainingIgnoreCase(email).stream().forEach(x -> {
                alunos.add(x);
            });
        }
        return alunos.stream().toList();
    }

    public List<Aluno> listarByAfiliado(Integer id) {
        return repository.findAllByAfiliado(afiliadoService.listarById(id));
    }

    public void verificarAniversarioAluno() {
        EmailUtil sender = new EmailUtil();
        listar().stream().forEach(x -> {
            if (fazAniversarioHoje(x)) {
                try {
                    log.info("Enviando email para aluno: " + x.getNome());
                    CupomDesconto cupomDesconto = cupomDescontoService.gerarCupomDescontoAniversario(x);
                        String assunto = "🎁 " + x.getNome() + ", Ecocursos quer te dar um presente!";
                        String destinatario = x.getEmail(); 
                        String nome = x.getNome();
                        String presente = cupomDesconto.getCodigo();
                        sender.happyBirthday(assunto, presente, nome, destinatario);
                    //sender.sender("Feliz aniversario, temos um presente para você", "Parabéns pelo aniversário e para comemorar essa data, estamos lhe oferecendo um cupom de desconto:" + cupomDesconto.getCodigo(), x.getEmail(), "cerickandrade@gmail.com");
                    x.setEmailAniversario(true);
                    repository.save(x);
                } catch (Exception e) {
                    log.error("Erro ao enviar email: " + x.getEmail());
                    x.setEmailAniversario(false);
                    if (x.getTentativas() == null) {
                        x.setTentativas(0);
                    } else {
                        x.setTentativas(x.getTentativas() + 1);
                    }
                    repository.save(x);
                }
            }
        });
    }

    public boolean fazAniversarioHoje(Aluno aluno) {
        LocalDateTime hoje = LocalDateTime.now();
        return (aluno.getDataNascimento().getDayOfMonth() == hoje.getDayOfMonth() &&
                aluno.getDataNascimento().getMonth() == hoje.getMonth());
    }


    public boolean existsByUsuario(Integer id) {
        return repository.existsByUsuario(userRepository.findById(id).get());
    }

    public void atualizarHorasDisponiveis(Pedido pedido) {
        if (pedido.getAluno().getParceiro() != null) {
            if (pedido.getAluno().getParceiro().getIsParceiro()) {
                Pedido pedidoEncontrado = pedidoService.listarById(pedido.getId());
                Integer horasSomadasCursos = pedidoEncontrado.getCursos().stream().filter(x -> x.getCategoria().getTitulo().equals("DIREITO ONLINE")).mapToInt(Curso::getCargaHoraria).sum();
                Aluno aluno = listarById(pedidoEncontrado.getAluno().getId());
                int diferenca = aluno.getHorasDisponiveis() - horasSomadasCursos;
                if (diferenca >= 0) {
                    aluno.setHorasDisponiveis(diferenca);
                }
                repository.save(aluno);
            }
        }
    }

    public void save(Aluno aluno) {
        repository.save(aluno);
    }

    public void alterarObservacao(Integer id, String observacao) {
        Aluno aluno = listarById(id);
        aluno.setObservacao(aluno.getObservacao() + "<br>" + observacao);
        repository.save(aluno);
    }

    public void desativar(String referencia) {
        try {
            asaasService.desativar("customers/" + referencia);
        } catch(Exception e) {
            throw new ErrorException("Erro no processo de desativação de cliente");
        }
    }

    public void reativar(String referencia) {
        try {
            asaasService.reativar("customers/" + referencia + "/restore");
        } catch (Exception e) {
            throw new ErrorException("Erro no processo de reativação do cliente");
        }
    }

    public void recuperarSenha(Map<String, String> map) {
        try {
            if (alunoRepository.existsByCpf(map.get("cpf")) && alunoRepository.existsByEmail(map.get("email"))) { // Mudança aqui de CPF pra Email
                Aluno aluno = alunoRepository.findByCpf(map.get("cpf"));
                String novaSenha = PasswordGenerator.generateRandomPassword(5); // Gerar senha aleatória
                String senhaCriptografada = DigestUtils.md5DigestAsHex(novaSenha.getBytes()); // Criptografar senha usando MD5

                
                // Atualizar a senha criptografada no usuário
                userRepository.atualizarSenha(aluno.getEmail(), senhaCriptografada);

                // Enviar e-mail de recuperação de senha
                EmailUtil sender = new EmailUtil();
                String assunto = "Recuperação de Senha";
                String destinatario = aluno.getEmail(); 
                String nome = aluno.getNome();

                sender.resetPassword(assunto, novaSenha, nome, destinatario);


            } else {
                throw new ErrorException("Erro ao recuperar senha do aluno, verifique os dados!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar solicitação: " + e.getMessage());
            e.printStackTrace(); // Isso imprimirá o rastreamento da pilha da exceção no console
            throw new ErrorException("Os dados informados não conferem!");
        }
    }
}
