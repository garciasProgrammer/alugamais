package br.com.alugamais.web.converter;


import br.com.alugamais.service.LocadorService;
import br.com.alugamais.web.domain.Locador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToLocadorConverter implements Converter<String, Locador>{

	@Autowired
	private LocadorService service;
	
	@Override
	public Locador convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id);
	}

}
