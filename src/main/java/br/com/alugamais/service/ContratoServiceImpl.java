package br.com.alugamais.service;

import br.com.alugamais.dao.ContratoDao;
import br.com.alugamais.web.domain.Contrato;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    ContratoDao dao;

//    @Autowired
//    WebSocketController webSocketController;

    @Override
    public void salvar(Contrato contrato) {
        dao.save(contrato);
//        Long qtdContratos = dao.qtdContratos();
//        webSocketController.sendCardUpdate(new CardUpdate("contratos", qtdContratos));
    }

    @Override
    public void editar(Contrato contrato) {
        dao.update(contrato);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Contrato buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrato> buscarTodos() {

        return dao.findAll();
    }
}
