package br.com.alugamais.dao;

import br.com.alugamais.web.domain.Usuario;

import java.util.List;

public interface UsuarioDao {

    void save(Usuario usuario);

    void update(Usuario usuario);

    void delete(Long id);

    Usuario findById(Long id);

    List<Usuario> findAll();

    Usuario findByUserName(String userName);
    void updatePassword(Long id, String senha);

     Usuario findByEmail(String email);

}
