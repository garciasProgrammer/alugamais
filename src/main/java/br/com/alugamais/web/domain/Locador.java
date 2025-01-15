package br.com.alugamais.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "locador")
public class Locador extends AbstractEntity<Long>{

    @NotBlank
    @Size(max = 255, min = 3)
    @Column(nullable = false, unique = true)
    private String nome;

    @NotNull
    @PastOrPresent(message = "{PastOrPresent.locador.dataCadastro}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column (name = "data_cadastro",nullable = false, columnDefinition = "DATE")
    private LocalDate dataCadastro = LocalDate.now();

    @JsonIgnore
    @OneToMany(mappedBy = "locador")
    private List<Contrato> contratos;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id_fk")
    private Endereco endereco;

    @NotBlank
    @Size(max = 18, min = 11)
    @Column (nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Size(max = 14, min = 8)
    @Column (nullable = false, unique = true)
    private String rg;


    @NotBlank
    @Size(max = 150, min = 10)
    @Column (nullable = false, unique = true)
    private String email;

    @Size(max = 255, min = 3)
    @Column (nullable = false, unique = true)
    private String celularLocador;

    @Column (name="taxa_juros")
    private int taxaJuros;
}
