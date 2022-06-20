package com.huawei.sbom.analyzer.vcs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VcsToken {

    private static final Logger logger = LoggerFactory.getLogger(VcsToken.class);
    private static final Properties tokens = new Properties();

    static {
        try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/vcsToken.properties")) {
            tokens.load(is);
        } catch (IOException e) {
            logger.error("failed to get vcs token from config", e);
            throw new RuntimeException(e);
        }
    }

    public static String getToken(VcsEnum vcs) {
        return tokens.getProperty(vcs.name().toLowerCase());
    }
}
