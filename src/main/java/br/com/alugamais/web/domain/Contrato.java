package br.com.alugamais.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@SuppressWarnings("serial")
@Entity
@Table(name = "contrato")
@Getter
@Setter
public class Contrato extends AbstractEntity<Long> {

    @Valid
    @NotNull(message = "Locador é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "locador_id_fk")
    private Locador locador;

    @Valid
    @NotNull(message = "Locatário é obrigatório.")
    @OneToOne
    @JoinColumn(name = "cliente_id_fk")
    private Locatario locatario;

    @Valid
    @NotNull(message = "Imóvel é obrigatório.")
    @OneToOne
    @JoinColumn(name = "imovel_id_fk")
    private Imovel imovel;

    @NotNull(message = "selecione uma situação válida")
    @Size(max = 20, min = 6, message = "Selecione uma opção: ABERTO, CANCELADO, SUSPENSO")
    @Column(nullable = false, unique = false)
    private String situacao;


    @NotNull(message = "Selecione uma Data Inicial.")
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_inicial", nullable = false, columnDefinition = "DATE")
    private LocalDate dataInicial;


    @NotNull(message = "Selecione uma Data Final.")
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_final", nullable = false, columnDefinition = "DATE")
    private LocalDate dataFinal;

    @NotNull(message = "Data de Assinatura deve ser preenchida.")
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_assinatura", nullable = true, columnDefinition = "DATE")
    private LocalDate dataAssinatura;

    @NotNull(message = "Digite um valor de Diária.")
    @NumberFormat(style = Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal valorAluguel;

    @NotNull(message = "Digite uma Data de renovação")
    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_renovacao", nullable = false, columnDefinition = "DATE")
    private LocalDate dataRenovacao;

    @Size(max = 3, min = 1, message = "Entre com um período válido de Meses.")
    @Column(nullable = false, unique = false)
    private String meses;


    @NotBlank(message = "Digite um período de renovação.")
    @Size(max = 12, min = 1, message = "Digite um período de renovação entre {min} e {max} meses.")
    @Column(nullable = false, unique = false)
    private String periodoRenovacao;


    @OneToMany(mappedBy = "parcela", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Parcelas> parcelas;


    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_paralisacao",  nullable = false, columnDefinition = "DATE")
    private LocalDate dataParalisacao;

    @DateTimeFormat(iso = ISO.DATE)
    @Column(name = "data_quitacao",  nullable = false, columnDefinition = "DATE")
    private LocalDate dataQuitacao;


}
