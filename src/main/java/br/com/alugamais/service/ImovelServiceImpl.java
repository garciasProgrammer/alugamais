package br.com.alugamais.service;

import br.com.alugamais.dao.ImovelDao;
import br.com.alugamais.web.domain.Imovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImovelServiceImpl implements ImovelService{
    
    @Autowired
    ImovelDao dao;

    @Override
    public void salvar(Imovel Imovel) {
        dao.save(Imovel);

    }

    @Override
    public void editar(Imovel Imovel) {
        dao.update(Imovel);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Imovel buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Imovel> buscarTodos() {

        return dao.findAll();
    }
    @Override
    public List<Imovel> getImoveisPorLocador(String situacao, Long locadorId){
        return dao.getImoveisPorLocador(situacao, locadorId);
    }
}
