package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;
import org.springframework.stereotype.Repository;

@Repository
public class ImovelDaoImpl  extends AbstractDao<Imovel, Long> implements ImovelDao{
}
