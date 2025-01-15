package br.com.alugamais.web.converter;

import br.com.alugamais.service.ImovelService;
import br.com.alugamais.web.domain.Imovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class StringToImovelConverter implements Converter<String, Imovel>{

	@Autowired
	private ImovelService service;
	
	@Override
	public Imovel convert(String text) {
		if(text.isEmpty()) {
			return null;
		}
		Long id = Long.valueOf(text);
		return service.buscarPorId(id);
	}

}
