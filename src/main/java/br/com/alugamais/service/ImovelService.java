package br.com.alugamais.service;

import br.com.alugamais.web.domain.Imovel;

import java.util.List;

public interface ImovelService {

    void salvar(Imovel Imovel);

    void editar(Imovel Imovel);

    void excluir(Long id);

    Imovel buscarPorId(Long id);

    List<Imovel> buscarTodos();

    List<Imovel> getImoveisPorLocador(String situacao, Long locadorId);
}
