package com.ecocursos.ecocursos.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ecocursos.ecocursos.models.User;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecocursos.ecocursos.models.AuthenticationRequest;
import com.ecocursos.ecocursos.models.AuthenticationResponse;
import com.ecocursos.ecocursos.models.RegisterRequest;
import com.ecocursos.ecocursos.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
  @RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

  private final AuthenticationService service;

  @GetMapping
  public ResponseEntity<List<User>> listar() {
    return ResponseEntity.ok().body(service.listar());
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deletar(@PathVariable Integer id) {
    service.deletar(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.register(request));
  }

  @PutMapping("/{id}/change/password")
  public ResponseEntity<String> changePassword(@PathVariable Integer id,
                                                      @RequestBody Map<String, String> map) {
    service.changePassword(id, map);
    return ResponseEntity.ok().body("Senha do usuário alterada com sucesso");
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request,
      @RequestParam(required = false) String token
      ) {
    return ResponseEntity.ok(service.authenticate(request, token));  
  }

  @PostMapping("/authenticate/user/{id}/{email}")
  public ResponseEntity<AuthenticationResponse> authenticateForAluno(
      @PathVariable Integer id,
      @PathVariable String email
      ) {
    return ResponseEntity.ok(service.authenticateForAluno(id, email));  
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    service.refreshToken(request, response);
  }


}
