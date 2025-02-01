package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Imovel;

import java.util.List;

public interface ImovelDao {

    void save(Imovel Imovel);

    void update(Imovel Imovel);

    void delete(Long id);

    Imovel findById(Long id);

    List<Imovel> findAll();

   List<Imovel> getImoveisPorLocador(List<String> situacao, Long locadorId);

    Object getImoveisALugados();

    Object getImoveisAlugadosPorcentagem();
}
