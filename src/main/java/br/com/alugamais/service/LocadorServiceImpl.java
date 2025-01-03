package br.com.alugamais.service;

import br.com.alugamais.dao.LocadorDao;
import br.com.alugamais.web.domain.Locador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocadorServiceImpl implements LocadorService {

    @Autowired
    LocadorDao dao;

    @Override
    public void salvar(Locador Locador) {
        dao.save(Locador);

    }

    @Override
    public void editar(Locador Locador) {
        dao.update(Locador);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Locador buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locador> buscarTodos() {

        return dao.findAll();
    }
}
