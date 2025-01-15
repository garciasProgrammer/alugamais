package br.com.alugamais.web.config.security;


import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/job/**", "/public/**", "/webjars/**", "/login","/recuperar-senha","/usuarios/recuperar-senha/**","/image/**", "/video/**", "/css/**", "/js/**","/assets/**").permitAll() // Permitir acesso não autenticado
                .anyRequest().authenticated() // Todas as outras requisições precisam de autenticação
                .and()
                .addFilterBefore(new TenantIdentificationFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login") // URL do formulário de login
                .loginProcessingUrl("/logar") // URL que processa o login (submissão do formulário)
                .defaultSuccessUrl("/home", true) // URL para redirecionar após o login bem sucedido
                .failureUrl("/login?error") // URL para redirecionar após falha no login
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()
                .rememberMe()
                .and()
                .sessionManagement()
                .maximumSessions(1000)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/login"); // Adicionado para redirecionar em caso de sessão expirada
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry (){
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<?> servletListenerRegistrationBean(){
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}
