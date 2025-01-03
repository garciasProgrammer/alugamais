package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "locador")
public class Locador extends AbstractEntity<Long>{
}
