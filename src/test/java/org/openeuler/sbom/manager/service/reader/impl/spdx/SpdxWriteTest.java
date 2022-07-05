package org.openeuler.sbom.manager.service.reader.impl.spdx;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openeuler.sbom.manager.TestConstants;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.ChecksumRepository;
import org.openeuler.sbom.manager.dao.ExternalPurlRefRepository;
import org.openeuler.sbom.manager.dao.ExternalVulRefRepository;
import org.openeuler.sbom.manager.dao.PackageRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeExcludedFileRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeRepository;
import org.openeuler.sbom.manager.dao.SbomCreatorRepository;
import org.openeuler.sbom.manager.dao.SbomElementRelationshipRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.dao.VulnerabilityRepository;
import org.openeuler.sbom.manager.model.Vulnerability;
import org.openeuler.sbom.manager.service.writer.impl.spdx.SpdxWriter;
import org.openeuler.sbom.manager.utils.SbomFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpdxWriteTest {

    private static final String PRODUCT_ID = "SpdxWriterTest";

    @Autowired
    @Qualifier(SbomConstants.SPDX_NAME + SbomConstants.READER_NAME)
    private SpdxReader spdxReader;


    @Autowired
    @Qualifier(SbomConstants.SPDX_NAME + SbomConstants.WRITER_NAME)
    private SpdxWriter spdxWriter;

    @Autowired
    private SbomRepository sbomRepository;

    @Autowired
    private SbomCreatorRepository sbomCreatorRepository;

    @Autowired
    private SbomElementRelationshipRepository sbomElementRelationshipRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PkgVerfCodeRepository pkgVerfCodeRepository;

    @Autowired
    private PkgVerfCodeExcludedFileRepository pkgVerfCodeExcludedFileRepository;

    @Autowired
    private ChecksumRepository checksumRepository;

    @Autowired
    private ExternalPurlRefRepository externalPurlRefRepository;

    @Autowired
    private VulnerabilityRepository vulnerabilityRepository;

    @Autowired
    private ExternalVulRefRepository externalVulRefRepository;

    @Test
    @Order(1)
    public void insertSbom() throws IOException {
        Vulnerability vulnerability = vulnerabilityRepository.findById("cve-2022-00000").orElse(new Vulnerability());
        vulnerability.setVulId("cve-2022-00000");
        vulnerability.setType("cve");
        vulnerabilityRepository.save(vulnerability);

        spdxReader.read(PRODUCT_ID, new ClassPathResource(TestConstants.SAMPLE_UPLOAD_FILE_NAME).getFile());
    }

    @Test
    @Order(2)
    public void writeSbom() throws IOException {
        byte[] result = spdxWriter.write(PRODUCT_ID, SbomFormat.EXT_TO_FORMAT.get("json"));
        assertThat(new JsonMapper().createParser(result).isNaN()).isFalse();
    }
}