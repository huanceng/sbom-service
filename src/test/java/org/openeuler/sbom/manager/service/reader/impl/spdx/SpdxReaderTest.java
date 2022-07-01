package org.openeuler.sbom.manager.service.reader.impl.spdx;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.dao.ChecksumRepository;
import org.openeuler.sbom.manager.dao.ExternalPurlRefRepository;
import org.openeuler.sbom.manager.dao.ExternalVulRefRepository;
import org.openeuler.sbom.manager.dao.PackageRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeExcludedFileRepository;
import org.openeuler.sbom.manager.dao.PkgVerfCodeRepository;
import org.openeuler.sbom.manager.dao.PurlQualifierRepository;
import org.openeuler.sbom.manager.dao.PurlRepository;
import org.openeuler.sbom.manager.dao.SbomCreatorRepository;
import org.openeuler.sbom.manager.dao.SbomElementRelationshipRepository;
import org.openeuler.sbom.manager.dao.SbomRepository;
import org.openeuler.sbom.manager.dao.VulnerabilityRepository;
import org.openeuler.sbom.manager.model.Checksum;
import org.openeuler.sbom.manager.model.ExternalPurlRef;
import org.openeuler.sbom.manager.model.ExternalVulRef;
import org.openeuler.sbom.manager.model.Package;
import org.openeuler.sbom.manager.model.PkgVerfCode;
import org.openeuler.sbom.manager.model.PkgVerfCodeExcludedFile;
import org.openeuler.sbom.manager.model.Purl;
import org.openeuler.sbom.manager.model.PurlQualifier;
import org.openeuler.sbom.manager.model.Sbom;
import org.openeuler.sbom.manager.model.SbomCreator;
import org.openeuler.sbom.manager.model.SbomElementRelationship;
import org.openeuler.sbom.manager.model.Vulnerability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SpdxReaderTest {

    private static final String SAMPLE_UPLOAD_FILE_NAME = "sample/sample-spdx.json";

    private static final String PRODUCT_ID = "SpdxReaderTest";

    @Autowired
    @Qualifier(SbomConstants.SPDX_NAME + SbomConstants.READER_NAME)
    private SpdxReader spdxReader;

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
    private PurlRepository purlRepository;

    @Autowired
    private PurlQualifierRepository purlQualifierRepository;

    @Autowired
    private VulnerabilityRepository vulnerabilityRepository;

    @Autowired
    private ExternalVulRefRepository externalVulRefRepository;

    @Test
    @Order(1)
    public void setup() {
        cleanDb();
    }

    @Test
    @Order(2)
    public void insertSbom() throws IOException {
        functionBody();
    }

    @Test
    @Order(3)
    public void updateSbom() throws IOException {
        functionBody();
    }

    @Test
    @Order(4)
    public void deleteSbom() {
        sbomRepository.deleteAll();
        assertThat(sbomRepository.findAll().size()).isEqualTo(0);
        assertThat(sbomCreatorRepository.findAll().size()).isEqualTo(0);
        assertThat(sbomElementRelationshipRepository.findAll().size()).isEqualTo(0);
        assertThat(packageRepository.findAll().size()).isEqualTo(0);
        assertThat(pkgVerfCodeRepository.findAll().size()).isEqualTo(0);
        assertThat(pkgVerfCodeExcludedFileRepository.findAll().size()).isEqualTo(0);
        assertThat(checksumRepository.findAll().size()).isEqualTo(0);
        assertThat(externalPurlRefRepository.findAll().size()).isEqualTo(0);
        assertThat(purlRepository.findAll().size()).isEqualTo(36);
        assertThat(purlQualifierRepository.findAll().size()).isEqualTo(2);
        assertThat(vulnerabilityRepository.findAll().size()).isEqualTo(1);
        assertThat(externalVulRefRepository.findAll().size()).isEqualTo(0);
    }

    private void cleanDb() {
        sbomRepository.deleteAll();
        sbomCreatorRepository.deleteAll();
        sbomElementRelationshipRepository.deleteAll();
        packageRepository.deleteAll();
        pkgVerfCodeRepository.deleteAll();
        pkgVerfCodeExcludedFileRepository.deleteAll();
        checksumRepository.deleteAll();
        externalPurlRefRepository.deleteAll();
        purlRepository.deleteAll();
        purlQualifierRepository.deleteAll();
        vulnerabilityRepository.deleteAll();
        externalVulRefRepository.deleteAll();
    }

    private void functionBody() throws IOException {
        Vulnerability vulnerability = vulnerabilityRepository.findById("cve-2022-00000").orElse(new Vulnerability());
        vulnerability.setVulId("cve-2022-00000");
        vulnerability.setType("cve");
        vulnerabilityRepository.save(vulnerability);

        spdxReader.read(PRODUCT_ID, new ClassPathResource(SAMPLE_UPLOAD_FILE_NAME).getFile());

        Sbom sbom = sbomRepository.findByProductId(PRODUCT_ID).orElse(null);
        assertThat(sbom).isNotNull();
        assertThat(sbom.getProductId()).isEqualTo(PRODUCT_ID);

        List<SbomCreator> sbomCreators = sbomCreatorRepository.findAll();
        assertThat(sbomCreators.size()).isEqualTo(1);
        assertThat(sbomCreators.get(0).getSbom().getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(sbomCreators.get(0).getName()).isEqualTo("Tool: OSS Review Toolkit - e5b343ff71-dirty");

        List<SbomElementRelationship> sbomElementRelationships = sbomElementRelationshipRepository.findAll();
        assertThat(sbomElementRelationships.size()).isEqualTo(77);

        List<Package> packages = packageRepository.findAll();
        assertThat(packages.size()).isEqualTo(78);
        packages.forEach(p -> assertThat(p.getSbom().getId()).isEqualTo(sbom.getId()));

        List<Package> specificPackages = packageRepository.findBySpdxId("SPDXRef-Package-PyPI-asttokens-2.0.5-vcs");
        assertThat(specificPackages.size()).isEqualTo(1);
        assertThat(specificPackages.get(0).getName()).isEqualTo("asttokens");

        List<PkgVerfCode> pkgVerfCodes = pkgVerfCodeRepository.findAll();
        assertThat(pkgVerfCodes.size()).isEqualTo(1);
        assertThat(pkgVerfCodes.get(0).getValue()).isEqualTo("8aba92182455b539af15d0524fe5baffd3d9248b");

        List<PkgVerfCodeExcludedFile> pkgVerfCodeExcludedFiles = pkgVerfCodeExcludedFileRepository.findAll();
        assertThat(pkgVerfCodeExcludedFiles.size()).isEqualTo(2);

        List<Checksum> checksums = checksumRepository.findAll();
        assertThat(checksums.size()).isEqualTo(1);
        assertThat(checksums.get(0).getAlgorithm()).isEqualTo("SHA256");
        assertThat(checksums.get(0).getValue()).isEqualTo("b5dcc8da8a08e73dc2acdf1b1c4b06ca0bab0db5d9da9417c2841c1d6872c126");

        List<ExternalPurlRef> externalPurlRefs = externalPurlRefRepository.findAll();
        assertThat(externalPurlRefs.size()).isEqualTo(76);

        List<Purl> purls = purlRepository.findAll();
        assertThat(purls.size()).isEqualTo(36);

        List<PurlQualifier> purlQualifiers = purlQualifierRepository.findAll();
        assertThat(purlQualifiers.size()).isEqualTo(2);

        List<ExternalVulRef> externalVulRefs = externalVulRefRepository.findAll();
        assertThat(externalVulRefs.size()).isEqualTo(1);
    }
}