package br.com.alugamais.web.converter;

import br.com.alugamais.service.LocatarioService;
import br.com.alugamais.web.domain.Locatario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToLocatarioConverter implements Converter<String, Locatario>{

	@Autowired
	private LocatarioService service;
	
	@Override
	public Locatario convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id);
	}

}
