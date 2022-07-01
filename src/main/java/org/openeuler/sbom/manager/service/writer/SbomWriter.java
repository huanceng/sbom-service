package org.openeuler.sbom.manager.service.writer;


import org.openeuler.sbom.manager.utils.SbomFormat;

import java.io.IOException;

public interface SbomWriter<T> {


    byte[] write(String productId, SbomFormat format) throws IOException;
}
