package org.openeuler.sbom.manager.service.reader;

import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomSpecification;

import java.io.File;
import java.io.IOException;

public interface SbomReader {

    void read(File file) throws IOException;

    void read(SbomFormat format, SbomSpecification specification, byte[] fileContent) throws IOException;
}
