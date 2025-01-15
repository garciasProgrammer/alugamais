package br.com.alugamais.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "imovel")
public class Imovel extends AbstractEntity<Long>{

    @NotNull(message = "{NotNull.imovel.numero}")
    @Column(nullable = false, length = 5)
    private String numero;

    @NotNull(message = "{NotNull.imovel.andar}")
    @Column(nullable = false, length = 50)
    private String andar;

    @NotNull(message = "{NotNull.imovel.andar}")
    @Column(nullable = false, length = 50)
    private String tipo;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id_fk")
    private Endereco endereco;

    @Size(min = 0, max = 6000)
    private String descricao;

    @Valid
    @NotNull(message = "Locador é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "locador_id_fk")
    private Locador locador;

    @NotNull(message = "selecione uma situação válida")
    @Size(max = 20, min = 4, message = "Selecione uma opção: LIVRE, ALUGADO")
    @Column(nullable = false, unique = false)
    private String situacao;

    @NotNull(message = "Selecione uma Data de cadastro.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_inicial", nullable = false, columnDefinition = "DATE")
    private LocalDate dataCadastro = LocalDate.now();

    @NotNull(message = "Digite um valor para a Parcela de aluguel.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal valor;

    @NotNull(message = "Digite um valor para a Parcela de aluguel.")
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal valorTaxa;
}
