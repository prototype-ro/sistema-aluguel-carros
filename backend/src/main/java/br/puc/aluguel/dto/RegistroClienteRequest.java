package br.puc.aluguel.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record RegistroClienteRequest(
        @NotBlank String login,
        @NotBlank String senha,
        @NotBlank String rg,
        @NotBlank String cpf,
        @NotBlank String nome,
        @NotBlank String endereco,
        String profissao,
        List<EmpregadorRequest> empregadores
) {
}
