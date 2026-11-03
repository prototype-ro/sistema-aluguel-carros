package br.puc.aluguel.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("troque-este-segredo-em-producao-0123456789", 86400000);
    }

    @Test
    void geraTokenEExtraiLoginCorretamente() {
        String token = jwtService.gerarToken("cliente1@example.com");
        assertEquals("cliente1@example.com", jwtService.extrairLogin(token));
        assertTrue(jwtService.tokenValido(token, "cliente1@example.com"));
    }
}
