package br.com.alugamais.web.domain;

import br.com.alugamais.web.enums.UF;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name = "endereco")
public class Endereco  extends AbstractEntity<Long>{

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String logradouro;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String bairro;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(nullable = false)
    private String cidade;

    @NotNull(message = "{NotNull.endereco.uf}")
    @Column(nullable = false, length = 2)
    @Enumerated(EnumType.STRING)
    private UF uf;

    @NotBlank
    @Size(min = 9, max = 9, message = "{Size.endereco.cep}")
    @Column(nullable = false, length = 9)
    private String cep;

    @NotNull(message = "{NotNull.endereco.numero}")
    @Column(nullable = false, length = 5)
    private Integer numero;

    @Size(min = 0, max = 255)
    private String complemento;
}
