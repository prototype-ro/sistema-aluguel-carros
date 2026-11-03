package br.puc.aluguel.controller;

import br.puc.aluguel.dto.*;
import br.puc.aluguel.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registrar/cliente")
    public ResponseEntity<Void> registrarCliente(@Valid @RequestBody RegistroClienteRequest request) {
        authService.registrarCliente(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/registrar/agente")
    public ResponseEntity<Void> registrarAgente(@Valid @RequestBody RegistroAgenteRequest request) {
        authService.registrarAgente(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
