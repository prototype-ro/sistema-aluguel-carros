package br.puc.aluguel.dto;

import br.puc.aluguel.model.TipoProprietario;

public record AutomovelResponse(
        Long id, String matricula, Integer ano, String marca, String modelo,
        String placa, TipoProprietario tipoProprietario, Long proprietarioId
) {
}
