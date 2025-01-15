package br.com.alugamais.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigDecimal;

@Configuration
public class GlobalBinderConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverter(String.class, BigDecimal.class, source -> {
            if (source == null || source.isEmpty()) {
                return null;
            }
            try {
                // Remove prefixo e ajusta o formato
                String valorLimpo = source.replace("R$", "").replace(".", "").replace(",", ".").trim();
                return new BigDecimal(valorLimpo);
            } catch (Exception e) {
                throw new IllegalArgumentException("Valor inválido: " + source);
            }
        });
    }
}
