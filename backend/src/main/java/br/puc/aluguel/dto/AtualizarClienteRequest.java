package br.puc.aluguel.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record AtualizarClienteRequest(
        @NotBlank String rg,
        @NotBlank String endereco,
        String profissao,
        List<EmpregadorRequest> empregadores
) {
}
