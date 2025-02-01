package br.com.alugamais.service;

import br.com.alugamais.dao.ParcelaDao;
import br.com.alugamais.dao.PixDao;
import br.com.alugamais.web.domain.Pagamento;
import br.com.alugamais.web.domain.Parcelas;
import br.com.alugamais.web.domain.Pix;
import br.com.alugamais.web.util.PaginacaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional()
public class PixServiceImpl implements PixService {

    @Autowired
    PixDao dao;

    @Override
    public void salvar(Pix pix) {
        dao.save(pix);

    }

    @Override
    public void editar(Pix pix) {
        dao.update(pix);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Pix buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pix> buscarTodos() {

        return dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pix> getDadosPix(String pixQrCodeId) {
        return dao.getDadosPix(pixQrCodeId);
    }

}
