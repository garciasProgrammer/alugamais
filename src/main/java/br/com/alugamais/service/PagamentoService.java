package br.com.alugamais.service;

import br.com.alugamais.web.domain.Pagamento;

import java.util.List;

public interface PagamentoService {

    void salvar(Pagamento Pagamento);

    void editar(Pagamento Pagamento);

    void excluir(Long id);

    Pagamento buscarPorId(Long id);

    List<Pagamento> buscarTodos();
}
