package com.joonseolee.webfluxboilerplate.config.database;

import lombok.RequiredArgsConstructor;
import org.hibernate.internal.util.config.ConfigurationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RequiredArgsConstructor
@Configuration
public class DatabaseMainConfiguration {

    private static final String PACKAGE_DIRECTORY = "";

    private final DatabaseMainProperties databaseMainProperties;

    @Bean
    public DataSource mainMasterDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(databaseMainProperties.getMaster().getDriverClassName())
                .url(databaseMainProperties.getMaster().getUrl())
                .username(databaseMainProperties.getMaster().getUsername())
                .password(databaseMainProperties.getMaster().getPassword())
                .build();
    }

    @Bean
    public DataSource mainSlaveDataSource() {
        var slaveProperties = databaseMainProperties.getSlaves().stream()
                .findFirst()
                .orElseThrow(() -> new ConfigurationException("main slave datasource is an empty."));

        return DataSourceBuilder.create()
                .driverClassName(slaveProperties.getDriverClassName())
                .url(slaveProperties.getUrl())
                .username(slaveProperties.getUsername())
                .password(slaveProperties.getPassword())
                .build();
    }

    @Bean
    public DataSource mainRoutingDataSource(@Qualifier("mainMasterDataSource") DataSource mainMasterDataSource,
                                            @Qualifier("mainSlaveDataSource") DataSource mainSlaveDataSource) {
        var routingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(ReplicationType.MASTER, mainMasterDataSource);
        dataSourceMap.put(ReplicationType.SLAVE, mainSlaveDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        routingDataSource.setDefaultTargetDataSource(mainMasterDataSource);

        return routingDataSource;
    }

    @Bean
    public DataSource mainDataSource(@Qualifier("mainRoutingDataSource") DataSource mainRoutingDataSource) {
        return new LazyConnectionDataSourceProxy(mainRoutingDataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory(
            @Qualifier("mainDataSource") DataSource mainDataSource) {
        var entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(mainDataSource);
        entityManagerFactoryBean.setPackagesToScan(PACKAGE_DIRECTORY);
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(new Properties());

        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager mainTransactionManager(
            @Qualifier("mainEntityManagerFactory") EntityManagerFactory mainEntityManagerFactory) {
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mainEntityManagerFactory);
        return transactionManager;
    }
}
