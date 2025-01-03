package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;
import br.com.alugamais.web.domain.Pagamento;
import org.springframework.stereotype.Repository;

@Repository
public class PagamentoDaoImpl extends AbstractDao<Pagamento, Long> implements PagamentoDao{
}
