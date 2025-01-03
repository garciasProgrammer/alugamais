package br.com.alugamais.service;

import br.com.alugamais.dao.PagamentoDao;
import br.com.alugamais.web.domain.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PagamentoServiceImpl implements PagamentoService{
    
    @Autowired
    PagamentoDao dao;

    @Override
    public void salvar(Pagamento Pagamento) {
        dao.save(Pagamento);

    }

    @Override
    public void editar(Pagamento Pagamento) {
        dao.update(Pagamento);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Pagamento buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pagamento> buscarTodos() {

        return dao.findAll();
    }
}
