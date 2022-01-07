package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class TestContainersSetup {
    private static final String COUCHBASE_CONTAINER = "couchbase:enterprise-6.5.1";
    private static final Logger COUCHBASE_LOGGER = LoggerFactory.getLogger("container.Couchbase");

    private static final CouchbaseContainer couchbaseContainer =
            new CouchbaseContainer(DockerImageName.parse(COUCHBASE_CONTAINER)
                    .asCompatibleSubstituteFor("couchbase/server"));


    public static void initTestContainers(ConfigurableEnvironment configEnv) {
        if(couchbaseContainer.isRunning()){
            return;
        }

        String userName = configEnv.getProperty("spring.couchbase.username");
        String password = configEnv.getProperty("spring.couchbase.password");
        String bucketName = configEnv.getProperty("spring.data.couchbase.bucket-name");

        couchbaseContainer.withCredentials(userName, password)
                .withBucket(new BucketDefinition(bucketName));

        COUCHBASE_LOGGER.info("Stating couchbase test container");

        couchbaseContainer.start();


        couchbaseContainer.followOutput(new Slf4jLogConsumer(COUCHBASE_LOGGER));


    }

    public static String getConnectionString() {
        return couchbaseContainer.getConnectionString();
    }

}
