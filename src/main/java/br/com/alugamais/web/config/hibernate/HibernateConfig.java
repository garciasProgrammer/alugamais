package br.com.alugamais.web.config.hibernate;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;

    private DataSource createDataSourceForTenant(String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public TenantConnectionProvider tenantConnectionProvider() {
        Map<String, DataSource> dataSourcesMap = new HashMap<>();

        // Criar e adicionar DataSources para cada tenant

        dataSourcesMap.put("residencial-sofia", createDataSourceForTenant("jdbc:mysql://localhost:3306/wwflee_residencial_sofia?createDatabaseIfNotExist=true", "wwflee_admin", "@Java123!"));

        return new TenantConnectionProvider(dataSourcesMap);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            MultiTenantConnectionProvider multiTenantConnectionProvider,
            CurrentTenantIdentifierResolver tenantIdentifierResolver) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("br.com.alugamais.web.domain"); // Substitua pelo pacote das suas entidades
        em.setPersistenceProvider(new HibernatePersistenceProvider());

        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect"); //dialeto para MySQL
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.SHOW_SQL, "false");
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.FORMAT_SQL, "false");
        em.getJpaPropertyMap().put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "none");

        return em;
    }
}
