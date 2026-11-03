package br.puc.aluguel.dto;

import java.util.List;

public record ClienteResponse(
        Long id, String rg, String cpf, String nome, String endereco,
        String profissao, List<EmpregadorRequest> empregadores
) {
}
