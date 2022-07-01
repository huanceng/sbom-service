package org.openeuler.sbom.manager.service;

import org.openeuler.sbom.manager.model.RawSbom;

import java.io.IOException;

public interface SbomService {

    void readSbomFile(String productId, String fileName, byte[] fileContent) throws IOException;

    RawSbom writeSbomFile(String productId, String spec, String specVersion, String format);

    byte[] writeSbom(String productId, String spec, String specVersion, String format) throws IOException;
}
