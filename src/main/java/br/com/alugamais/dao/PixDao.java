package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Pix;

import java.util.List;

public interface PixDao {

    void save(Pix pix);

    void update(Pix pix);

    void delete(Long id);

    Pix findById(Long id);

    List<Pix> findAll();

    List<Pix> getDadosPix(String pixQrCodeId);

}
