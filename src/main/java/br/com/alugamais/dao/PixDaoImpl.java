package br.com.alugamais.dao;


import br.com.alugamais.web.domain.Pix;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PixDaoImpl extends AbstractDao<Pix, Long> implements PixDao {

    public List<Pix> getDadosPix(String pixQrCodeId){

            List<Pix> dados = getEntityManager().createQuery("SELECT p FROM Pix p WHERE p.transactionId=:pixQrCodeId")
                    .setParameter("pixQrCodeId", pixQrCodeId)
                    .getResultList();

            return dados;

    }
}
