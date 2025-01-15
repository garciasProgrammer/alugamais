package br.com.alugamais.web.converter;


import br.com.alugamais.service.ContratoService;
import br.com.alugamais.web.domain.Contrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class StringToContratoConverter implements Converter<String, Contrato>{

	@Autowired
	private ContratoService service;
	
	@Override
	public Contrato convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id);
	}

}
