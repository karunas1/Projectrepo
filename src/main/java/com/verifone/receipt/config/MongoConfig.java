package com.verifone.receipt.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.verifone.receipt.exception.ReceiptServiceException;
import com.verifone.receipt.security.VaultSecure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.database.name}")
    private String dbName;

    @Value("${spring.data.mongodb.username}")
    private String userName;

    @Value("${spring.data.mongodb.mongodbSecretkey}")
    private String mongodbSecretkey;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Autowired
    VaultSecure vaultSecure;

    @Bean
    @Override
    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Bean
    @Qualifier("mongoTemplate")
    @Override
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongotemplate = new MongoTemplate(mongoDbFactory(), null);
        mongotemplate.executeCommand("{ buildInfo: 1 }");
        return mongotemplate;
    }

    @Override
    public MongoClient mongoClient() {
        String mongoPassword = null;
        MongoClient mongoClient = null;
        try {
            mongoPassword = vaultSecure.retrievePassword(mongodbSecretkey);
            MongoCredential credential =
                    MongoCredential.createScramSha256Credential(userName, dbName, mongoPassword.toCharArray());
            List<MongoCredential> credentialList = new ArrayList<>();
            String[] connectionstring = host.split(",");
            List<ServerAddress> list = new ArrayList<>();
            for (String mongohostport : connectionstring) {
                String[] hostport = mongohostport.split(":");
                String hostName = hostport[0];
                int port = Integer.parseInt(hostport[1]);
                list.add(new ServerAddress(hostName, port));
                credentialList.add(credential);
            }
            mongoClient = new MongoClient(list, credentialList);
        } catch (Exception e) {
            LOGGER.error("MongoDB password retrieval failed: ", e);
            try {
                throw new ReceiptServiceException("Internal Server error.");
            } catch (ReceiptServiceException ex) {
                LOGGER.error("Error while retrieving Mongo password ",ex);
            }
        }

        return mongoClient;
    }

    @Override
    protected String getDatabaseName() {
        LOGGER.info("Getting Database name.");
        return dbName;
    }
}
