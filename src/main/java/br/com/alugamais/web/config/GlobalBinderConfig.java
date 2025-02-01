package br.com.alugamais.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;

@Configuration
public class GlobalBinderConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBigDecimalConverter());
    }

    static class StringToBigDecimalConverter implements Converter<String, BigDecimal> {
        @Override
        public BigDecimal convert(String source) {
            if (source == null || source.trim().isEmpty()) {
                return null;
            }
            try {
                // Limpeza do valor
                String valorLimpo = source.replace("R$", "")
                        .replaceAll("[^0-9,.]", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .trim();
                // Log para depuração
                System.out.println("Convertendo valor: " + source + " para: " + valorLimpo);
                return new BigDecimal(valorLimpo);
            } catch (NumberFormatException e) {
                // Log do erro para depuração
                System.err.println("Erro ao converter valor: " + source);
                throw new IllegalArgumentException("Valor inválido para conversão em BigDecimal: " + source, e);
            }
        }
    }
}
