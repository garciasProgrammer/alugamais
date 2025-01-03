package br.com.alugamais.web.config.hibernate;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;


@Component
public class TenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private Map<String, DataSource> dataSourcesMap;

    public TenantConnectionProvider(Map<String, DataSource> dataSourcesMap) {

        this.dataSourcesMap = dataSourcesMap;
    }

    public Map<String, DataSource> getDataSourcesMap() {
        return this.dataSourcesMap;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        // Retorne qualquer DataSource como padrão. Por exemplo, o DataSource do tenant default.
        return dataSourcesMap.values().iterator().next();
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        // Retorne o DataSource específico para o tenant baseado no tenantIdentifier.
        return dataSourcesMap.get(tenantIdentifier);
    }
}

