/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.AssetStore;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ge.lighting.innovation.persistence.ext.hibernate.json.JsonPostgisDialect;

/**
 *
 * @author predix
 */
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {
    AssetStore.ENTITY_PACKAGE,
    ModelStore.ENTITY_PACKAGE
})
@EnableJpaRepositories(basePackages = {
    AssetStore.DAL_PACKAGE,
    ModelStore.DAL_PACKAGE
})
@ComponentScan({
    "com.ge.lighting.hydra.asset"
})
@Profile("local")
public class AssetPersistenceConfigLocal {

	private static final String PREFIX = "jdbc:postgresql:";
	private static final String ADDRESS = "3.117.210.197";
	private static final String DB = "hydra";
	private static final String PORT = "5432";
	private static final String USER = "hydra";
	private static final String PASSWORD = "ombre2383";

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setPersistenceUnitName("hydraPu");
		em.setJpaProperties(additionalProperties());
		em.setJpaDialect(new HibernateJpaDialect());
		return em;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(org.postgresql.Driver.class
				.getCanonicalName());
		dataSource.setUrl(PREFIX + "//" + ADDRESS + ":" + PORT + "/" + DB);
		dataSource.setUsername(USER);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(
			EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("spring.jpa.show-sql", "true");
		 properties.setProperty("hibernate.hbm2ddl.auto", "update");
//		properties.setProperty("hibernate.hbm2ddl.auto", "create");
		properties.setProperty("hibernate.show_sql", "true");
		properties.setProperty("hibernate.dialect",
				JsonPostgisDialect.class.getCanonicalName());
		return properties;
	}
}
