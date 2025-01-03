package br.com.alugamais.service;

import br.com.alugamais.web.domain.Usuario;

import java.util.List;

public interface UsuarioService {

    void salvar(Usuario usuario);
    void editar(Usuario usuario);
    void excluir(Long id);
    Usuario buscarPorId(Long id);
    List<Usuario> buscarTodos();

    Usuario buscarPorUserName(String userName);

    void updatePassword(Long id, String senha);

    Usuario findByEmail(String email);

}
