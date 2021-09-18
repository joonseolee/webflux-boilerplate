package com.joonseolee.webfluxboilerplate.config.database;

import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@EnableR2dbcRepositories(
        basePackages = "com.joonseolee.webfluxboilerplate",
        entityOperationsRef = "mainEntityTemplate"
)
public class DatabaseMainConfiguration {

    private static final String PACKAGE_DIRECTORY = "";

    private final DatabaseMainProperties databaseMainProperties;

    @Bean
    public ConnectionFactory mainMasterConnectionFactory() {
        var master = databaseMainProperties.getMaster();

        return ConnectionFactoryBuilder
                .withUrl(master.getUrl())
                .username(master.getUsername())
                .password(master.getPassword())
                .build();
    }

    @Bean
    public ConnectionFactory mainSlaveConnectionFactory() {
        var slave = databaseMainProperties.getSlaves()
                .stream()
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("main slave datasource is an empty."));

        return ConnectionFactoryBuilder
                .withUrl(slave.getUrl())
                .username(slave.getUsername())
                .password(slave.getPassword())
                .build();
    }

    @Bean
    public ConnectionFactory mainConnectionFactory(@Qualifier("mainMasterConnectionFactory") ConnectionFactory mainMasterConnectionFactory,
                                                   @Qualifier("mainSlaveConnectionFactory") ConnectionFactory mainSlaveConnectionFactory) {
        var mainConnectionFactory = new DynamicRoutingConnectionFactory();

        Map<ReplicationType, ConnectionFactory> factories = new HashMap<>();
        factories.put(ReplicationType.MASTER, mainMasterConnectionFactory);
        factories.put(ReplicationType.SLAVE, mainSlaveConnectionFactory);
        mainConnectionFactory.setDefaultTargetConnectionFactory(mainMasterConnectionFactory);
        mainConnectionFactory.setTargetConnectionFactories(factories);

        return mainConnectionFactory;
    }

    @Bean
    public R2dbcEntityOperations mainEntityTemplate(
            @Qualifier("mainConnectionFactory") ConnectionFactory mainConnectionFactory) {
        var strategy = new DefaultReactiveDataAccessStrategy(MySqlDialect.INSTANCE);
        var databaseClient = DatabaseClient.builder()
                .connectionFactory(mainConnectionFactory)
                .bindMarkers(MySqlDialect.INSTANCE.getBindMarkersFactory())
                .build();

        return new R2dbcEntityTemplate(databaseClient, strategy);
    }

    @Bean
    public R2dbcTransactionManager mainR2dbcTransactionManager(
            @Qualifier("mainMasterConnectionFactory") ConnectionFactory mainMasterConnectionFactory) {
        return new R2dbcTransactionManager(mainMasterConnectionFactory);
    }
}
