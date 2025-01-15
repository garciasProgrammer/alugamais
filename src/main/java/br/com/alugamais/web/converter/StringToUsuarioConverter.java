package br.com.alugamais.web.converter;

import br.com.alugamais.service.UsuarioService;
import br.com.alugamais.web.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToUsuarioConverter implements Converter<String, Usuario> {

    @Autowired
    private UsuarioService service;

    @Override
    public Usuario convert(String text) {
        if(text.isEmpty()) {
            return null;
        }
        Long id = Long.valueOf(text);
        return service.buscarPorId(id);
    }

}
