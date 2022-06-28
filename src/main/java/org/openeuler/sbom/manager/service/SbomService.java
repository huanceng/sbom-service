package org.openeuler.sbom.manager.service;

import org.openeuler.sbom.manager.model.RawSbom;

import java.io.IOException;

public interface SbomService {

    // TODO 后续把productName换成productID
    void readSbomFile(String productName, String fileName, byte[] fileContent) throws IOException;

    // TODO 后续把productName换成productID
    RawSbom writeSbomFile(String productName, String spec, String specVersion, String format);
}
