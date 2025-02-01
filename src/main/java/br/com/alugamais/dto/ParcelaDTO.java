package br.com.alugamais.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ParcelaDTO {

    private String parcela;
    private String dataVencimento;
    private BigDecimal valorAluguel;
}
