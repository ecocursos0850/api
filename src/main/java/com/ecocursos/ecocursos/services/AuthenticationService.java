package com.ecocursos.ecocursos.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.AuthenticationRequest;
import com.ecocursos.ecocursos.models.AuthenticationResponse;
import com.ecocursos.ecocursos.models.RegisterRequest;
import com.ecocursos.ecocursos.models.Role;
import com.ecocursos.ecocursos.models.Token;
import com.ecocursos.ecocursos.models.TokenType;
import com.ecocursos.ecocursos.models.User;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.models.enums.StatusAfiliado;
import com.ecocursos.ecocursos.repositories.TokenRepository;
import com.ecocursos.ecocursos.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final AlunoService alunoService;
  private final AfiliadoService afiliadoService;

  public List<User> listar() {
    return repository.findAll();
  }

  @SneakyThrows
  public AuthenticationResponse register(RegisterRequest request) {
    repository.findByEmail(request.getEmail()).ifPresent(x -> {
      throw new ErrorException("Já existe um usuário com esse email");
    });
    String hash = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
     var user = User.builder()
        .firstname(request.getNome())
        .lastname(request.getSobrenome())
        .email(request.getEmail())
        .password(hash)
        .role(request.getRole())
        .identificador(request.getId())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    // updateUser(savedUser);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticateForAluno(Integer id, String email) {
    User usuarioAcesso = repository.findById(id)
      .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    User userAluno = repository.findByEmail(email).orElse(null);
    if(userAluno == null) {
      throw new ErrorException("Erro ao logar em conta do aluno");
    }
    var jwtToken = jwtService.generateToken(userAluno);
    var refreshToken = jwtService.generateRefreshToken(userAluno);
    // revokeAllUserTokens(user);
    saveUserToken(userAluno, jwtToken);
    return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .role(userAluno.getRole())
              .id(userAluno.getIdentificador())
              .idUser(userAluno.getId())
              .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    User exists = repository.findByEmail(request.getEmail()).orElse(null);
    // if(exists == null) {
    //   if(alunoService.existsByEmail(request.getEmail())) {
    //     Aluno aluno = alunoService.findByEmail(request.getEmail());
    //     if (aluno.getStatus().equals(Status.INATIVO)) {
    //       throw new ErrorException("Aluno está inativo!");
    //     }
    //     String hash = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
    //     if (!hash.equals(aluno.getSenha())) {
    //       throw new ErrorException("Verifique os dados de acesso!");
    //     }
    //     RegisterRequest registerRequest = new RegisterRequest();
    //     registerRequest.setId(aluno.getId());
    //     registerRequest.setPassword(aluno.getSenha());
    //     registerRequest.setNome(aluno.getNome().split(" ")[0]);
    //     registerRequest.setRole(Role.USER);
    //     registerRequest.setEmail(aluno.getEmail());
    //     var user = User.builder()
    //             .firstname(registerRequest.getNome())
    //             .lastname(registerRequest.getSobrenome())
    //             .email(registerRequest.getEmail())
    //             .password(registerRequest.getPassword())
    //             .role(Role.USER)
    //             .identificador(aluno.getId())
    //             .build();
    //     var savedUser = repository.save(user);
    //     var jwtToken = jwtService.generateToken(user);
    //     var refreshToken = jwtService.generateRefreshToken(user);
    //     saveUserToken(user, jwtToken);
    //     return AuthenticationResponse.builder()
    //             .accessToken(jwtToken)
    //             .refreshToken(refreshToken)
    //             .role(user.getRole())
    //             .id(user.getIdentificador())
    //             .idUser(user.getId())
    //             .build();
    //   }
    // }
    if (exists.getRole() != null && exists.getRole() == Role.USER) {
      Aluno aluno = alunoService.findByEmail(request.getEmail());
      if (aluno.getStatus() == Status.INATIVO) {
        throw new ErrorException("Aluno está inativo!");
      }
    }
    if (exists.getRole() == Role.AFILIADO) {
      Afiliado afiliado = afiliadoService.findByEmail(request.getEmail());
      if (afiliado.getStatus() == StatusAfiliado.AGUARDANDO_LIBERACAO || afiliado.getStatus() == StatusAfiliado.NAO_APROVADO || afiliado.getStatus() == StatusAfiliado.BLOQUEADO) {
        throw new ErrorException("Status invalido do Afiliado!");
      }
    }
    String hashSenha = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
    if (!hashSenha.equals(exists.getPassword())) {
      throw new ErrorException("Verifique os dados de acesso!");
    }
//    authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(
//            request.getEmail(),
//            request.getPassword()
//        )
//    );
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    // revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .role(user.getRole())
            .id(user.getIdentificador())
            .idUser(user.getId())
            .build();
  }

  private static boolean compareMD5Hashes(String hash1, String hash2) {
    // Utilizando o método md5Hex da classe DigestUtils para calcular o hash MD5
    return DigestUtils.md5DigestAsHex(hash1.getBytes()).toUpperCase().equals(DigestUtils.md5DigestAsHex(hash2.getBytes()).toUpperCase());
  }

  private void updateUser(User user) {
    if(user.getRole() == Role.USER) {
      Aluno aluno = alunoService.listarById(user.getIdentificador());
      if(aluno.getUsuario() != null) {
        throw new ErrorException("O Aluno já possui um usuário");
      }
      aluno.setUsuario(user);
      alunoService.alterar(aluno.getId(), aluno);
    }
    if(user.getRole() == Role.AFILIADO) {
      Afiliado afiliado = afiliadoService.listarById(user.getIdentificador());
      if(afiliado.getUsuario() != null) {
        throw new ErrorException("O Afiliado já possui um usuário");
      }
      afiliado.setUsuario(user);
      afiliadoService.alterar(afiliado.getId(), afiliado);
    }
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  public User changePassword(Integer id, Map<String, String> map) {
    User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    String hash = DigestUtils.md5DigestAsHex(map.get("password").getBytes());
    user.setPassword(hash);
    repository.save(user);
    return user;
  }

  public void deletar(Integer id) {
    User user = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
    if(user.getRole() == Role.USER || user.getRole() == Role.AFILIADO) {
      if(user.getRole() == Role.USER && alunoService.existsByUsuario(user.getId())) {
        throw new ErrorException("O usuário possui um aluno vinculado");
      }
      if(user.getRole() == Role.AFILIADO && afiliadoService.existsByUsuario(user.getId())) {
        throw new ErrorException("O usuário possui um afiliado vinculado");
      }
    }
    repository.delete(user);
  }

}