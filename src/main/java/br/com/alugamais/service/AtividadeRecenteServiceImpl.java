package br.com.alugamais.service;

import br.com.alugamais.dao.AtividadeRecenteDao;
import br.com.alugamais.dao.ImovelDao;
import br.com.alugamais.web.domain.AtividadeRecente;
import br.com.alugamais.web.domain.Imovel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AtividadeRecenteServiceImpl implements AtividadeRecenteService {

    @Autowired
    AtividadeRecenteDao dao;

    @Override
    public void salvar(AtividadeRecente atividadeRecente) {
        dao.save(atividadeRecente);

    }

    @Override
    public void editar(AtividadeRecente atividadeRecente) {
        dao.update(atividadeRecente);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public AtividadeRecente buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeRecente> buscarTodos() {

        return dao.findAll();
    }

    public List<AtividadeRecente> getAtividades(){
        return dao.getAtividades();
    }
}
