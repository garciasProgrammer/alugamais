package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Table(name = "atividade_recente")
@Getter
@Setter
@Entity
public class AtividadeRecente extends AbstractEntity<Long> {

    @NotNull
    private String tipoAtividade;

    private String atividade;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "data_criacao", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime dataCriacao;

}
