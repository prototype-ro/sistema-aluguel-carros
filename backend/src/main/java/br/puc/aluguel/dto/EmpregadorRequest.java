package br.puc.aluguel.dto;

import java.math.BigDecimal;

public record EmpregadorRequest(String nome, BigDecimal rendimento) {
}
