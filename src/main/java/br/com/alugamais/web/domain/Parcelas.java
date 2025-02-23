package br.com.alugamais.web.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Table(name = "parcela")
@Getter
@Setter
public class Parcelas extends AbstractEntity<Long>{

	@Valid
	@ManyToOne
	@JoinColumn(name = "contrato_id_fk")
	@JsonBackReference
	private Contrato contrato;
	
	@NotNull
	@NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
	@Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal valorAluguel;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_vencimento", nullable = false, columnDefinition = "DATE")
	private LocalDate dataVencimento;

	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_pagamento", columnDefinition = "DATE")
	private LocalDate dataPagamento;
	
	@NotNull
	@NotBlank
	@Column(nullable = false)
	private String parcela;

	@Column()
	private String codigoPagamento;
	
	@NotNull
	@NotBlank
	@Column(nullable = false)
	private String situacao;

}
