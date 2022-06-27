package org.openeuler.sbom.manager.service.reader.impl.spdx;

import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.service.reader.SbomReader;
import org.openeuler.sbom.manager.utils.SbomMapperUtil;
import org.ossreviewtoolkit.utils.spdx.model.SpdxDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional(rollbackFor = Exception.class)
public class SpdxReader implements SbomReader {

    @Autowired
    private SbomRepository sbomRepository;

    @Override
    public void read(File file) throws IOException {
        SpdxDocument document = SbomMapperUtil.read(file);
        saveSbom(document);
    }

    private void saveSbom(SpdxDocument document) {
        Sbom sbom = new Sbom();
        sbom.setId(document.getSpdxId());
        sbom.setCreated(document.getCreationInfo().getCreated().toString());
        sbom.setDataLicense(document.getDataLicense());
        sbom.setLicenseListVersion(document.getCreationInfo().getLicenseListVersion());
        sbom.setName(document.getName());
        sbom.setNamespace(document.getDocumentNamespace());
        sbom.setSpec("SPDX");
        sbom.setSpecVersion(document.getSpdxVersion().substring(document.getSpdxVersion().lastIndexOf("-") + 1));
        sbomRepository.save(sbom);
    }
}
