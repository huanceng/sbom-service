package org.openeuler.sbom.manager.service.writer.impl.spdx;

import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.SbomCreatorRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.SbomCreator;
import org.openeuler.sbom.manager.model.spdx.SpdxCreationInfo;
import org.openeuler.sbom.manager.model.spdx.SpdxDocument;
import org.openeuler.sbom.manager.service.writer.SbomWriter;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.openeuler.sbom.manager.utils.SbomMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = SbomConstants.SPDX_NAME + SbomConstants.WRITER_NAME)
@Transactional(rollbackFor = Exception.class)
public class SpdxWriter implements SbomWriter<SpdxDocument> {
    private static final String SPDX_VERSION = "SPDX-2.2";
    @Autowired
    private SbomRepository sbomRepository;

    @Autowired
    private SbomCreatorRepository sbomCreatorRepository;


    @Override
    public byte[] write(String productId, SbomFormat format) throws IOException {
        Sbom sbom = sbomRepository.findByProductId(productId).orElseThrow(() -> new RuntimeException("can`t find %s `s sbom metadata".formatted(productId)));
        SpdxDocument document = new SpdxDocument(sbom.getId().toString());
        document.setName(sbom.getName());
        document.setSpdxVersion(SPDX_VERSION);
        document.setDataLicense(sbom.getDataLicense());
        document.setDocumentNamespace(sbom.getNamespace());

        List<SbomCreator> creatorList = sbom.getSbomCreators();
        List<String> creators = creatorList.stream().map(SbomCreator::getName).collect(Collectors.toList());
        SpdxCreationInfo creationInfo = new SpdxCreationInfo(null, Instant.parse(sbom.getCreated()), creators, sbom.getLicenseListVersion());
        document.setCreationInfo(creationInfo);

        return SbomMapperUtil.writeAsBytes(document, format);
    }
}
