package br.puc.aluguel.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "contrato_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "contrato_id", nullable = false, unique = true)
    private Contrato contrato;

    @ManyToOne
    @JoinColumn(name = "agente_id", nullable = false)
    private Agente agente;

    @Column(name = "valor_financiado", nullable = false)
    private BigDecimal valorFinanciado;

    @Column(nullable = false)
    private Integer parcelas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Contrato getContrato() { return contrato; }
    public void setContrato(Contrato contrato) { this.contrato = contrato; }
    public Agente getAgente() { return agente; }
    public void setAgente(Agente agente) { this.agente = agente; }
    public BigDecimal getValorFinanciado() { return valorFinanciado; }
    public void setValorFinanciado(BigDecimal valorFinanciado) { this.valorFinanciado = valorFinanciado; }
    public Integer getParcelas() { return parcelas; }
    public void setParcelas(Integer parcelas) { this.parcelas = parcelas; }
}
