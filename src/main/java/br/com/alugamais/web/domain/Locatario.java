package br.com.alugamais.web.domain;

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

@Entity
@Getter
@Setter
@Table (name = "locatario")
public class Locatario extends AbstractEntity<Long>{

    @NotBlank(message = "Campo obrigatório")
    @Size(max = 255, min = 3)
    @Column (nullable = false, unique = false)
    private String nome;

    @NotNull
    @PastOrPresent(message = "{PastOrPresent.cliente.dataNascimento}")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column (name = "data_nascimento",nullable = false, columnDefinition = "DATE")
    private LocalDate dataNascimento;


    @Valid
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id_fk")
    private Endereco endereco;

    @NotBlank
    @Size(max = 18, min = 14)
    @Column (nullable = false, unique = true)
    private String cpf;

    @NotBlank
    @Size(max = 15, min = 8)
    @Column (nullable = false, unique = true)
    private String rg;

    @NotBlank
    @Size(max = 20, min = 6)
    @Column (nullable = false, unique = false)
    private String estadoCivil;

    @NotBlank
    @Size(max = 100, min = 1)
    @Column (nullable = false, unique = false)
    private String profissao;

    @NotBlank
    @Size(max = 20, min = 3)
    @Column (nullable = false, unique = false)
    private String nacionalidade;

    @Size(max = 150, min = 10,message = "Este e-mail está fora do tamanho permitido entre {min} e {max} caracteres.")
    @Column (nullable = false, unique = false)
    private String email;


    @Size(max = 255, min = 3)
    @Column (nullable = false, unique = false)
    private String celularPrincipalCliente;

    @NotNull(message = "Campo obrigatório")
    @Size(max = 255, min = 0)
    @Column (nullable = true, unique = false)
    private String celularWhatsApp;

}
