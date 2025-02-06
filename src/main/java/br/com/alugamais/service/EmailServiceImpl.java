package br.com.alugamais.service;

import br.com.alugamais.dao.EmailDao;

import br.com.alugamais.web.domain.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    @Autowired
    EmailDao dao;


    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void salvar(Email email) {
        dao.save(email);
    }

    @Override
    public void editar(Email email) {
        dao.update(email);
    }

    @Override
    public void excluir(Long id) {
        dao.delete(id);
    }

    @Override
    public Email buscarPorId(Long id) {
        return dao.findById(id);
    }

    @Override
    public List<Email> buscarTodos() {
        return dao.findAll();
    }

    @Override
    public void enviarEmailSimples(String para, String assunto, String texto) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(para);
        mensagem.setSubject(assunto);
        mensagem.setText(texto);
        emailSender.send(mensagem);
    }

    @Override
    public void enviarEmailHtml(String para, String assunto, String html) throws Exception {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("access-manager@fleetcarservice.com.br");
        helper.setTo(para);
        helper.setSubject(assunto);
        helper.setText(html, true);

        // Adicione a imagem como um anexo inline
        ClassPathResource resource = new ClassPathResource("static/image/bemvindo-alugamais.png");
        helper.addInline("backgroundImage", resource);

        emailSender.send(mimeMessage);
    }

}

