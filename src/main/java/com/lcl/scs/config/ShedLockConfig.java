package com.lcl.scs.config;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoCollection;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;

@Configuration
public class ShedLockConfig {
    @Autowired
    private MongoTemplate template;

    @Bean
    public LockProvider lockProvider(){
        MongoCollection<Document> mongo = template.getCollection("shedLock");
        return new MongoLockProvider(mongo);
    }
}
