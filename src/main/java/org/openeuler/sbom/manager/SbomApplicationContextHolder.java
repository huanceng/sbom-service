package org.openeuler.sbom.manager;

import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SbomApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SbomApplicationContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getSbomReader(String serviceName) {
        return (T) applicationContext.getBean(serviceName + SbomConstants.READER_NAME, SbomReader.class);
    }

    public static <T> T getSbomWriter(String serviceName) {
        return (T) applicationContext.getBean(serviceName + SbomConstants.WRITER_NAME, SbomWriter.class);
    }
}
