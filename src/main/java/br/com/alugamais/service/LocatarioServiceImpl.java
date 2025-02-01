package br.com.alugamais.service;

import br.com.alugamais.dao.LocatarioDao;
import br.com.alugamais.web.domain.Locatario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocatarioServiceImpl implements LocatarioService {

    @Autowired
    LocatarioDao dao;

    @Override
    public void salvar(Locatario Locatario) {
        dao.save(Locatario);

    }

    @Override
    public void editar(Locatario Locatario) {
        dao.update(Locatario);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Locatario buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Locatario> buscarTodos() {

        return dao.findAll();
    }

    public Object getClientes(){
        return dao.getClientes();
    }
}
