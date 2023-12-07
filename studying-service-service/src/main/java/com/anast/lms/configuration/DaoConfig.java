package com.anast.lms.configuration;

import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DaoConfig {

    @Bean
    public DefaultDSLContext dslContext(DataSource dataSource) {
        DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(
                new TransactionAwareDataSourceProxy(dataSource));
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
        jooqConfiguration.set(connectionProvider);
        jooqConfiguration.set(SQLDialect.POSTGRES);
        jooqConfiguration.set(new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));
        return new DefaultDSLContext(jooqConfiguration);
    }
}
