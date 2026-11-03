package br.puc.aluguel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "empregador")
public class Empregador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal rendimento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getRendimento() { return rendimento; }
    public void setRendimento(BigDecimal rendimento) { this.rendimento = rendimento; }
}
