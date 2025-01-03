package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Usuario;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class UsuarioDaoImpl extends AbstractDao<Usuario, Long> implements UsuarioDao {

    @Override
    public Usuario findByUserName(String userName) {
        TypedQuery<Usuario> query = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.userName = :userName", Usuario.class);
        query.setParameter("userName", userName);
        List<Usuario> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public void updatePassword(Long id, String senha) {
        Query query = getEntityManager().createNativeQuery("UPDATE usuario u SET u.password = :senha WHERE u.id = :id");
       query.setParameter("id", id)
            .setParameter("senha", senha)
            .executeUpdate();
    }

    @Override
    public Usuario findByEmail(String email){
        TypedQuery<Usuario> query = getEntityManager().createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        List<Usuario> users = query.getResultList();
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }
}
