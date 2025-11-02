package co.edu.unicauca.usuario_service.config;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.*;

@Configuration
@EnableTransactionManagement
public class MultitenantConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SchemaTenantIdentifierResolver tenantIdentifierResolver;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(Environment.MULTI_TENANT, org.hibernate.MultiTenancyStrategy.SCHEMA);
        props.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, new SchemaConnectionProvider(dataSource));
        props.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        props.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        props.put(Environment.HBM2DDL_AUTO, "update");
        props.put(Environment.SHOW_SQL, true);

        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setPackagesToScan("com.agroveredal.usuario.entity");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setDataSource(dataSource);
        emf.setJpaPropertyMap(props);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}