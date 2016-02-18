package com.ge.lighting.hydra.asset;

import com.ge.lighting.hydra.asset.AssetStore;
import java.util.logging.Logger;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Component
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = {
		AssetStore.ENTITY_PACKAGE
})
@EnableJpaRepositories(basePackages = {
		AssetStore.DAL_PACKAGE
})
@ComponentScan({
    "com.ge.lighting.hydra.asset"
})
@Profile("cloud")
public class AssetPersistenceConfigCloud extends DataSource implements EnvironmentAware {

    private static final Logger LOG = Logger.getLogger(AssetPersistenceConfigCloud.class.getName());

    public AssetPersistenceConfigCloud() {
    }

    public AssetPersistenceConfigCloud(PoolConfiguration poolProperties) {
        super(poolProperties);
    }
    
//	@Override
    public void setEnvironment(Environment env) {
        LOG.info("Persistence configuration ...");
        
        String dbServiceName = env.getProperty("db_service_name");
        if (dbServiceName != null) {
            String envURL = env.getProperty("vcap.services." + dbServiceName + ".credentials.uri");
            System.out.println("DB Url : " + envURL);
            setUrl(envURL);
            String envUserName = env.getProperty("vcap.services." + dbServiceName + ".credentials.username");
            System.out.println("DB userName  : " + envUserName);
            if (envUserName != null) {
                setUsername(envUserName);
            }
            String envUserPassword = env.getProperty("vcap.services." + dbServiceName + ".credentials.password");
            System.out.println("DB userPassword  : " + envUserPassword);
            if (envUserPassword != null) {
                setPassword(envUserPassword);
            }
        }

        
    }

}
