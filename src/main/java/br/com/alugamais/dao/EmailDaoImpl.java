package br.com.alugamais.dao;


import br.com.alugamais.web.domain.Email;
import org.springframework.stereotype.Repository;

@Repository
public class EmailDaoImpl extends AbstractDao<Email, Long> implements EmailDao {

}
