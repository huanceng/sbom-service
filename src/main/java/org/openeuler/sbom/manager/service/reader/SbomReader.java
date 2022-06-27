package org.openeuler.sbom.manager.service.reader;

import java.io.File;
import java.io.IOException;

public interface SbomReader {

    void read(File file) throws IOException;
}
