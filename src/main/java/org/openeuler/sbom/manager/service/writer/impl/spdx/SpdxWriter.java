package org.openeuler.sbom.manager.service.writer.impl.spdx;

import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.ossreviewtoolkit.utils.spdx.model.SpdxDocument;

import java.io.File;
import java.io.IOException;

public class SpdxWriter implements SbomWriter<SpdxDocument> {
    @Override
    public void write(SpdxDocument sbomDocument, File file, SbomFormat format) throws IOException {

    }

    @Override
    public String writeAsString(SpdxDocument sbomDocument, SbomFormat format) throws IOException {
        return null;
    }
}
