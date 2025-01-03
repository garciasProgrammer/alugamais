package br.com.alugamais.service;

import br.com.alugamais.dao.UsuarioDao;
import br.com.alugamais.web.config.hibernate.TenantContext;
import br.com.alugamais.web.config.security.CustomUserDetails;
import br.com.alugamais.web.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    UsuarioDao dao;

    @Override
    public void salvar(Usuario usuario) {

        dao.save(usuario);

    }

    @Override
    public void editar(Usuario usuario) {

        dao.update(usuario);

    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);

    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {

        return dao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {

        return dao.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorUserName(String userName) {

        return dao.findByUserName(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = buscarPorUserName(username);

        if (usuario != null) {


            String tenantId = TenantContext.getCurrentTenant();
            return new CustomUserDetails(usuario.getUserName(),
                    usuario.getPassword(),
                    AuthorityUtils.createAuthorityList("ROLE_" + usuario.getTipoDeUsuario().toString()),
                    tenantId);


        } else {
            throw new UsernameNotFoundException("Usuário não encontrado com nome de usuário: " + username);

        }

    }

    public void updatePassword(Long id, String senha) {
        dao.updatePassword(id, senha);
    }

    public Usuario findByEmail(String email){
        return dao.findByEmail(email);
    }

}
