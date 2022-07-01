package org.openeuler.sbom.manager;

import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SbomApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SbomApplicationContextHolder.applicationContext = applicationContext;
    }

    public static SbomReader getSbomReader(String serviceName) {
        return applicationContext.getBean(serviceName + SbomConstants.READER_NAME, SbomReader.class);
    }

    public static SbomWriter getSbomWriter(String serviceName) {
        return applicationContext.getBean(serviceName + SbomConstants.WRITER_NAME, SbomWriter.class);
    }
}
