package br.puc.aluguel.service;

import br.puc.aluguel.dto.AtualizarClienteRequest;
import br.puc.aluguel.dto.EmpregadorRequest;
import br.puc.aluguel.model.Cliente;
import br.puc.aluguel.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @InjectMocks private ClienteService clienteService;

    @Test
    void atualizaDadosDoClienteSubstituindoEmpregadores() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setRg("MG0000001");
        cliente.setEndereco("Endereço antigo");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        var request = new AtualizarClienteRequest(
                "MG1111111", "Endereço novo", "Advogado",
                List.of(new EmpregadorRequest("Nova Empresa", new BigDecimal("3000")))
        );

        Cliente atualizado = clienteService.atualizar(1L, request);

        assertEquals("MG1111111", atualizado.getRg());
        assertEquals("Endereço novo", atualizado.getEndereco());
        assertEquals(1, atualizado.getEmpregadores().size());
    }

    @Test
    void rejeitaAtualizacaoComMaisDeTresEmpregadores() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        var request = new AtualizarClienteRequest(
                "MG1111111", "Endereço novo", "Advogado",
                List.of(
                        new EmpregadorRequest("A", BigDecimal.ONE),
                        new EmpregadorRequest("B", BigDecimal.ONE),
                        new EmpregadorRequest("C", BigDecimal.ONE),
                        new EmpregadorRequest("D", BigDecimal.ONE)
                )
        );

        assertThrows(IllegalArgumentException.class, () -> clienteService.atualizar(1L, request));
    }
}
