package com.simplekey.spk_sai_sfmi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.simplekey.spk_sai_sfmi.api.sfmi.mpc.repository",
        entityManagerFactoryRef = "tertiaryEntityManagerFactory",
        transactionManagerRef = "tertiaryTransactionManager"
)
public class TertiaryDatabaseConfig {

    @Bean(name = "tertiaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tertiary")
    public DataSource tertiaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "tertiaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tertiaryEntityManagerFactory(
            @Qualifier("tertiaryDataSource") DataSource tertiaryDataSource,
            HibernateProperties hibernateProperties,
            JpaProperties jpaProperties) {

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(tertiaryDataSource);
        factory.setPackagesToScan("com.simplekey.spk_sai_sfmi.api.sfmi.mpc.domain");
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = hibernateProperties.determineHibernateProperties(
            jpaProperties.getProperties(), new HibernateSettings()
        );

        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.default_schema", "f1_conf");
        factory.setJpaPropertyMap(properties);
        return factory;
    }

    @Bean(name = "tertiaryTransactionManager")
    public PlatformTransactionManager tertiaryTransactionManager(
            @Qualifier("tertiaryEntityManagerFactory") LocalContainerEntityManagerFactoryBean tertiaryEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(tertiaryEntityManagerFactory.getObject()));
    }
}
