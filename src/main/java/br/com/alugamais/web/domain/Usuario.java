package br.com.alugamais.web.domain;


import br.com.alugamais.web.enums.TipoDeUsuario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario extends AbstractEntity<Long> {

    @NotBlank(message = "Campo nome é obrigatório")
    @NotNull
    @Size(max = 255, min = 3)
    @Column(nullable = false, unique = false)
    private String nome;

    @NotBlank(message = "Campo usuário é obrigatório")
    @Size(max = 255, min = 3)
    @Column (nullable = false, unique = false)
    private String userName;

    @Column (nullable = false, unique = false)
    private String password;

    @Column (nullable = false, unique = false)
    private TipoDeUsuario tipoDeUsuario;

    @Column(name = "ativo", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean ativo;

    @NotBlank(message = "Campo E-mail obrigatório")
    @Size(max = 300, min = 6)
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Formato de e-mail inválido")
    @Column (nullable = false, unique = false)
    private String email;

    @Size(max = 100)
    private String fone;

    @Column (columnDefinition = "LONGBLOB")
    private byte[] imagem;



}
