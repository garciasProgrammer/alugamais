package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Pagamento;

import java.util.List;

public interface PagamentoDao {

    void save(Pagamento Pagamento);

    void update(Pagamento Pagamento);

    void delete(Long id);

    Pagamento findById(Long id);

    List<Pagamento> findAll();
}
