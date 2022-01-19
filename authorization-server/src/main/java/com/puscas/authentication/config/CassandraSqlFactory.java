package com.puscas.authentication.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CqlSessionFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionFactoryFactoryBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DataCenterReplication;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.SimpleUserTypeResolver;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Configuration
//@EnableCassandraRepositories(basePackages = { "com.puscas.authentication.model" })
public class CassandraSqlFactory extends AbstractCassandraConfiguration
{


    protected String getKeyspaceName() {
        return "mykeyspace";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.puscas.authentication.model.model"};
    }

    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace("mykeyspace")
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withNetworkReplication(DataCenterReplication.of("datacenter1", 3), DataCenterReplication.of("bar", 2));

        return Arrays.asList(specification);
    }


    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace("mykeyspace"));
    }

    @Bean
    public CqlSessionFactoryBean cassandraSession() {

        CqlSessionFactoryBean session = new CqlSessionFactoryBean();
        session.setContactPoints("172.10.10.2");
        session.setKeyspaceName("mykeyspace");
        session.setPort(9042);
        session.setLocalDatacenter("datacenter1");
        session.setKeyspaceCreations(getKeyspaceCreations());
           session.setKeyspaceStartupScripts(new ArrayList<String>(){{
        add("CREATE TABLE IF NOT EXISTS mykeyspace.users (email text, grantedauthority list<text>, id uuid, password text, username text, PRIMARY KEY (id));");
        add("CREATE TABLE IF NOT EXISTS mykeyspace.users_credentials (email text, authorities list<text>,  password text, user_id uuid, is_account_non_expired boolean, is_account_non_locked boolean," +
                "is_credentials_non_expired boolean," +
                "is_enabled boolean," +
                " PRIMARY KEY (email));");
        add("INSERT INTO \"mykeyspace\".\"users_credentials\" (\"email\", \"authorities\", \"password\", user_id , is_account_non_expired,is_account_non_locked,is_credentials_non_expired,is_enabled) " +
                "VALUES ('puscas@puscas.com', ['read','write'], '$2a$10$Udcy3RS6dEapZ5GuNillsOSPrz.LW/DRSyFvAPygI0NH7EJVCO6G2', bbc3d2ee-77a9-11ec-90d6-0242ac120003, true, true, true, true);");
        add("INSERT INTO \"mykeyspace\".\"users\" (\"id\", \"email\", \"grantedauthority\", \"password\", \"username\") VALUES (bbc3d2ee-77a9-11ec-90d6-0242ac120003, 'puscas@puscas.com', ['read','write'], '$2a$10$Udcy3RS6dEapZ5GuNillsOSPrz.LW/DRSyFvAPygI0NH7EJVCO6G2', 'ebypuschi');");

        add("CREATE TABLE IS NOT EXISTS mykeyspace.oauth2_consent(registered_client text,principal_name text,authorities list<text>, PRIMARY KEY(registered_client, principal_name));");
        }});
        session.setUsername("root");
        session.setPassword("root");
         return session;
    }


   @Bean
    public SessionFactoryFactoryBean cassandraSessionFactory(CqlSession session, CassandraConverter converter) {

        SessionFactoryFactoryBean sessionFactory = new SessionFactoryFactoryBean();
        sessionFactory.setSession(session);
        sessionFactory.setConverter(converter);
        sessionFactory.setSchemaAction(SchemaAction.NONE);

        return sessionFactory;
    }

    @Bean
    public CassandraMappingContext cassandraMapping(CqlSession cqlSession) throws ClassNotFoundException{

        CassandraMappingContext mappingContext = new CassandraMappingContext();
        mappingContext.setUserTypeResolver(new SimpleUserTypeResolver(cqlSession));

        return mappingContext;
    }

    @Bean
    public CassandraConverter cassandraConverter(CassandraMappingContext mappingContext) {
        return new MappingCassandraConverter(mappingContext);
    }

    @Bean
    public CassandraOperations cassandraTemplate(SessionFactory sessionFactory, CassandraConverter converter) {
        return new CassandraTemplate(sessionFactory, converter);
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
