package br.com.alugamais.service;

import br.com.alugamais.web.domain.Email;

import java.util.List;

public interface EmailService {

    void salvar(Email usuario);

    void editar(Email usuario);

    void excluir(Long id);

    Email buscarPorId(Long id);

    List<Email> buscarTodos();

    void enviarEmailSimples(String para, String assunto, String texto);
    void enviarEmailHtml(String para, String assunto, String html) throws Exception;


}
