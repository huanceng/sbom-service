package org.openeuler.sbom.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SbomManagerApplication {

    private static final Logger logger = LoggerFactory.getLogger(SbomManagerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SbomManagerApplication.class, args);
        logger.info("Sbom service has started");
    }
}
