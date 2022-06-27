package org.openeuler.sbom.manager.service.writer;

import org.openeuler.sbom.manager.utils.SbomFormat;

import java.io.File;
import java.io.IOException;

public interface SbomWriter<T> {

    void write(T sbomDocument, File file, SbomFormat format) throws IOException;

    String writeAsString(T sbomDocument, SbomFormat format) throws IOException;
}
