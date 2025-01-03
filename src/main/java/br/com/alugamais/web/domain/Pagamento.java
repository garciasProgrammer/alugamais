package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;


@SuppressWarnings("serial")
@Entity
@Table(name = "pagamento")
@Getter
@Setter
public class Pagamento extends AbstractEntity<Long> {

	@NotNull
	@Size(max = 600, min = 1)
	@Column(nullable = false)
	private String descricao;

	@NotNull
	@Size(max = 500, min = 1)
	@Column(nullable = false)
	private String tipoDePagamento;

	@NotNull
	@Size(max = 100, min = 1)
	@Column(name="tipo_de_documento", nullable = false, unique = false)
	private String tipoDeDocumento;

	@NotNull
	@Column(nullable = false)
	private String usuario;

	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal valor;

	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal qtdParcela;

	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal valorTotal;
	
	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal desconto;
	
	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal valorTotalLiquido;

	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal valorRecebido;

	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal juros;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_pagamento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataPagamento;

	@OneToOne
	@JoinColumn(name = "contrato_id_fk")
	private Contrato contrato;

	@OneToOne
	@JoinColumn(name = "locador_id_fk")
	private Locador locador;

	@OneToOne
	@JoinColumn(name = "locatario_id_fk")
	private Locatario locatario;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_cancelado", columnDefinition = "DATE")
	private LocalDate dataCancelado;

	@Column(name = "usuario_cancelador")
	private String usuarioCancelador;
}
